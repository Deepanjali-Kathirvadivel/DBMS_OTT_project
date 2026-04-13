import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterPage extends JFrame {

    RegisterPage() {
        setTitle("Register");
        setSize(400,350);
        setLocationRelativeTo(null);

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
        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setFont(labelFont);
        formPanel.add(nameLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField name = new JTextField(20);
        name.setFont(fieldFont);
        formPanel.add(name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setFont(labelFont);
        formPanel.add(emailLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField email = new JTextField(20);
        email.setFont(fieldFont);
        formPanel.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(labelFont);
        formPanel.add(passLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField pass = new JPasswordField(20);
        pass.setFont(fieldFont);
        formPanel.add(pass, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton reg = new JButton("Register");
        JButton back = new JButton("Back");
        reg.setFont(buttonFont);
        back.setFont(buttonFont);
        buttonPanel.add(reg);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        reg.addActionListener(e -> {
            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users(name,email,password,subscription_type,role) VALUES(?,?,?,?,?)"
                );

                ps.setString(1,name.getText());
                ps.setString(2,email.getText());
                ps.setString(3,new String(pass.getPassword()));
                ps.setString(4,"basic");
                ps.setString(5,"user");

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Registered!");
                dispose();

            } catch(Exception ex){ ex.printStackTrace(); }
        });

        back.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        setVisible(true);
    }
}
