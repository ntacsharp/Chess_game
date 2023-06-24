package com.chess.engine.minigame.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class MiniTable {
    public static final Color lightTileColor = new Color(99, 105, 136);
    public static final Color darkTileColor = new Color(56, 57, 97);
    public static final Color lightBorderColor = new Color(219, 233, 238);
    public static final Color darkBorderColor = new Color(141, 153, 174);
    public static final Color chosenBackground = new Color(255, 181, 63);
    public static final Color darkRed = new Color(147, 62, 100);
    public static final Color lightRed = new Color(169, 85, 119);

    private static final String ENEMY_ICON_PATH = "art/pieces/enemies/";
    private static final String PLAYER_ICON_PATH = "art/pieces/player/";

    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final JFrame gameFrame;
    private GameState gameState;
    private Card chosenCard = null;
    private BoardPanel boardPanel;
    private SouthPanel southPanel;
    private NorthPanel northPanel;

    public MiniTable() {
        this.gameFrame = new JFrame();
        this.gameFrame.setSize(screenSize);
        // this.gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.gameFrame.setUndecorated(true);
        setUpGamingRound();
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

    public void setting(){
        System.out.println("setting");
    }

    public void drawBoard(final MiniBoard board) {
        boardPanel.drawBoard(board);
    }

    private void setUpShoppingRound() {
        this.gameFrame.removeAll();
    }

    private void setUpGamingRound() {
        this.gameState = new GameState(PieceType.BABARIAN);
        // this.gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.gameFrame.setTitle("Pawnbarian Mode");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.boardPanel = new BoardPanel();
        this.southPanel = new SouthPanel(this);
        this.northPanel = new NorthPanel(this);
        JPanel eastPanel = new JPanel();
        eastPanel.setPreferredSize(new Dimension((int) screenSize.getWidth() / 3, (int) screenSize.getHeight()));
        eastPanel.setBackground(darkTileColor);
        JPanel westPanel = new JPanel();
        westPanel.setPreferredSize(new Dimension((int) screenSize.getWidth() / 3, (int) screenSize.getHeight()));
        westPanel.setBackground(darkTileColor);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(southPanel, BorderLayout.SOUTH);
        this.gameFrame.add(northPanel, BorderLayout.NORTH);
        this.gameFrame.add(eastPanel, BorderLayout.EAST);
        this.gameFrame.add(westPanel, BorderLayout.WEST);
        this.gameFrame.setVisible(true);
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;
        EnemyPiece enemyPiece;

        BoardPanel() {
            super(new GridLayout(5, 5));
            CompoundBorder boardBorder = new CompoundBorder(new LineBorder(lightBorderColor, 4),
                    new LineBorder(darkBorderColor, 4));
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
                        boardTiles.get(cor).colorTile();
                    }
                }
                enemyPiece = null;
            }
        }
    }

    private class TilePanel extends JPanel {
        private final int row, col;

        TilePanel(final BoardPanel boardPanel, final int row, final int col) {
            super(new GridBagLayout());
            this.row = row;
            this.col = col;
            colorTile();
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
                    setBackground(lightBorderColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    boardPanel.highlightDangerousTile(row, col, false);
                    colorTile();
                }

            });
            validate();
        }

        public void drawTile(MiniBoard board) {
            removeAll();
            colorTile();
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
                        final BufferedImage bufferedImage = ImageIO.read(new File(PLAYER_ICON_PATH
                                + board.getPlayerPiece().getPieceType().toString() + ".png"));
                        final ImageIcon icon = new ImageIcon(bufferedImage);
                        add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH))));
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
                        final BufferedImage bufferedImage = ImageIO.read(new File(filePath));
                        final ImageIcon icon = new ImageIcon(bufferedImage);
                        add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH))));
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        private void colorTile() {
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
