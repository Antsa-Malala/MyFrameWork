package models;
import annotation.Model;
import annotation.Scope;
import framework.ModelView;
import framework.FileUpload;
import java.util.ArrayList;

@Scope
public class Personne {
    private int id;
    private String nom;
    private String prenom;
    private int age;
    private double mesure;
    private String[] option;
    private FileUpload sary;
    private int test=0;

    public int getid()
    {
        return this.id;
    }
    public String getnom()
    {
        return this.nom;
    }
    public String getprenom()
    {
        return this.prenom;
    }
    public int getage()
    {
        return this.age;
    }
    public double getmesure()
    {
        return this.mesure;
    }
    public String[] getoption()
    {
        return this.option;
    }
    public FileUpload getsary()
    {
        return this.sary;
    }
    public int gettest()
    {
        return this.test;
    }
    public void setid(int id)
    {
        this.id=id;
    }
    public void setnom(String nom)
    {
        this.nom=nom;
    }
    public void setprenom(String prenom)
    {
        this.prenom=prenom;
    }
    public void setage(int age)
    {
        this.age=age;
    }
    public void setmesure(double mesure)
    {
        this.mesure=mesure;
    }
    public void setoption(String[] option)
    {
        this.option=new String[option.length];
        for(int i=0;i<option.length;i++)
        {
            this.option[i]=option[i];
        }
    }
    public void setsary(FileUpload sary)
    {
        this.sary=sary;
    }
    public void settest(int test)
    {
        this.test=test;
    }

    public Personne(int id,String nom,String prenom,int age,double mesure)
    {
        this.setid(id);
        this.setnom(nom);
        this.setprenom(prenom);
        this.setage(age);
        this.setmesure(mesure);
    }
    public Personne()
    {

    }

    @Model(url="Personne/nomcomplet")
    public ModelView get_nomcomplet()
    {
        ModelView m=new ModelView();
        m.setview("View.jsp");
        ArrayList<Personne> olona=new ArrayList<Personne>();
        Personne jean=new Personne(1,"Rakoto","Jean",16,1.60);
        Personne robert=new Personne(2,"Randria","Robert",18,1.66);
        Personne jeanne=new Personne(3,"Andria","Jeanne",20,1.76);
        Personne marie=new Personne(4,"Rasoa","Marie",30,1.80);
        olona.add(jean);
        olona.add(robert);
        olona.add(jeanne);
        olona.add(marie);
        m.addItem("Liste_personne",olona);
        return m;
    }

    @Model(url="load_form")
    public ModelView load_form()
    {
        ModelView mv=new ModelView();
        mv.setview("Form.jsp");
        return mv;
    }
    @Model(url="formulaire")
    public ModelView getcoordonnees()
    {
        this.settest(this.gettest()+1);
        ModelView mv=new ModelView();
        mv.setview("Valider.jsp");
        ArrayList<Personne> olona=new ArrayList<Personne>();
        Personne user=new Personne(this.getid(),this.getnom(),this.getprenom(),this.getage(),this.getmesure());
        user.settest(this.gettest());
        olona.add(user);
        mv.addItem("Liste_personne",olona);
        return mv;
    }

    @Model(url="Personne/detail")
    public ModelView voir_detail(int id)
    {
        ModelView mv=new ModelView();
        mv.setview("Details.jsp");
        ArrayList<Personne> olona=new ArrayList<Personne>();
        Personne[] pers=new Personne[4];
        Personne jean=new Personne(1,"Rakoto","Jean",16,1.60);
        pers[0]=jean;
        Personne robert=new Personne(2,"Randria","Robert",18,1.66);
        pers[1]=robert;
        Personne jeanne=new Personne(3,"Andria","Jeanne",20,1.76);
        pers[2]=jeanne;
        Personne marie=new Personne(4,"Rasoa","Marie",30,1.80);
        pers[3]=marie;
        olona.add(pers[id-1]);
        mv.addItem("Liste_personne",olona);
        return mv;
    }
    @Model(url="load_choice")
    public ModelView load_choice()
    {
        ModelView mv=new ModelView();
        mv.setview("Choix.jsp");
        return mv;
    }
    @Model(url="choice")
    public ModelView getchoix()
    {
        ModelView mv=new ModelView();
        mv.setview("TonChoix.jsp");
        ArrayList<Personne> olona=new ArrayList<Personne>();
        Personne user=new Personne();
        user.setoption(this.getoption());
        olona.add(user);
        mv.addItem("Liste_personne",olona);
        return mv;
    }
    @Model(url="load_upload")
    public ModelView load_upload()
    {
        ModelView mv=new ModelView();
        mv.setview("Upload.jsp");
        return mv;
    }
    @Model(url="upload")
    public ModelView getupload()
    {
        ModelView mv=new ModelView();
        mv.setview("Sary.jsp");
        ArrayList<Personne> olona=new ArrayList<Personne>();
        Personne user=new Personne();
        System.out.print(this.getsary().getname());
        user.setsary(this.getsary());
        olona.add(user);
        mv.addItem("Liste_personne",olona);
        return mv;
    }
}