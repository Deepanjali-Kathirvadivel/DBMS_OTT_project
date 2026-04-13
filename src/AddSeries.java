import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddSeries extends JFrame {

    AddSeries(){

        setTitle("Add Series");
        setSize(400,450);
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
        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setFont(labelFont);
        formPanel.add(titleLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField title = new JTextField(20);
        title.setFont(fieldFont);
        formPanel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel genreLbl = new JLabel("Genre:");
        genreLbl.setFont(labelFont);
        formPanel.add(genreLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField genre = new JTextField(20);
        genre.setFont(fieldFont);
        formPanel.add(genre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel yearLbl = new JLabel("Year:");
        yearLbl.setFont(labelFont);
        formPanel.add(yearLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField year = new JTextField(20);
        year.setFont(fieldFont);
        formPanel.add(year, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel ratingLbl = new JLabel("Rating:");
        ratingLbl.setFont(labelFont);
        formPanel.add(ratingLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField rating = new JTextField(20);
        rating.setFont(fieldFont);
        formPanel.add(rating, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel seasonsLbl = new JLabel("Total Seasons:");
        seasonsLbl.setFont(labelFont);
        formPanel.add(seasonsLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField seasons = new JTextField(20);
        seasons.setFont(fieldFont);
        formPanel.add(seasons, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel episodesLbl = new JLabel("Total Episodes:");
        episodesLbl.setFont(labelFont);
        formPanel.add(episodesLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField episodes = new JTextField(20);
        episodes.setFont(fieldFont);
        formPanel.add(episodes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton save = new JButton("Save");
        JButton addEpisodes = new JButton("Add Episodes");
        JButton back = new JButton("Back");
        save.setFont(buttonFont);
        addEpisodes.setFont(buttonFont);
        back.setFont(buttonFont);
        buttonPanel.add(save);
        buttonPanel.add(addEpisodes);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        save.addActionListener(e -> {
            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO content(title,description,genre,release_year,rating,content_type) VALUES(?,?,?,?,?,'series')",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1,title.getText());
                ps.setString(2,"");
                ps.setString(3,genre.getText());
                ps.setInt(4,Integer.parseInt(year.getText()));
                ps.setDouble(5,Double.parseDouble(rating.getText()));

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int contentId = rs.getInt(1);

                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO series(content_id,total_seasons,total_episodes) VALUES(?,?,?)"
                );

                ps2.setInt(1,contentId);
                ps2.setInt(2,Integer.parseInt(seasons.getText()));
                ps2.setInt(3,Integer.parseInt(episodes.getText()));

                ps2.executeUpdate();

                JOptionPane.showMessageDialog(this,"Series Added");
                dispose();

            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        addEpisodes.addActionListener(e -> {
            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO content(title,description,genre,release_year,rating,content_type) VALUES(?,?,?,?,?,'series')",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setString(1,title.getText());
                ps.setString(2,"");
                ps.setString(3,genre.getText());
                ps.setInt(4,Integer.parseInt(year.getText()));
                ps.setDouble(5,Double.parseDouble(rating.getText()));

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int contentId = rs.getInt(1);

                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO series(content_id,total_seasons,total_episodes) VALUES(?,?,?)"
                );

                ps2.setInt(1,contentId);
                ps2.setInt(2,Integer.parseInt(seasons.getText()));
                ps2.setInt(3,Integer.parseInt(episodes.getText()));

                ps2.executeUpdate();

                PreparedStatement ps3 = conn.prepareStatement("SELECT series_id FROM series WHERE content_id=?");
                ps3.setInt(1, contentId);
                ResultSet rs3 = ps3.executeQuery();
                rs3.next();
                int seriesId = rs3.getInt(1);

                new AddEpisode(seriesId, Integer.parseInt(seasons.getText()), Integer.parseInt(episodes.getText()));
                dispose();

            } catch(Exception ex){ ex.printStackTrace(); JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }
}
