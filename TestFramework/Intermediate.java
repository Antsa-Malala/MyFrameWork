package outils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import table.*;
import connexion.Connect;

public class Intermediate extends Object {
    String prefixe;
    int longPK;
    String nomfonction;

    public void setprefixe(String ss)throws Exception {
        if(ss.length()<=3)
        {
            this.prefixe = ss;
        }
        else{
            throw new Exception("Taille de prefixe invalide");
        }
    }

    public void setlongPK(int n) throws Exception{
        if(n>3)
        {
            this.longPK = n;
        }
        else{
            throw new Exception("longueur primary key invalide");
        }
    }
    public void setlongPK(String n) throws Exception{
        if(Intermediate.isNumeric(n))
        {
            this.setlongPK(Integer.valueOf(n));
        }
        else{
            throw new Exception("longueur pk non numerique");
        }
    }

    public static boolean isNumeric(String texte)
    {
        for(Character c : texte.toCharArray()) {
            if (!Character.isDigit(c) && !c.toString().equals("\\.")) {
                return false;
            }
        }
    return true;
    }

    public void setnomfonction(String ss) {
        this.nomfonction = ss;
    }

    public String getprefixe() {
        return this.prefixe;
    }

    public int getlongPK() {
        return this.longPK;
    }

    public String getnomfonction() {
        return this.nomfonction;
    }

    // HISTORISER
    public void historiser(Connection con,String action) throws Exception{
        int noforonina=0;
        if(con==null)
        {
            con=Connect.getconnection("oracle");
            noforonina=1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy");
        Date d = Date.valueOf(LocalDate.now());
        String dateStr = sdf.format(d);
        try {
            Object[] o = this.maka(con,"*");
            Intermediate oi = (Intermediate) o[0];
            Historique h = new Historique("HST","getSeqHist",this.getClass().getSimpleName(),action,dateStr,oi.value());
            h.mapiditra(con);
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        } 
        finally{
            try{
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                throw e;
            }
        }  
    }

    public String value() throws Exception {
        String valeur = "";
        if (methode() == null) {
            throw new Exception("tsisy methode");
        } else {
            Field[] f = this.getClass().getDeclaredFields();
            Method[] methode = methode();
            for (int i = 0; i < methode.length; i++) {
                if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("String")|| methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Date")|| methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Timestamp")) {
                    if (methode[i].invoke(this, (Object[]) null) != null) {
                        valeur += f[i].getName() + ":" + methode[i].invoke(this, (Object[]) null) + ";";
                    } else {
                        if (valeur != "") {
                            valeur += ";";
                        }
                        valeur += "null";
                    }
                } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("int")||methode[i].getReturnType().getSimpleName().equalsIgnoreCase("integer")) {
                    int m = (Integer) (methode[i].invoke(this, (Object[]) null));
                    if (m != 0) {
                        valeur += f[i].getName() + ":" + methode[i].invoke(this, (Object[]) null) + ";";
                    } else {
                        if (valeur != "") {
                            valeur += ";";
                        }
                        valeur += "0";
                    }
                } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Double")) {
                    double m = (Double) (methode[i].invoke(this, (Object[]) null));
                    if (m != 0.0) {
                        valeur += f[i].getName() + ":" + methode[i].invoke(this, (Object[]) null) + ";";
                    } else {
                        if (valeur != "") {
                            valeur += ";";
                        }
                        valeur += "0";
                    }
                }
            }
        }
        return valeur;
    }

    // CREATE PK
    public String construirePK(Connection con) throws Exception{
        String primarykey = "";
        int noforonina=0;
        Statement sta=null;
        ResultSet rs=null;
        if(con==null)
        {
            con =Connect.getconnection("oracle");
            noforonina=1;
        }
        try {
            sta = con.createStatement();
            System.out.println("requete : select " + getnomfonction() + " from dual");
            rs = sta.executeQuery("select " + getnomfonction() + " from dual");
            int seq = 0;
            while (rs.next()) {
                seq = rs.getInt(1);
            }
            primarykey = getprefixe() + completerZero(getlongPK(), seq);
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        }  finally {
            try{
                if( rs != null ){
                    rs.close();
                }
                if(sta != null ){
                    sta.close();
                }
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        return primarykey;
    }

    public String completerZero(int longPK, int seq) {
        String valiny = "";
        int sequence = String.valueOf(seq).length();
        while (valiny.length() + getprefixe().length() + sequence < longPK) {
            valiny += "0";
        }
        valiny += String.valueOf(seq);
        return valiny;
    }

    // INSERT
    public void mapiditra(Connection con) throws Exception{
        int noforonina=0;
        Statement sta=null;
        if(con==null)
        {
            con =Connect.getconnection("oracle");
            noforonina=1;
        }
        try {
            sta = con.createStatement();
            System.out.println("requete : insert into " + this.getClass().getSimpleName() + " values(" + valeur() + ")");
            int rs = sta.executeUpdate("insert into " + this.getClass().getSimpleName() + " values(" + valeur() + ")");
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        } 
        finally{
            try{
                if(sta != null ){
                    sta.close();
                }
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                throw e;
            }
        }  

    }

    // DELETE
    public void mamafa(Connection con) throws Exception {
        this.historiser(con,"delete");
        int noforonina=0;
        Statement sta=null;
        if(con==null)
        {
            con = Connect.getconnection("oracle");
            noforonina=1;
        }
        try {
            sta = con.createStatement();
            System.out.println("requete : delete from " + this.getClass().getSimpleName() + condition());
            int rs = sta.executeUpdate("delete from " + this.getClass().getSimpleName() + condition());
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        } 
        finally{
            try{
                if(sta != null ){
                    sta.close();
                }
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                throw e;
            }
        }  
    }

    // UPDATE
    public void ovay(Connection con) throws Exception {
        this.historiser(con,"update");
        Method m = this.getClass().getMethod("getid");
        int noforonina=0;
        Statement sta=null;
        if(con==null)
        {
            con = Connect.getconnection("oracle");
            noforonina=1;
        }
        try {
            sta = con.createStatement();
            System.out.println("requete : update " + this.getClass().getSimpleName() + manova() + " where ID='"+ m.invoke(this, (Object[]) null) + "'");
            int rs = sta.executeUpdate("update " + this.getClass().getSimpleName() + manova() + " where ID='"+ m.invoke(this, (Object[]) null) + "'");
            con.commit();

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        } 
        finally{
            try{
                if(sta != null ){
                    sta.close();
                }
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                throw e;
            }
        }  

    }

    // SELECT
    public Object[] maka(Connection con,String s) throws Exception {
        Vector<Object> vaovao = new Vector<Object>();
        Statement sta=null;
        ResultSet rs=null;
        if (this.getClass().getDeclaredFields() == null) {
            throw new Exception("Misy tsy norma ny maka field ao");
        } else {
            Field[] fields = this.getClass().getDeclaredFields();
            int noforonina=0;
        if(con==null)
        {
            con =Connect.getconnection("oracle");
            noforonina=1;
        }
            try {
                sta = con.createStatement();
                System.out.println("requete : select " + s + " from " + this.getClass().getSimpleName() + condition());
                rs = sta.executeQuery("select " + s + " from " + this.getClass().getSimpleName() + condition());
                while (rs.next()) {
                    ResultSetMetaData res = rs.getMetaData();
                    Object[] donnees = new Object[res.getColumnCount()];
                    for (int i = 0; i < res.getColumnCount(); i++) {
                        if (fields[i].getType().getSimpleName().equalsIgnoreCase("int")||fields[i].getType().getSimpleName().equalsIgnoreCase("integer")) {
                            donnees[i] = rs.getInt(i + 1);
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("double")) {
                            donnees[i] = rs.getDouble(i + 1);
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("Date")) {
                            donnees[i] = rs.getDate(i + 1);
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("Timestamp")) {
                            donnees[i] = rs.getTimestamp(i + 1);
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("String")) {
                            donnees[i] = rs.getString(i + 1);
                        }
                    }
                    vaovao.add(createObject(donnees));
                }
            } catch (Exception e) {
                try {
                    con.rollback();
                } catch (Exception ex) {
                    throw ex;
                }
            }   finally{
                try{
                    if( rs != null ){
                        rs.close();
                    }
                    if(sta != null ){
                        sta.close();
                    }
                    if(noforonina==1)
                    {
                        con.close();
                    }
                    if (!vaovao.isEmpty()) {
                        Object[] valiny = mamadika(vaovao);
                        return valiny;
                    }
                }
                catch(Exception e)
                {
                    throw e;
                }
            }  
        }
        return null;
    }
    public Object createObject(Object[] donnees) throws Exception {
        if (this.getClass().getDeclaredFields() == null) {
            throw new Exception("Misy tsy norma ny maka field ao");
        } else {
            Field[] fields = this.getClass().getDeclaredFields();
            ArrayList<Class> classe=new ArrayList<Class>();
           for(int i=0;i<fields.length;i++){
                if(!fields[i].getType().getName().equals("framework.FileUpload"))
                {
                    classe.add(fields[i].getType());
                }
            }
            Class[] classes=new Class[classe.size()];
            classe.toArray(classes);
            Constructor c = this.getClass().getDeclaredConstructor(classes);
            Object zavatra = c.newInstance(donnees);
            return zavatra;
        }
    }

    public Object[] mamadika(Vector<Object> v) {
        Object[] objet = new Object[v.size()];
        v.copyInto(objet);
        for (int i = 0; i < objet.length; i++) {
            this.getClass().cast(objet[i]);
        }
        return objet;
    }

    public String condition()
            throws Exception {
        String condition = "";
        if (this.getClass().getDeclaredFields() == null) {
            throw new Exception("Misy tsy norma ny maka field ao");
        } else {
            Field[] field = this.getClass().getDeclaredFields();
            Method[] methode = methode();
            for (int i = 0; i < methode.length; i++) {
                if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("String")|| methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Date")||methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Timestamp")) {
                    if (methode[i].invoke(this, (Object[]) null) != null) {
                        if (condition == "") {
                            condition += " where " + field[i].getName() + " = '"+ methode[i].invoke(this, (Object[]) null) + "'";
                        } else {
                            condition += " and " + field[i].getName() + " = '"+ methode[i].invoke(this, (Object[]) null) + "'";
                        }
                    }
                } else if(methode[i].getReturnType().getSimpleName().equalsIgnoreCase("int")||methode[i].getReturnType().getSimpleName().equalsIgnoreCase("integer")) {
                    int m = (Integer) (methode[i].invoke(this, (Object[]) null));
                    if (m != 0) {
                        if (condition == "") {
                            condition += " where " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                        } else {
                            condition += " and " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                        }
                    }
                } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Double")) {
                    double m = (Double) (methode[i].invoke(this, (Object[]) null));
                    if (m != 0.0) {
                        if (condition == "") {
                            condition += " where " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                        } else {
                            condition += " and " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                        }
                    }
                }
            }
        }
        return condition;
    }

    public Method[] methode() throws Exception {
        if (this.getClass().getDeclaredFields() == null) {
            throw new Exception("Misy tsy norma ny maka field ao");
        } else {
            Field[] field = this.getClass().getDeclaredFields();
            String[] get = new String[field.length];
            Method[] methode = new Method[field.length];
            int i = 0;
            while (i < field.length) {
                get[i] = "get" + field[i].getName();
                i++;
            }
            for (i = 0; i < get.length; i++) {
                if (this.getClass().getMethod(get[i]) == null) {
                    throw new Exception("Tsy misy an'io methode io zany");
                } else {
                    methode[i] = this.getClass().getMethod(get[i]);
                }
            }
            return methode;
        }
    }

    public Method[] methodeset() throws Exception {
        if (this.getClass().getDeclaredFields() == null) {
            throw new Exception("Misy tsy norma ny maka field ao");
        } else {
            Field[] field = this.getClass().getDeclaredFields();
            String[] set = new String[field.length];
            Method[] methode = new Method[field.length];
            int i = 0;
            while (i < field.length) {
                set[i] = "set" + field[i].getName();
                i++;
            }
            for (i = 0; i < set.length; i++) {
                if (this.getClass().getMethod(set[i], field[i].getType()) == null) {
                    throw new Exception("Tsy misy an'io methode io zany");
                } else {
                    methode[i] = this.getClass().getMethod(set[i], field[i].getType());
                }
            }
            return methode;
        }
    }

    public String valeur() throws Exception {
        String valeur = "";
        if (methode() == null) {
            throw new Exception("tsisy methode");
        } else {
            Method[] methode = methode();
            for (int i = 0; i < methode.length; i++) {
                if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("String")) {
                    if (methode[i].invoke(this, (Object[]) null) != null) {
                        if (valeur == "") {
                            valeur += "'" + methode[i].invoke(this, (Object[]) null) + "'";
                        } else {
                            valeur += ",'" + methode[i].invoke(this, (Object[]) null) + "'";
                        }
                    } else {
                        if (valeur != "") {
                            valeur += ",";
                        }
                        valeur += "null";
                    }
                }
                else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Timestamp")) {
                    if (methode[i].invoke(this, (Object[]) null) != null) {
                        if (valeur == "") {
                            valeur += "TO_TIMESTAMP('"+methode[i].invoke(this, (Object[]) null)+"', 'YYYY-MM-DD HH24:MI:SS.FF1')";
                        } else {
                            valeur += ",TO_TIMESTAMP('"+methode[i].invoke(this, (Object[]) null)+"', 'YYYY-MM-DD HH24:MI:SS.FF1')";
                        }
                    } else {
                        if (valeur != "") {
                            valeur += ",";
                        }
                        valeur += "null";
                    }
                } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("date")) {
                    if (methode[i].invoke(this, (Object[]) null) != null) {
                        if (valeur == "") {
                            valeur += "TO_DATE('"+methode[i].invoke(this, (Object[]) null)+"', 'YYYY-MM-DD')";
                        } else {
                            valeur += ",TO_DATE('"+methode[i].invoke(this, (Object[]) null)+"', 'YYYY-MM-DD')";
                        }
                    } else {
                        if (valeur != "") {
                            valeur += ",";
                        }
                        valeur += "null";
                    }
                }else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("int")||methode[i].getReturnType().getSimpleName().equalsIgnoreCase("integer")) {
                    int m = (Integer) (methode[i].invoke(this, (Object[]) null));
                    if (m != 0) {
                        if (valeur == "") {
                            valeur += methode[i].invoke(this, (Object[]) null);
                        } else {
                            valeur += "," + methode[i].invoke(this, (Object[]) null);
                        }
                    } else {
                        if (valeur != "") {
                            valeur += ",";
                        }
                        valeur += "0";
                    }
                } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Double")) {
                    double m = (Double) (methode[i].invoke(this, (Object[]) null));
                    if (m != 0.0) {
                        if (valeur == "") {
                            valeur += methode[i].invoke(this, (Object[]) null);
                        } else {
                            valeur += "," + methode[i].invoke(this, (Object[]) null);
                        }
                    } else {
                        if (valeur != "") {
                            valeur += ",";
                        }
                        valeur += "0";
                    }
                }
            }
        }
        return valeur;
    }

    public String manova()
            throws Exception {
        String condition = "";
        if (this.getClass().getDeclaredFields() == null) {
            throw new Exception("Misy tsy norma ny maka field ao");
        } else {
            Field[] field = this.getClass().getDeclaredFields();
            if (methode() == null) {
                throw new Exception("tsisy methode");
            } else {
                Method[] methode = methode();
                for (int i = 0; i < methode.length; i++) {
                    if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("String")|| methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Date")) {
                        if (methode[i].invoke(this, (Object[]) null) != null) {
                            if (condition == "") {
                                condition += " set " + field[i].getName() + " = '"+ methode[i].invoke(this, (Object[]) null) + "'";
                            } else {
                                condition += " , " + field[i].getName() + " = '"+ methode[i].invoke(this, (Object[]) null) + "'";
                            }
                        }
                    } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Timestamp")) {
                        if (methode[i].invoke(this, (Object[]) null) != null) {
                            if (condition == "") {
                                condition += " set " + field[i].getName() + " = TO_TIMESTAMP('"+methode[i].invoke(this, (Object[]) null)+"', 'YYYY-MM-DD HH24:MI:SS.FF1')";
                            } else {
                                condition += " , " + field[i].getName() + " = TO_TIMESTAMP('"+methode[i].invoke(this, (Object[]) null)+"', 'YYYY-MM-DD HH24:MI:SS.FF1')";
                            }
                        }
                    } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("int")||methode[i].getReturnType().getSimpleName().equalsIgnoreCase("integer")) {
                        int m = (Integer) (methode[i].invoke(this, (Object[]) null));
                        if (m != 0) {
                            if (condition == "") {
                                condition += " set " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                            } else {
                                condition += " , " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                            }
                        }
                    } else if (methode[i].getReturnType().getSimpleName().equalsIgnoreCase("Double")) {
                        double m = (Double) (methode[i].invoke(this, (Object[]) null));
                        if (m != 0.0) {
                            if (condition == "") {
                                condition += " set " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                            } else {
                                condition += " , " + field[i].getName() + " = "+ methode[i].invoke(this, (Object[]) null);
                            }
                        }
                    }
                }
            }
        }
        return condition;
    }

    // AFFICHE
    public void affiche(Object[] o)throws Exception {
        JFrame jd = new JFrame();
        jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jd.setResizable(false);
        jd.getContentPane().setLayout(new GridLayout(0, 4));
        for (int i = 0; i < o.length; i++) {
            if (this.getClass().cast(o[i]).methode() == null) {
                throw new Exception("tsisy methode");
            } else {
                Method[] methode = this.getClass().cast(o[i]).methode();
                JPanel jp = new JPanel(new GridLayout(0, 1));
                jp.add(new JLabel("NUMERO " + String.valueOf(i + 1)));
                for (int j = 0; j < methode.length; j++) {
                    jp.add(new JLabel(String.valueOf(methode[j].invoke(o[i], (Object[]) null))));
                }
                jd.getContentPane().add(jp);
            }
        }
        jd.pack();
        jd.setVisible(true);
    }
    public Intermediate() throws Exception{
        this.setlongPK(7);
    }
    public Intermediate(String prefixe,String nomfonction) throws Exception{
        this.setlongPK(7);
        this.setprefixe(prefixe);
        this.setnomfonction(nomfonction);
    }
}