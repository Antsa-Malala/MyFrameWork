package table;

import java.lang.reflect.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;
import connexion.Connect;
import java.time.LocalDate;
import outils.*;

public class Historique extends Intermediate {
    String id;
    String table;
    String action;
    String daty;
    String valeur;

    public void setid(String id) {
        this.id = id;
    }

    public void settable(String tab) {
        this.table = tab;
    }

    public void setaction(String act) throws Exception{
        if(act.equalsIgnoreCase("delete")||act.equalsIgnoreCase("update"))
        {
            this.action = act;
        }
        else{
            throw new Exception("action invalide");
        }
    }

    public void setdaty(String d) throws Exception{
        if(Date.valueOf(LocalDate.now()).after(Date.valueOf(d))||Date.valueOf(LocalDate.now()).equals(Date.valueOf(d)))
        {
            this.daty = d;
        }
        else{
            throw new Exception("date historique invalide");
        }
    }

    public void setvaleur(String val) {
        this.valeur = val;
    }

    public String getid() {
        return this.id;
    }

    public String gettable() {
        return this.table;
    }

    public String getaction() {
        return this.action;
    }

    public String getdaty() {
        return this.daty;
    }

    public String getvaleur() {
        return this.valeur;
    }

    public Historique() throws Exception{
    }

    public Historique(String table, String action, String d, String valeur)  throws Exception{
        this.setid(this.construirePK(null));
        this.settable(table);
        this.setaction(action);
        this.setdaty(d);
        this.setvaleur(valeur);
    }


    public Historique(String prefixe, String nomfonction,String table,String action,String daty,String valeur)  throws Exception{
        super(prefixe,nomfonction);
        this.setid(super.construirePK(null));
        this.settable(table);
        this.setaction(action);
        this.setdaty(daty);
        this.setvaleur(valeur);
    }
    
}