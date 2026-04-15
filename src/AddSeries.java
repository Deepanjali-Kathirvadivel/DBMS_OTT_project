import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddSeries extends JFrame {

    JTextField title, genre, year, rating, seasons, episodes, trailer, poster;
    String accessType = "basic";

    AddSeries() {
        setTitle("Add Series");
        setSize(450, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 18);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel);

        JLabel formTitle = new JLabel("Add New Series");
        formTitle.setFont(headerFont);
        formTitle.setForeground(UIStyle.PRIMARY_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(formTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setFont(labelFont);
        formPanel.add(titleLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        title = new JTextField(20);
        title.setFont(fieldFont);
        title.setPreferredSize(new Dimension(200, 32));
        formPanel.add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel genreLbl = new JLabel("Genre:");
        genreLbl.setFont(labelFont);
        formPanel.add(genreLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        genre = new JTextField(20);
        genre.setFont(fieldFont);
        genre.setPreferredSize(new Dimension(200, 32));
        formPanel.add(genre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel yearLbl = new JLabel("Year:");
        yearLbl.setFont(labelFont);
        formPanel.add(yearLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        year = new JTextField(20);
        year.setFont(fieldFont);
        year.setPreferredSize(new Dimension(200, 32));
        formPanel.add(year, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel ratingLbl = new JLabel("Rating:");
        ratingLbl.setFont(labelFont);
        formPanel.add(ratingLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        rating = new JTextField(20);
        rating.setFont(fieldFont);
        rating.setPreferredSize(new Dimension(200, 32));
        formPanel.add(rating, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel seasonsLbl = new JLabel("Total Seasons:");
        seasonsLbl.setFont(labelFont);
        formPanel.add(seasonsLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        seasons = new JTextField(20);
        seasons.setFont(fieldFont);
        seasons.setPreferredSize(new Dimension(200, 32));
        formPanel.add(seasons, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        JLabel episodesLbl = new JLabel("Total Episodes:");
        episodesLbl.setFont(labelFont);
        formPanel.add(episodesLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        episodes = new JTextField(20);
        episodes.setFont(fieldFont);
        episodes.setPreferredSize(new Dimension(200, 32));
        formPanel.add(episodes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        JLabel trailerLbl = new JLabel("Trailer URL:");
        trailerLbl.setFont(labelFont);
        formPanel.add(trailerLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        trailer = new JTextField(20);
        trailer.setFont(fieldFont);
        trailer.setPreferredSize(new Dimension(200, 32));
        formPanel.add(trailer, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0;
        JLabel posterLbl = new JLabel("Poster Path:");
        posterLbl.setFont(labelFont);
        formPanel.add(posterLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        poster = new JTextField(20);
        poster.setFont(fieldFont);
        poster.setPreferredSize(new Dimension(200, 32));
        poster.setText("images/");
        formPanel.add(poster, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0;
        JLabel accessLbl = new JLabel("Access Type:");
        accessLbl.setFont(labelFont);
        formPanel.add(accessLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel accessPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        accessPanel.setBackground(Color.WHITE);
        
        JRadioButton basic = new JRadioButton("Basic");
        basic.setFont(labelFont);
        basic.setBackground(Color.WHITE);
        JRadioButton premium = new JRadioButton("Premium");
        premium.setFont(labelFont);
        premium.setBackground(Color.WHITE);
        
        ButtonGroup accessGroup = new ButtonGroup();
        accessGroup.add(basic);
        accessGroup.add(premium);
        basic.setSelected(true);
        
        accessPanel.add(basic);
        accessPanel.add(premium);
        formPanel.add(accessPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton save = new JButton("Save Series");
        JButton addEpisodes = new JButton("Add Episodes");
        JButton back = new JButton("Back");
        save.setFont(buttonFont);
        addEpisodes.setFont(buttonFont);
        back.setFont(buttonFont);
        save.setPreferredSize(new Dimension(130, 40));
        addEpisodes.setPreferredSize(new Dimension(130, 40));
        back.setPreferredSize(new Dimension(130, 40));
        
        UIStyle.stylePrimaryButton(save);
        UIStyle.styleButton(addEpisodes, new Color(80, 180, 80));
        UIStyle.styleSecondaryButton(back);
        
        buttonPanel.add(save);
        buttonPanel.add(addEpisodes);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        save.addActionListener(e -> saveSeries(basic, premium, false));
        addEpisodes.addActionListener(e -> saveSeries(basic, premium, true));
        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void saveSeries(JRadioButton basic, JRadioButton premium, boolean openEpisodeAdder) {
        if (title.getText().isEmpty() || genre.getText().isEmpty() || year.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in required fields (Title, Genre, Year)", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();

            String access = basic.isSelected() ? "basic" : "premium";

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO content(title, description, genre, release_year, rating, trailer_link, content_type, poster, access_type) " +
                    "VALUES(?, ?, ?, ?, ?, ?, 'series', ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, title.getText());
            ps.setString(2, "");
            ps.setString(3, genre.getText());
            ps.setInt(4, Integer.parseInt(year.getText()));
            ps.setDouble(5, Double.parseDouble(rating.getText()));
            ps.setString(6, trailer.getText());
            ps.setString(7, poster.getText());
            ps.setString(8, access);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int contentId = rs.getInt(1);

            PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO series(content_id, total_seasons, total_episodes) VALUES(?, ?, ?)"
            );

            ps2.setInt(1, contentId);
            ps2.setInt(2, Integer.parseInt(seasons.getText()));
            ps2.setInt(3, Integer.parseInt(episodes.getText()));

            ps2.executeUpdate();

            if (openEpisodeAdder) {
                PreparedStatement ps3 = conn.prepareStatement("SELECT series_id FROM series WHERE content_id = ?");
                ps3.setInt(1, contentId);
                ResultSet rs3 = ps3.executeQuery();
                if (rs3.next()) {
                    int seriesId = rs3.getInt(1);
                    new AddEpisode(seriesId, Integer.parseInt(seasons.getText()), Integer.parseInt(episodes.getText()));
                }
                JOptionPane.showMessageDialog(this,
                        "Series added successfully!\n\nNow add episodes.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Series added successfully!\n\n\"" + title.getText() + "\" has been added to the library.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}