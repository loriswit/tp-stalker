import javax.swing.*;
import java.util.Timer;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        Timer timer = new Timer();
        
        try
        {
            MainWindow window = new MainWindow();
            timer.schedule(window, 5000, 5000);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString() + "\nCheck your Internet connexion.",
                "Error", JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
            
            timer.cancel();
        }
    }
}
