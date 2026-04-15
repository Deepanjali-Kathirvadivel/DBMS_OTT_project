import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {

    JTextField email;
    JPasswordField password;

    LoginPage() {
        setTitle("OTT Platform - Login");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel appTitle = new JLabel("OTT Platform");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appTitle.setForeground(UIStyle.PRIMARY_COLOR);
        appTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(appTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setFont(labelFont);
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        email = new JTextField(20);
        email.setFont(fieldFont);
        email.setPreferredSize(new Dimension(200, 35));
        formPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(labelFont);
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        password = new JPasswordField(20);
        password.setFont(fieldFont);
        password.setPreferredSize(new Dimension(200, 35));
        formPanel.add(password, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setPreferredSize(new Dimension(280, 50));
        
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        login.setFont(buttonFont);
        register.setFont(buttonFont);
        
        login.setBackground(new Color(100, 149, 237));
        login.setForeground(Color.WHITE);
        login.setOpaque(true);
        login.setBorderPainted(false);
        
        register.setBackground(new Color(120, 180, 220));
        register.setForeground(Color.WHITE);
        register.setOpaque(true);
        register.setBorderPainted(false);
        
        login.setPreferredSize(new Dimension(120, 40));
        register.setPreferredSize(new Dimension(120, 40));
        
        buttonPanel.add(login);
        buttonPanel.add(register);
        formPanel.add(buttonPanel, gbc);

        login.addActionListener(e -> performLogin());
        password.addActionListener(e -> performLogin());

        register.addActionListener(e -> {
            new RegisterPage();
            dispose();
        });

        setVisible(true);
    }

    void performLogin() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE email=? AND password=?"
            );

            ps.setString(1, email.getText());
            ps.setString(2, new String(password.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                int userId = rs.getInt("user_id");
                
                Date expiry = rs.getDate("expiry_date");
                boolean isPremium = false;
                if (expiry != null) {
                    isPremium = expiry.after(new Date(System.currentTimeMillis()));
                }
                
                new Dashboard(userId, role, isPremium);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}