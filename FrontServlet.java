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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.DirectoryIteratorException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import models.*;
import annotation.Model;

public class FrontServlet extends HttpServlet{
    HashMap<String,Mapping> MappingUrls;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           String url = response.encodeRedirectURL(request.getRequestURL().toString());
           String[] requete=url.split("Control/");
           if(requete.length>1){
               out.print("Votre requete : "+requete[1]);
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
    Path directory = Paths.get(classesPath+"/models");
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.class")) {
        for (Path file: stream) {
            classeAnnote.add(Class.forName("models." + file.getFileName().toString().substring(0, file.getFileName().toString().length() - 6)));
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

}