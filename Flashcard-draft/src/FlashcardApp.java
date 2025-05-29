import ui.FlashcardUI;

import javax.swing.*;

public class FlashcardApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlashcardUI().initUI());
    }
}