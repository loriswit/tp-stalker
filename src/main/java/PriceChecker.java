import java.util.ArrayList;
import java.util.function.Predicate;

public class PriceChecker
{
    private ArrayList<Product> products = new ArrayList<>();
    private FileData file;
    
    private final String configFilename = "config";
    private final String priceTag = "td.spaceVert";
    private final String nameTag = "h1";
    
    public PriceChecker() throws Exception
    {
        file = new FileData(configFilename);
        
        System.out.println("Loading config (" + file.getKeys().size() + " items)");
    
        for(Object key : file.getKeys())
            products.add(new Product(key.toString(), priceTag, nameTag));
    }
    
    public Product addProduct(String url) throws Exception
    {
        if(file.getProperty(url) != null)
        {
            System.out.println("Product " + url + " already exists.");
            return null;
        }
    
        Product product = new Product(url, priceTag, nameTag);
        products.add(product);
    
        product.refresh();
        file.setProperty(url, String.valueOf(product.getPrice()));
        file.save();
    
        System.out.println("Product " + url + " added.");
        
        return product;
    }
    
    public void removeProduct(String url) throws Exception
    {
        if(file.getProperty(url) == null)
        {
            System.out.println("Product " + url + " does not exist.");
            return;
        }
        
        file.remove(url);
        file.save();
    
        Predicate<Product> predicate = (product) -> product.getUrl().equals(url);
        products.removeIf(predicate);
        
        System.out.println("Product " + url + " removed");
    }
    
    public void check()
    {
        try
        {
            for(Product product : products)
            {
                product.refresh();
                float currentPrice = product.getPrice();
                file.refresh();
    
                String property = product.getUrl();
                
                String oldPrice = file.getProperty(product.getUrl());
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
    
                file.setProperty(property, String.valueOf(currentPrice));
                file.save();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.toString());
        }
    }
    
    ArrayList<Product> getProducts()
    {
        return products;
    }
}