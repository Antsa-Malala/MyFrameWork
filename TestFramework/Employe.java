package models;
import outils.Intermediate;
import java.sql.*;
import java.util.ArrayList;

import annotation.Model;
import annotation.Auth;
import framework.ModelView;
import framework.FileUpload;
import connexion.Connect;
public class Employe extends Intermediate {
    String id;
    String nom;
    String prenom;

    public void setid(String id)
    {
        this.id=id;
    }

    public void setid() throws Exception{
        this.id = super.construirePK(null);
    }
    public void setnom(String nom)
    {
        this.nom=nom;
    }
    public void setprenom(String prenom)
    {
        this.prenom=prenom;
    }
    public String getid()
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
    public Employe(String id,String nom,String prenom) throws Exception
    {
        this.setid(id);
        this.setnom(nom);
        this.setprenom(prenom);
    }
    public Employe(String id) throws Exception
    {
        this.setid(id);
    }
    public Employe() throws Exception
    {

    }

}
