import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddEpisode extends JFrame {

    int seriesId;
    int totalSeasons;
    int totalEpisodes;

    AddEpisode(int seriesId, int totalSeasons, int totalEpisodes){
        this.seriesId = seriesId;
        this.totalSeasons = totalSeasons;
        this.totalEpisodes = totalEpisodes;

        setTitle("Add Episodes");
        setSize(400,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
        JLabel seasonLbl = new JLabel("Season:");
        seasonLbl.setFont(labelFont);
        formPanel.add(seasonLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JSpinner seasonSpinner = new JSpinner(new SpinnerNumberModel(1, 1, totalSeasons, 1));
        seasonSpinner.setFont(fieldFont);
        formPanel.add(seasonSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel episodeLbl = new JLabel("Episode:");
        episodeLbl.setFont(labelFont);
        formPanel.add(episodeLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JSpinner episodeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, totalEpisodes, 1));
        episodeSpinner.setFont(fieldFont);
        formPanel.add(episodeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setFont(labelFont);
        formPanel.add(titleLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField title = new JTextField(20);
        title.setFont(fieldFont);
        formPanel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel durationLbl = new JLabel("Duration (min):");
        durationLbl.setFont(labelFont);
        formPanel.add(durationLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField duration = new JTextField(20);
        duration.setFont(fieldFont);
        formPanel.add(duration, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton save = new JButton("Save");
        JButton addMore = new JButton("Add More");
        JButton back = new JButton("Back");
        save.setFont(buttonFont);
        addMore.setFont(buttonFont);
        back.setFont(buttonFont);
        buttonPanel.add(save);
        buttonPanel.add(addMore);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        save.addActionListener(e -> {
            if (title.getText().isEmpty() || duration.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO episodes(series_id,season_number,episode_number,title,duration_minutes) VALUES(?,?,?,?,?)"
                );

                ps.setInt(1, seriesId);
                ps.setInt(2, (int) seasonSpinner.getValue());
                ps.setInt(3, (int) episodeSpinner.getValue());
                ps.setString(4, title.getText());
                ps.setInt(5, Integer.parseInt(duration.getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Episode Added");
                dispose();

            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        addMore.addActionListener(e -> {
            if (title.getText().isEmpty() || duration.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO episodes(series_id,season_number,episode_number,title,duration_minutes) VALUES(?,?,?,?,?)"
                );

                ps.setInt(1, seriesId);
                ps.setInt(2, (int) seasonSpinner.getValue());
                ps.setInt(3, (int) episodeSpinner.getValue());
                ps.setString(4, title.getText());
                ps.setInt(5, Integer.parseInt(duration.getText()));

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Episode Added");
                title.setText("");
                duration.setText("");

            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }
}
