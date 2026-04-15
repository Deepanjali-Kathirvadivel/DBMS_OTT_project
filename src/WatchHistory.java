import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class WatchHistory extends JFrame {

    WatchHistory(int userId){

        setTitle("History");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setResizable(true);

        Font buttonFont = new Font("Arial", Font.PLAIN, 14);

        JLabel title = new JLabel("Watch History");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        model.setColumnIdentifiers(new String[]{"Title", "Genre", "Date"});

        table.getColumnModel().getColumn(0).setMinWidth(300);
        table.getColumnModel().getColumn(1).setMinWidth(120);
        table.getColumnModel().getColumn(2).setMinWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton back = new JButton("Back");
        back.setFont(buttonFont);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

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
