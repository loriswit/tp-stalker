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
        for(Product product : priceChecker.getProducts())
            model.addRow(new Object[]{product.getName(), product.getPrice(), product.getUrl()});
    
        table.setModel(model);
        table.setRowSorter(null);
        
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
                Product product = priceChecker.addProduct(url);
                if(product != null)
                    model.addRow(new Object[]{product.getName(), product.getPrice(), product.getUrl()});
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
                for(int i=0; i<table.getRowCount(); ++i)
                    if(model.getValueAt(i, model.getColumnCount()-1).toString().equals(url))
                    {
                        model.removeRow(i);
                        return;
                    }
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
        for(Product product : priceChecker.getProducts())
            for(int i=0; i<table.getRowCount(); ++i)
                if(model.getValueAt(i, model.getColumnCount()-1).toString().equals(product.getUrl()))
                    model.setValueAt(product.getPrice(), i, 1);
    }
    
    private JFrame frame;
    private JButton addButton;
    private JButton removeButton;
    private JPanel view;
    private JTable table;
    private JButton openWebpageButton;
}
