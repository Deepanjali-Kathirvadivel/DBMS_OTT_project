import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {

    int userId;
    String role;
    boolean isPremium;
    JLabel subscriptionLabel;

    Dashboard(int userId, String role, boolean isPremium) {
        this.userId = userId;
        this.role = role;
        this.isPremium = isPremium;

        setTitle("Dashboard - OTT Platform");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 15);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 16);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.setBackground(Color.WHITE);
        add(topPanel, BorderLayout.NORTH);

        JLabel title = new JLabel("OTT Platform");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIStyle.PRIMARY_COLOR);
        topPanel.add(title, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);
        
        subscriptionLabel = new JLabel(isPremium ? "Premium" : "Basic");
        subscriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        subscriptionLabel.setForeground(isPremium ? UIStyle.ACCENT_COLOR : Color.GRAY);
        rightPanel.add(subscriptionLabel);

        JButton logout = new JButton("Logout");
        logout.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(logout);
        topPanel.add(rightPanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        centerPanel.setBackground(Color.WHITE);
        add(centerPanel, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createTitledBorder(null, "Menu", 
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), UIStyle.PRIMARY_COLOR));
        menuPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JButton addMovie = new JButton("Add Movie");
        JButton viewMovies = new JButton("View Movies");
        JButton addSeries = new JButton("Add Series");
        JButton viewSeries = new JButton("View Series");
        JButton history = new JButton("History");
        JButton analytics = new JButton("Analytics");
        JButton trending = new JButton("Trending");
        JButton upgrade = new JButton("Upgrade to Premium");

        JButton[] adminButtons = {addMovie, addSeries, analytics};
        JButton[] userButtons = {viewMovies, viewSeries, history, trending};

        for (JButton btn : adminButtons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(new Dimension(200, 45));
            UIStyle.styleButton(btn);
        }

        for (JButton btn : userButtons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(new Dimension(200, 45));
            UIStyle.styleAccentButton(btn);
        }

        upgrade.setFont(buttonFont);
        upgrade.setPreferredSize(new Dimension(200, 45));
        UIStyle.styleSuccessButton(upgrade);

        gbc.gridx = 0;
        gbc.gridy = 0;
        if (role.equals("admin")) {
            menuPanel.add(addMovie, gbc);
        }

        gbc.gridy = 1;
        menuPanel.add(viewMovies, gbc);

        gbc.gridy = 2;
        if (role.equals("admin")) {
            menuPanel.add(addSeries, gbc);
        }

        gbc.gridy = 3;
        menuPanel.add(viewSeries, gbc);

        gbc.gridy = 4;
        menuPanel.add(history, gbc);

        gbc.gridy = 5;
        menuPanel.add(trending, gbc);

        gbc.gridy = 6;
        if (role.equals("admin")) {
            menuPanel.add(analytics, gbc);
        }

        gbc.gridy = 7;
        if (!isPremium) {
            menuPanel.add(upgrade, gbc);
        }

        centerPanel.add(menuPanel);

        centerPanel.add(Box.createVerticalStrut(20));

        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder(null, "Library Stats", 
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), UIStyle.PRIMARY_COLOR));
        statsPanel.setBackground(Color.WHITE);

        JLabel movieCount = new JLabel("Loading...");
        movieCount.setFont(labelFont);
        movieCount.setForeground(UIStyle.PRIMARY_COLOR);
        movieCount.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel seriesCount = new JLabel("Loading...");
        seriesCount.setFont(labelFont);
        seriesCount.setForeground(UIStyle.PRIMARY_COLOR);
        seriesCount.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(15, 30, 15, 30);
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        statsPanel.add(movieCount, gbc2);

        gbc2.gridx = 1;
        statsPanel.add(seriesCount, gbc2);

        centerPanel.add(statsPanel);

        centerPanel.add(Box.createVerticalStrut(20));

        JPanel continuePanel = new JPanel();
        continuePanel.setLayout(new BoxLayout(continuePanel, BoxLayout.Y_AXIS));
        continuePanel.setBorder(BorderFactory.createTitledBorder(null, "Continue Watching", 
            javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), UIStyle.PRIMARY_COLOR));
        continuePanel.setBackground(Color.WHITE);

        viewMovies.addActionListener(e -> new ViewMovies(userId, role, isPremium));
        addMovie.addActionListener(e -> new AddMovie());
        viewSeries.addActionListener(e -> new ViewSeries(userId, role, isPremium));
        addSeries.addActionListener(e -> new AddSeries());
        history.addActionListener(e -> new WatchHistory(userId));
        analytics.addActionListener(e -> new AdminDashboard());
        trending.addActionListener(e -> showTrending());
        upgrade.addActionListener(e -> handleUpgrade());

        logout.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        loadStats(movieCount, seriesCount);
        loadContinueWatching(continuePanel);
        
        setVisible(true);
    }

    void loadStats(JLabel movieCount, JLabel seriesCount) {
        try {
            Connection conn = DBConnection.getConnection();
            
            ResultSet rs1 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM content WHERE content_type='movie'");
            if (rs1.next()) {
                movieCount.setText("Movies: " + rs1.getInt(1));
            }

            ResultSet rs2 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM content WHERE content_type='series'");
            if (rs2.next()) {
                seriesCount.setText("Series: " + rs2.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadContinueWatching(JPanel continuePanel) {
        try {
            Connection conn = DBConnection.getConnection();
            
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.content_id, c.title, c.content_type, w.progress, w.episode, w.watch_date " +
                    "FROM watch_history w JOIN content c ON w.content_id = c.content_id " +
                    "WHERE w.user_id = ? ORDER BY w.watch_date DESC LIMIT 5"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            boolean hasContent = false;
            while (rs.next()) {
                hasContent = true;
                String title = rs.getString("title");
                String type = rs.getString("content_type");
                int progress = rs.getInt("progress");
                int episode = rs.getInt("episode");

                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                itemPanel.setMaximumSize(new Dimension(600, 55));
                itemPanel.setBackground(UIStyle.SUCCESS_COLOR);

                JLabel itemLabel = new JLabel();
                if (type.equals("series")) {
                    itemLabel.setText(title + " - S1E" + episode);
                } else {
                    itemLabel.setText(title + " (" + progress + "%)");
                }
                itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                itemPanel.add(itemLabel);

                JButton resumeBtn = new JButton("Resume");
                resumeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                UIStyle.styleButton(resumeBtn, new Color(80, 180, 80));
                
                final int contentId = rs.getInt("content_id");
                final String contentType = type;
                
                resumeBtn.addActionListener(e2 -> {
                    if (contentType.equals("series")) {
                        try {
                            PreparedStatement ps3 = conn.prepareStatement("SELECT series_id FROM series WHERE content_id=?");
                            ps3.setInt(1, contentId);
                            ResultSet rs3 = ps3.executeQuery();
                            if (rs3.next()) {
                                new ViewEpisodes(rs3.getInt("series_id"), title, userId, role, isPremium);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        new ViewMovies(userId, role, isPremium);
                    }
                });
                itemPanel.add(resumeBtn);

                continuePanel.add(itemPanel);
            }

            if (!hasContent) {
                JLabel noContent = new JLabel("No watch history yet. Start watching to see your progress!");
                noContent.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noContent.setForeground(Color.GRAY);
                noContent.setAlignmentX(Component.LEFT_ALIGNMENT);
                continuePanel.add(noContent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showTrending() {
        JFrame trendingFrame = new JFrame("Trending Content");
        trendingFrame.setSize(600, 400);
        trendingFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);
        trendingFrame.add(panel);

        JLabel title = new JLabel("Most Watched Content");
        title.setFont(UIStyle.headerFont);
        title.setForeground(UIStyle.PRIMARY_COLOR);
        panel.add(title, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        model.setColumnIdentifiers(new String[]{"Title", "Type", "Views"});

        JTable table = new JTable(model);
        table.setFont(UIStyle.tableFont);
        table.setRowHeight(25);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

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
                    "SELECT c.title, c.content_type, COUNT(*) as views " +
                    "FROM watch_history w JOIN content c ON w.content_id = c.content_id " +
                    "GROUP BY w.content_id ORDER BY views DESC LIMIT 15"
            );
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("title"),
                        rs.getString("content_type"),
                        rs.getInt("views")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        trendingFrame.setVisible(true);
    }

    void handleUpgrade() {
        String[] options = {"1 Month - $9.99", "1 Year - $99.99"};
        int choice = JOptionPane.showOptionDialog(this,
                "Choose a premium plan:\n\n" +
                "Premium Features:\n" +
                "- Access to all premium content\n" +
                "- Ad-free experience\n" +
                "- Early access to new releases\n",
                "Upgrade to Premium",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice >= 0) {
            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps;
                
                if (choice == 0) {
                    ps = conn.prepareStatement("UPDATE users SET expiry_date = DATE_ADD(NOW(), INTERVAL 1 MONTH) WHERE user_id = ?");
                } else {
                    ps = conn.prepareStatement("UPDATE users SET expiry_date = DATE_ADD(NOW(), INTERVAL 1 YEAR) WHERE user_id = ?");
                }
                
                ps.setInt(1, userId);
                ps.executeUpdate();

                isPremium = true;
                subscriptionLabel.setText("Premium");
                subscriptionLabel.setForeground(UIStyle.ACCENT_COLOR);
                
                JOptionPane.showMessageDialog(this, 
                        "Congratulations! You are now a Premium member.\n\n" +
                        "Enjoy unlimited access to all content!",
                        "Upgrade Successful",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}