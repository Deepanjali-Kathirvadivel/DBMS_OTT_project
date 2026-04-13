import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ViewMovies extends JFrame {

    DefaultTableModel model;
    JTable table;

    int userId;
    String role;

    ViewMovies(int userId, String role){

        this.userId = userId;
        this.role = role;

        setTitle("View Movies");
        setSize(700,450);
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
                "ID","Title","Genre","Year","Rating","Director","Cast"
        });

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // BUTTONS
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton watch = new JButton("Watch");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton back = new JButton("Back");

        watch.setFont(buttonFont);
        edit.setFont(buttonFont);
        delete.setFont(buttonFont);
        back.setFont(buttonFont);

        panel.add(watch);
        panel.add(edit);
        panel.add(delete);
        panel.add(back);

        add(panel, BorderLayout.SOUTH);

        // ROLE
        if(!role.equals("admin")){
            edit.setVisible(false);
            delete.setVisible(false);
        }

        loadMovies("");

        // SEARCH
        search.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent e){
                loadMovies(search.getText());
            }
        });

        // WATCH
        watch.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select movie");
                return;
            }

            int id = (int) model.getValueAt(row,0);

            try {
                Connection conn = DBConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO watch_history(user_id,content_id) VALUES(?,?)"
                );

                ps.setInt(1,userId);
                ps.setInt(2,id);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Added to history");

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        // EDIT
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select movie");
                return;
            }

            int id = (int) model.getValueAt(row,0);
            new EditMovie(id, this);
        });

        // DELETE
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(this,"Select movie");
                return;
            }

            int id = (int) model.getValueAt(row,0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this movie?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if(confirm != JOptionPane.YES_OPTION){
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();

                Statement stmt = conn.createStatement();
                stmt.execute("SET FOREIGN_KEY_CHECKS=0");
                
                stmt.execute("DELETE FROM watch_history WHERE content_id=" + id);
                stmt.execute("DELETE FROM movies WHERE movie_id=" + id);
                stmt.execute("DELETE FROM content WHERE content_id=" + id);
                
                stmt.execute("SET FOREIGN_KEY_CHECKS=1");

                JOptionPane.showMessageDialog(this,"Deleted");

                loadMovies("");

            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting movie: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void loadMovies(String key){
        try {
            model.setRowCount(0);

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.content_id, c.title, c.genre, c.release_year, c.rating, m.director, m.cast " +
                    "FROM content c LEFT JOIN movies m ON c.content_id = m.movie_id " +
                    "WHERE c.content_type='movie' AND c.title LIKE ?"
            );

            ps.setString(1, "%" + key + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String director = rs.getString("director");
                String cast = rs.getString("cast");
                model.addRow(new Object[]{
                        rs.getInt("content_id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("release_year"),
                        rs.getDouble("rating"),
                        director != null ? director : "",
                        cast != null ? cast : ""
                });
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
