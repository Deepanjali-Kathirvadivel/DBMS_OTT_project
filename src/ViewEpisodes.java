import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ViewEpisodes extends JFrame {

    DefaultTableModel model;
    JTable table;

    int seriesId;
    int contentId;
    String seriesTitle;
    int userId;
    String role;

    ViewEpisodes(int seriesId, String seriesTitle, int userId, String role){

        this.seriesId = seriesId;
        this.seriesTitle = seriesTitle;
        this.userId = userId;
        this.role = role;

        setTitle("Episodes - " + seriesTitle);
        setSize(800,450);
        setLocationRelativeTo(null);

        Font buttonFont = new Font("Arial", Font.PLAIN, 14);

        setLayout(new BorderLayout());

        // TITLE
        JLabel titleLabel = new JLabel("Episodes of: " + seriesTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // TABLE
        model = new DefaultTableModel(){
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{
                "ID","Season","Episode","Title","Duration (min)"
        });

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BUTTONS
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton watch = new JButton("Watch");
        JButton addEpisode = new JButton("Add Episode");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton back = new JButton("Back");

        watch.setFont(buttonFont);
        addEpisode.setFont(buttonFont);
        edit.setFont(buttonFont);
        delete.setFont(buttonFont);
        back.setFont(buttonFont);

        panel.add(watch);
        panel.add(addEpisode);
        panel.add(edit);
        panel.add(delete);
        panel.add(back);

        add(panel, BorderLayout.SOUTH);

        // ROLE
        if(!role.equals("admin")){
            addEpisode.setVisible(false);
            edit.setVisible(false);
            delete.setVisible(false);
        }

        loadEpisodes();
        refreshContentId();

        // WATCH
        watch.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select episode");
                return;
            }

            int episodeId = (int) model.getValueAt(row,0);

            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO watch_history(user_id,content_id) VALUES(?,?)"
                );

                ps.setInt(1,userId);
                ps.setInt(2,contentId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Added to history");

            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // ADD EPISODE
        addEpisode.addActionListener(e -> {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT total_seasons, total_episodes FROM series WHERE series_id=?"
                );
                ps.setInt(1, seriesId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    new AddEpisode(seriesId, rs.getInt("total_seasons"), rs.getInt("total_episodes"));
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        // EDIT
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select episode");
                return;
            }

            int episodeId = (int) model.getValueAt(row,0);
            new EditEpisode(episodeId, seriesId, this);
        });

        // DELETE
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select episode");
                return;
            }

            int episodeId = (int) model.getValueAt(row,0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this episode?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if(confirm != JOptionPane.YES_OPTION){
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM episodes WHERE episode_id=?"
                );

                ps.setInt(1,episodeId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Deleted");

                loadEpisodes();

            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting episode: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void refreshContentId(){
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT content_id FROM series WHERE series_id=?");
            ps.setInt(1, seriesId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                contentId = rs.getInt("content_id");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    void loadEpisodes(){
        try {
            model.setRowCount(0);

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT episode_id, season_number, episode_number, title, duration_minutes " +
                    "FROM episodes WHERE series_id=? ORDER BY season_number, episode_number"
            );

            ps.setInt(1, seriesId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("episode_id"),
                        rs.getInt("season_number"),
                        rs.getInt("episode_number"),
                        rs.getString("title"),
                        rs.getInt("duration_minutes")
                });
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
