package framework;
import java.util.ArrayList;
import java.util.HashMap;

public class ModelView {
    private String view;
    private HashMap<String,Object> data;
    private HashMap<String,Object> session;
    private boolean isJson;
    private boolean invalidateSession;
    private ArrayList<String> removeSession;

    public String getview()
    {
        return this.view;
    }
    public void setview(String view)
    {
        this.view=view;
    }
    public void setdata(HashMap<String,Object> data)
    {
        this.data=data;
    }
    public void setsession(HashMap<String,Object> session)
    {
        this.session=session;
    }

    public HashMap<String,Object> getdata()
    {
        return this.data;
    }
    
    public HashMap<String,Object> getsession()
    {
        return this.session;
    }

    public void addItem(String cle,Object value)
    {
        this.data.put(cle,value);
    }

    public void addSession(String cle,Object value)
    {
        this.session.put(cle,value);
    }
    public boolean getisJson()
    {
        return this.isJson;
    }
    public void setisJson(boolean isJson)
    {
        this.isJson=isJson;
    }
    public boolean getinvalidateSession()
    {
        return this.invalidateSession;
    }
    public void setinvalidateSession(boolean invalidateSession)
    {
        this.invalidateSession=invalidateSession;
    }
    public ArrayList<String> getremoveSession()
    {
        return this.removeSession;
    }
    public void setremoveSession(ArrayList<String> session)
    {
        this.removeSession=session;
    }
    public void addRemoveSession(String sessionName)
    {
        this.removeSession.add(sessionName);
    }

    public ModelView()
    {
        this.setdata(new HashMap<String,Object>());
        this.setsession(new HashMap<String,Object>());
        this.setisJson(false);
        this.setinvalidateSession(false);
        this.setremoveSession(new ArrayList<String>());
    }
}