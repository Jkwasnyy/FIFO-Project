import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Products extends JFrame{
    private JPanel panel1;
    private JTextField textPrice;
    private JTextField textQty;
    private JTextField textUseBefore;
    private JTextField textName;
    private JComboBox comboBox1;
    private JButton searchButton;
    private JButton ADDButton;
    private JButton NEWButton;
    private JButton UPDATEButton;
    private JButton DELETEButton;
    private JTable table1;
    private int width = 500, height = 400;

    public Products(){
        super("Products");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        Connect();
        InitializeTable();
        LoadProductNo();
        Fetch();

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String product_name = textName.getText();
                    String price = textPrice.getText();
                    String qty = textQty.getText();
                    String usage_date = textUseBefore.getText();

                    pst = connection.prepareStatement("INSERT INTO products_tbl (product_name,price,qty,usage_date) VALUES (?,?,?,?)");
                    pst.setString(1,product_name);
                    pst.setString(2,price);
                    pst.setString(3,qty);
                    pst.setString(4,usage_date);

                    int k = pst.executeUpdate();

                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record Added!");
                        textName.setText("");
                        textPrice.setText("");
                        textQty.setText("");
                        textUseBefore.setText("");
                        Fetch();
                        LoadProductNo();
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
                    pst = connection.prepareStatement("SELECT * FROM products_tbl WHERE id=?");
                    pst.setString(1,pid);
                    rs=pst.executeQuery();
                    if(rs.next() == true){
                        textName.setText(rs.getString(2));
                        textPrice.setText(rs.getString(3));
                        textQty.setText(rs.getString(4));
                        textUseBefore.setText(rs.getString(5));
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"No record found!");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        });

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pname = textName.getText();
                String price = textPrice.getText();
                String qty = textQty.getText();
                String date = textUseBefore.getText();
                String pid = comboBox1.getSelectedItem().toString();

                try {
                    pst = connection.prepareStatement("UPDATE products_tbl SET product_name=?,price=?,qty=?,usage_date=? WHERE id=?");
                    pst.setString(1,pname);
                    pst.setString(2,price);
                    pst.setString(3,qty);
                    pst.setString(4,date);
                    pst.setString(5,pid);
                    int k = pst.executeUpdate();
                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record has been succesfully updated!");
                        textName.setText("");
                        textPrice.setText("");
                        textQty.setText("");
                        textUseBefore.setText("");
                        textName.requestFocus();
                        LoadProductNo();
                        Fetch();
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        });

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pid = comboBox1.getSelectedItem().toString();
                try {
                    pst = connection.prepareStatement("DELETE FROM products_tbl WHERE id=?");
                    pst.setString(1,pid);
                    int k = pst.executeUpdate();
                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record has been succesfully deleted!");
                        textName.setText("");
                        textPrice.setText("");
                        textQty.setText("");
                        textUseBefore.setText("");
                        textName.requestFocus();
                        LoadProductNo();
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

    public void LoadProductNo(){
        try {
            pst = connection.prepareStatement("SELECT id FROM products_tbl");
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
        model.addColumn("id");
        model.addColumn("product_name");
        model.addColumn("price");
        model.addColumn("qty");
        model.addColumn("usage_date");
        table1.setModel(model);
    }

    private void Fetch(){
        try {
            int q;
            pst = connection.prepareStatement("SELECT * FROM products_tbl");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            q = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel)table1.getModel();
            df.setRowCount(0);
            while(rs.next()){
                Vector v2 = new Vector();
                    v2.add(rs.getString("id"));
                    v2.add(rs.getString("product_name"));
                    v2.add(rs.getString("price"));
                    v2.add(rs.getString("qty"));
                    v2.add(rs.getString("usage_date"));
                df.addRow(v2);
            }
        } catch (SQLException e) {
            Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
