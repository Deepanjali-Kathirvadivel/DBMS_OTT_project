import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ViewSeries extends JFrame {

    DefaultTableModel model;
    JTable table;

    int userId;
    String role;

    ViewSeries(int userId, String role){

        this.userId = userId;
        this.role = role;

        setTitle("View Series");
        setSize(800,450);
        setLocationRelativeTo(null);

        Font buttonFont = new Font("Arial", Font.PLAIN, 14);

        setLayout(new BorderLayout());

        // SEARCH
        JTextField search = new JTextField();
        search.setPreferredSize(new Dimension(200,30));
        search.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel searchPanel = new JPanel();
        searchPanel.add(search);
        add(searchPanel, BorderLayout.NORTH);

        // TABLE
        model = new DefaultTableModel(){
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{
                "ID","Title","Genre","Year","Rating","Seasons","Episodes"
        });

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BUTTONS
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton viewEpisodes = new JButton("View Episodes");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton back = new JButton("Back");

        viewEpisodes.setFont(buttonFont);
        edit.setFont(buttonFont);
        delete.setFont(buttonFont);
        back.setFont(buttonFont);

        panel.add(viewEpisodes);
        panel.add(edit);
        panel.add(delete);
        panel.add(back);

        add(panel, BorderLayout.SOUTH);

        // ROLE
        if(!role.equals("admin")){
            edit.setVisible(false);
            delete.setVisible(false);
        }

        loadSeries("");

        // SEARCH
        search.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent e){
                loadSeries(search.getText());
            }
        });

        // VIEW EPISODES
        viewEpisodes.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select series");
                return;
            }

            int contentId = (int) model.getValueAt(row,0);
            String title = (String) model.getValueAt(row,1);

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT series_id FROM series WHERE content_id=?");
                ps.setInt(1, contentId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    int seriesId = rs.getInt("series_id");
                    new ViewEpisodes(seriesId, title, userId, role);
                }
            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // EDIT
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select series");
                return;
            }

            int seriesId = (int) model.getValueAt(row,0);
            new EditSeries(seriesId, this);
        });

        // DELETE
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select series");
                return;
            }

            int id = (int) model.getValueAt(row,0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this series?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if(confirm != JOptionPane.YES_OPTION){
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();

                Statement stmt = conn.createStatement();
                stmt.execute("SET FOREIGN_KEY_CHECKS=0");

                PreparedStatement ps0 = conn.prepareStatement("SELECT series_id FROM series WHERE content_id=?");
                ps0.setInt(1, id);
                ResultSet rs0 = ps0.executeQuery();
                int seriesId = 0;
                if(rs0.next()){
                    seriesId = rs0.getInt("series_id");
                }

                if(seriesId > 0){
                    stmt.execute("DELETE FROM episodes WHERE series_id=" + seriesId);
                }
                stmt.execute("DELETE FROM watch_history WHERE content_id=" + id);
                stmt.execute("DELETE FROM series WHERE content_id=" + id);
                stmt.execute("DELETE FROM content WHERE content_id=" + id);

                stmt.execute("SET FOREIGN_KEY_CHECKS=1");

                JOptionPane.showMessageDialog(this,"Deleted");

                loadSeries("");

            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting series: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void loadSeries(String key){
        try {
            model.setRowCount(0);

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.content_id, c.title, c.genre, c.release_year, c.rating, s.total_seasons, s.total_episodes " +
                    "FROM content c JOIN series s ON c.content_id = s.content_id " +
                    "WHERE c.content_type='series' AND c.title LIKE ?"
            );

            ps.setString(1, "%" + key + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("content_id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("release_year"),
                        rs.getDouble("rating"),
                        rs.getInt("total_seasons"),
                        rs.getInt("total_episodes")
                });
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
