package framework;

public class FileUpload {
    String path;
    String name;
    byte[] sary;

    public void setpath(String path)
    {
        this.path=path;
    }
    public void setname(String name)
    {
        this.name=name;
    }
    public void setsary(byte[] sary)
    {
        this.sary=sary;
    }

    public String getname()
    {
        return this.name;
    }
    public String getpath()
    {
        return this.path;
    }
    public byte[] getsary()
    {
        return this.sary;
    }

    public FileUpload(String path,String name,byte[] sary)
    {
        this.setname(name);
        this.setpath(path);
        this.setsary(sary);
    }
    public FileUpload()
    {

    }
}
