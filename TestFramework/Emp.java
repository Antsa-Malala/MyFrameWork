package models;
import annotation.Model;
import annotation.Auth;
import annotation.Sess;
import annotation.RestAPI;
import framework.ModelView;
import framework.FileUpload;
import java.util.ArrayList;
import java.util.HashMap;

//La classe non annotee Scope
public class Emp {
    private String nom;
    private String prenom;
    private int test=0;
    HashMap<String,Object> session;

    public String getnom()
    {
        return this.nom;
    }
    public String getprenom()
    {
        return this.prenom;
    }
    public void setnom(String nom)
    {
        this.nom=nom;
    }
    public void setprenom(String prenom)
    {
        this.prenom=prenom;
    }
    public void setsession(HashMap<String,Object> session)
    {
        this.session=session;
    }
    public HashMap<String,Object> getsession()
    {
        return this.session;
    }
    public int gettest()
    {
        return this.test;
    }
    public void settest(int test)
    {
        this.test=test;
    }
    public Emp(String nom,String prenom)
    {
        this.setnom(nom);
        this.setprenom(prenom);
    }
    public Emp(){

    }
    @Model(url="load_form_emp")
    public ModelView load_form()
    {
        ModelView mv=new ModelView();
        mv.setview("FormEmp.jsp");
        return mv;
    }

    @Auth
    @Model(url="Emp/formulaire")
    public ModelView getcoordonnees()
    {
        this.settest(this.gettest()+1);
        ModelView mv=new ModelView();
        mv.setview("ValiderEmp.jsp");
        ArrayList<Emp> olona=new ArrayList<Emp>();
        Emp user=new Emp(this.getnom(),this.getprenom());
        user.settest(this.gettest());
        olona.add(user);
        mv.addItem("Liste_emp",olona);
        return mv;
    }

    @Model(url="try_delete")
    public ModelView delete()
    {
        ModelView mv=new ModelView();
        mv.setview("DeleteEmp.jsp");
        return mv;
    }
    
    @Auth(profil="admin")
    @Model(url="Emp/delete")
    public ModelView deleteEmp()
    {
        ModelView mv=new ModelView();
        mv.setview("Deleted.jsp");
        ArrayList<Emp> olona=new ArrayList<Emp>();
        Emp user=new Emp(this.getnom(),this.getprenom());
        user.settest(this.gettest());
        olona.add(user);
        mv.addItem("Liste_emp",olona);
        return mv;
    }
    @Sess
    @Model(url="Emp/session")
    public ModelView sessions()
    {
        ModelView mv=new ModelView();
        mv.setisJson(true);
        mv.setview("Session.jsp");
        ArrayList<Emp> olona=new ArrayList<Emp>();
        Emp user=new Emp(this.getnom(),this.getprenom());
        user.settest(this.gettest());
        user.setsession(this.getsession());
        olona.add(user);
        mv.addItem("Liste_emp",olona);
        return mv;
    }

    @RestAPI
    @Model(url="Emp/session2")
    public Emp[] findAll()
    {
        Emp[] emp=new Emp[2];
        emp[0]=new Emp("Rakoto","Jean");
        emp[1]=new Emp("Randria","Luc");
        return emp;
    }


    @Model(url="Logout")
    public ModelView deconnection() 
    {
        ModelView mv=new ModelView();
        mv.setinvalidateSession(true);
        mv.setview("Login.jsp");
        return mv;
    }
    @Model(url="removeSession")
    public ModelView removeSession() 
    {
        ModelView mv=new ModelView();
        mv.addRemoveSession("profil");
        mv.setview("Login.jsp");
        return mv;
    }

    @Sess
    @Model(url="Emp/Logout")
    public ModelView deconnexion()
    {
        ModelView mv=new ModelView();
        mv.setisJson(true);
        mv.setinvalidateSession(true);
        mv.setview("Session.jsp");
        ArrayList<Emp> olona=new ArrayList<Emp>();
        Emp user=new Emp(this.getnom(),this.getprenom());
        user.settest(this.gettest());
        user.setsession(this.getsession());
        olona.add(user);
        mv.addItem("Liste_emp",olona);
        return mv;
    }
    @Sess
    @Model(url="Emp/removeSession")
    public ModelView mamafaSession()
    {
        ModelView mv=new ModelView();
        mv.setisJson(true);
        mv.addRemoveSession("isConnected");
        mv.setview("Session.jsp");
        ArrayList<Emp> olona=new ArrayList<Emp>();
        Emp user=new Emp(this.getnom(),this.getprenom());
        user.settest(this.gettest());
        user.setsession(this.getsession());
        olona.add(user);
        mv.addItem("Liste_emp",olona);
        return mv;
    }
}
