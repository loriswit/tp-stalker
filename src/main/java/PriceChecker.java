import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TimerTask;
import java.util.function.Predicate;

public class PriceChecker extends TimerTask
{
    private ArrayList<Product> products = new ArrayList<>();
    private Properties config = new Properties();
    
    private final String configFilename = "config";
    private final String priceTag = "td.spaceVert";
    private final String nameTag = "h1";
    
    public PriceChecker() throws Exception
    {
        File configFile = new File(configFilename);
        if(!configFile.exists())
            configFile.createNewFile();
    
        FileInputStream inputFile = new FileInputStream(configFilename);
        config.load(inputFile);
        inputFile.close();
    
        for(Object key : config.keySet())
            products.add(new Product(key.toString(), priceTag, nameTag));
    }
    
    public void addProduct(String url) throws Exception
    {
        FileInputStream inputFile = new FileInputStream(configFilename);
        config.load(inputFile);
        inputFile.close();
        
        if(config.getProperty(url) != null)
            return;
    
        Product product = new Product(url, priceTag, nameTag);
        products.add(product);
    
        config.setProperty(url, String.valueOf(product.getLowestPrice()));
    
        FileOutputStream outputFile = new FileOutputStream(configFilename);
        config.store(outputFile, null);
        outputFile.close();
    
        System.out.println("Product " + url + " added.");
    }
    
    public void removeProduct(String url) throws Exception
    {
        FileInputStream inputFile = new FileInputStream(configFilename);
        config.load(inputFile);
        inputFile.close();
        
        config.remove(url);
    
        Predicate<Product> predicate = (product) -> product.getUrl().equals(url);
        products.removeIf(predicate);
        
        System.out.println("Product " + url + " removed");
    }
    
    @Override
    public void run()
    {
        try
        {
            for(Product product : products)
            {
                float currentPrice = product.getLowestPrice();
                FileInputStream inputFile = new FileInputStream(configFilename);
                config.load(inputFile);
                inputFile.close();
    
                String property = product.getUrl();
                
                String oldPrice = config.getProperty(product.getUrl());
                if(oldPrice == null)
                    throw new Exception("Property " + property + " does not exist.");
                
                if(currentPrice < Float.parseFloat(oldPrice))
                {
                    float difference = Float.parseFloat(oldPrice) - currentPrice;
                    System.out.print("Price of " + product.getName() + " has been lowered to CHF ");
                    System.out.println(currentPrice + " (CHF " + difference + " cheaper).");
                    System.out.println("Check it out: " + product.getUrl());
                }
                else
                {
                    System.out.print("Price of " + product.getName() + " has not been lowered");
                    System.out.println(" (CHF " + currentPrice + ").");
                }
    
                config.setProperty(property, String.valueOf(currentPrice));

                FileOutputStream outputFile = new FileOutputStream(configFilename);
                config.store(outputFile, null);
                outputFile.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.toString());
            e.printStackTrace();
        }
    }
}