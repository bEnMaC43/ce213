import java.util.*;


public class TicTacToe {

    public static void main(String[] args) {
        AIplayer AI= new AIplayer(); //instance of ai player class saved in AI variable
        Board b = new Board(); //creates a board
        Point p = new Point(0, 0); //creates empty point value

        b.displayBoard();

        System.out.println("Who makes first move? (1)Computer (2)User: ");
        int choice = b.scan.nextInt();
        if(choice == 1){ //User selected 1, AI player starts
            AI.callMinimax(0, 1, b); //completes minimax search of board
            b.placeAMove(AI.returnBestMove(), 1); //places what the ai has determined as the best move
            b.displayBoard();
        }
        //repeats until someone wins
        while (!b.isGameOver()) {
            System.out.println("Your move: line (1, 2,3,4 or 5) column (1, 2,3,4 or 5)");
            Point userMove = new Point(b.scan.nextInt()-1, b.scan.nextInt()-1); //gets the next two lines of user input (x and y coord of move)
            while (b.getState(userMove)!=0) { //if marker already on this spot get inputs again
            	System.out.println("Invalid move. Make your move again: ");
            	userMove.x=b.scan.nextInt()-1;
            	userMove.y=b.scan.nextInt()-1;
            }
            b.placeAMove(userMove, 2);//place user move
            b.displayBoard();


            //if winner found end game(ends while loop)
            if (b.isGameOver()) {
                break;
            }
            //completes minimax on board
            AI.callMinimax(0, 1, b);
            b.placeAMove(AI.returnBestMove(), 1);//places best possible move as determined by minimax
            b.displayBoard();

        }

        if (b.hasXWon()) {
            System.out.println("Unfortunately, you lost!");
        } else if (b.hasOWon()) {
            System.out.println("You win!");
        } else {
            System.out.println("It's a draw!");
        }
    }
}