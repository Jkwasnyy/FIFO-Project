import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Orders extends JFrame{
    private JPanel panel1;
    private JTextField textCustomer;
    private JTextField textProduct;
    private JTextField textOrderName;
    private JTextField textDate;
    private JComboBox comboBox1;
    private JButton searchButton;
    private JButton ADDButton;
    private JButton EXPORTButton;
    private JButton DELETEButton;
    private JTable table1;
    private int width = 500, height = 400;

    public Orders() {
        super("Orders");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        Connect();
        InitializeTable();
        LoadOrderNo();
        Fetch();

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String id_customer = textCustomer.getText();
                    String id_product = textProduct.getText();
                    String order_name = textOrderName.getText();

                    pst = connection.prepareStatement("INSERT INTO orders_tbl (id_customer,id_product,order_name) VALUES (?,?,?)");
                    pst.setString(1,id_customer);
                    pst.setString(2,id_product);
                    pst.setString(3,order_name);

                    int k = pst.executeUpdate();

                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record Added!");
                        textCustomer.setText("");
                        textProduct.setText("");
                        textOrderName.setText("");
                        textDate.setText("");
                        Fetch();
                        LoadOrderNo();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Record failed to saved!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pid = comboBox1.getSelectedItem().toString();
                try {
                    pst = connection.prepareStatement("SELECT * FROM orders_tbl WHERE order_id=?");
                    pst.setString(1,pid);
                    rs=pst.executeQuery();
                    if(rs.next() == true){
                        textCustomer.setText(rs.getString(2));
                        textProduct.setText(rs.getString(3));
                        textDate.setText(rs.getString(4));
                        textOrderName.setText(rs.getString(5));
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"No record found!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        });

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pst = connection.prepareStatement("DELETE FROM orders_tbl ORDER BY order_date ASC LIMIT 1");
                    int k = pst.executeUpdate();
                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record has been succesfully deleted!");
                        textCustomer.setText("");
                        textProduct.setText("");
                        textOrderName.setText("");
                        textDate.setText("");
                        textCustomer.requestFocus();
                        LoadOrderNo();
                        Fetch();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Record failed to delete!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,ex);
                }

            }
        });

        EXPORTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = "C:/Users/Admin/IdeaProjects/FIFO/Exported_orders_file.csv";
                try {
                    FileWriter fw = new FileWriter(filename);
                    pst = connection.prepareStatement("SELECT * FROM orders_tbl");
                    rs = pst.executeQuery();
                    while(rs.next()){
                        fw.append(rs.getString(1));
                        fw.append(';');
                        fw.append(rs.getString(2));
                        fw.append(';');
                        fw.append(rs.getString(3));
                        fw.append(';');
                        fw.append(rs.getString(4));
                        fw.append(';');
                        fw.append(rs.getString(5));
                        fw.append('\n');
                    }
                    JOptionPane.showMessageDialog(null,"Exported to CSV file!");
                    fw.flush();
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    Connection connection;
    PreparedStatement pst;
    ResultSet rs;
    public void Connect(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/shop","root","");
        } catch (ClassNotFoundException e) {
            //throw new RuntimeException(e);
            Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,e);
        } catch (SQLException e) {
            Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    public void LoadOrderNo(){
        try {
            pst = connection.prepareStatement("SELECT order_id FROM orders_tbl");
            rs = pst.executeQuery();
            comboBox1.removeAllItems();
            while(rs.next()){
                comboBox1.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    private void InitializeTable(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("order_id");
        model.addColumn("id_customer");
        model.addColumn("id_product");
        model.addColumn("order_date");
        model.addColumn("order_name");
        table1.setModel(model);
    }

    private void Fetch(){
        try {
            int q;
            pst = connection.prepareStatement("SELECT * FROM orders_tbl");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            q = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel)table1.getModel();
            df.setRowCount(0);
            while(rs.next()){
                Vector v2 = new Vector();
                v2.add(rs.getString("order_id"));
                v2.add(rs.getString("id_customer"));
                v2.add(rs.getString("id_product"));
                v2.add(rs.getString("order_date"));
                v2.add(rs.getString("order_name"));
                df.addRow(v2);
            }
        } catch (SQLException e) {
            Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,e);
        }
    }

}
