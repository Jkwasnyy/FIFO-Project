import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.io.FileFilter;

public class Customers extends JFrame{
    private JPanel panel2;
    private JButton ADDButton;
    private JButton EXPORTButton;
    private JButton DELETEButton;
    private JButton UPDATEButton;
    private JTable table1;
    private JComboBox comboBox1;
    private JButton searchButton;
    private JTextField textName;
    private JTextField textSurname;
    private JTextField textCompany;
    private JTextField textNumber;
    private int width = 500, height = 400;

    public Customers(){
        super("Customers");
        this.setContentPane(this.panel2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        Connect();
        InitializeTable();
        LoadCustomerNo();
        Fetch();

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String customer_name = textName.getText();
                    String customer_surname = textSurname.getText();
                    String company_name = textCompany.getText();
                    String number = textNumber.getText();

                    pst = connection.prepareStatement("INSERT INTO customers_tbl (customer_name,customer_surname,company_name,number) VALUES (?,?,?,?)");
                    pst.setString(1,customer_name);
                    pst.setString(2,customer_surname);
                    pst.setString(3,company_name);
                    pst.setString(4,number);

                    int k = pst.executeUpdate();

                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record Added!");
                        textName.setText("");
                        textSurname.setText("");
                        textCompany.setText("");
                        textNumber.setText("");
                        Fetch();
                        LoadCustomerNo();
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
                    pst = connection.prepareStatement("SELECT * FROM customers_tbl WHERE customer_id=?");
                    pst.setString(1,pid);
                    rs=pst.executeQuery();
                    if(rs.next() == true){
                        textName.setText(rs.getString(2));
                        textSurname.setText(rs.getString(3));
                        textCompany.setText(rs.getString(4));
                        textNumber.setText(rs.getString(5));
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
                String cname = textName.getText();
                String csurname = textSurname.getText();
                String company = textCompany.getText();
                String number = textNumber.getText();
                String pid = comboBox1.getSelectedItem().toString();

                try {
                    pst = connection.prepareStatement("UPDATE customers_tbl SET customer_name=?,customer_surname=?,company_name=?,number=? WHERE customer_id=?");
                    pst.setString(1,cname);
                    pst.setString(2,csurname);
                    pst.setString(3,company);
                    pst.setString(4,number);
                    pst.setString(5,pid);
                    int k = pst.executeUpdate();
                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record has been succesfully updated!");
                        textName.setText("");
                        textSurname.setText("");
                        textCompany.setText("");
                        textNumber.setText("");
                        textName.requestFocus();
                        LoadCustomerNo();
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
                    pst = connection.prepareStatement("DELETE FROM customers_tbl WHERE customer_id=?");
                    pst.setString(1,pid);
                    int k = pst.executeUpdate();
                    if(k == 1){
                        JOptionPane.showMessageDialog(null,"Record has been succesfully deleted!");
                        textName.setText("");
                        textSurname.setText("");
                        textCompany.setText("");
                        textNumber.setText("");
                        textName.requestFocus();
                        LoadCustomerNo();
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
                String filename = "C:/Users/Admin/IdeaProjects/FIFO/Exported_customers_file.csv";
                try {
                    FileWriter fw = new FileWriter(filename);
                    pst = connection.prepareStatement("SELECT * FROM customers_tbl");
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

    public void LoadCustomerNo(){
        try {
            pst = connection.prepareStatement("SELECT customer_id FROM customers_tbl");
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
        model.addColumn("customer_id");
        model.addColumn("customer_name");
        model.addColumn("customer_surname");
        model.addColumn("company_name");
        model.addColumn("number");
        table1.setModel(model);
    }

    private void Fetch(){
        try {
            int q;
            pst = connection.prepareStatement("SELECT * FROM customers_tbl");
            rs = pst.executeQuery();
            ResultSetMetaData rss = rs.getMetaData();
            q = rss.getColumnCount();
            DefaultTableModel df = (DefaultTableModel)table1.getModel();
            df.setRowCount(0);
            while(rs.next()){
                Vector v2 = new Vector();
                v2.add(rs.getString("customer_id"));
                v2.add(rs.getString("customer_name"));
                v2.add(rs.getString("customer_surname"));
                v2.add(rs.getString("company_name"));
                v2.add(rs.getString("number"));
                df.addRow(v2);
            }
        } catch (SQLException e) {
            Logger.getLogger(Products.class.getName()).log(Level.SEVERE,null,e);
        }
    }
}
