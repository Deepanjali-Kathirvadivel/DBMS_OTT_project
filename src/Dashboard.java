import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Dashboard extends JFrame {

    Dashboard(int userId, String role){

        setTitle("Dashboard");
        setSize(450,450);
        setLocationRelativeTo(null);

        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        Font labelFont = new Font("Arial", Font.BOLD, 18);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(buttonPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JButton addMovie = new JButton("Add Movie");
        JButton viewMovies = new JButton("View Movies");
        JButton addSeries = new JButton("Add Series");
        JButton viewSeries = new JButton("View Series");
        JButton history = new JButton("History");
        JButton logout = new JButton("Logout");

        addMovie.setFont(buttonFont);
        viewMovies.setFont(buttonFont);
        addSeries.setFont(buttonFont);
        viewSeries.setFont(buttonFont);
        history.setFont(buttonFont);
        logout.setFont(buttonFont);

        JLabel movieCount = new JLabel();
        movieCount.setFont(labelFont);
        movieCount.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel seriesCount = new JLabel();
        seriesCount.setFont(labelFont);
        seriesCount.setHorizontalAlignment(SwingConstants.CENTER);

        if(!role.equals("admin")){
            addMovie.setVisible(false);
            addSeries.setVisible(false);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        buttonPanel.add(addMovie, gbc);

        gbc.gridy = 1;
        buttonPanel.add(viewMovies, gbc);

        gbc.gridy = 2;
        buttonPanel.add(addSeries, gbc);

        gbc.gridy = 3;
        buttonPanel.add(viewSeries, gbc);

        gbc.gridy = 4;
        buttonPanel.add(history, gbc);

        gbc.gridy = 5;
        buttonPanel.add(movieCount, gbc);

        gbc.gridy = 6;
        buttonPanel.add(seriesCount, gbc);

        gbc.gridy = 7;
        buttonPanel.add(logout, gbc);

        viewMovies.addActionListener(e -> new ViewMovies(userId, role));
        addMovie.addActionListener(e -> new AddMovie());
        viewSeries.addActionListener(e -> new ViewSeries(userId, role));
        addSeries.addActionListener(e -> new AddSeries());
        history.addActionListener(e -> new WatchHistory(userId));

        logout.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        try {
            Connection conn = DBConnection.getConnection();
            
            ResultSet rs1 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM content WHERE content_type='movie'");
            if(rs1.next()){
                movieCount.setText("Total Movies: " + rs1.getInt(1));
            }

            ResultSet rs2 = conn.createStatement()
                    .executeQuery("SELECT COUNT(*) FROM content WHERE content_type='series'");
            if(rs2.next()){
                seriesCount.setText("Total Series: " + rs2.getInt(1));
            }

        } catch(Exception e){ e.printStackTrace(); }

        setVisible(true);
    }
}
