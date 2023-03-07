import java.util.*;

// AIplayer is a class that contains the methods for implementing the minimax search for playing the TicTacToe game.
class AIplayer {
    List<Point> availablePoints; //an instance of the List class, a list of Point objects (equivalent to possible moves)
    List<PointsAndScores> rootsChildrenScores; //an instance of the List class, a list of PointsAndScores objects holding the available moves and their values at the root of the search tree, i.e., the current game board.
    Board b = new Board(); //an instance of the Board class
    private static final int MAX_DEPTH = 8;
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
    int opponent;
    if (player ==1)
        opponent=2;
    else opponent=1;
    int score =0;
    int playerCounters=0 ,opponentCounters =0;
    //Evaluate rows
    for (int i = 0; i < b.size; i++) {
        //First row
        for (int j = 0; j < b.size; j++) {
            if (b.board[i][j] == player)
                playerCounters++;
            else if (b.board[i][j] == opponent)
                opponentCounters++;
        }
        if (playerCounters==0&&opponentCounters>0)
            score--;
        else if (opponentCounters==0&&playerCounters>0)
            score++;
    }
    //Evaluate columns
    playerCounters=0; opponentCounters =0;
    for (int i = 0; i < b.size; i++) {
        //First column
        for (int j = 0; j < b.size; j++) {
            if (b.board[j][i] == player)
                playerCounters++;
            else if (b.board[j][i] == opponent)
                opponentCounters++;
        }
        if (playerCounters==0&&opponentCounters>0)
            for (int x = 0; x < opponentCounters; x++)
                score--;
        else if (opponentCounters==0&&playerCounters>0)
            score++;
    }

    //diagonals
    playerCounters=0; opponentCounters =0;
    for (int i = 0; i < b.size ; i++){
        if (b.board[i][i] == player)
            playerCounters++;
        else if (b.board[i][i] == opponent)
            opponentCounters++;
    }
    if (playerCounters==0&&opponentCounters>0)
        score--;
    else if (opponentCounters==0&&playerCounters>0)
        score++;
    playerCounters=0; opponentCounters =0;
    for (int i = 0; i < b.size ; i++){
        if (b.board[i][b.size-1-i] == player)
            playerCounters++;
        else if (b.board[i][b.size-1-i] == opponent)
            opponentCounters++;
    }
    if (playerCounters==0&&opponentCounters>0)
        score--;
    else if (opponentCounters==0&&playerCounters>0)
        score++;


    return score;
}

}