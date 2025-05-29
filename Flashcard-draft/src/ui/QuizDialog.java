package ui;

import model.Flashcard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class QuizDialog extends JDialog {
    private final List<Flashcard> questions;
    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JTextField answerField;
    private JLabel feedbackLabel;
    private JButton checkButton, nextButton;
    private JLabel progressLabel;

    public QuizDialog(JFrame parent, List<Flashcard> allCards) {
        super(parent, "Flashcard Quiz", true);
        setSize(500, 320);
        setLocationRelativeTo(parent);
        setLayout(null);

        Collections.shuffle(allCards);
        questions = new ArrayList<>(allCards.subList(0, Math.min(3, allCards.size())));

        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        questionLabel.setBounds(40, 30, 400, 40);
        add(questionLabel);

        answerField = new JTextField();
        answerField.setBounds(40, 80, 400, 35);
        add(answerField);

        checkButton = new JButton("Check");
        checkButton.setBounds(100, 130, 100, 35);
        add(checkButton);

        nextButton = new JButton("Next");
        nextButton.setBounds(280, 130, 100, 35);
        nextButton.setEnabled(false);
        add(nextButton);

        feedbackLabel = new JLabel("", JLabel.CENTER);
        feedbackLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        feedbackLabel.setBounds(40, 180, 400, 30);
        add(feedbackLabel);

        progressLabel = new JLabel("", JLabel.CENTER);
        progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        progressLabel.setBounds(40, 220, 400, 30);
        add(progressLabel);

        checkButton.addActionListener(e -> checkAnswer());
        nextButton.addActionListener(e -> nextQuestion());

        loadQuestion();
    }

    //m béo vãi

    private void loadQuestion() {
        if (currentIndex >= questions.size()) {
            questionLabel.setText("Quiz Finished!");
            answerField.setVisible(false);
            checkButton.setVisible(false);
            nextButton.setVisible(false);
            feedbackLabel.setText("Your score: " + score + "/" + questions.size());
            progressLabel.setText("");
            return;
        }
        Flashcard card = questions.get(currentIndex);
        questionLabel.setText("What is the meaning of: " + card.getKey() + "?");
        answerField.setText("");
        answerField.setEditable(true);
        feedbackLabel.setText("");
        checkButton.setEnabled(true);
        nextButton.setEnabled(false);
        progressLabel.setText("Question " + (currentIndex + 1) + " of " + questions.size());
    }

    private void checkAnswer() {
        Flashcard card = questions.get(currentIndex);
        String userAnswer = answerField.getText().trim().toLowerCase();
        String correctAnswer = card.getDescription().trim().toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            feedbackLabel.setText("✅ Correct!");
            score++;
        } else {
            feedbackLabel.setText("<html>❌ Incorrect!<br/>Answer: " + card.getDescription() + "</html>");
        }
        answerField.setEditable(false);
        checkButton.setEnabled(false);
        nextButton.setEnabled(true);
    }

    private void nextQuestion() {
        currentIndex++;
        loadQuestion();
    }
}

