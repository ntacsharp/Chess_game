import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.chess.engine.maingame.GUI.Table;
import com.screen.GameState;

public class App {
    private GameState state = GameState.TITLE;
    private static JFrame gameFrame = new JFrame();
    private static final JMenuBar defaultMenuBar = setUpMenuBar();

    public static void main(String[] args) {
        // Board board = Board.createStandardBoard();

        // System.out.println(board);
        
        gameFrame.setJMenuBar(defaultMenuBar);
        //gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //gameFrame.setUndecorated(true);
        Table table = new Table(gameFrame);
    }

    private static JMenuBar setUpMenuBar() {
        final JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        return menubar;
    }

    private static JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }
}
