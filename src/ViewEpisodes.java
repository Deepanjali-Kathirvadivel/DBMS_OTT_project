import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    boolean isPremium;

    ViewEpisodes(int seriesId, String seriesTitle, int userId, String role, boolean isPremium) {
        this.seriesId = seriesId;
        this.seriesTitle = seriesTitle;
        this.userId = userId;
        this.role = role;
        this.isPremium = isPremium;

        setTitle("Episodes - " + seriesTitle);
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 18);

        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout(15, 0));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        titlePanel.setBackground(Color.WHITE);
        add(titlePanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel(seriesTitle);
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(UIStyle.PRIMARY_COLOR);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        
        JLabel premiumBadge = new JLabel();
        if (isPremium) {
            premiumBadge.setText("PREMIUM");
            premiumBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
            premiumBadge.setForeground(Color.WHITE);
            premiumBadge.setBackground(UIStyle.ACCENT_COLOR);
            premiumBadge.setOpaque(true);
            premiumBadge.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        btnPanel.add(premiumBadge);
        titlePanel.add(btnPanel, BorderLayout.EAST);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.setColumnIdentifiers(new String[]{
                "ID", "Season", "Episode", "Title", "Duration (min)"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(UIStyle.PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        UIStyle.stylePrimaryButton(watch);
        UIStyle.styleButton(addEpisode, new Color(80, 180, 80));
        UIStyle.styleSecondaryButton(edit);
        UIStyle.styleDangerButton(delete);
        UIStyle.styleSecondaryButton(back);

        bottomPanel.add(watch);
        bottomPanel.add(addEpisode);
        bottomPanel.add(edit);
        bottomPanel.add(delete);
        bottomPanel.add(back);

        add(bottomPanel, BorderLayout.SOUTH);

        if (!role.equals("admin")) {
            addEpisode.setVisible(false);
            edit.setVisible(false);
            delete.setVisible(false);
        }

        loadEpisodes();
        refreshContentId();

        watch.addActionListener(e -> handleWatch());
        addEpisode.addActionListener(e -> handleAddEpisode());
        edit.addActionListener(e -> handleEdit());
        delete.addActionListener(e -> handleDelete());
        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void handleWatch() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an episode");
            return;
        }

        int episodeId = (int) model.getValueAt(row, 0);
        int season = (int) model.getValueAt(row, 1);
        int episode = (int) model.getValueAt(row, 2);

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO watch_history(user_id, content_id, progress, episode) VALUES(?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE watch_date = NOW(), episode = ?, progress = 0"
            );

            ps.setInt(1, userId);
            ps.setInt(2, contentId);
            ps.setInt(3, episode);
            ps.setInt(4, episode);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, 
                    "Now watching " + seriesTitle + " S" + season + "E" + episode + ": " + model.getValueAt(row, 3));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    void handleAddEpisode() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT total_seasons, total_episodes FROM series WHERE series_id = ?"
            );
            ps.setInt(1, seriesId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                new AddEpisode(seriesId, rs.getInt("total_seasons"), rs.getInt("total_episodes"));
                loadEpisodes();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an episode");
            return;
        }

        int episodeId = (int) model.getValueAt(row, 0);
        new EditEpisode(episodeId, seriesId, this);
    }

    void handleDelete() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an episode");
            return;
        }

        int episodeId = (int) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this episode?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM episodes WHERE episode_id = ?"
            );

            ps.setInt(1, episodeId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Episode deleted successfully");
            loadEpisodes();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting episode: " + ex.getMessage());
        }
    }

    void refreshContentId() {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT content_id FROM series WHERE series_id = ?");
            ps.setInt(1, seriesId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contentId = rs.getInt("content_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadEpisodes() {
        try {
            model.setRowCount(0);

            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT episode_id, season_number, episode_number, title, duration_minutes " +
                    "FROM episodes WHERE series_id = ? ORDER BY season_number, episode_number"
            );

            ps.setInt(1, seriesId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("episode_id"),
                        rs.getInt("season_number"),
                        rs.getInt("episode_number"),
                        rs.getString("title"),
                        rs.getInt("duration_minutes")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}