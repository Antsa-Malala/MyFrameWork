package etu1963.framework.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import etu1963.framework.Mapping;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.DirectoryIteratorException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import annotation.Model;
import framework.*;
import jakarta.servlet.http.Part;
import java.util.Set;
import jakarta.servlet.RequestDispatcher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import jakarta.servlet.annotation.MultipartConfig;

@MultipartConfig
public class FrontServlet<T> extends HttpServlet{
    HashMap<String,Mapping> MappingUrls;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //recupere l'url
            String url = response.encodeRedirectURL(request.getRequestURL().toString());

            //split l'url
            String[] requete=url.split("FrontServlet/");

            //recupere la valeur du package de l'utilisateur dans web.xml
            String pack=this.getInitParameter("Package");

            if(requete.length>1){
                //recupere les toutes les clés dans MappingUrls
                Set<String> keySet = MappingUrls.keySet();

                //Parcours ces clés pour acceder à la valeur 
                for (String key : keySet) {
                    if(requete[1].equals(key))
                    {
                        //recupere le mapping correspondant
                        Mapping m =(Mapping)MappingUrls.get(key);

                        //recupere la classe
                        String className=pack+"."+m.getClassName();

                        //recupere la fonction
                        String methode=m.getMethod();

                        //instance l'objet
                        T objet=instantiate(className);

                        //recupere la fonction
                        Method[] methods = objet.getClass().getDeclaredMethods();
                        Method fonction = null;
                        
                        for (Method mt : methods) {
                            if (mt.getName().equals(methode)) {
                                fonction = mt;
                                break;
                            }
                        }
                        
                        //recupere les attributs de la classe
                        Field[] field = objet.getClass().getDeclaredFields();

                        //les transformer en tableau de string pour la comparaison
                        String[] attributs = new String[field.length];
                        for(int j=0;j<field.length;j++)
                        {
                            attributs[j] = field[j].getName();
                        }  
                        // Parcourir tous les paramètres et les valeurs du formulaire
                        Parameter[] param=fonction.getParameters();
                        ArrayList<Object> parameter=new ArrayList<>();
                        Enumeration<String> paramNames = request.getParameterNames();
                        try{
                            Collection<Part> files = request.getParts();
                            for(int i=0;i<field.length;i++){
                                Field f=field[i];
                                if(f.getType() == framework.FileUpload.class){
                                    Method method= objet.getClass().getMethod("set"+attributs[i], field[i].getType());
                                    FileUpload o = this.upload(files, attributs[i]);
                                    method.invoke(objet , o);
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        while (paramNames.hasMoreElements()) {
                            String paramName = paramNames.nextElement();
                            //Verifier si le parametre fait partie des attributs de la classe 
                            for(int j=0;j<attributs.length;j++)
                            {
                                Method method= objet.getClass().getMethod("set"+attributs[j], field[j].getType());
                                if(attributs[j].equals(paramName))
                                {
                                    String[] paramValues = request.getParameterValues(paramName);
                                    Object para=convertParam(paramValues.length, field[j].getType(),paramValues);
                                    if(field[j].getType().isArray())
                                    {
                                        method.invoke(objet,para);
                                    }
                                    else{
                                        Object paramValue = convertParamValue(paramValues[0], field[j].getType());
                                        method.invoke(objet,paramValue);
                                    }
                                }  
                            }
                            for(int l=0;l<param.length;l++)
                            {
                                if(param[l].getName().equals(paramName))
                                {
                                    String[] paramValues = request.getParameterValues(paramName);
                                    Object paramValue = convertParamValue(paramValues[0], param[l].getType());
                                    parameter.add(paramValue);
                                }
                            }
                        }
                        Object[] paramfonction=parameter.toArray();
                        //invoque la fonction et recupere la valeur de retour ModelView
                        ModelView mv= (ModelView) fonction.invoke(objet, paramfonction);


                        //Parcours les données envoyées et le met en attributs de la requete
                        HashMap data=mv.getdata();  
                        Set<String> keydata = data.keySet();
                        for (String keyobject : keydata) {
                            T object=(T)data.get(keyobject);
                            request.setAttribute(keyobject,object);
                        }
                        
                        //Renvoie vers la vue de la valeure de retour de la fonction
                        RequestDispatcher rd = request.getRequestDispatcher("/Views/"+mv.getview());
                        rd.forward(request, response);
                        return;
                    }
                }
           }
           else{
               out.print("Aucune commande valide");
           }
        }
        catch(Exception e)
        {
            Throwable cause = e.getCause();
            cause.printStackTrace();
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       processRequest(request, response);
   }
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
       processRequest(request, response);
   }
   public void init() throws ServletException {
    MappingUrls=new HashMap<String,Mapping>();
    ArrayList<Class> classeAnnote=new ArrayList<Class>();
    String classesPath = getServletContext().getRealPath("/WEB-INF/classes");
    String pack=this.getInitParameter("Package");
    Path directory = Paths.get(classesPath+"/"+pack);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.class")) {
        for (Path file: stream) {
            classeAnnote.add(Class.forName(pack+"." + file.getFileName().toString().substring(0, file.getFileName().toString().length() - 6)));
        }
        for(int l=0;l<classeAnnote.size();l++)
        {
            Method[] met=classeAnnote.get(l).getDeclaredMethods();
            for(int j=0;j<met.length;j++)
            {
                Annotation[] ano=met[j].getAnnotations();
                for(int k=0;k<ano.length;k++)
                {
                    if(ano[k].annotationType()==Model.class)
                    {
                        Model mo=(Model)ano[k];
                        Mapping map=new Mapping(classeAnnote.get(l).getSimpleName(),met[j].getName());
                        MappingUrls.put(mo.url(),map);
                    }
                }
            }
        }
    }
    catch(Exception e)
    {
       e.printStackTrace();

    }
}
public static <T> T instantiate(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Class<T> clazz = (Class<T>) Class.forName(className);
    Constructor<T> constructor = clazz.getConstructor(); 
    T instance = constructor.newInstance();
    return instance;
} 

private Object convertParamValue(String paramValue, Class<?> paramType) {
    if (paramType == String.class) {
        return paramValue;
    } else if (paramType == int.class || paramType == Integer.class) {
        return Integer.parseInt(paramValue);
    } else if (paramType == boolean.class || paramType == Boolean.class) {
        return Boolean.parseBoolean(paramValue);
    }else if (paramType == double.class || paramType == Double.class) {
        return Double.parseDouble(paramValue);
    }
    return null; 
}
private Object convertParam(int taille,Class<?> paramType,String[] val) {
    if (paramType == String[].class) {
        String[] n=new String[taille];
        for(int i=0;i<taille;i++)
        {
            n[i]=(String)convertParamValue(val[i],paramType.getComponentType());
        }
        return (Object)n;
    } else if (paramType == int[].class || paramType == Integer[].class) {
        int[] n=new int[taille];
        for(int i=0;i<taille;i++)
        {
            n[i]=(int)convertParamValue(val[i],paramType.getComponentType());
        }
        return (Object)n;
    } else if (paramType == boolean[].class || paramType == Boolean[].class) {
        boolean[] n=new boolean[taille];
        for(int i=0;i<taille;i++)
        {
            n[i]=(boolean)convertParamValue(val[i],paramType.getComponentType());
        }
        return (Object)n;
    }else if (paramType == double[].class || paramType == Double[].class) {
        double[] n=new double[taille];
        for(int i=0;i<taille;i++)
        {
            n[i]=(double)convertParamValue(val[i],paramType.getComponentType());
        }
        return (Object)n;
    }
    return null; 
}
private FileUpload upload( Collection<Part> files, String namefield) throws Exception{
    String path=null;
    Part filepart = null;
    for( Part part : files ){
        if( part.getName().equals(namefield) ){
            path = Paths.get(part.getSubmittedFileName()).toString();
            filepart = part;
            break;
        }
    }
    try(InputStream io = filepart.getInputStream()){
        ByteArrayOutputStream buffers = new ByteArrayOutputStream();
        byte[] buffer = new byte[(int)filepart.getSize()];
        int read;
        while( ( read = io.read( buffer , 0 , buffer.length )) != -1 ){
            buffers.write( buffer , 0, read );
        }
        FileUpload file = new FileUpload(path,this.getFileName(filepart),buffers.toByteArray());
        return file;
    }catch (Exception e) {
       throw e;
    }
}

private String getFileName(Part part) {
    String contentDisposition = part.getHeader("content-disposition");
    String[] parts = contentDisposition.split(";");
    for (String partStr : parts) {
        if (partStr.trim().startsWith("filename"))
            return partStr.substring(partStr.indexOf('=') + 1).trim().replace("\"", "");
    }
    return null;
}

}