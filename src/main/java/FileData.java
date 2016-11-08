import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

public class FileData
{
    private Properties config = new Properties();
    private File file;
    
    public FileData(String filename) throws Exception
    {
        file = new File(filename);
        if(!file.exists())
            file.createNewFile();
    
        refresh();
    }
    
    public void refresh() throws Exception
    {
        FileInputStream inputFile = new FileInputStream(file.getName());
        config.load(inputFile);
        inputFile.close();
    }
    
    public void save() throws Exception
    {
        FileOutputStream outputFile = new FileOutputStream(file.getName());
        config.store(outputFile, null);
        outputFile.close();
    }
    
    public Properties getProperties()
    {
        return config;
    }
    
    public String getProperty(String key)
    {
        return config.getProperty(key);
    }
    
    public void setProperty(String key, String value)
    {
        config.setProperty(key, value);
    }
    
    public void remove(String key)
    {
        config.remove(key);
    }
    
    public Set<Object> getKeys()
    {
        return config.keySet();
    }
}
