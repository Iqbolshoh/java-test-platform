import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Test {
    private JFrame frame;
    private JPanel panel;
    private JLabel questionLabel, timerLabel, emojiLabel, statusLabel;
    private JRadioButton[] options;
    private JButton nextButton;
    private ButtonGroup group;
    private int currentQuestion = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private int score = 0;
    private Timer questionTimer;
    private int timeLeft = 15;

    // List of questions (words, translations, and emojis)
    private String[][] questions = {
            {"Drill", "burg'ulash", "🛠️"},
            {"Preview", "ko'rib chiqish", "👀"},
            {"Stand", "turish", "🚶"},
            {"Impaired view", "ko'rish buzilgan", "👓"},
            {"Long jump", "uzunlikka sakrash", "🏃‍♂️"},
            {"Hurdle", "to'siq", "🏃‍♂️💨"},
            {"Helmet", "dubulg'a", "⛑️"},
            {"Skates", "konkilar", "⛸️"},
            {"Glove", "qo'lqop", "🧤"},
            {"Shoulder pad", "yelka yostig'i", "🏈"}
    };

    public Test() {
        frame = new JFrame("Test App");
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Top section (Question, Emoji, Status)
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        statusLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel = new JLabel("", SwingConstants.CENTER);
        emojiLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel = new JLabel("", SwingConstants.CENTER);

        topPanel.add(statusLabel);
        topPanel.add(questionLabel);
        topPanel.add(emojiLabel);

        // Bottom section (Options and Button)
        JPanel bottomPanel = new JPanel(new GridLayout(4, 1));
        options = new JRadioButton[3];
        group = new ButtonGroup();

        for (int i = 0; i < 3; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            bottomPanel.add(options[i]);
        }

        nextButton = new JButton("Next Question ->");
        nextButton.addActionListener(e -> nextQuestion());
        bottomPanel.add(nextButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(timerLabel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        startTimer();
        loadQuestion();
    }

    // Load the questions and generate random answer choices
    private void loadQuestion() {
        if (currentQuestion >= questions.length) {
            showResult();
            return;
        }

        statusLabel.setText("Question: " + (currentQuestion + 1) + "/" + questions.length + "  |  Score: " + score);
        questionLabel.setText(questions[currentQuestion][0]);
        emojiLabel.setText(questions[currentQuestion][2]);
        timerLabel.setText("Time: " + timeLeft + " sec");

        String[] variants = generateVariants(currentQuestion);
        for (int i = 0; i < 3; i++) {
            options[i].setText(variants[i]);
            options[i].setSelected(false);
        }

        timeLeft = 15;
        startTimer();
    }

    // Generate random answer choices
    private String[] generateVariants(int questionIndex) {
        String[] variants = new String[3];
        Random random = new Random();

        variants[0] = questions[questionIndex][1]; // Correct answer
        for (int i = 1; i < 3; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(questions.length);
            } while (randomIndex == questionIndex);
            variants[i] = questions[randomIndex][1];
        }

        // Shuffle the answer choices
        for (int i = 0; i < 3; i++) {
            int swapIndex = random.nextInt(3);
            String temp = variants[i];
            variants[i] = variants[swapIndex];
            variants[swapIndex] = temp;
        }

        return variants;
    }

    // Start the timer for each question
    private void startTimer() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        questionTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft + " sec");
            if (timeLeft <= 0) {
                questionTimer.stop();
                nextQuestion();
            }
        });
        questionTimer.start();
    }

    // Move to the next question
    private void nextQuestion() {
        checkAnswer();
        currentQuestion++;
        group.clearSelection();
        loadQuestion();
    }

    // Check the selected answer
    private void checkAnswer() {
        for (JRadioButton option : options) {
            if (option.isSelected()) {
                if (option.getText().equals(questions[currentQuestion][1])) {
                    correctAnswers++;
                    score += 10;
                } else {
                    incorrectAnswers++;
                }
                break;
            }
        }
    }

    // Show the final result
    private void showResult() {
        questionTimer.stop();
        JOptionPane.showMessageDialog(frame, "Test Completed!\nCorrect Answers: " + correctAnswers + "\nIncorrect Answers: " + incorrectAnswers + "\nTotal Score: " + score);
        System.exit(0);
    }

    public static void main(String[] args) {
        new Test();
    }
}