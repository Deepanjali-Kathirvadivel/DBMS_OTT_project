import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterPage extends JFrame {

    RegisterPage() {
        setTitle("Register");
        setSize(450, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel appTitle = new JLabel("Register New User");
        appTitle.setFont(headerFont);
        appTitle.setForeground(UIStyle.PRIMARY_COLOR);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(appTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setFont(labelFont);
        formPanel.add(nameLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField name = new JTextField(20);
        name.setFont(fieldFont);
        name.setPreferredSize(new Dimension(200, 35));
        formPanel.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setFont(labelFont);
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField email = new JTextField(20);
        email.setFont(fieldFont);
        email.setPreferredSize(new Dimension(200, 35));
        formPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(labelFont);
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField pass = new JPasswordField(20);
        pass.setFont(fieldFont);
        pass.setPreferredSize(new Dimension(200, 35));
        formPanel.add(pass, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        JButton reg = new JButton("Register");
        JButton back = new JButton("Back");
        reg.setFont(buttonFont);
        back.setFont(buttonFont);
        reg.setPreferredSize(new Dimension(120, 40));
        back.setPreferredSize(new Dimension(120, 40));
        
        UIStyle.stylePrimaryButton(reg);
        UIStyle.styleSecondaryButton(back);
        
        buttonPanel.add(reg);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        reg.addActionListener(e -> {
            if (name.getText().isEmpty() || email.getText().isEmpty() || new String(pass.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement psCheck = conn.prepareStatement(
                        "SELECT * FROM users WHERE email = ?"
                );
                psCheck.setString(1, email.getText());
                ResultSet rs = psCheck.executeQuery();
                
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Email already registered", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users(name, email, password, role) VALUES(?, ?, ?, 'user')"
                );

                ps.setString(1, name.getText());
                ps.setString(2, email.getText());
                ps.setString(3, new String(pass.getPassword()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Registration successful!\nYou can now login.");
                new LoginPage();
                dispose();

            } catch(Exception ex) { 
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        back.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        setVisible(true);
    }
}