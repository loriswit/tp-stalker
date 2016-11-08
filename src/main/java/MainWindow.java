import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.net.URI;
import java.util.TimerTask;

public class MainWindow extends TimerTask
{
    private PriceChecker priceChecker;
    private DefaultTableModel model;
    
    public MainWindow() throws Exception
    {
        model = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
    
        model.setColumnIdentifiers(new String[]{"Product", "Price", "URL"});
        
        priceChecker = new PriceChecker();
        updateTable();
    
        table.setModel(model);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.removeColumn(columnModel.getColumn(model.getColumnCount()-1));
    
        frame = new JFrame("Toppreise Stalker");
        frame.setContentPane(view);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        addButton.addActionListener(actionEvent ->
        {
            String url = JOptionPane.showInputDialog("Enter product URL:");
            if(url == null)
                return;
            
            try
            {
                priceChecker.addProduct(url);
                updateTable();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(frame, "Failed to add product.\n(" + e.toString() + ")",
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
        removeButton.addActionListener(actionEvent ->
        {
            int selectedRow = table.getSelectedRow();
            if(selectedRow < 0)
                return;
            
            String url = model.getValueAt(selectedRow, model.getColumnCount()-1).toString();
            
            try
            {
                priceChecker.removeProduct(url);
                updateTable();
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(frame, "Failed to remove product.\n(" + e.toString() + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
        openWebpageButton.addActionListener(actionEvent ->
        {
            int selectedRow = table.getSelectedRow();
            if(selectedRow < 0)
                return;

            String url = model.getValueAt(selectedRow, model.getColumnCount()-1).toString();
            
            try
            {
                if(Desktop.isDesktopSupported())
                    Desktop.getDesktop().browse(new URI(url));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }
    
    @Override
    public void run()
    {
        priceChecker.check();
        updateTable();
    }
    
    private void updateTable()
    {
        while(model.getRowCount() > 0)
            model.removeRow(0);
        
        for(Product product : priceChecker.getProducts())
            model.addRow(new Object[]{product.getName(), product.getPrice(), product.getUrl()});
    }
    
    private JFrame frame;
    private JButton addButton;
    private JButton removeButton;
    private JPanel view;
    private JTable table;
    private JButton openWebpageButton;
}
