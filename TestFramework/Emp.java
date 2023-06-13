package models;
import annotation.Model;
import annotation.Auth;
import framework.ModelView;
import framework.FileUpload;
import java.util.ArrayList;

//La classe non annotee Scope
public class Emp {
    private String nom;
    private String prenom;
    private int test=0;

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

}
