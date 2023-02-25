//You are supposed to add your comments

import java.util.*;

class AIplayer {
    List<Point> availablePoints;
    public List<PointsAndScores> rootsChildrenScores = new ArrayList<>();
    private static final int MAX_DEPTH = 3;

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
//        for (Point p : b.getAvailablePoints())
//            System.out.println(p);
//        System.out.println("break");

        minimax(depth, turn, b);
    }

    public int minimax(int depth, int turn, Board b) {
        if (depth > MAX_DEPTH)//stops minimax search continuing to any depth reducing search time significantly
            return 0;
        if (b.hasXWon()) return 1;
        if (b.hasOWon()) return -1;
        List<PointsAndScores> pointsAvailable = getBestMoves(b);
        if (pointsAvailable.isEmpty()) return 0;

        List<Integer> scores = new ArrayList<>();

//        for (PointsAndScores item:pointsAvailable)
//            System.out.println(item.point +"  " +item.score);
//        System.out.println();
//        b.displayBoard();
        for (int i = 0; i < pointsAvailable.size(); ++i) {
//            Point point = getBestMoves(b,1).get(i).point;
            Point point = pointsAvailable.get(i).point;

            if (turn == 1) {
                b.placeAMove(point, 1);
                int currentScore = minimax(depth + 1, 2, b);
                scores.add(currentScore);
                if (depth == 0 && rootsChildrenScores != null)
                    rootsChildrenScores.add(new PointsAndScores(currentScore, point));

            } else if (turn == 2) {
                b.placeAMove(point, 2);
                scores.add(minimax(depth + 1, 1, b));
            }
            b.placeAMove(point, 0);
        }
        return turn == 1 ? returnMax(scores) : returnMin(scores);
    }

    public List<PointsAndScores> getBestMoves(Board b) {
        int max;
        Point point;
        Board copy;

        List<PointsAndScores> scores = new ArrayList<>();
        for (int i = 0; i < b.getAvailablePoints().size(); ++i) {
            point = b.getAvailablePoints().get(i);
            copy = new Board();
            copy.board = copyBoard(b);
            copy.placeAMove(new Point(0,1),2);
            copy.placeAMove(point, 1);
            copy.displayBoard();
            System.out.println(heuristicScore(copy));
            scores.add(new PointsAndScores(heuristicScore(copy), point));
        }

        Collections.reverse(scores);
        return scores;
    }

    public int heuristicScore(Board b) {
        int score = 0;
        int potentialScore;
        //Evaluate rows
        for (int i = 0; i < b.size; i++) {
            potentialScore = 0;
            //First row
            for (int j = 0; j < b.size; j++) {
                if (b.board[i][j] == 1)
                    potentialScore++;
                if (b.board[i][j] == 2) {
                    potentialScore = -1;
                    break;
                }

            }
            score += potentialScore;
        }
        //Evaluate columns
        for (int i = 0; i < b.size; i++) {
            potentialScore = 0;
            //First column
            for (int j = 0; j < b.size; j++) {
                if (b.board[j][i] == 1)
                    potentialScore++;
                if (b.board[j][i] == 2) {
                    potentialScore = -1;
                    break;
                }
            }
            score += potentialScore;
        }
        return score;
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



