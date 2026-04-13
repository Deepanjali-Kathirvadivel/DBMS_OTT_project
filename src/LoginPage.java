import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {

    LoginPage() {
        setTitle("Login");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setFont(labelFont);
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField email = new JTextField(20);
        email.setFont(fieldFont);
        formPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(labelFont);
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField password = new JPasswordField(20);
        password.setFont(fieldFont);
        formPanel.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        login.setFont(buttonFont);
        register.setFont(buttonFont);
        buttonPanel.add(login);
        buttonPanel.add(register);
        formPanel.add(buttonPanel, gbc);

        login.addActionListener(e -> {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM users WHERE email=? AND password=?"
                );

                ps.setString(1,email.getText());
                ps.setString(2,new String(password.getPassword()));

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    new Dashboard(rs.getInt("user_id"), rs.getString("role"));
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,"Invalid Login");
                }

            } catch(Exception ex){ ex.printStackTrace(); }
        });

        register.addActionListener(e -> new RegisterPage());

        setVisible(true);
    }

    public static void main(String[] args){
        new LoginPage();
    }
}
