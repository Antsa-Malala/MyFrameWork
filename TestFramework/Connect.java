package connexion;

import java.sql.DriverManager;
import java.sql.Connection;

public class Connect {
    public static Connection getconnection(String base) throws Exception{
        try{
            if(base.equalsIgnoreCase("mysql"))
            {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/katsaka", "root", "abc");
                return connection;
            }
            else if(base.equalsIgnoreCase("oracle"))
            {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","servlet","servlet");
                return con;
            }
            else{
                Class.forName("org.postgresql.Driver");
                Connection connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/servlet", "servlet", "servlet");
                return connect;    
            }
        }
        catch(Exception e){
            throw e;
        }
    }
}  
