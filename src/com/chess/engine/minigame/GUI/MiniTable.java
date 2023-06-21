package com.chess.engine.minigame.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniBoardUtils;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class MiniTable {
    private final JFrame gameFrame;
    private GameState gameState;
    private Card chosenCard = null;
    private BoardPanel boardPanel;
    private HandPanel handPanel;
    private MiniTile tileEntered = null;

    private static final Dimension FRAME_DIMENSION = new Dimension(700, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(300, 250);
    private static final Dimension HAND_PANEL_DIMENSION = new Dimension(100, 250);
    private static final Dimension CARD_PANEL_DIMENSION = new Dimension(100, 100);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(50, 50);

    private static final Color lightTileColor = new Color(99, 105, 136);
    private static final Color darkTileColor = new Color(56, 57, 97);
    private static final Color lightHighlight = new Color(167, 170, 190);
    private static final Color darkHighlight = new Color(139, 140, 186);
    private static final Color redHighlight = new Color(193, 164, 190);
    private static final Color cardBackground = new Color(217, 231, 236);

    private static final String PIECE_ICON_PATH = "art/pieces/";
    private static final String POWER_ICON_PATH = "art/other/power/";
    private static final String ENEMY_ICON_PATH = "art/pieces/enemies/";
    private static final String PLAYER_ICON_PATH = "art/pieces/player/";

    public MiniTable() {
        this.gameFrame = new JFrame();
        setUpGamingRound();
    }

    private void setUpShoppingRound() {
        this.gameFrame.removeAll();

    }

    private void setUpGamingRound() {
        this.gameState = new GameState(PieceType.BABARIAN);
        this.gameFrame.setSize(FRAME_DIMENSION);
        this.gameFrame.setTitle("Pawnbarian Mode");
        this.gameFrame.setLayout(new BorderLayout());
        this.gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.boardPanel = new BoardPanel();
        this.handPanel = new HandPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.handPanel, BorderLayout.SOUTH);
        this.gameFrame.setVisible(true);
    }

    private class HandPanel extends JPanel {
        final List<CardPanel> handCards;

        HandPanel() {
            super(new FlowLayout());
            this.handCards = new ArrayList<CardPanel>();
            for (int i = 0; i < gameState.getDeck().getHand().size(); i++) {
                final CardPanel cardPanel = new CardPanel(this, gameState.getDeck().getHand().get(i));
                this.handCards.add(cardPanel);
                add(cardPanel);
            }
            setPreferredSize(HAND_PANEL_DIMENSION);
            validate();
        }

        public void drawHand(Deck deck) {
            removeAll();
            for (CardPanel cardPanel : handCards) {
                cardPanel.drawCard(deck);
                add(cardPanel);
            }
            validate();
            repaint();
        }
    }

    private class CardPanel extends JPanel {
        private final Card card;

        CardPanel(final HandPanel handPanel, final Card card) {
            super(new BorderLayout());
            this.card = card;
            setBackground(cardBackground);
            setPreferredSize(CARD_PANEL_DIMENSION);
            decorateCard();
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        chosenCard = card;
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        chosenCard = null;
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            handPanel.drawHand(gameState.getDeck());
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
                    // TODO Auto-generated method stub
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                }
            });
        }

        private void decorateCard() {
            this.removeAll();
            try {
                final BufferedImage mainImage = ImageIO.read(new File(PIECE_ICON_PATH
                        + "W" + this.card.getCardType().toString() + ".png"));
                add(new JLabel(new ImageIcon(mainImage)), BorderLayout.CENTER);
                JLabel powerLabel = new JLabel();
                powerLabel.setLayout(new FlowLayout());
                powerLabel.setPreferredSize(new Dimension(20, 20));
                powerLabel.setBackground(Color.CYAN);
                for (int i = 0; i < 4; i++) {

                    if (this.card.getHasPower(i)) {
                        System.out.println(this.card.getPowerByID(i).toString());
                        BufferedImage powerImage = ImageIO
                                .read(new File(POWER_ICON_PATH + this.card.getPowerByID(i).toString() + ".png"));
                        final ImageIcon icon = new ImageIcon(powerImage);
                        powerLabel.add(new JLabel(
                                new ImageIcon(icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH))));
                    }
                }
                add(powerLabel, BorderLayout.NORTH);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        private void highlightChosenCard(final Deck deck, final boolean clicked) {

        }

        public void drawCard(Deck deck) {
            setBackground(cardBackground);
            decorateCard();
            validate();
            repaint();
        }
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(5, 5));
            this.boardTiles = new ArrayList<>();
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    final TilePanel tilePanel = new TilePanel(this, r, c);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
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
    }

    private class TilePanel extends JPanel {
        private final int row, col;
        private boolean isDangeous;

        TilePanel(final BoardPanel boardPanel, final int row, final int col) {
            super(new GridBagLayout());
            this.row = row;
            this.col = col;
            this.isDangeous = false;
            tileEntered = null;
            setPreferredSize(TILE_PANEL_DIMENSION);
            colorTile();
            placePieceIconOnTile(gameState.getChessBoard());
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (chosenCard != null) {
                        final MiniMove move = MiniMove.MoveFactory.findMove(gameState.getChessBoard(), chosenCard,
                                gameState.getChessBoard().getPlayerPiece().getCorID(),
                                row * MiniBoardUtils.NUM_TILE_PER_ROW + col);
                        if (move != null) {
                            gameState.setChessBoard(move.execute());
                            gameState.getDeck().getHand().remove(chosenCard);
                            if (!chosenCard.getHasPower(3)) {
                                gameState.setMoveLeft(gameState.getMoveLeft() - 1);
                            } else {
                                gameState.getDeck().draw();
                            }
                            if (chosenCard.getHasPower(2))
                                gameState.setShield(gameState.getShield() + 1);
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            boardPanel.drawBoard(gameState.getChessBoard());
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
                    // tileEntered = gameState.getChessBoard().getTile(row, col);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // tileEntered = null;
                }

            });
            validate();
        }

        public void drawTile(MiniBoard board) {
            colorTile();
            legalMovesHighlighter(board);
            // dangerHighlighter(board);
            placePieceIconOnTile(board);
            validate();
            repaint();
        }

        private void placePieceIconOnTile(final MiniBoard board) {
            this.removeAll();
            if (board.getTile(this.row, this.col).isOccupied()) {
                if (board.getTile(row, col).getPiece() instanceof PlayerPiece) {
                    try {
                        // final BufferedImage bufferedImage = ImageIO.read(new
                        // File("art/pieces/enemies/AR.png"));
                        final BufferedImage bufferedImage = ImageIO.read(new File(PLAYER_ICON_PATH
                                + board.getPlayerPiece().getPieceType().toString() + ".png"));
                        final ImageIcon icon = new ImageIcon(bufferedImage);
                        add(new JLabel(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH))));
                    } catch (IOException exception) {
                        System.out.println(PLAYER_ICON_PATH
                                + board.getPlayerPiece().getPieceType().toString() + ".png");
                        exception.printStackTrace();
                    }
                } else {
                    EnemyPiece enemyPiece = (EnemyPiece) board.getTile(row, col).getPiece();
                    String filePath = ENEMY_ICON_PATH;
                    // if(this.isDangeous) filePath += "d";
                    // else
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

        private void dangerHighlighter(final MiniBoard board) {
            if (board.getTile(row, col).isOccupied()) {
                MiniPiece piece = board.getTile(row, col).getPiece();
                if (piece instanceof EnemyPiece) {
                    EnemyPiece enemyPiece = (EnemyPiece) piece;
                    if (enemyPiece.canAttactk(tileEntered.getRow(), tileEntered.getCol()))
                        this.isDangeous = true;
                }
            }
        }

        private void colorTile() {
            setBackground(((this.row + this.col) % 2 == 0) ? lightTileColor : darkTileColor);
        }

        private void legalMovesHighlighter(final MiniBoard board) {
            for (MiniMove move : chosenCard.legalMoves(board, board.getPlayerPiece())) {
                if (move.getDestinationRow() == this.row && move.getDestinationCol() == this.col) {
                    setBackground(lightHighlight);
                }
            }
        }
    }
}
