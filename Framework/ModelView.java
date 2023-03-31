package framework;

public class ModelView {
    private String view;
    public String getview()
    {
        return this.view;
    }
    public void setview(String view)
    {
        this.view=view;
    }

    public ModelView(String view)
    {
        this.setview(view);
    }
}