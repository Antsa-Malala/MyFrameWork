package models;
import outils.Intermediate;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import annotation.Model;
import annotation.Auth;
import framework.ModelView;
import framework.FileUpload;
import connexion.Connect;
public class PlatC extends Intermediate{
    String id;
    Date daty;
    String idemp;
    String idplat;
    FileUpload sary;

    public void setid(String id)
    {
        this.id=id;
    }

    public void setid() throws Exception{
        this.id = super.construirePK(null);
    }
    public void setdaty(Date daty)
    {
        this.daty=daty;
    }
    public Date getdaty()
    {
        return this.daty;
    }

    public void setidemp(String idemp)
    {
        this.idemp=idemp;
    }

    public void setidplat(String idplat)
    {
        this.idplat=idplat;
    }

    public String getidemp()
    {
        return this.idemp;
    }
    public String getidplat()
    {
        return this.idplat;
    }
    public String getid()
    {
        return this.id;
    }
    public void setsary(FileUpload s)
    {
        this.sary=s;
    }
    public FileUpload getsary()
    {
        return this.sary;
    }

    public PlatC(String id,Date daty,String idemp,String idplat) throws Exception
    {
        this.setid(id);
        this.setdaty(daty);
        this.setidemp(idemp);
        this.setidplat(idplat);
        
    }
    public PlatC(String id) throws Exception
    {
        this.setid(id);
    }
    public PlatC() throws Exception{
        
    }

    @Auth
    @Model(url="load_plat")
    public ModelView load_plat() throws Exception
    {
        System.out.println("bui");
        ModelView mv=new ModelView();
        Object[] p=new Plat().maka(null,"*");
        mv.addItem("liste_plat",p);
        Object[] e=new Employe().maka(null,"*");
        mv.addItem("liste_employe",e);
        mv.setview("Plat.jsp");
        return mv;
    }
    @Auth
    @Model(url="insert_platC")
    public ModelView insert_platC() throws Exception
    {
        Connection con =Connect.getconnection("oracle");
        ModelView mv=new ModelView();
        try{
            PlatC t=new PlatC();
            t.setprefixe("PLC");
            t.setnomfonction("getSeqPlatC");
            t.setid();
            System.out.println(this.getdaty());
            t.setdaty(this.getdaty());
            t.setidemp(this.getidemp());
            t.setidplat(this.getidplat());
            t.mapiditra(con);
            t.setsary(this.getsary());
            t.insert_file(con);
            Object[] v=new Plat().maka(null,"*");
            mv.addItem("liste_plat",v);
            Object[] c=new Employe().maka(null,"*");
            mv.addItem("liste_employe",c);
            mv.setview("Plat.jsp");
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        }  finally {
            con.close();
            return mv;
        }
    }

    @Model(url="insert_file")
    public void insert_file(Connection con) throws Exception
    {
        int noforonina=0;
        PreparedStatement preparedStatement =null;
        if(con==null)
        {
            con =Connect.getconnection("oracle");
            noforonina=1;
        }
        try {
            String sql = "INSERT INTO fichier_plat VALUES (?,?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, this.getid());
            preparedStatement.setBytes(2, this.getsary().getsary());
            preparedStatement.executeUpdate();
            con.commit();
        } catch (Exception e) {
            try {
                e.printStackTrace();
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }  finally {
            try{
                if(preparedStatement != null ){
                    preparedStatement.close();
                }
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Model(url="liste_plat")
    public ModelView liste_plat() throws Exception
    {
        Connection con =Connect.getconnection("oracle");
        ModelView mv=new ModelView();
        try{
        Object[] t=new PlatC().maka(null,"*");
        mv.addItem("liste_plat",t);
        ArrayList<String> sary=new ArrayList<String>();
        for(int i=0;i<t.length;i++)
        {
            PlatC tr=(PlatC) t[i];
            String id=tr.getid();
            sary.add(get_file(null,id));
        }
        mv.addItem("liste_sary",sary);
        mv.setview("Liste_plat.jsp");
        con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {
                throw ex;
            }
        }  finally {
            con.close();
            return mv;
        }
    }

    public String get_file(Connection con,String id) throws Exception
    {
        int noforonina=0;
        ResultSet rs =null;
        Statement sta=null;
        if(con==null)
        {
            con =Connect.getconnection("oracle");
            noforonina=1;
        }
        try {
            sta = con.createStatement();
            System.out.println("select sary from fichier_plat where idplatc='"+id+"'");
            rs = sta.executeQuery("select sary from fichier_plat where idplatc='"+id+"'");
            while (rs.next()) {
                byte[] binaryData = rs.getBytes("sary");
                String base64Image = java.util.Base64.getEncoder().encodeToString(binaryData);
                return base64Image;
            }
            
        } catch (Exception e) {
            try {
                e.printStackTrace();
                con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }  finally {
            try{
                if(rs != null ){
                    rs.close();
                }
                if(noforonina==1)
                {
                    con.close();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return "";
    }
    @Model(url="plat.xml")
    public ModelView plat() throws Exception
    {
        ModelView mv=new ModelView();
        mv.setisXml(true);
        mv.setdataxml(this.xml());
        return mv;
    }

    public String xml() throws Exception {
        Object[] t = new PlatC().maka(null, "*");
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlBuilder.append("<platconsomme>");
    
        for (int i = 0; i < t.length; i++) {
            PlatC plat = (PlatC) t[i];
            xmlBuilder.append("<platconsomme>");
            xmlBuilder.append("<date>").append(plat.getdaty()).append("</date>");
            Employe c = (Employe) new Employe(plat.getidemp()).maka(null, "*")[0];
            xmlBuilder.append("<nom>").append(c.getnom()).append("</nom>");
            Plat v = (Plat) new Plat(plat.getidplat()).maka(null, "*")[0];
            xmlBuilder.append("<plat>").append(v.getlibelle()).append("</plat>");
            xmlBuilder.append("</platconsomme>");
        }
    
        xmlBuilder.append("</platconsomme>");
        return xmlBuilder.toString();
    }
}
