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
            String url = response.encodeRedirectURL(request.getRequestURL().toString());
            String[] requete=url.split("FrontServlet/");
            String pack=this.getInitParameter("Package");
            if(requete.length>1){
                Set<String> keySet = MappingUrls.keySet();
                for (String key : keySet) {
                    if(requete[1].equals(key))
                    {
                        Mapping m =(Mapping)MappingUrls.get(key);
                        String classe=m.getClassName();
                        String methode=m.getMethod();
                        String className = pack+"."+classe; 
                        T objet=instantiate(className);
                        Method fonction=objet.getClass().getMethod(methode);
                        ModelView mv=(ModelView)fonction.invoke(objet, (Object[]) null);
                        HashMap data=mv.getdata();  
                        Set<String> keydata = data.keySet();
                        for (String keyobject : keydata) {
                            T object=(T)data.get(keyobject);
                            request.setAttribute(keyobject,object);
                        }
                        RequestDispatcher rd = request.getRequestDispatcher("/"+mv.getview());
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

}