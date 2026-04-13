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

    EditMovie(int movieId){
        this(movieId, null);
    }

    EditMovie(int movieId, ViewMovies parent){
        this.movieId = movieId;
        this.parent = parent;

        setTitle("Edit Movie");
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
        titleField = new JTextField(20);
        titleField.setFont(fieldFont);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel genreLbl = new JLabel("Genre:");
        genreLbl.setFont(labelFont);
        formPanel.add(genreLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        genreField = new JTextField(20);
        genreField.setFont(fieldFont);
        formPanel.add(genreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel yearLbl = new JLabel("Year:");
        yearLbl.setFont(labelFont);
        formPanel.add(yearLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        yearField = new JTextField(20);
        yearField.setFont(fieldFont);
        formPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel ratingLbl = new JLabel("Rating:");
        ratingLbl.setFont(labelFont);
        formPanel.add(ratingLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        ratingField = new JTextField(20);
        ratingField.setFont(fieldFont);
        formPanel.add(ratingField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel durationLbl = new JLabel("Duration (min):");
        durationLbl.setFont(labelFont);
        formPanel.add(durationLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        durationField = new JTextField(20);
        durationField.setFont(fieldFont);
        formPanel.add(durationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel directorLbl = new JLabel("Director:");
        directorLbl.setFont(labelFont);
        formPanel.add(directorLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        directorField = new JTextField(20);
        directorField.setFont(fieldFont);
        formPanel.add(directorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        JLabel castLbl = new JLabel("Cast:");
        castLbl.setFont(labelFont);
        formPanel.add(castLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        castField = new JTextField(20);
        castField.setFont(fieldFont);
        formPanel.add(castField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton save = new JButton("Save");
        JButton back = new JButton("Back");
        save.setFont(buttonFont);
        back.setFont(buttonFont);
        buttonPanel.add(save);
        buttonPanel.add(back);
        formPanel.add(buttonPanel, gbc);

        loadMovieData();

        save.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to save changes?",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION){
                saveChanges();
            }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void loadMovieData(){
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT title, genre, release_year, rating FROM content WHERE content_id = ?"
            );
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                titleField.setText(rs.getString("title") != null ? rs.getString("title") : "");
                genreField.setText(rs.getString("genre") != null ? rs.getString("genre") : "");
                int year = rs.getInt("release_year");
                yearField.setText(year > 0 ? String.valueOf(year) : "");
                double rating = rs.getDouble("rating");
                ratingField.setText(rating > 0 ? String.valueOf(rating) : "");
            } else {
                JOptionPane.showMessageDialog(this, "Movie not found");
                dispose();
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM movies WHERE movie_id=?");
            ps2.setInt(1, movieId);
            ResultSet rs2 = ps2.executeQuery();
            if(rs2.next()){
                int duration = rs2.getInt("duration");
                durationField.setText(duration > 0 ? String.valueOf(duration) : "90");
                directorField.setText(rs2.getString("director") != null ? rs2.getString("director") : "");
                castField.setText(rs2.getString("cast") != null ? rs2.getString("cast") : "");
            } else {
                durationField.setText("90");
                directorField.setText("");
                castField.setText("");
            }

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading movie data: " + ex.getMessage());
        }
    }

    void saveChanges(){
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE content SET title=?, genre=?, release_year=?, rating=? WHERE content_id=?"
            );
            ps.setString(1, titleField.getText());
            ps.setString(2, genreField.getText());
            ps.setInt(3, Integer.parseInt(yearField.getText()));
            ps.setDouble(4, Double.parseDouble(ratingField.getText()));
            ps.setInt(5, movieId);
            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE movies SET duration=?, director=?, cast=? WHERE movie_id=?"
            );
            ps2.setInt(1, Integer.parseInt(durationField.getText()));
            ps2.setString(2, directorField.getText());
            ps2.setString(3, castField.getText());
            ps2.setInt(4, movieId);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Movie updated successfully!");
            if(parent != null){
                parent.loadMovies("");
            }
            dispose();

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage());
        }
    }
}
