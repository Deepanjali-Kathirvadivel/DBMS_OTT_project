import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditMovie extends JFrame {

    int movieId;
    ViewMovies parent;
    JTextField titleField;
    JTextField genreField;
    JTextField yearField;
    JTextField ratingField;
    JTextField durationField;
    JTextField directorField;
    JTextField castField;
    JTextField trailerField;
    JTextField posterField;
    JRadioButton basicRadio;
    JRadioButton premiumRadio;

    EditMovie(int movieId) {
        this(movieId, null);
    }

    EditMovie(int movieId, ViewMovies parent) {
        this.movieId = movieId;
        this.parent = parent;

        setTitle("Edit Movie");
        setSize(450, 550);
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

        JLabel formTitle = new JLabel("Edit Movie");
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
        JLabel durationLbl = new JLabel("Duration (min):");
        durationLbl.setFont(labelFont);
        formPanel.add(durationLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        durationField = new JTextField(20);
        durationField.setFont(fieldFont);
        durationField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(durationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        JLabel directorLbl = new JLabel("Director:");
        directorLbl.setFont(labelFont);
        formPanel.add(directorLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        directorField = new JTextField(20);
        directorField.setFont(fieldFont);
        directorField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(directorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        JLabel castLbl = new JLabel("Cast:");
        castLbl.setFont(labelFont);
        formPanel.add(castLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        castField = new JTextField(20);
        castField.setFont(fieldFont);
        castField.setPreferredSize(new Dimension(200, 32));
        formPanel.add(castField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
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
        gbc.gridy = 9;
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
        gbc.gridy = 10;
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
        gbc.gridy = 11;
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

        loadMovieData();

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

    void loadMovieData() {
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT title, genre, release_year, rating, trailer_link, poster, access_type FROM content WHERE content_id = ?"
            );
            ps.setInt(1, movieId);
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
            } else {
                JOptionPane.showMessageDialog(this, "Movie not found");
                dispose();
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM movies WHERE movie_id = ?");
            ps2.setInt(1, movieId);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {
                int duration = rs2.getInt("duration");
                durationField.setText(duration > 0 ? String.valueOf(duration) : "90");
                directorField.setText(rs2.getString("director") != null ? rs2.getString("director") : "");
                castField.setText(rs2.getString("cast") != null ? rs2.getString("cast") : "");
            } else {
                durationField.setText("90");
                directorField.setText("");
                castField.setText("");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading movie data: " + ex.getMessage());
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
            ps.setInt(8, movieId);
            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE movies SET duration = ?, director = ?, cast = ? WHERE movie_id = ?"
            );
            ps2.setInt(1, Integer.parseInt(durationField.getText()));
            ps2.setString(2, directorField.getText());
            ps2.setString(3, castField.getText());
            ps2.setInt(4, movieId);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Movie updated successfully!");
            if (parent != null) {
                parent.loadMovies("");
            }
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage());
        }
    }
}