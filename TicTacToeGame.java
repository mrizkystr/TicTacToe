import java.awt.*;
import java.util.Stack;

public class TicTacToeGame {
    private final String[][] board;
    private boolean playerXTurn;
    private boolean vsComputer;
    private Stack<Point> moveHistory;

    public TicTacToeGame(int size) {
        board = new String[size][size];
        playerXTurn = true;
        moveHistory = new Stack<>();
    }

    public void setMode(boolean vsComputer) {
        this.vsComputer = vsComputer;
    }

    public boolean isVsComputer() {
        return vsComputer;
    }

    public boolean isPlayerXTurn() {
        return playerXTurn;
    }

    public void makeMove(int x, int y) {
        if (board[x][y] == null) {
            board[x][y] = playerXTurn ? "X" : "O";
            playerXTurn = !playerXTurn;
            moveHistory.push(new Point(x, y));
        }
    }

    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            Point lastMove = moveHistory.pop();
            board[lastMove.x][lastMove.y] = null;
            playerXTurn = !playerXTurn;
        }
    }

    public boolean isGameOver() {
        return getWinner() != null || isBoardFull();
    }

    public boolean isBoardFull() {
        for (String[] row : board) {
            for (String cell : row) {
                if (cell == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getWinner() {
        for (int i = 0; i < board.length; i++) {
            if (checkRow(i)) {
                return board[i][0];
            }
            if (checkColumn(i)) {
                return board[0][i];
            }
        }
        if (checkDiagonal1()) {
            return board[0][0];
        }
        if (checkDiagonal2()) {
            return board[0][board.length - 1];
        }
        return null;
    }

    private boolean checkRow(int row) {
        String first = board[row][0];
        if (first == null) {
            return false;
        }
        for (int i = 1; i < board.length; i++) {
            if (!board[row][i].equals(first)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumn(int column) {
        String first = board[0][column];
        if (first == null) {
            return false;
        }
        for (int i = 1; i < board.length; i++) {
            if (!board[i][column].equals(first)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDiagonal1() {
        String first = board[0][0];
        if (first == null) {
            return false;
        }
        for (int i = 1; i < board.length; i++) {
            if (!board[i][i].equals(first)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkDiagonal2() {
        String first = board[0][board.length - 1];
        if (first == null) {
            return false;
        }
        for (int i = 1; i < board.length; i++) {
            if (!board[i][board.length - 1 - i].equals(first)) {
                return false;
            }
        }
        return true;
    }

    public Point getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Point bestMove = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "O";
                    int score = minimax(0, false);
                    board[i][j] = null;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Point(i, j);
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing) {
        String winner = getWinner();
        if (winner != null) {
            return winner.equals("O") ? 1 : -1;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == null) {
                        board[i][j] = "O";
                        int score = minimax(depth + 1, false);
                        board[i][j] = null;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == null) {
                        board[i][j] = "X";
                        int score = minimax(depth + 1, true);
                        board[i][j] = null;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
