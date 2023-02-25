//You are supposed to add your comments

import java.util.*;

class AIplayer { 
    List<Point> availablePoints;
    public List<PointsAndScores> rootsChildrenScores = new ArrayList<>();
    private static final int MAX_DEPTH = 6;
    
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
 
    public void callMinimax(int depth, int turn, Board b){
        rootsChildrenScores = new ArrayList<>();
        minimax(depth, turn, b);
    }
    
    public int minimax(int depth, int turn, Board b) {
        if (depth > MAX_DEPTH)//stops minimax search continuing to any depth reducing search time significantly
            return 0;
        if (b.hasXWon()) return 1;
        if (b.hasOWon()) return -1;
        List<Point> pointsAvailable = b.getAvailablePoints();//25 on a 5x5 at the start
        if (pointsAvailable.isEmpty()) return 0; 

        List<Integer> scores = new ArrayList<>(); 

        for (int i = 0; i < pointsAvailable.size(); ++i) {
            Point point = pointsAvailable.get(i);  

            if (turn == 1) {
                b.placeAMove(getBestMove(pointsAvailable,b,1), 1);
                int currentScore = minimax(depth + 1, 2, b);  
                scores.add(currentScore); 
                if (depth == 0 && rootsChildrenScores!=null)
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));
                
            } else if (turn == 2) {
                b.placeAMove(getBestMove(pointsAvailable,b,2), 2);
                scores.add(minimax(depth + 1, 1, b));  
            }
            b.placeAMove(getBestMove(pointsAvailable,b,0), 0);
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }
    public Point getBestMove(List<Point> pointsAvailable, Board b,int player) {
        int max;
        Point point;
        Board copy;

        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < pointsAvailable.size(); ++i) {
            point = pointsAvailable.get(i);
            copy = b.getBoard();
            copy.placeAMove(point, 1);
            scores.add(heuristicScore(copy,player));
        }
        List<Integer> scoresSorted = scores;
        Collections.sort(scoresSorted);
        max =scoresSorted.get(scoresSorted.size()-1);
        point = pointsAvailable.get(scores.indexOf(max)); // "scores.indexOf(max) shares the index of its respective point in pointsAvailable
        return point;
    }
    public int heuristicScore(Board b,int player) {
        int score = 0;
        int potentialScore;
        char marker;
        char enemy;
        if (player == 1) {
            marker = 'X';
            enemy = 'O';
        }
        if (player == 2) {
            marker = 'O';
            enemy = 'X';
        }
        else return 0;
//        if (b.hasXWon())
//            return 100;//score for already won
//        if (b.hasOWon())
//            return -100;//score for already lost :(
        //Evaluate rows
        for (int i = 0; i < b.size; i++) {
            potentialScore =0;
            //First row
            for (int j = 0; i < b.size; j++) {
                if (b.board[i][j] == marker)
                    potentialScore++;
                if (b.board[i][j] == enemy) {
                    if (potentialScore > 0)
                        potentialScore = 0;
                    else potentialScore--;
                }
            }
            score+=potentialScore;
        }
        //Evaluate columns
        for (int i = 0; i < b.size; i++) {
            potentialScore =0;
            //First row
            for (int j = 0; i < b.size; j++) {
                if (b.board[j][i] == marker)
                    potentialScore++;
                if (b.board[j][i] == enemy) {
                    if (potentialScore > 0)
                        potentialScore = 0;
                    else potentialScore--;
                }
            }
            score+=potentialScore;
        }
        return score;
    }
}
