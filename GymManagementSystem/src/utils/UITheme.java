package utils;

import java.awt.*;

/**
 * UITheme.java
 * Central place for the application's color palette and fonts,
 * so that every view shares a consistent, professional look.
 */
public class UITheme {
    public static final Color PRIMARY_DARK = new Color(24, 34, 51);      // sidebar background
    public static final Color PRIMARY = new Color(33, 87, 165);          // primary accent
    public static final Color PRIMARY_LIGHT = new Color(66, 133, 244);   // hover / highlights
    public static final Color ACCENT_GREEN = new Color(39, 174, 96);
    public static final Color ACCENT_RED = new Color(214, 69, 65);
    public static final Color ACCENT_ORANGE = new Color(230, 126, 34);
    public static final Color BACKGROUND = new Color(244, 246, 249);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_DARK = new Color(33, 37, 41);
    public static final Color TEXT_MUTED = new Color(120, 128, 140);
    public static final Color BORDER = new Color(222, 226, 230);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_NAV = new Font("Segoe UI", Font.PLAIN, 14);
}
