package models;
import annotation.Model;
import framework.ModelView;
import java.util.ArrayList;

public class Personne {
    private String nom;
    private String prenom;
    private int age;
    private double mesure;

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

    public Personne(String nom,String prenom,int age,double mesure)
    {
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
        Personne jean=new Personne("Rakoto","Jean",16,1.60);
        Personne robert=new Personne("Randria","Robert",18,1.66);
        Personne jeanne=new Personne("Andria","Jeanne",20,1.76);
        Personne marie=new Personne("Rasoa","Marie",30,1.80);
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
        ModelView mv=new ModelView();
        mv.setview("Valider.jsp");
        ArrayList<Personne> olona=new ArrayList<Personne>();
        Personne user=new Personne(this.getnom(),this.getprenom(),this.getage(),this.getmesure());
        olona.add(user);
        mv.addItem("Liste_personne",olona);
        return mv;
    }
}