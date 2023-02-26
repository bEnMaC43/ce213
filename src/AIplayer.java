import java.util.*;

/*
   AIplayer is a class that contains the methods for implementing the minimax search for playing the TicTacToe game.
*/
class AIplayer {
    List<Point> availablePoints; //an instance of the List class, a list of Point objects (equivalent to possible moves)
    List<PointsAndScores> rootsChildrenScores; //an instance of the List class, a list of PointsAndScores objects holding the available moves and their values at the root of the search tree, i.e., the current game board.
    Board b = new Board(); //an instance of the Board class
    private static final int MAX_DEPTH = 6;
    //constructor
    public AIplayer() {
    }

    public Point returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < rootsChildrenScores.size(); ++i) {
            if (MAX < rootsChildrenScores.get(i).score) {
                MAX = rootsChildrenScores.get(i).score;
                best = i;
            }
        }
        return rootsChildrenScores.get(best).point;
    }

    public int returnMin(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) < min) {
                min = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public int returnMax(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        int index = -1;

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) > max) {
                max = list.get(i);
                index = i;
            }
        }
        return list.get(index);
    }

    public void callMinimax(int depth, int turn, Board b) {
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, b);
    }

    public int minimax(int depth, int turn, int alpha, int beta, Board b) {
        if (b.hasXWon()) return 1;
        if (b.hasOWon()) return -1;
        List<Point> pointsAvailable = b.getAvailablePoints();
        if (pointsAvailable.isEmpty()) return 0;

        List<Integer> scores = new ArrayList<>();

        int temp;
        if (turn == 1) temp = Integer.MIN_VALUE;
        else temp = Integer.MAX_VALUE;

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);

            if (turn == 1) {
                b.placeAMove(point, 1);
                int currentScore = 0;
                if (depth < MAX_DEPTH) {
                    currentScore = minimax(depth + 1, 2, alpha, beta, b);
                } else {
                    currentScore = heuristicScore(b, 1);
                }
                scores.add(currentScore);
                temp = Math.max(temp, currentScore);
                alpha = Math.max(alpha, temp);
                if (depth == 0)
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));
            } else if (turn == 2) {
                b.placeAMove(point, 2);
                int currentScore = 0;
                if (depth < MAX_DEPTH) {
                    currentScore = minimax(depth + 1, 1, alpha, beta, b);
                } else {
                    currentScore = heuristicScore(b, 2);
                }
                scores.add(currentScore);
                temp = Math.min(temp, currentScore);
                beta = Math.min(beta, temp);
            }
            b.placeAMove(point, 0);
            if (temp == Integer.MIN_VALUE || temp == Integer.MAX_VALUE) break; //added for alpha-beta pruning
            if (beta <= alpha) break; //added for alpha-beta pruning
        }
        if (turn == 1) return returnMax(scores);
        else return returnMin(scores);
    }

    public int heuristicScore(Board b,int player) {
        int score = 0;
        int op;
        if (player ==1)
             op = 2;
        else op =1;

        // check rows
        for (int i = 0; i < 5; i++) {
            int countPlayer1 = 0;
            int countPlayer2 = 0;
            for (int j = 0; j < 5; j++) {
                if (b.board[i][j] == player) {
                    countPlayer1++;
                } else if (b.board[i][j] == op) {
                    countPlayer2++;
                }
            }
            score += scoreRow(countPlayer1, countPlayer2);
        }

        // check columns
        for (int j = 0; j < 5; j++) {
            int countPlayer1 = 0;
            int countPlayer2 = 0;
            for (int i = 0; i < 5; i++) {
                if (b.board[i][j] == player) {
                    countPlayer1++;
                } else if (b.board[i][j] == op) {
                    countPlayer2++;
                }
            }
            score += scoreColumn(countPlayer1, countPlayer2);
        }

        // check diagonals
        int countPlayer1Diag1 = 0;
        int countPlayer2Diag1 = 0;
        int countPlayer1Diag2 = 0;
        int countPlayer2Diag2 = 0;
        for (int i = 0; i < 5; i++) {
            if (b.board[i][i] == player) {
                countPlayer1Diag1++;
            } else if (b.board[i][i] == op) {
                countPlayer2Diag1++;
            }
            if (b.board[i][4-i] == player) {
                countPlayer1Diag2++;
            } else if (b.board[i][4-i] == op) {
                countPlayer2Diag2++;
            }
        }
        score += scoreDiagonal(countPlayer1Diag1, countPlayer2Diag1);
        score += scoreDiagonal(countPlayer1Diag2, countPlayer2Diag2);

        return score;
    }

    private int scoreRow(int countPlayer1, int countPlayer2) {
        if (countPlayer1 == 5) {
            return 100;
        } else if (countPlayer2 == 5) {
            return -100;
        } else if (countPlayer1 == 4 && countPlayer2 == 0) {
            return 10;
        } else if (countPlayer1 == 3 && countPlayer2 == 0) {
            return 5;
        } else if (countPlayer1 == 2 && countPlayer2 == 0) {
            return 2;
        } else if (countPlayer2 == 4 && countPlayer1 == 0) {
            return -10;
        } else if (countPlayer2 == 3 && countPlayer1 == 0) {
            return -5;
        } else if (countPlayer2 == 2 && countPlayer1 == 0) {
            return -2;
        } else {
            return 0;
        }
    }

    private int scoreColumn(int countPlayer1, int countPlayer2) {
        if (countPlayer1 == 5) {
            return 100;
        } else if (countPlayer2 == 5) {
            return -100;
        } else if (countPlayer1 == 4 && countPlayer2 == 0) {
            return 10;
        } else if (countPlayer1 == 3 && countPlayer2 == 0) {
            return 5;
        } else if (countPlayer1 == 2 && countPlayer2 == 0) {
            return 2;
        } else if (countPlayer2 == 4 && countPlayer1 == 0) {
            return -10;
        } else if (countPlayer2 == 3 && countPlayer1 == 0) {
            return -5;
        } else if (countPlayer2 == 2 && countPlayer1 == 0) {
            return -2;
        } else {
            return 0;
        }
    }

    private int scoreDiagonal(int countPlayer1, int countPlayer2) {
        if (countPlayer1 == 5) {
            return 100;
        } else if (countPlayer2 == 5) {
            return -100;
        } else if (countPlayer1 == 4 && countPlayer2 == 0) {
            return 10;
        } else if (countPlayer1 == 3 && countPlayer2 == 0) {
            return 5;
        } else if (countPlayer1 == 2 && countPlayer2 == 0) {
            return 2;
        } else if (countPlayer2 == 4 && countPlayer1 == 0) {
            return -10;
        } else if (countPlayer2 == 3 && countPlayer1 == 0) {
            return -5;
        } else if (countPlayer2 == 2 && countPlayer1 == 0) {
            return -2;
        } else {
            return 0;
        }
    }


    public int[][] copyBoard(Board b) {
        int[][] original = b.board;
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }


}