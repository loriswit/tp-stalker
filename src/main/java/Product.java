import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Product
{
    private String m_url;
    private String m_priceTag;
    private String m_nameTag;
    
    private float m_price;
    private String m_name;
    
    public Product(String url, String priceTag, String nameTag) throws Exception
    {
        m_url = url;
        m_priceTag = priceTag;
        m_nameTag = nameTag;
    
        refresh();
    }
    
    public void refresh() throws Exception
    {
        Document doc = Jsoup.connect(m_url).get();
    
        m_name = doc.select(m_nameTag).first().text();
    
        String value = doc.select(m_priceTag).first().text().replaceAll("'", "");
        m_price = Float.parseFloat(value);
    }
    
    public String getUrl()
    {
        return m_url;
    }
    
    public String getName()
    {
        final int maxLength = 70;
        return m_name.length() > maxLength ? m_name.substring(0, maxLength) + "..." : m_name;
    }
    
    public float getPrice()
    {
        return m_price;
    }
}
