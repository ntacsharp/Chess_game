package com.chess.engine.minigame.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.GameState;
import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.cards.Deck;
import com.chess.engine.minigame.pieces.player.PlayerPiece.PieceType;

public class MiniTable {
    private final JFrame gameFrame;
    private GameState gameState;
    private Card chosenCard;
    private BoardPanel boardPanel;
    private HandPanel handPanel;

    // private static final Color lightTileColor = new Color(99, 105, 136);
    // private static final Color darkTileColor = new Color(56, 57, 97);
    private static final Color lightTileColor = new Color(99, 105, 136);
    private static final Color darkTileColor = new Color(56, 57, 97);
    private static final Color lightHighlight = new Color(167, 170, 190);
    private static final Color darkHighlight = new Color(139, 140, 186);
    private static final Color redHighlight = new Color(193, 164, 190);
    private static final Color cardBackground = new Color(217, 231, 236);

    private static final String PIECE_ICON_PATH = "art/pieces/";
    private static final String POWER_ICON_PATH = "art/other/power/";

    public MiniTable() {
        this.gameState = new GameState(PieceType.BABARIAN);
        this.gameFrame = new JFrame();
        this.gameFrame.setTitle("Pawnbarian Mode");
        this.gameFrame.setLayout(new BorderLayout());
        
        
    }

    private void setUpShoppingRound(){
        this.gameFrame.removeAll();

    }

    private void setUpGamingRound(){
        this.gameFrame.removeAll();
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
            validate();
        }
    }

    private class CardPanel extends JPanel {
        private final Card card;

        CardPanel(final HandPanel handPanel, final Card card) {
            super(new BorderLayout());
            this.card = card;
            setBackground(cardBackground);
            decorateCard();
        }

        private void decorateCard() {
            this.removeAll();
            try {
                final BufferedImage mainImage = ImageIO.read(new File(PIECE_ICON_PATH
                        + "W" + this.card.getCardType().toString() + ".png"));
                add(new JLabel(new ImageIcon(mainImage)), BorderLayout.CENTER);
                JLabel powerLabel = new JLabel();
                powerLabel.setLayout(new FlowLayout());
                for (int i = 0; i < 4; i++) {
                    if (this.card.getHasPower(i)) {
                        BufferedImage powerImage = ImageIO
                                .read(new File(POWER_ICON_PATH + this.card.getPowerByID(i).toString() + ".png"));
                        powerLabel.add(new JLabel(new ImageIcon(powerImage)));
                    }
                }
                add(powerLabel, BorderLayout.NORTH);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
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
            // setPreferredSize(BOARD_PANEL_DIMENSION);
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

        TilePanel(final BoardPanel boardPanel, final int row, final int col) {
            super(new GridBagLayout());
            this.row = row;
            this.col = col;
            // setPreferredSize(TILE_PANEL_DIMENSION);
            colorTile();
            placePieceIconOnTile(gameState.getChessBoard());
            validate();
        }

        public void drawTile(MiniBoard board) {
            colorTile();
            placePieceIconOnTile(board);
            legalMovesHighlighter(board);
            validate();
            repaint();
        }

        private void placePieceIconOnTile(final MiniBoard board) {
            this.removeAll();
            if (board.getTile(this.row, this.col).isOccupied()) {
                // try {
                // final BufferedImage bufferedImage = ImageIO.read(new File(PIECE_ICON_PATH
                // + board.getTile(this.row,
                // this.col).getPiece().getPieceParty().toString().substring(0, 1)
                // + board.getTile(this.row, this.col).getPiece().toString() + ".png"));
                // add(new JLabel(new ImageIcon(bufferedImage)));
                // } catch (IOException exception) {
                // exception.printStackTrace();
                // }
            }
        }

        private void colorTile() {
            setBackground(((this.row + this.col) % 2 == 0) ? lightTileColor : darkTileColor);
        }

        private void legalMovesHighlighter(final MiniBoard board) {
            for (MiniMove move : calculateLegalMoves(board)) {
                if (move.getDestinationRow() == this.row && move.getDestinationCol() == this.col) {
                    if (move.isAttackingMove()) {
                        setBackground(redHighlight);
                    } else {
                        setBackground(((this.row + this.col) % 2 == 0) ? lightHighlight : darkHighlight);
                    }
                }
            }
        }

        private Collection<MiniMove> calculateLegalMoves(final MiniBoard board) {
            return chosenCard.legalMoves(gameState.getChessBoard(), board.getPlayerPiece());
        }
    }
}
