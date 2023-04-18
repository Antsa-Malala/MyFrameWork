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
import java.util.Set;
import jakarta.servlet.RequestDispatcher;

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
                        Enumeration<String> paramNames = request.getParameterNames();
                        while (paramNames.hasMoreElements()) {
                            String paramName = paramNames.nextElement();

                            //Verifier si le parametre fait partie des attributs de la classe 
                            for(int j=0;j<attributs.length;j++)
                            {
                                if(attributs[j].equals(paramName))
                                {
                                    String[] paramValues = request.getParameterValues(paramName);
                                    Method method= objet.getClass().getMethod("set"+attributs[j], field[j].getType());
                                    Object paramValue = convertParamValue(paramValues[0], field[j].getType());
                                    method.invoke(objet,paramValue);
                                }
                                
                            }
                        }

                        //invoque la fonction et recupere la valeur de retour ModelView
                        ModelView mv = (ModelView) fonction.invoke(objet, (Object[]) null);


                        //Parcours les données envoyées et le met en attibuts de la requete
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


}