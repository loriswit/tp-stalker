import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Product
{
    private String m_url;
    private String m_priceTag;
    private String m_nameTag;
    
    private String m_name;
    
    public Product(String url, String priceTag, String nameTag)
    {
        m_url = url;
        m_priceTag = priceTag;
        m_nameTag = nameTag;
    }
    
    public float getLowestPrice() throws Exception
    {
        Document doc = Jsoup.connect(m_url).get();
    
        m_name = doc.select(m_nameTag).first().text();
    
        String value = doc.select(m_priceTag).first().text().replaceAll("'", "");
        return Float.parseFloat(value);
    }
    
    public String getUrl()
    {
        return m_url;
    }
    
    public String getName() throws Exception
    {
        final int maxLength = 70;
        return m_name.length() > maxLength ? m_name.substring(0, maxLength) + "..." : m_name;
    }
}
