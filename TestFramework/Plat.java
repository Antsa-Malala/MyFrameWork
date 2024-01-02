package models;
import outils.Intermediate;
import java.sql.*;
import java.util.ArrayList;

import annotation.Model;
import annotation.Auth;
import framework.ModelView;
import framework.FileUpload;
import connexion.Connect;
public class Plat extends Intermediate {
    String id;
    String libelle;

    public void setid(String id)
    {
        this.id=id;
    }

    public void setid() throws Exception{
        this.id = super.construirePK(null);
    }
    public void setlibelle(String libelle)
    {
        this.libelle=libelle;
    }
    public String getid()
    {
        return this.id;
    }
    public String getlibelle()
    {
        return this.libelle;
    }
    public Plat(String id,String libelle) throws Exception
    {
        this.setid(id);
        this.setlibelle(libelle);
    }
    public Plat(String id) throws Exception
    {
        this.setid(id);
    }
    public Plat() throws Exception
    {

    }

}
