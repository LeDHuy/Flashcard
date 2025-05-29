package ui;

import data_structure.FlashcardLinkedList;
import model.Flashcard;
import model.FlashcardNode;

import javax.swing.*;
import java.awt.*;

public class FlashcardActions {
    public static void addCard(JFrame parentFrame, FlashcardLinkedList list, FlashcardUI ui) {
        JDialog dialog = new JDialog(parentFrame, "Add a new card", true);
        dialog.setSize(400, 500);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.getContentPane().setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Add a new card", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBounds(50, 20, 300, 40);
        dialog.add(titleLabel);

        JLabel keyLabel = new JLabel("Key");
        keyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        keyLabel.setBounds(50, 80, 100, 30);
        dialog.add(keyLabel);

        JTextArea keyField = new JTextArea();
        keyField.setLineWrap(true);
        keyField.setWrapStyleWord(true);
        JScrollPane keyScroll = new JScrollPane(keyField);
        keyScroll.setBounds(50, 110, 300, 80);
        dialog.add(keyScroll);

        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descLabel.setBounds(50, 200, 150, 30);
        dialog.add(descLabel);

        JTextArea descField = new JTextArea();
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descField);
        descScroll.setBounds(50, 230, 300, 120);
        dialog.add(descScroll);

        JButton addButton = new JButton("Add");
        addButton.setBounds(150, 370, 100, 40);
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        dialog.add(addButton);

        addButton.addActionListener(e -> {
            String key = keyField.getText().trim();
            String desc = descField.getText().trim();

            if (key.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Both fields must be filled in.");
                return;
            }

            list.add(new Flashcard(key, desc));
            ui.getTrie().insert(key, desc);
            if (ui.getCurrentNode() == null) {
                ui.setCurrentNode(list.getTail());
            }
            ui.updateCardDisplay();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    public static void deleteCard(JFrame frame, FlashcardLinkedList list, FlashcardUI ui) {
        FlashcardNode current = ui.getCurrentNode();
        if (current == null) {
            JOptionPane.showMessageDialog(frame, "No card to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this card?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        FlashcardNode nextNode = current.next != null ? current.next : current.prev;
        list.delete(current);
        ui.setCurrentNode(nextNode);
        ui.updateCardDisplay();
    }

    public static void editCard(JFrame parentFrame, FlashcardLinkedList list, FlashcardUI ui) {
        FlashcardNode current = ui.getCurrentNode();
        if (current == null) {
            JOptionPane.showMessageDialog(parentFrame, "No card to edit.");
            return;
        }

        JDialog dialog = new JDialog(parentFrame, "Edit card", true);
        dialog.setSize(400, 500);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(parentFrame);
        dialog.getContentPane().setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Edit card", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBounds(50, 20, 300, 40);
        dialog.add(titleLabel);

        JLabel keyLabel = new JLabel("Key");
        keyLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        keyLabel.setBounds(50, 80, 100, 30);
        dialog.add(keyLabel);

        JTextArea keyField = new JTextArea(current.data.getKey());
        keyField.setLineWrap(true);
        keyField.setWrapStyleWord(true);
        JScrollPane keyScroll = new JScrollPane(keyField);
        keyScroll.setBounds(50, 110, 300, 80);
        dialog.add(keyScroll);

        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descLabel.setBounds(50, 200, 150, 30);
        dialog.add(descLabel);

        JTextArea descField = new JTextArea(current.data.getDescription());
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descField);
        descScroll.setBounds(50, 230, 300, 120);
        dialog.add(descScroll);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(150, 370, 100, 40);
        saveButton.setBackground(Color.DARK_GRAY);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            String newKey = keyField.getText().trim();
            String newDesc = descField.getText().trim();

            if (newKey.isEmpty() || newDesc.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Both fields must be filled in.");
                return;
            }

            current.data.setKey(newKey);
            current.data.setDescription(newDesc);
            ui.updateCardDisplay();
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}