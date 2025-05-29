package ui;

import data_structure.FlashcardLinkedList;
import data_structure.Trie;
import model.Flashcard;
import model.FlashcardNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlashcardUI {
    private JFrame frame;
    private JLabel cardLabel, indexLabel;
    private JTextField searchField;
    private JButton prevButton, nextButton;
    private boolean showingKey = true;
    private final Trie trie = new Trie();

    private final FlashcardLinkedList flashcardList = new FlashcardLinkedList();
    private FlashcardNode currentNode;

    public void initUI() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus is not available. Default Look and Feel will be used.");
        }

        frame = new JFrame("My Flashcards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        setupHeader();
        setupSidebar();
        setupCardDisplay();
        setupNavigation();

        loadFlashcardsFromFile("src/cards/flashcards.txt");

        currentNode = flashcardList.getHead();
        updateCardDisplay();

        frame.setVisible(true);
    }

    public void loadFlashcardsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    flashcardList.add(new Flashcard(parts[0].trim(), parts[1].trim()));
                    trie.insert(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading flashcards: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Trie getTrie() {
        return trie;
    }

    private void setupHeader() {
        JLabel titleLabel = new JLabel("MY FLASHCARDS", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setBounds(340, 20, 400, 40);
        frame.add(titleLabel);

        searchField = new JTextField();
        searchField.setBounds(100, 80, 700, 40);
        frame.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(820, 80, 120, 40);
        searchButton.setBackground(Color.BLACK);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        searchButton.addActionListener(e -> {
            String prefix = searchField.getText().trim();
            if (prefix.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a prefix to search.");
                return;
            }

            List<String> suggestions = trie.suggest(prefix);
            if (suggestions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No matching flashcards found.");
            }
            else if (suggestions.size() == 1) {
                String firstResult = suggestions.get(0);
                String key = firstResult.split(":", 2)[0].trim();
                FlashcardNode node = flashcardList.findByKey(key);
                if (node != null) {
                    setCurrentNode(node);
                    updateCardDisplay();
                }
            } else {
                JList<String> list = new JList<>(suggestions.stream().map(s -> s.split(":", 2)[0].trim()).toArray(String[]::new));
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setVisibleRowCount(Math.min(100, suggestions.size())); // Tối đa 8 hàng hiển thị

                JScrollPane scrollPane = new JScrollPane(list);
                scrollPane.setPreferredSize(new Dimension(350, 150));

                int result = JOptionPane.showConfirmDialog(
                        frame,
                        scrollPane,
                        "Select a flashcard to view",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION && list.getSelectedValue() != null) {
                    String selected = list.getSelectedValue();
                    String key = selected.split(":", 2)[0].trim();
                    FlashcardNode node = flashcardList.findByKey(key);
                    if (node != null) {
                        setCurrentNode(node);
                        updateCardDisplay();
                    }
                }
            }
        });

        frame.add(searchButton);
    }

    private void setupSidebar() {
        String[] labels = {"Add card", "Delete card", "Edit card", "Arrange cards", "Quiz"};

        JButton button = null;
        for (int i = 0; i < labels.length; i++) {
            button = new JButton(labels[i]);
            button.setBounds(50, 150 + i * 60, 200, 50);
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("SansSerif", Font.BOLD, 20));
            frame.add(button);

            switch (labels[i]) {
                case "Add card":
                    button.addActionListener(e -> FlashcardActions.addCard(frame, flashcardList, this));
                    break;
                case "Delete card":
                    button.addActionListener(e -> FlashcardActions.deleteCard(frame, flashcardList, this));
                    break;
                case "Edit card":
                    button.addActionListener(e -> FlashcardActions.editCard(frame, flashcardList, this));
                    break;
                case "Quiz":
                    button.addActionListener(e -> {
                        List<Flashcard> cards = flashcardList.toList(); // Viết thêm hàm này nếu chưa có
                        if (cards.isEmpty()) {
                            JOptionPane.showMessageDialog(frame, "No flashcards available for quiz.");
                        } else {
                            QuizDialog quizDialog = new QuizDialog(frame, cards);
                            quizDialog.setVisible(true);
                        }
                    });
                    break;
            }
        }

    }

    private void setupCardDisplay() {
        cardLabel = new JLabel("", JLabel.CENTER);
        cardLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        cardLabel.setOpaque(true);
        cardLabel.setBackground(Color.WHITE);
        cardLabel.setBounds(300, 150, 700, 400);
        cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showingKey = !showingKey;
                updateCardDisplay();
            }
        });
        frame.add(cardLabel);
    }

    private void setupNavigation() {
        prevButton = new JButton("←");
        prevButton.setBounds(400, 580, 100, 40);
        prevButton.addActionListener(e -> {
            if (currentNode != null && currentNode.prev != null) {
                currentNode = currentNode.prev;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(prevButton);

        nextButton = new JButton("→");
        nextButton.setBounds(700, 580, 100, 40);
        nextButton.addActionListener(e -> {
            if (currentNode != null && currentNode.next != null) {
                currentNode = currentNode.next;
                showingKey = true;
                updateCardDisplay();
            }
        });
        frame.add(nextButton);

        indexLabel = new JLabel("", JLabel.CENTER);
        indexLabel.setBounds(480, 580, 200, 40);
        indexLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        frame.add(indexLabel);
    }

    public void updateCardDisplay() {
        if (currentNode == null) {
            cardLabel.setText("No cards available");
            indexLabel.setText("0/0");
            return;
        }

        Flashcard card = currentNode.data;
        cardLabel.setText("<html><body style='width: 420px; text-align: center;'>" +
                (showingKey ? card.getKey() : card.getDescription()) +
                "</body></html>");

        int index = flashcardList.indexOf(currentNode);
        indexLabel.setText((index + 1) + "/" + flashcardList.getSize());
    }

    public FlashcardNode getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(FlashcardNode node) {
        currentNode = node;
    }
}