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

    EditSeries(int contentId){
        this(contentId, null);
    }

    EditSeries(int contentId, ViewSeries parent){
        this.contentId = contentId;
        this.parent = parent;

        setTitle("Edit Series");
        setSize(400,500);
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
        JLabel seasonsLbl = new JLabel("Total Seasons:");
        seasonsLbl.setFont(labelFont);
        formPanel.add(seasonsLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        seasonsField = new JTextField(20);
        seasonsField.setFont(fieldFont);
        formPanel.add(seasonsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel episodesLbl = new JLabel("Total Episodes:");
        episodesLbl.setFont(labelFont);
        formPanel.add(episodesLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        episodesField = new JTextField(20);
        episodesField.setFont(fieldFont);
        formPanel.add(episodesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
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

        loadSeriesData();

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

    void loadSeriesData(){
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.title, c.genre, c.release_year, c.rating, s.total_seasons, s.total_episodes " +
                    "FROM content c JOIN series s ON c.content_id = s.content_id " +
                    "WHERE c.content_id = ?"
            );
            ps.setInt(1, contentId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                titleField.setText(rs.getString("title") != null ? rs.getString("title") : "");
                genreField.setText(rs.getString("genre") != null ? rs.getString("genre") : "");
                int year = rs.getInt("release_year");
                yearField.setText(year > 0 ? String.valueOf(year) : "");
                double rating = rs.getDouble("rating");
                ratingField.setText(rating > 0 ? String.valueOf(rating) : "");
                seasonsField.setText(String.valueOf(rs.getInt("total_seasons")));
                episodesField.setText(String.valueOf(rs.getInt("total_episodes")));
            } else {
                JOptionPane.showMessageDialog(this, "Series not found");
                dispose();
            }

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading series data: " + ex.getMessage());
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
            ps.setInt(5, contentId);
            ps.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE series SET total_seasons=?, total_episodes=? WHERE content_id=?"
            );
            ps2.setInt(1, Integer.parseInt(seasonsField.getText()));
            ps2.setInt(2, Integer.parseInt(episodesField.getText()));
            ps2.setInt(3, contentId);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Series updated successfully!");
            if(parent != null){
                parent.loadSeries("");
            }
            dispose();

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage());
        }
    }
}
