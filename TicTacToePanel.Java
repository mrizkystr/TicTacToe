import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToePanel extends JPanel implements ActionListener {
    private TicTacToeGame game;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JComboBox<String> modeSelector;
    private JComboBox<String> sizeSelector;

    public TicTacToePanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        modeSelector = new JComboBox<>(new String[]{"Versus Friend", "Versus Computer"});
        sizeSelector = new JComboBox<>(new String[]{"3x3", "4x4", "5x5"});
        topPanel.add(new JLabel("Mode:"));
        topPanel.add(modeSelector);
        topPanel.add(new JLabel("Size:"));
        topPanel.add(sizeSelector);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                centerPanel.add(buttons[i][j]);
            }
        }

        add(centerPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Turn: X");
        add(statusLabel, BorderLayout.SOUTH);

        game = new TicTacToeGame(3);
    }

    private void startGame() {
        String mode = (String) modeSelector.getSelectedItem();
        String sizeStr = (String) sizeSelector.getSelectedItem();
        int size = Integer.parseInt(sizeStr.substring(0, 1));
        game = new TicTacToeGame(size);
        game.setMode(mode.equals("Versus Computer"));

        removeAll();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Mode:"));
        topPanel.add(modeSelector);
        topPanel.add(new JLabel("Size:"));
        topPanel.add(sizeSelector);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        topPanel.add(startButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(size, size));
        buttons = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(this);
                centerPanel.add(buttons[i][j]);
            }
        }

        add(centerPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Turn: X");
        add(statusLabel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        Point point = getButtonPosition(buttonClicked);
        if (point == null || !buttonClicked.getText().equals("")) {
            return;
        }

        if (game.isPlayerXTurn()) {
            buttonClicked.setText("X");
        } else {
            buttonClicked.setText("O");
        }
        game.makeMove(point.x, point.y);

        if (game.isGameOver()) {
            String winner = game.getWinner();
            JOptionPane.showMessageDialog(this, (winner != null ? winner + " wins!" : "It's a tie!"));
            startGame();
        } else {
            statusLabel.setText("Turn: " + (game.isPlayerXTurn() ? "X" : "O"));
            if (!game.isPlayerXTurn() && game.isVsComputer()) {
                Point move = game.getBestMove();
                buttons[move.x][move.y].setText("O");
                game.makeMove(move.x, move.y);

                if (game.isGameOver()) {
                    String winner = game.getWinner();
                    JOptionPane.showMessageDialog(this, (winner != null ? winner + " wins!" : "It's a tie!"));
                    startGame();
                } else {
                    statusLabel.setText("Turn: X");
                }
            }
        }
    }

    private Point getButtonPosition(JButton button) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j] == button) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }
}
