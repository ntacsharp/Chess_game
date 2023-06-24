package com.chess.engine.minigame.GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;
import com.chess.menu.StartMenu;

public class MiniTable {
    public static final Color lightTileColor = new Color(99, 105, 136);
    public static final Color darkTileColor = new Color(56, 57, 97);
    public static final Color lightBorderColor = new Color(219, 233, 238);
    public static final Color darkBorderColor = new Color(141, 153, 174);
    public static final Color chosenBackground = new Color(255, 181, 63);
    public static final Color darkRed = new Color(147, 62, 100);
    public static final Color lightRed = new Color(169, 85, 119);
    private static final Color darkGreen = new Color(62, 147, 100);
    private static final Color lightGreen = new Color(85, 169, 119);
    private static final Color transparentBg = new Color(56, 57, 97, 100);

    private static final String ENEMY_ICON_PATH = "art\\pieces\\enemies\\";
    private static final String PLAYER_ICON_PATH = "art\\pieces\\player\\";

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final JPanel gamePanel;
    private final JPanel settingPanel;
    private final JFrame frame;
    private GameState gameState;
    private Card chosenCard = null;
    public BoardPanel boardPanel;
    private SouthPanel southPanel;
    private NorthPanel northPanel;
    private WestPanel westPanel;
    public EastPanel eastPanel;

    public MiniTable() {
        this.frame = new JFrame();
        frame.setLayout(new CardLayout(0, 0));
        this.frame.setSize(screenSize);
        this.frame.setUndecorated(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setTitle("My Chess Game");
        // this.gamePanel.setUndecorated(true);
        // this.gamePanel.setLayout(new BorderLayout());
        this.frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setting();
                }
            }

        });
        this.gamePanel = new JPanel(new BorderLayout());
        this.settingPanel = new SettingPanel();
        setUpGamePanel();
        frame.add(settingPanel);
        frame.setVisible(true);
    }

    public Card getChosenCard() {
        return chosenCard;
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void skip() {
        System.out.println("adad");
    }

    public void setting() {
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        cardLayout.next(frame.getContentPane());
    }

    private void setUpGamePanel() {
        this.gamePanel.removeAll();
        this.gamePanel.setSize(screenSize);
        this.gameState = new GameState(PieceType.BABARIAN);
        this.boardPanel = new BoardPanel();
        this.southPanel = new SouthPanel(this);
        this.northPanel = new NorthPanel(this);
        this.westPanel = new WestPanel(this);
        this.eastPanel = new EastPanel();
        this.gamePanel.add(boardPanel, BorderLayout.CENTER);
        this.gamePanel.add(southPanel, BorderLayout.SOUTH);
        this.gamePanel.add(northPanel, BorderLayout.NORTH);
        this.gamePanel.add(eastPanel, BorderLayout.EAST);
        this.gamePanel.add(westPanel, BorderLayout.WEST);
        frame.add(gamePanel);
    }

    public class SettingPanel extends JPanel {
        SettingPanel() {
            super(new GridLayout(6, 1, 0, 0));
            setBackground(transparentBg);
            setSize(screenSize);
            
            JLabel quitLabel = new JLabel("Exit Game");
            quitLabel.setHorizontalAlignment(JLabel.CENTER);
            quitLabel.setFont(new Font("Arial", Font.BOLD, 36));
            quitLabel.setForeground(chosenBackground);
            quitLabel.setBackground(transparentBg);
            quitLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

            });
            add(quitLabel);
            validate();
        }
    }

    public class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        EnemyPiece enemyPiece;

        BoardPanel() {
            super(new GridLayout(5, 5));
            CompoundBorder boardBorder = new CompoundBorder(new LineBorder(lightBorderColor, 4),
                    new LineBorder(darkBorderColor, 4));
            this.setBackground(darkBorderColor);
            this.setBorder(boardBorder);
            this.boardTiles = new ArrayList<>();
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    final TilePanel tilePanel = new TilePanel(this, r, c);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }
            validate();
        }

        public void drawBoard(MiniBoard board) {
            removeAll();
            for (TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

        public void highlightDangerousTile(final int row, final int col, final boolean highlight) {
            if (highlight) {
                if (gameState.getChessBoard().getTile(row, col).isOccupied()) {
                    MiniPiece piece = gameState.getChessBoard().getTile(row, col).getPiece();
                    if (piece instanceof EnemyPiece) {
                        enemyPiece = (EnemyPiece) piece;
                        for (Integer cor : enemyPiece.getRange()) {
                            boardTiles.get(cor).redHighLight();
                        }
                    }
                }
            } else {
                if (enemyPiece != null) {
                    for (Integer cor : enemyPiece.getRange()) {
                        boardTiles.get(cor).colorTile(gameState.getChessBoard());
                    }
                }
                enemyPiece = null;
            }
        }
    }

    private class TilePanel extends JPanel {
        private final int row, col;

        TilePanel(final BoardPanel boardPanel, final int row, final int col) {
            super(new BorderLayout(0, 0));
            this.row = row;
            this.col = col;
            colorTile(gameState.getChessBoard());
            placePieceIconOnTile(gameState.getChessBoard());
            legalMovesHighlighter(gameState.getChessBoard());
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (chosenCard != null) {
                        boolean isMoveLegal = false;
                        MiniMove legalMove = null;
                        for (MiniMove move : chosenCard.legalMoves(gameState.getChessBoard(),
                                gameState.getChessBoard().getPlayerPiece())) {
                            if (move.getDestinationRow() == row && move.getDestinationCol() == col) {
                                isMoveLegal = true;
                                legalMove = move;
                                break;
                            }
                        }
                        if (isMoveLegal) {
                            gameState.setChessBoard(legalMove.execute());
                            gameState.getDeck().getHand().remove(chosenCard);
                            if (!chosenCard.getHasPower(3)) {
                                gameState.setMoveLeft(gameState.getMoveLeft() - 1);
                            } else {
                                gameState.getDeck().draw();
                            }
                            if (chosenCard.getHasPower(2))
                                gameState.setShield(gameState.getShield() + 1);
                            chosenCard = null;
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            boardPanel.drawBoard(gameState.getChessBoard());
                            southPanel.redraw();
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    boardPanel.highlightDangerousTile(row, col, true);
                    eastPanel.redraw(gameState.getChessBoard().getTile(row, col));
                    setBackground(lightBorderColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    boardPanel.highlightDangerousTile(row, col, false);
                    colorTile(gameState.getChessBoard());
                }

            });
            validate();
        }

        public void drawTile(MiniBoard board) {
            removeAll();
            colorTile(board);
            legalMovesHighlighter(board);
            placePieceIconOnTile(board);
            validate();
            repaint();
        }

        private void placePieceIconOnTile(final MiniBoard board) {
            this.removeAll();
            if (board.getTile(this.row, this.col).isOccupied()) {
                if (board.getTile(row, col).getPiece() instanceof PlayerPiece) {
                    try {
                        final BufferedImage playerImage = ImageIO.read(new File(PLAYER_ICON_PATH
                                + board.getPlayerPiece().getPieceType().toString() + ".png"));
                        final ImageIcon icon = new ImageIcon(playerImage);
                        add(new JLabel(
                                new ImageIcon(icon.getImage().getScaledInstance((int) screenSize.getWidth() / 19,
                                        (int) screenSize.getHeight() / 11, Image.SCALE_SMOOTH))),
                                BorderLayout.CENTER);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } else {
                    EnemyPiece enemyPiece = (EnemyPiece) board.getTile(row, col).getPiece();
                    String filePath = ENEMY_ICON_PATH;
                    if (enemyPiece.isImmune())
                        filePath += "i";
                    filePath += enemyPiece.getPieceType().toString() + ".png";
                    try {
                        final BufferedImage enemyImage = ImageIO.read(new File(filePath));
                        final ImageIcon icon = new ImageIcon(enemyImage);
                        add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance((int) screenSize.getWidth() / 21,
                                (int) screenSize.getHeight() / 13, Image.SCALE_SMOOTH))), BorderLayout.CENTER);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }

        }

        private void colorTile(final MiniBoard board) {
            if (board.getTile(row, col).isBlighted())
                setBackground(((this.row + this.col) % 2 == 0) ? lightGreen : darkGreen);
            else
                setBackground(((this.row + this.col) % 2 == 0) ? lightTileColor : darkTileColor);
        }

        public void redHighLight() {
            setBackground(((this.row + this.col) % 2 == 0) ? lightRed : darkRed);
        }

        private void legalMovesHighlighter(final MiniBoard board) {
            boolean highlight = false;
            if (chosenCard != null) {
                for (MiniMove move : chosenCard.legalMoves(board, board.getPlayerPiece())) {
                    if (move.getDestinationRow() == this.row && move.getDestinationCol() == this.col) {
                        highlight = true;
                        setBorder(new LineBorder(chosenBackground, 4));
                    }
                }
            }
            if (!highlight) {
                setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
}
