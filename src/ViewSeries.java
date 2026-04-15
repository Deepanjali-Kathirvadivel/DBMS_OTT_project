import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.Desktop;
import java.net.URI;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.Set;

public class ViewSeries extends JFrame {

    DefaultTableModel model;
    JTable table;
    int userId;
    String role;
    boolean isPremium;
    JTextField search;
    JButton searchButton;
    Set<Integer> watchedIds = new HashSet<>();

    ViewSeries(int userId, String role, boolean isPremium) {
        this.userId = userId;
        this.role = role;
        this.isPremium = isPremium;

        setTitle("View Series");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 18);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.setBackground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);

        JLabel title = new JLabel("TV Series");
        title.setFont(headerFont);
        title.setForeground(UIStyle.PRIMARY_COLOR);
        topPanel.add(title, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        
        search = new JTextField();
        search.setPreferredSize(new Dimension(200, 32));
        search.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        search.setText("Search series...");
        search.setForeground(Color.GRAY);
        
        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (search.getText().equals("Search series...")) {
                    search.setText("");
                    search.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (search.getText().isEmpty()) {
                    search.setText("Search series...");
                    search.setForeground(Color.GRAY);
                }
            }
        });
        
        searchButton = new JButton("Search");
        searchButton.setFont(buttonFont);
        UIStyle.styleButton(searchButton);
        searchPanel.add(search);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.EAST);

        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.setColumnIdentifiers(new Object[]{
                "ID", "Title", "Genre", "Year", "Rating", "Seasons", "Episodes", "Access", "Trailer"
        });

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setSelectionBackground(UIStyle.PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        showPoster(row);
                    }
                }
            }
        });
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton viewEpisodes = new JButton("View Episodes");
        JButton trailer = new JButton("Trailer");
        JButton trending = new JButton("Trending");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton back = new JButton("Back");

        viewEpisodes.setFont(buttonFont);
        trailer.setFont(buttonFont);
        trending.setFont(buttonFont);
        edit.setFont(buttonFont);
        delete.setFont(buttonFont);
        back.setFont(buttonFont);

        UIStyle.stylePrimaryButton(viewEpisodes);
        UIStyle.styleButton(trailer, UIStyle.ACCENT_COLOR);
        UIStyle.styleAccentButton(trending);
        UIStyle.styleSecondaryButton(edit);
        UIStyle.styleDangerButton(delete);
        UIStyle.styleSecondaryButton(back);

        panel.add(viewEpisodes);
        panel.add(trailer);
        panel.add(trending);
        panel.add(edit);
        panel.add(delete);
        panel.add(back);

        add(panel, BorderLayout.SOUTH);

        if (!role.equals("admin")) {
            edit.setVisible(false);
            delete.setVisible(false);
        }

        loadWatchedIds();
        loadSeries("");

        search.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                loadSeries(search.getText().equals("Search series...") ? "" : search.getText());
            }
        });

        searchButton.addActionListener(e -> {
            loadSeries(search.getText().equals("Search series...") ? "" : search.getText());
        });

        viewEpisodes.addActionListener(e -> handleViewEpisodes());

        trailer.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a series");
                return;
            }

            String url = (String) model.getValueAt(row, 8);
            if (url == null || url.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No trailer available for this series");
                return;
            }

            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening trailer: " + ex.getMessage());
            }
        });

        trending.addActionListener(e -> showTrending());

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a series");
                return;
            }

            int seriesId = (int) model.getValueAt(row, 0);
            new EditSeries(seriesId, this);
        });

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a series");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this series?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
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
                if (rs0.next()) {
                    seriesId = rs0.getInt("series_id");
                }

                if (seriesId > 0) {
                    stmt.execute("DELETE FROM episodes WHERE series_id=" + seriesId);
                }
                stmt.execute("DELETE FROM watch_history WHERE content_id=" + id);
                stmt.execute("DELETE FROM series WHERE content_id=" + id);
                stmt.execute("DELETE FROM content WHERE content_id=" + id);

                stmt.execute("SET FOREIGN_KEY_CHECKS=1");

                JOptionPane.showMessageDialog(this, "Series deleted successfully");
                loadSeries(search.getText().equals("Search series...") ? "" : search.getText());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting series: " + ex.getMessage());
            }
        });

        back.addActionListener(e -> dispose());

        setVisible(true);
    }

    void loadWatchedIds() {
        watchedIds.clear();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT content_id FROM watch_history WHERE user_id = ?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                watchedIds.add(rs.getInt("content_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        applyWatchedHighlighting();
    }

    void applyWatchedHighlighting() {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                int contentId = -1;
                try {
                    contentId = (Integer) model.getValueAt(row, 0);
                } catch (Exception e) {
                }

                if (!isSelected) {
                    if (watchedIds.contains(contentId)) {
                        c.setBackground(UIStyle.SUCCESS_COLOR);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(UIStyle.PRIMARY_COLOR);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
    }

    void loadSeries(String key) {
        try {
            model.setRowCount(0);

            Connection conn = DBConnection.getConnection();

            String searchKey = key.isEmpty() ? "%" : "%" + key + "%";
            
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.content_id, c.title, c.genre, c.release_year, c.rating, c.trailer_link, s.total_seasons, s.total_episodes, c.access_type " +
                    "FROM content c JOIN series s ON c.content_id = s.content_id " +
                    "WHERE c.content_type='series' AND c.title LIKE ?"
            );

            ps.setString(1, searchKey);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String trailer = rs.getString("trailer_link");
                String accessType = rs.getString("access_type");
                
                model.addRow(new Object[]{
                        rs.getInt("content_id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("release_year"),
                        rs.getDouble("rating"),
                        rs.getInt("total_seasons"),
                        rs.getInt("total_episodes"),
                        accessType != null ? accessType : "basic",
                        trailer != null ? trailer : ""
                });
            }

            applyWatchedHighlighting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleViewEpisodes() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a series");
            return;
        }

        int contentId = (int) model.getValueAt(row, 0);
        String title = (String) model.getValueAt(row, 1);
        String accessType = (String) model.getValueAt(row, 7);

        if (!isPremium && "premium".equals(accessType)) {
            JOptionPane.showMessageDialog(this,
                    "This is a PREMIUM series.\n\nUpgrade to Premium to watch!",
                    "Premium Content",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT series_id FROM series WHERE content_id=?");
            ps.setInt(1, contentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                new ViewEpisodes(rs.getInt("series_id"), title, userId, role, isPremium);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    void showPoster(int row) {
        String title = (String) model.getValueAt(row, 1);
        
        JDialog posterDialog = new JDialog(this, title + " - Poster", true);
        posterDialog.setSize(400, 500);
        posterDialog.setLocationRelativeTo(null);
        posterDialog.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.BLACK);
        
        JLabel noPosterLabel = new JLabel("No poster available");
        noPosterLabel.setForeground(Color.WHITE);
        noPosterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        noPosterLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        imagePanel.add(noPosterLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoPanel.setBackground(Color.DARK_GRAY);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel titleValue = new JLabel(title);
        titleValue.setForeground(Color.WHITE);
        
        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel genreValue = new JLabel((String) model.getValueAt(row, 2));
        genreValue.setForeground(Color.WHITE);
        
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setForeground(Color.WHITE);
        yearLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JLabel yearValue = new JLabel(String.valueOf(model.getValueAt(row, 3)));
        yearValue.setForeground(Color.WHITE);

        infoPanel.add(titleLabel);
        infoPanel.add(titleValue);
        infoPanel.add(genreLabel);
        infoPanel.add(genreValue);
        infoPanel.add(yearLabel);
        infoPanel.add(yearValue);

        posterDialog.add(imagePanel, BorderLayout.CENTER);
        posterDialog.add(infoPanel, BorderLayout.SOUTH);
        posterDialog.setVisible(true);
    }

    void showTrending() {
        JFrame trendingFrame = new JFrame("Trending Series");
        trendingFrame.setSize(600, 400);
        trendingFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);
        trendingFrame.add(panel);

        JLabel titleLabel = new JLabel("Most Watched Series");
        titleLabel.setFont(UIStyle.headerFont);
        titleLabel.setForeground(UIStyle.PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel trendModel = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        trendModel.setColumnIdentifiers(new String[]{"Title", "Views"});

        JTable trendTable = new JTable(trendModel);
        trendTable.setFont(UIStyle.tableFont);
        trendTable.setRowHeight(25);
        panel.add(new JScrollPane(trendTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton close = new JButton("Close");
        close.setFont(UIStyle.buttonFont);
        UIStyle.styleButton(close);
        btnPanel.add(close);
        panel.add(btnPanel, BorderLayout.SOUTH);

        close.addActionListener(e -> trendingFrame.dispose());

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.title, COUNT(*) as views " +
                    "FROM watch_history w JOIN content c ON w.content_id = c.content_id " +
                    "WHERE c.content_type = 'series' " +
                    "GROUP BY w.content_id ORDER BY views DESC LIMIT 15"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                trendModel.addRow(new Object[]{
                        rs.getString("title"),
                        rs.getInt("views")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        trendingFrame.setVisible(true);
    }
}