import com.chess.engine.board.Board;

public class App {
    public static void main(String[] args){
        Board board = Board.createStandardBoard();

        System.out.println(board);
    }
}
