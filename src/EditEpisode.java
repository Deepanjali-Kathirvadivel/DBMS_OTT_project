import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditEpisode extends JFrame {

    int episodeId;
    int seriesId;
    ViewEpisodes parent;
    JTextField titleField;
    JSpinner seasonSpinner;
    JSpinner episodeSpinner;
    JTextField durationField;

    EditEpisode(int episodeId, int seriesId){
        this(episodeId, seriesId, null);
    }

    EditEpisode(int episodeId, int seriesId, ViewEpisodes parent){
        this.episodeId = episodeId;
        this.seriesId = seriesId;
        this.parent = parent;

        setTitle("Edit Episode");
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
        JLabel seasonLbl = new JLabel("Season:");
        seasonLbl.setFont(labelFont);
        formPanel.add(seasonLbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        seasonSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
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
        episodeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
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
        titleField = new JTextField(20);
        titleField.setFont(fieldFont);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
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
        gbc.gridy = 4;
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

        loadEpisodeData();

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

    void loadEpisodeData(){
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT season_number, episode_number, title, duration_minutes FROM episodes WHERE episode_id = ?"
            );
            ps.setInt(1, episodeId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                seasonSpinner.setValue(rs.getInt("season_number"));
                episodeSpinner.setValue(rs.getInt("episode_number"));
                titleField.setText(rs.getString("title") != null ? rs.getString("title") : "");
                durationField.setText(String.valueOf(rs.getInt("duration_minutes")));
            } else {
                JOptionPane.showMessageDialog(this, "Episode not found");
                dispose();
            }

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading episode data: " + ex.getMessage());
        }
    }

    void saveChanges(){
        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE episodes SET season_number=?, episode_number=?, title=?, duration_minutes=? WHERE episode_id=?"
            );
            ps.setInt(1, (int) seasonSpinner.getValue());
            ps.setInt(2, (int) episodeSpinner.getValue());
            ps.setString(3, titleField.getText());
            ps.setInt(4, Integer.parseInt(durationField.getText()));
            ps.setInt(5, episodeId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Episode updated successfully!");
            if(parent != null){
                parent.loadEpisodes();
            }
            dispose();

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage());
        }
    }
}
