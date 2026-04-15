import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class HoverEffects {
    
    private static final Color HOVER_COLOR = new Color(100, 149, 237);
    private static final Color DEFAULT_COLOR = new Color(70, 130, 180);
    private static final Color DARK_HOVER_COLOR = new Color(60, 100, 200);
    private static final Color DARK_DEFAULT_COLOR = new Color(100, 149, 237);
    
    public static Set<JButton> hoverButtons = new HashSet<>();
    
    public static void addHoverEffect(JButton button) {
        addHoverEffect(button, DEFAULT_COLOR, HOVER_COLOR);
    }
    
    public static void addHoverEffect(JButton button, Color defaultColor, Color hoverColor) {
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        
        final Color defaultBg = defaultColor;
        final Color hoverBg = hoverColor;
        
        button.setBackground(defaultBg);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultBg);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(hoverBg.darker());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(hoverBg);
            }
        });
        
        hoverButtons.add(button);
    }
    
    public static void addThemeHoverEffect(JButton button) {
        if (ThemeManager.isDark()) {
            addHoverEffect(button, DARK_DEFAULT_COLOR, DARK_HOVER_COLOR);
        } else {
            addHoverEffect(button, DEFAULT_COLOR, HOVER_COLOR);
        }
    }
    
    public static void applyToAllButtons(Container root) {
        for (Component comp : getAllComponents(root)) {
            if (comp instanceof JButton) {
                addHoverEffect((JButton) comp);
            }
        }
    }
    
    private static Component[] getAllComponents(Container container) {
        return container.getComponents();
    }
    
    public static void refreshButtonColors() {
        for (JButton btn : hoverButtons) {
            if (ThemeManager.isDark()) {
                btn.setBackground(DARK_DEFAULT_COLOR);
            } else {
                btn.setBackground(DEFAULT_COLOR);
            }
        }
    }
}

class ColorUtil {
    public static Color darker(Color color) {
        int factor = 2;
        int r = Math.max(color.getRed() - factor * 25, 0);
        int g = Math.max(color.getGreen() - factor * 25, 0);
        int b = Math.max(color.getBlue() - factor * 25, 0);
        return new Color(r, g, b);
    }
}

class ColorExtension {
    public static Color darker() {
        return new Color(0);
    }
}

class ColorExtensions {
    public static Color darker(Color color) {
        int factor = 2;
        int r = Math.max(color.getRed() - factor * 25, 0);
        int g = Math.max(color.getGreen() - factor * 25, 0);
        int b = Math.max(color.getBlue() - factor * 25, 0);
        return new Color(r, g, b);
    }
}