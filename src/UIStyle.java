import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UIStyle {
    
    public static final Color PRIMARY_COLOR = new Color(100, 149, 237);
    public static final Color ACCENT_COLOR = new Color(255, 165, 0);
    public static final Color SUCCESS_COLOR = new Color(60, 200, 60);
    public static final Color WARNING_COLOR = new Color(255, 200, 100);
    public static final Color DARK_PRIMARY = new Color(70, 130, 180);
    public static final Color DARK_BG = new Color(240, 240, 245);
    public static final Color DARK_PANEL = new Color(50, 50, 55);
    public static final Color LIGHT_BG = Color.WHITE;
    public static final Color TEXT_DARK = Color.BLACK;
    public static final Color TEXT_LIGHT = Color.WHITE;
    
    private static Set<JButton> styledButtons = new HashSet<>();
    
    public static Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);
    public static Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
    public static Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
    public static Font tableFont = new Font("Segoe UI", Font.PLAIN, 13);
    
    public static void styleButton(JButton button) {
        styleButton(button, PRIMARY_COLOR);
    }
    
    public static void styleButton(JButton button, Color color) {
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(buttonFont);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        button.setFocusPainted(false);
        
        final Color defaultColor = color;
        final Color hoverColor = new Color(
            Math.min(color.getRed() + 30, 255),
            Math.min(color.getGreen() + 30, 255),
            Math.min(color.getBlue() + 30, 255)
        );
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        styledButtons.add(button);
    }
    
    public static void styleToggleButton(JToggleButton button) {
        styleToggleButton(button, PRIMARY_COLOR);
    }
    
    public static void styleToggleButton(JToggleButton button, Color color) {
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(buttonFont);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setFocusPainted(false);
        
        final Color defaultColor = color;
        final Color hoverColor = new Color(
            Math.min(color.getRed() + 30, 255),
            Math.min(color.getGreen() + 30, 255),
            Math.min(color.getBlue() + 30, 255)
        );
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    public static void stylePrimaryButton(JButton button) {
        styleButton(button, PRIMARY_COLOR);
    }
    
    public static void styleAccentButton(JButton button) {
        styleButton(button, ACCENT_COLOR);
    }
    
    public static void styleDangerButton(JButton button) {
        styleButton(button, new Color(220, 80, 80));
    }
    
    public static void styleSuccessButton(JButton button) {
        styleButton(button, new Color(80, 180, 80));
    }
    
    public static void styleSecondaryButton(JButton button) {
        styleButton(button, new Color(120, 180, 220));
    }
    
    public static JToggleButton styleThemeToggle() {
        JToggleButton toggle = new JToggleButton("Dark Mode");
        toggle.setFont(buttonFont);
        toggle.setBackground(DARK_PRIMARY);
        toggle.setForeground(Color.WHITE);
        toggle.setOpaque(true);
        toggle.setBorderPainted(false);
        
        toggle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        
        return toggle;
    }
    
    public static void styleTable(JTable table, Set<Integer> watchedIds) {
        table.setFont(tableFont);
        table.setRowHeight(25);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        
        if (watchedIds != null && !watchedIds.isEmpty()) {
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, 
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    int contentId = (Integer) table.getModel().getValueAt(row, 0);
                    
                    if (watchedIds.contains(contentId)) {
                        c.setBackground(SUCCESS_COLOR);
                    } else if (isSelected) {
                        c.setBackground(PRIMARY_COLOR);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                    return c;
                }
            });
        }
    }
    
    public static void styleSearchField(JTextField field) {
        field.setFont(tableFont);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    public static void stylePanel(JPanel panel) {
        panel.setBackground(Color.WHITE);
    }
    
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(Color.WHITE);
    }
    
    public static void refreshTheme() {
        for (JButton btn : styledButtons) {
            if (ThemeManager.isDark()) {
                btn.setBackground(DARK_PRIMARY);
            } else {
                btn.setBackground(PRIMARY_COLOR);
            }
        }
    }
    
    public static void applyRowColor(DefaultTableModel model, JTable table, Set<Integer> watchedIds) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected && watchedIds != null) {
                    int contentId = -1;
                    try {
                        contentId = (Integer) model.getValueAt(row, 0);
                    } catch (Exception e) {
                        // ignore
                    }
                    
                    if (watchedIds.contains(contentId)) {
                        c.setBackground(SUCCESS_COLOR);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
    }
    
    public static DefaultTableModel createStyledModel() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
    }
}