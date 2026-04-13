import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class WatchHistory extends JFrame {

    WatchHistory(int userId){

        setTitle("History");
        setSize(600,400);
        setLocationRelativeTo(null);

        Font buttonFont = new Font("Arial", Font.PLAIN, 14);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(contentPanel);

        JLabel title = new JLabel("Watch History");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(title, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        model.setColumnIdentifiers(new String[]{"Title","Genre","Date"});

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton back = new JButton("Back");
        back.setFont(buttonFont);
        bottomPanel.add(back);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        back.addActionListener(e -> dispose());

        try{
            Connection conn = DBConnection.getConnection();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.title,c.genre,w.watch_date FROM watch_history w JOIN content c ON w.content_id=c.content_id WHERE w.user_id=?"
            );

            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getTimestamp(3)
                });
            }

        } catch(Exception e){ e.printStackTrace(); }

        setVisible(true);
    }
}
