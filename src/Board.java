import java.util.*;

/*
   Point is a simple class with a constructor and an int to string method
*/
class Point implements Cloneable{
    int x, y;

    //constructor that takes two integers as arguments to define a point
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    //method for converting integer to string
    public String toString() {
        return "[" + (x+1) + ", " + (y+1) + "]";
    }
}

/*
   PointsAndScores is a simple class with a constructor
*/
class PointsAndScores implements Comparable<PointsAndScores> {
    int score;
    Point point;

    //constructor that takes a Point object and its score as arguments
    PointsAndScores(int score, Point point){
        this.score = score;
        this.point = point;
    }
    @Override
    public int compareTo(PointsAndScores other){
        return Integer.compare(this.score,other.score);
    }
}

/*
   Board is a class that contains methods for checking game status and position status,
   getting available moves (empty positions on the board), placing a player's move at a given position,
   and displaying the current board.
*/
class Board implements Cloneable{
    public static final int size = 5;
    List<Point> availablePoints;
    Scanner scan;
    int[][] board;

    //constructor
    public Board() {
        scan = new Scanner(System.in); //an instance of the Scanner class
        board = new int[size][size]; //an integer array holding the 3x3 game positions. 0 - empty, 1 - Player 'X', 2 - Player 'O'.

    }
    @Override
    public Board clone()throws CloneNotSupportedException{
        try {
            return (Board) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();  // should not happen
        }

    }

    //method for checking whether the game is over
    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    //method for checking whether player 'X' has won (Player 'X' is represented as 1)
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

    //method for checking whether player 'O' has won (Player 'O' is represented as 2)
    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3] && board[0][0] == board[4][4] && board[0][0] == 2) ||
                (board[0][4] == board[1][3] && board[0][4] == board[2][2] && board[0][4] == board[3][1] && board[0][4] == board[4][0] && board[0][4] == 2))
            return true;
        //Check diagonal lines

        for (int i = 0; i < size; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == board[i][3] && board[i][0] == board[i][4] && board[i][0] == 2) ||
                    (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == board[4][i] && board[0][i] == 2))
                return true;
        }
        //Check rows and columns

        return false;
    }


    //method for getting available moves on the current board (empty positions on the board)
    public List<Point> getAvailablePoints() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j)); //Add an empty position to availablePoints
                }
            }
        }
        return availablePoints;
    }

    //method for getting the status of a position
    public int getState(Point point){
        return board[point.x][point.y];
    }

    //method for placing a player's move at a given position
    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;
    }

    //method for displaying the current board
    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (board[i][j]==1)
                    System.out.print("X ");
                else if (board[i][j]==2)
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }
    public Board getBoard(){return this;}
}
