import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {

    AdminDashboard(){

        setTitle("Admin Analytics Dashboard");
        setSize(700,500);
        setLocationRelativeTo(null);

        Font titleFont = new Font("Arial", Font.BOLD, 20);
        Font labelFont = new Font("Arial", Font.BOLD, 16);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(topPanel, BorderLayout.NORTH);

        JLabel title = new JLabel("Analytics Dashboard");
        title.setFont(titleFont);
        topPanel.add(title);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        add(centerPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setMaximumSize(new Dimension(600, 150));

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel userLabel = new JLabel("Total Users", SwingConstants.CENTER);
        userLabel.setFont(labelFont);
        JLabel userCount = new JLabel("0");
        userCount.setFont(new Font("Arial", Font.BOLD, 32));
        userCount.setHorizontalAlignment(SwingConstants.CENTER);
        userPanel.add(userLabel, BorderLayout.NORTH);
        userPanel.add(userCount, BorderLayout.CENTER);

        JPanel moviePanel = new JPanel(new BorderLayout());
        moviePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel movieLabel = new JLabel("Total Movies", SwingConstants.CENTER);
        movieLabel.setFont(labelFont);
        JLabel movieCount = new JLabel("0");
        movieCount.setFont(new Font("Arial", Font.BOLD, 32));
        movieCount.setHorizontalAlignment(SwingConstants.CENTER);
        moviePanel.add(movieLabel, BorderLayout.NORTH);
        moviePanel.add(movieCount, BorderLayout.CENTER);

        JPanel seriesPanel = new JPanel(new BorderLayout());
        seriesPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel seriesLabel = new JLabel("Total Series", SwingConstants.CENTER);
        seriesLabel.setFont(labelFont);
        JLabel seriesCount = new JLabel("0");
        seriesCount.setFont(new Font("Arial", Font.BOLD, 32));
        seriesCount.setHorizontalAlignment(SwingConstants.CENTER);
        seriesPanel.add(seriesLabel, BorderLayout.NORTH);
        seriesPanel.add(seriesCount, BorderLayout.CENTER);

        JPanel viewsPanel = new JPanel(new BorderLayout());
        viewsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel viewsLabel = new JLabel("Total Views", SwingConstants.CENTER);
        viewsLabel.setFont(labelFont);
        JLabel viewsCount = new JLabel("0");
        viewsCount.setFont(new Font("Arial", Font.BOLD, 32));
        viewsCount.setHorizontalAlignment(SwingConstants.CENTER);
        viewsPanel.add(viewsLabel, BorderLayout.NORTH);
        viewsPanel.add(viewsCount, BorderLayout.CENTER);

        statsPanel.add(userPanel);
        statsPanel.add(moviePanel);
        statsPanel.add(seriesPanel);
        statsPanel.add(viewsPanel);

        centerPanel.add(statsPanel);

        JPanel trendingPanel = new JPanel(new BorderLayout());
        trendingPanel.setBorder(BorderFactory.createTitledBorder("Trending Content"));
        trendingPanel.setMaximumSize(new Dimension(600, 300));

        DefaultTableModel trendModel = new DefaultTableModel(){
            public boolean isCellEditable(int r,int c){
                return false;
            }
        };
        trendModel.setColumnIdentifiers(new String[]{"Title", "Type", "Views"});

        JTable trendTable = new JTable(trendModel);
        trendTable.setFont(new Font("Arial", Font.PLAIN, 14));
        trendTable.setRowHeight(25);
        trendingPanel.add(new JScrollPane(trendTable), BorderLayout.CENTER);

        centerPanel.add(trendingPanel);

        JPanel buttonPanel = new JPanel();
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonPanel.add(closeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> dispose());

        try {
            Connection conn = DBConnection.getConnection();

            ResultSet rs1 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM users");
            if(rs1.next()){
                userCount.setText(String.valueOf(rs1.getInt(1)));
            }
            
            ResultSet rs2 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM content WHERE content_type='movie'");
            if(rs2.next()){
                movieCount.setText(String.valueOf(rs2.getInt(1)));
            }

            ResultSet rs3 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM content WHERE content_type='series'");
            if(rs3.next()){
                seriesCount.setText(String.valueOf(rs3.getInt(1)));
            }

            ResultSet rs4 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM watch_history");
            if(rs4.next()){
                viewsCount.setText(String.valueOf(rs4.getInt(1)));
            }

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.title, c.content_type, COUNT(*) as views " +
                    "FROM watch_history w JOIN content c ON w.content_id = c.content_id " +
                    "GROUP BY w.content_id ORDER BY views DESC LIMIT 10"
            );
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                trendModel.addRow(new Object[]{
                        rs.getString("title"),
                        rs.getString("content_type"),
                        rs.getInt("views")
                });
            }

        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading analytics: " + e.getMessage());
        }

        setVisible(true);
    }
}