import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditSeries extends JFrame {

    int contentId;
    ViewSeries parent;
    JTextField titleField;
    JTextField genreField;
    JTextField yearField;
    JTextField ratingField;
    JTextField seasonsField;
    JTextField episodesField;
    JTextField trailerField;
    JTextField posterField;
    JRadioButton basicRadio;
    JRadioButton premiumRadio;

    EditSeries(int contentId) {
        this(contentId, null);
    }

    EditSeries(int contentId, ViewSeries parent) {
        this.contentId = contentId;
        this.parent = parent;

        setTitle("Edit Series");
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

        JLabel formTitle = new JLabel("Edit Series");
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
        titleField = new JTextField(20);
        titleField.setFont(fieldFont);
        titleField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel genreLbl = new JLabel("Genre:");
        genreLbl.setFont(labelFont);
        formPanel.add(genreLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        genreField = new JTextField(20);
        genreField.setFont(fieldFont);
        genreField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(genreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel yearLbl = new JLabel("Year:");
        yearLbl.setFont(labelFont);
        formPanel.add(yearLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        yearField = new JTextField(20);
        yearField.setFont(fieldFont);
        yearField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel ratingLbl = new JLabel("Rating:");
        ratingLbl.setFont(labelFont);
        formPanel.add(ratingLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ratingField = new JTextField(20);
        ratingField.setFont(fieldFont);
        ratingField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(ratingField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel seasonsLbl = new JLabel("Total Seasons:");
        seasonsLbl.setFont(labelFont);
        formPanel.add(seasonsLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        seasonsField = new JTextField(20);
        seasonsField.setFont(fieldFont);
        seasonsField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(seasonsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        JLabel episodesLbl = new JLabel("Total Episodes:");
        episodesLbl.setFont(labelFont);
        formPanel.add(episodesLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        episodesField = new JTextField(20);
        episodesField.setFont(fieldFont);
        episodesField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(episodesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        JLabel trailerLbl = new JLabel("Trailer URL:");
        trailerLbl.setFont(labelFont);
        formPanel.add(trailerLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        trailerField = new JTextField(20);
        trailerField.setFont(fieldFont);
        trailerField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(trailerField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0;
        JLabel posterLbl = new JLabel("Poster Path:");
        posterLbl.setFont(labelFont);
        formPanel.add(posterLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        posterField = new JTextField(20);
        posterField.setFont(fieldFont);
        posterField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(posterField, gbc);

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

        basicRadio = new JRadioButton("Basic");
        basicRadio.setFont(labelFont);
        basicRadio.setBackground(Color.WHITE);
        premiumRadio = new JRadioButton("Premium");
        premiumRadio.setFont(labelFont);
        premiumRadio.setBackground(Color.WHITE);

        ButtonGroup accessGroup = new ButtonGroup();
        accessGroup.add(basicRadio);
        accessGroup.add(premiumRadio);
        basicRadio.setSelected(true);

        accessPanel.add(basicRadio);
        accessPanel.add(premiumRadio);
        formPanel.add(accessPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton save = new JButton("Save Changes");
        JButton back = new JButton("Back");
        save.setFont(buttonFont);
        back.setFont(buttonFont);
        save.setPreferredSize(new Dimension(130, 40));
        back.setPreferredSize(new Dimension(130, 40));

        UIStyle.stylePrimaryButton(save);
        UIStyle.styleSecondaryButton(back);

        buttonPanel.add(save);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        loadSeriesData();

        save.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to save changes?",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                saveChanges();
            }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void loadSeriesData() {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.title, c.genre, c.release_year, c.rating, c.trailer_link, c.poster, c.access_type, s.total_seasons, s.total_episodes " +
                    "FROM content c JOIN series s ON c.content_id = s.content_id " +
                    "WHERE c.content_id = ?"
            );
            ps.setInt(1, contentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                titleField.setText(rs.getString("title") != null ? rs.getString("title") : "");
                genreField.setText(rs.getString("genre") != null ? rs.getString("genre") : "");
                int year = rs.getInt("release_year");
                yearField.setText(year > 0 ? String.valueOf(year) : "");
                double rating = rs.getDouble("rating");
                ratingField.setText(rating > 0 ? String.valueOf(rating) : "");
                trailerField.setText(rs.getString("trailer_link") != null ? rs.getString("trailer_link") : "");
                posterField.setText(rs.getString("poster") != null ? rs.getString("poster") : "");
                String accessType = rs.getString("access_type");
                if ("premium".equals(accessType)) {
                    premiumRadio.setSelected(true);
                } else {
                    basicRadio.setSelected(true);
                }
                seasonsField.setText(String.valueOf(rs.getInt("total_seasons")));
                episodesField.setText(String.valueOf(rs.getInt("total_episodes")));
            } else {
                JOptionPane.showMessageDialog(this, "Series not found");
                dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading series data: " + ex.getMessage());
        }
    }

    void saveChanges() {
        try {
            Connection conn = DBConnection.getConnection();

            String accessType = basicRadio.isSelected() ? "basic" : "premium";

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE content SET title = ?, genre = ?, release_year = ?, rating = ?, trailer_link = ?, poster = ?, access_type = ? WHERE content_id = ?"
            );
            ps.setString(1, titleField.getText());
            ps.setString(2, genreField.getText());
            ps.setInt(3, Integer.parseInt(yearField.getText()));
            ps.setDouble(4, Double.parseDouble(ratingField.getText()));
            ps.setString(5, trailerField.getText());
            ps.setString(6, posterField.getText());
            ps.setString(7, accessType);
            ps.setInt(8, contentId);
            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE series SET total_seasons = ?, total_episodes = ? WHERE content_id = ?"
            );
            ps2.setInt(1, Integer.parseInt(seasonsField.getText()));
            ps2.setInt(2, Integer.parseInt(episodesField.getText()));
            ps2.setInt(3, contentId);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Series updated successfully!");
            if (parent != null) {
                parent.loadSeries("");
            }
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage());
        }
    }
}