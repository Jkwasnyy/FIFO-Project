import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    private JTextField textUsername;
    private JPasswordField fieldPassword;
    private JButton logInButton;
    private JPanel panel1;
    private int width = 500, height = 400;
    String user = "admin", password = "admin";
    public Login() {
        super("Account - Shop Manager");
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width,height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userNameInput = textUsername.getText();
                String userPasswordInput = new String(fieldPassword.getPassword());

                if (userNameInput.equals(user) && userPasswordInput.equals(password)){
                    dispose();
                    Menu menu = new Menu();
                }else {
                    JOptionPane.showMessageDialog(null,"Wrong username or password!",
                            "Failed to log in!", JOptionPane.ERROR_MESSAGE);
                    textUsername.setText("");
                    fieldPassword.setText("");
                }
            }
        });

    }
}
