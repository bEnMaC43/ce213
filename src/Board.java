import java.util.*;

//New class for x,y point
//Makes up the board
class Point {
    int x, y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //Format when printed to console
    @Override
    public String toString() {
        return "[" + (x+1) + ", " + (y+1) + "]";
    }
}

///New class for a point on board (x,y coord) and its current score (neutral,ai or player)
class PointsAndScores implements Comparable<PointsAndScores> {
    int score;
    Point point;

    //constructor that takes a Point object and its score as arguments
    PointsAndScores(int score, Point point){
        this.score = score;
        this.point = point;
    }
    //Facilitates boolean comparison operations on PointsAndScores objects by comparing their score values
    //This allows for a sort method to be executed on an Array List of type PointsAndScores in AIplayer.java
    @Override
    public int compareTo(PointsAndScores other){
        return Integer.compare(this.score,other.score);
    }
}

class Board {
    //width / length of the board
    public static final int size = 5;
    List<Point> availablePoints; //empty points on board
    Scanner scan; //Scanner object for getting user input
    int[][] board; //2d array to save board status/ board positions

    //constructor
    public Board() {
        scan = new Scanner(System.in);
        board = new int[size][size];

    }

    //returns boolean value for if game is over
    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    //returns boolean value for if Xs has won the game by analyzing the board
    public boolean hasXWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3] && board[0][0] == board[4][4] && board[0][0] == 1) ||
                (board[0][4] == board[1][3] && board[0][4] == board[2][2] && board[0][4] == board[3][1] && board[0][4] == board[4][0] && board[0][4] == 1))
            return true;
        //Check diagonal lines
        for (int i = 0; i < size; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == board[i][3] && board[i][0] == board[i][4] && board[i][0] == 1) ||
                    (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == board[4][i] && board[0][i] == 1))
                return true;
        }
        //Check rows and columns
        return false;
    }

    //returns boolean value for if 0s has won the game by analyzing the board2)
    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3] && board[0][0] == board[4][4] && board[0][0] == 2) ||
                (board[0][4] == board[1][3] && board[0][4] == board[2][2] && board[0][4] == board[3][1] && board[0][4] == board[4][0] && board[0][4] == 2))
            return true;
        for (int i = 0; i < size; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == board[i][3] && board[i][0] == board[i][4] && board[i][0] == 2) ||
                    (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == board[4][i] && board[0][i] == 2))
                return true;
        }

        return false;
    }


    //method that returns all empty positions on board
    public List<Point> getAvailablePoints() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j)); //converts this position into a point object and adds to list of available points
                }
            }
        }
        return availablePoints;
    }

    //method for getting the status of a position (X or 0 or empty?)
    public int getState(Point point) {
        return board[point.x][point.y];
    }

    //method for placing a player's move on the board 2d array
    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;
    }

    //method for displaying the current board
    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (board[i][j] == 1)
                    System.out.print("X ");
                else if (board[i][j] == 2)
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }
}
