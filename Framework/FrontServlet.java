package etu1963.framework.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import etu1963.framework.Mapping;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.DirectoryIteratorException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import annotation.Model;
import annotation.Auth;
import annotation.Scope;
import annotation.Sess;
import annotation.RestAPI;
import framework.*;
import jakarta.servlet.http.Part;
import java.util.Set;
import java.util.Map.Entry;

import jakarta.servlet.RequestDispatcher;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import jakarta.servlet.annotation.MultipartConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@MultipartConfig
public class FrontServlet<T> extends HttpServlet{
    HashMap<String,Mapping> MappingUrls;
    HashMap<String,Object> instance=new HashMap<String,Object>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try{
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
                        reset_attribut(objet);
                        if (request.getContentType()!=null && request.getContentType().startsWith("multipart/form-data")) {
                            Collection<Part> files = request.getParts();
                            for(int i=0;i<field.length;i++){
                                Field f=field[i];
                                if(f.getType() == framework.FileUpload.class){
                                    Method method= objet.getClass().getMethod("set"+attributs[i], field[i].getType());
                                    FileUpload o = this.upload(files, attributs[i]);
                                    method.invoke(objet , o);
                                }
                            }
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
                        Annotation[] ano=fonction.getAnnotations();
                        ModelView mv=null;
                        int executed=0;
                        for(int k=0;k<ano.length;k++)
                        {
                            if(ano[k].annotationType()==Auth.class)
                            {
                                String session=this.getInitParameter("SessionName");
                                Object c=request.getSession().getAttribute(session);
                                if(c!=null)
                                {
                                    Auth mo=(Auth)ano[k];
                                    if(!mo.profil().equals("")){
                                        String profil=this.getInitParameter("SessionProfil");
                                        String p=(String)request.getSession().getAttribute(profil);
                                        if(mo.profil().equals(p))
                                        {
                                            for(int i=0;i<ano.length;i++)
                                            {
                                                if(ano[i].annotationType()==RestAPI.class)
                                                {
                                                    response.setContentType("application/json");
                                                    response.setCharacterEncoding("UTF-8");
                                                    this.sendJson(out,  fonction.invoke(objet, paramfonction));
                                                    return;
                                                }
                                            }
                                            mv= (ModelView) fonction.invoke(objet, paramfonction);
                                            executed=1;
                                        }
                                        else{
                                            throw new Exception("L'execution de cette fonction necessite la connexion en tant que "+mo.profil());
                                        }
                                    }
                                    else{
                                        for(int i=0;i<ano.length;i++)
                                        {
                                            if(ano[i].annotationType()==RestAPI.class)
                                            {
                                                response.setContentType("application/json");
                                                response.setCharacterEncoding("UTF-8");
                                                this.sendJson(out,  fonction.invoke(objet, paramfonction));
                                                return;
                                            }
                                        }
                                        mv= (ModelView) fonction.invoke(objet, paramfonction);
                                        executed=1;
                                    }
                                }
                                else{
                                    throw new Exception("L'execution de cette fonction a besoin d'authentification");
                                }
                            }
                            if(ano[k].annotationType()==Sess.class)
                            {
                                HttpSession session=request.getSession();
                                Enumeration<String> attributes = session.getAttributeNames();
                                HashMap<String,Object> sess=new HashMap<String,Object>();
                                while (attributes.hasMoreElements()) {
                                    String cle = attributes.nextElement();
                                    Object valeur = session.getAttribute(cle);
                                    sess.put(cle,valeur);
                                }
                                Method setsession=objet.getClass().getMethod("setsession", HashMap.class);
                                setsession.invoke(objet, sess);
                            }
                        }
                        if(executed==0)
                        {
                            for(int i=0;i<ano.length;i++)
                            {
                                if(ano[i].annotationType()==RestAPI.class)
                                {
                                    response.setContentType("application/json");
                                    response.setCharacterEncoding("UTF-8");
                                    this.sendJson(out,  fonction.invoke(objet, paramfonction));
                                    return;
                                }
                            }
                            mv= (ModelView) fonction.invoke(objet, paramfonction);
                        }
                        ArrayList<String> sessionRemove=mv.getremoveSession();
                        HttpSession mysess = request.getSession(false);
                        if (mysess != null) {
                            System.out.println(mv.getinvalidateSession());
                            if(mv.getinvalidateSession())
                            {
                                mysess.invalidate();
                            }
                            for(int i=0;i<sessionRemove.size();i++)
                            {
                                System.out.println("bukjavhyuui");
                                mysess.removeAttribute(sessionRemove.get(i)); 
                            }
                        }
                        if(!mv.getsession().isEmpty())
                        {
                            String session=this.getInitParameter("SessionName");
                            String profil=this.getInitParameter("SessionProfil");
                            Object c=mv.getsession().get(session);
                            Object p=mv.getsession().get(profil);
                            HttpSession sess = request.getSession();
                            if(c!=null)
                            {
                                sess.setAttribute(session, c);
                            }
                            if(p!=null)
                            {
                                sess.setAttribute(profil, p);
                            }
                        }
                        
                        //Parcours les données envoyées et le met en attributs de la requete
                        HashMap data=mv.getdata();  
                        Set<String> keydata = data.keySet();
                        for (String keyobject : keydata) {
                            T object=(T)data.get(keyobject);
                            request.setAttribute(keyobject,object);
                        }
                        if(mv.getisJson())
                        {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            this.sendJson(out, data);
                            return;
                        }
                        if(mv.getisXml())
                        {
                            response.setContentType("text/xml");
                            this.sendXml(out,mv.getdataxml());
                            return;
                        }
                        else{
                            //Renvoie vers la vue de la valeure de retour de la fonction
                            RequestDispatcher rd = request.getRequestDispatcher("/Views/"+mv.getview());
                            rd.forward(request, response);
                            return;
                        }
                        
                    }
                }
           }
           else{
               out.print("Aucune commande valide");
           }
        }
        catch(Exception e)
        {
            out.println(e.getMessage());
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
   public void sendJson(PrintWriter out,Object data)
   {
            Gson gson = new GsonBuilder().create();
            String jsonString = gson.toJson(data);
            out.print(jsonString);
        out.flush();
   }

   public void sendXml(PrintWriter out,String dataxml) throws Exception{   
        out.print(dataxml);
        out.flush();
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
            setinstance(classeAnnote.get(l).getName());
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
public void setinstance(String classname) throws Exception {
    Class<T> clazz = (Class<T>) Class.forName(classname);
    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
    if(scopeAnnotation!=null)
    {
        T instance_object = instantiate(classname);
        instance.put(classname,null);
    }
} 

public void reset_attribut(Object objet) throws Exception
{
    Field[] fields = objet.getClass().getDeclaredFields();

    for (Field field : fields) {
        field.setAccessible(true);
        Object defaultValue = defaut(field.getType());
        field.set(objet, defaultValue);
    }
}
private static Object defaut(Class<?> type) {
    if (type == boolean.class || type ==Boolean.class) {
        return false;
    } else if (type == byte.class || type== Byte.class) {
        return (byte) 0;
    } else if (type == int.class|| type==Integer.class) {
        return 0;
    } else if (type == float.class|| type==Float.class || type == double.class || type== Double.class) {
        return 0.0;
    } else if (type == String.class) {
        return "";
    } else {
        return null;
    }
}
public T instantiate(String className) throws Exception{
    Set<String> keySet = instance.keySet();

    for (String key : keySet) {
        if(className.equals(key))
        {
            T m=(T)instance.get(className);
            if(m!=null)
            {
                return m;
            }
            else{
                    Class<T> clazz = (Class<T>) Class.forName(className);
                    Constructor<T> constructor = clazz.getConstructor(); 
                    T instance_object = constructor.newInstance();
                    instance.replace(className,instance_object);
                    return (T)instance.get(className);
            }
        }
    }
    Class<T> clazz = (Class<T>) Class.forName(className);
    Constructor<T> constructor = clazz.getConstructor(); 
    T instance_object = constructor.newInstance();
    return instance_object;
} 

private Object convertParamValue(String paramValue, Class<?> paramType) {
    System.out.println(paramType.toString());
    if (paramType == String.class) {
        return paramValue;
    } else if (paramType == int.class || paramType == Integer.class) {
        return Integer.parseInt(paramValue);
    } else if (paramType == boolean.class || paramType == Boolean.class) {
        return Boolean.parseBoolean(paramValue);
    }else if (paramType == double.class || paramType == Double.class) {
        return Double.parseDouble(paramValue);
    }else if (paramType == Date.class && !paramValue.equals("")) {
        return Date.valueOf(paramValue);
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