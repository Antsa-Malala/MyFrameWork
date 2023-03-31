package models;
import annotation.Model;

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

    @Model(url="Personne/nomcomplet")
    public String get_nomcomplet()
    {
        return this.getnom()+" "+this.getprenom();
    }
}