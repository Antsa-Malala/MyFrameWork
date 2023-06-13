package models;
import framework.ModelView;
import annotation.Model;

public class Login {
    String username;
    String mdp;

    public void setusername(String username)
    {
        this.username=username;
    }
    public void setmdp(String mdp)
    {
        this.mdp=mdp;
    }

    public String getusername()
    {
        return this.username;
    }
    public String getmdp()
    {
        return this.mdp;
    }

    @Model(url="load_login")
    public ModelView load_form()
    {
        ModelView mv=new ModelView();
        mv.setview("Login.jsp");
        return mv;
    }

    @Model(url="Login")
    public ModelView authentification() throws Exception
    {
        String[] user=new String[2];
        user[0]="admin";
        user[1]="simple";

        String[] pass=new String[2];
        pass[0]="mdpa";
        pass[1]="mdps";
        
        int check=0;
        ModelView mv=new ModelView();
        for(int i=0;i<user.length;i++)
        {
            if(this.username.equals(user[i])&&this.mdp.equals(pass[i]))
            {
                mv.addSession("isConnected", true);
                mv.addSession("profil", this.username);
                mv.setview("Ok.jsp");
                check=1;
                break;
            }
        }
        if(check==0)
        {
            mv.setview("No.jsp");
        }
        return mv;
    }
    public Login(String username,String mdp)
    {
        this.setusername(username);
        this.setmdp(mdp);
    }
    public Login()
    {
        
    }
}
