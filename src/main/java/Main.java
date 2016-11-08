import javax.swing.UIManager;
import java.util.Timer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        
        MainWindow window = new MainWindow();
    
        Timer timer = new Timer();
        timer.schedule(window, 5000, 5000);
    }
}
