import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    
    public static Color BG_COLOR = new Color(240, 240, 245);
    public static Color PANEL_COLOR = Color.WHITE;
    public static Color TEXT_COLOR = Color.BLACK;
    public static Color BUTTON_BG = new Color(70, 130, 180);
    public static Color WATCHED_ROW_COLOR = new Color(144, 238, 144);
    
    public static boolean isDark() {
        return false;
    }
    
    public static Color getBackground() {
        return BG_COLOR;
    }
    
    public static Color getPanelBackground() {
        return PANEL_COLOR;
    }
    
    public static Color getTextColor() {
        return TEXT_COLOR;
    }
    
    public static Color getButtonBackground() {
        return BUTTON_BG;
    }
    
    public static Color getWatchedRowColor() {
        return WATCHED_ROW_COLOR;
    }
    
    public static void applyTheme(Container root) {
        root.setBackground(BG_COLOR);
    }
    
    public static void applyToPanel(JPanel panel) {
        panel.setBackground(PANEL_COLOR);
    }
}