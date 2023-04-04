package framework;
import java.util.HashMap;

public class ModelView {
    private String view;
    private HashMap<String,Object> data;

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

    public HashMap<String,Object> getdata()
    {
        return this.data;
    }

    public void addItem(String cle,Object value)
    {
        this.data.put(cle,value);
    }


    public ModelView()
    {

    }
}