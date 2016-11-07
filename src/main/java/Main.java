import java.util.Timer;

public class Main
{
    public static void main(String[] args)
    {
        Timer timer = new Timer();
        try
        {
            PriceChecker priceChecker = new PriceChecker();
            timer.schedule(priceChecker, 0, 5000);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.toString());
        }
    }
}
