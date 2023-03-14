import java.util.*;

// AIplayer is a class that contains the methods for implementing the minimax search for playing the TicTacToe game.
class AIplayer {
    List<PointsAndScores> rootsChildrenScores; //a list of PointsAndScores objects holding the available moves and their values at the root of the search tree, i.e., the current game board.
    private static final int MAX_DEPTH = 10; //max depth of the minimax search
    //constructor
    public AIplayer() {
    }

    //returns the point of the best percieved move to make by the ai
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

    //gets smallest value from int list
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

    //gets biggest value from an int list
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

    //this method runs the code to begin a new minimax search with the game depth, whos turn it is and the board object being used as arguments
    public void callMinimax(int depth, int turn, Board b) {
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, turn, Integer.MIN_VALUE, Integer.MAX_VALUE, b);
    }

    //Minimax method for a tic tac toe game with alpha beta pruning
    //Arguments are the search depth, the players turn as an int, the game board
    public int minimax(int depth, int turn, int alpha, int beta, Board b) {
        //Checks if a player has already won
        if (b.hasXWon()) return 1;
        if (b.hasOWon()) return -1;
        //gets empty points on board
        List<Point> pointsAvailable = b.getAvailablePoints();
        //if board is full return 0 and end this iteration of the minimax
        if (pointsAvailable.isEmpty()) return 0;

        //creates list to hold the scores for each available point
        List<Integer> scores = new ArrayList<>();

        int temp;
        //if its Xs turn set integer temp to min value an int can be otherwise set it to the max value an int can be in java
        if (turn == 1) temp = Integer.MIN_VALUE;
        else temp = Integer.MAX_VALUE;

        //iterate through each available point on the board
        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);
            //if its Xs turn (ai)
            if (turn == 1) {
                b.placeAMove(point, 1); //make move on selected point
                int currentScore = 0;
                if (depth < MAX_DEPTH) { //if havent passed max tree depth continue minimax search down tree with new depth and board state
                    currentScore = minimax(depth + 1, 2, alpha, beta, b);
                } else { //once max depth reached analyse the heuristic potential of the board at this depth
                    currentScore = heuristicScore(b);
                }
                //the score of this point is saved and added to list
                scores.add(currentScore);
                temp = Math.max(temp, currentScore);//checks value is greater than the temp (Integer.min)
                alpha = Math.max(alpha, temp);
                // If we're at the root node, add the current score and point to the list of children scores
                if (depth == 0)
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));
            } else if (turn == 2) {
                // Place O's move on the board
                b.placeAMove(point, 2);
                int currentScore = 0;
                if (depth < MAX_DEPTH) {
                    // If we haven't reached the maximum depth yet, recursively call minimax with the updated board and increased depth
                    currentScore = minimax(depth + 1, 1, alpha, beta, b);
                } else {
                    // If we have reached the maximum depth, use the heuristic score to evaluate the board
                    currentScore = heuristicScore(b);
                }
                // Add the current score to the list of scores for each available point, and update the temp score and beta value
                scores.add(currentScore);
                temp = Math.min(temp, currentScore);//checks score is smaller than temp (Integer.max)
                beta = Math.min(beta, temp);
            }
            //undo the move we simulated
            b.placeAMove(point, 0);

            //If temp is at the minimum or maximum value of an int (unaltered by lines 95 and 113)
            //score cant be improved so we break out of loop entirely
            if (temp == Integer.MIN_VALUE || temp == Integer.MAX_VALUE)
                break;
            if (beta <= alpha)
                break;
        }

        if (turn == 1) return returnMax(scores); //if player is 1 we are maxamising the potential value of the game state
        else return returnMin(scores); //of the player is 2 we are minimising
    }

    //returns the heuristic potential of a game state
    //it has one argument, a board obj that has the game state being analysed saved
    public int heuristicScore(Board b) {
        int score =0; //saves heuristic score
        int Xcounters=0 ,Ocounters =0;//saves how many counters each player has on a row


        //Evaluate rows
        for (int i = 0; i < b.size; i++) {
            //First row
            for (int j = 0; j < b.size; j++) {
                if (b.board[i][j] == 'X')
                    Xcounters++; //foreach X counter increment
                else if (b.board[i][j] == 'O')
                    Ocounters++; //same for O counters
            }
            //if there is 0 X counters and multiple O counters decrease score for each opp counter
            if (Xcounters==0&&Ocounters>0)
                score-=Ocounters;
            //inverse for 0 O counters and each X counter
            else if (Ocounters==0&&Xcounters>0)
                score+=Xcounters;
            //if there is none of either or a mix then the row has no potential so score is unaltered
        }
        //Repeats for every column
        //Evaluate columns
        Xcounters=0; Ocounters =0;
        for (int i = 0; i < b.size; i++) {
            //First column
            for (int j = 0; j < b.size; j++) {
                if (b.board[j][i] == 'X')
                    Xcounters++;
                else if (b.board[j][i] == 'O')
                    Ocounters++;
            }
            if (Xcounters==0&&Ocounters>0)

                score-=Ocounters;
            else if (Ocounters==0&&Xcounters>0)
                score+=Xcounters;
        }

        //diagonals
        Xcounters=0; Ocounters =0;
        for (int i = 0; i < b.size ; i++){
            if (b.board[i][i] == 'X')
                Xcounters++;
            else if (b.board[i][i] == 'O')
                Ocounters++;
        }
        if (Xcounters==0&&Ocounters>0)
            score-=Ocounters;
        else if (Ocounters==0&&Xcounters>0)
            score+=Xcounters;
        Xcounters=0; Ocounters =0;
        //checks other diaganol
        for (int i = 0; i < b.size ; i++){
            if (b.board[i][b.size-1-i] == 'X')
                Xcounters++;
            else if (b.board[i][b.size-1-i] == 'O')
                Ocounters++;
        }
        if (Xcounters==0&&Ocounters>0)
            score-=Ocounters;
        else if (Ocounters==0&&Xcounters>0)
            score+=Xcounters;


        return score;
    }

}