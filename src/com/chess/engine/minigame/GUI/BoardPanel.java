package com.chess.engine.minigame.GUI;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.chess.Game;
import com.chess.engine.minigame.board.MiniBoard;
import com.chess.engine.minigame.board.MiniMove;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.cards.Card;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class BoardPanel extends JPanel {
    private final GamePanel gp;
    private final List<TilePanel> tileList = new ArrayList<>();
    private PiecePanel playerPanel;
    private List<PiecePanel> enemyPanels = new ArrayList<>();
    private MiniTile tileEntered = null;
    private PiecePanel isMoving = null;
    private int turn = 1;

    public BoardPanel(final GamePanel gp) {
        super(new GridLayout(5, 5, 0, 0));
        this.setPreferredSize(
                new Dimension((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getWidth() / 3));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                TilePanel tile = new TilePanel(this, i, j);
                tileList.add(tile);
                add(tile);
            }
        }
        this.gp = gp;
        this.playerPanel = new PiecePanel(this, gp.getGameState().getChessBoard().getPlayerPiece());
        for (EnemyPiece enemyPiece : gp.getGameState().getChessBoard().getEnemyPieces()) {
            PiecePanel enemyPanel = new PiecePanel(this, enemyPiece);
            enemyPanels.add(enemyPanel);
        }
    }

    public void draw(final Graphics2D g2) {
        g2.setColor(ColorList.lightBorderColor);
        g2.fillRect((int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getHeight() / 12,
                (int) Game.screenSize.getWidth() / 3, (int) Game.screenSize.getWidth() / 3);
        g2.setColor(ColorList.darkBorderColor);
        g2.fillRect((int) Game.screenSize.getWidth() / 3 + 5, (int) Game.screenSize.getHeight() / 12 + 5,
                (int) Game.screenSize.getWidth() / 3 - 10, (int) Game.screenSize.getWidth() / 3 - 10);
        g2.fillRect(ALLBITS, ABORT, WIDTH, HEIGHT);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tileList.get(i * 5 + j).draw(g2);
            }
        }
        playerPanel.draw(g2);
        for (PiecePanel enemyPanel : enemyPanels) {
            enemyPanel.draw(g2);
        }

    }

    public void update() {
        if (gp.getGameState().getMoveLeft() == 0) {
            EnemyPiece enemyPiece = gp.getGameState().getChessBoard().getNotMovedPiece(turn);
            if (enemyPiece != null) {
                for (PiecePanel piecePanel : enemyPanels) {
                    if (piecePanel.getPiece().equals(enemyPiece)) {
                        gp.getGameState().doMove(enemyPiece);
                        break;
                    }
                }

            } else {
                if (isMoving == null) {
                    gp.getGameState().getDeck().fillHand(3);
                    gp.getGameState().setMoveLeft(2);
                }
            }
        }

        for (TilePanel tilePanel : this.tileList) {
            tilePanel.update();
        }

        for (int i = 0; i < enemyPanels.size(); i++) {
            if (!gp.getGameState().getChessBoard().getEnemyPieces().contains(enemyPanels.get(i).getPiece())) {
                enemyPanels.remove(i);
                i--;
            }
        }

        for (EnemyPiece enemyPiece : gp.getGameState().getChessBoard().getEnemyPieces()) {
            boolean flag = true;
            for (PiecePanel piecePanel : enemyPanels) {
                if (piecePanel.getPiece().equals(enemyPiece)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                PiecePanel enemyPanel = new PiecePanel(this, enemyPiece);
                enemyPanels.add(enemyPanel);
            }
        }
        this.playerPanel.update();
        for (PiecePanel enemyPanel : enemyPanels) {
            enemyPanel.update();
        }

    }

    private void setTileEntered(final int r, final int c) {
        if (r == -1 && c == -1) {
            if (tileEntered != null && tileEntered.isOccupied()) {
                for (TilePanel tilePanel : tileList) {
                    tilePanel.setInRange(false);
                }
            }
            this.tileEntered = null;
        } else {
            this.tileEntered = gp.getGameState().getChessBoard().getTile(r, c);
            if (tileEntered.isOccupied()) {
                MiniPiece piece = tileEntered.getPiece();
                if (piece instanceof EnemyPiece) {
                    EnemyPiece enemyPiece = (EnemyPiece) piece;
                    for (int cor : enemyPiece.getRange()) {
                        tileList.get(cor).setInRange(true);
                    }
                }
            }
        }
    }

    public void endTurn() {
        gp.getGameState().getDeck().emptyHand();
        gp.getGameState().setTurn(++turn);
    }

    private class TilePanel extends JPanel {
        private int r, c;
        private BoardPanel bp;
        private boolean isEntered = false;
        private boolean isInRange = false;
        private boolean isMovable = false;

        TilePanel(final BoardPanel bp, final int r, final int c) {
            super(new FlowLayout());
            this.bp = bp;
            this.r = r;
            this.c = c;
            this.setPreferredSize(new Dimension(85, 85));
            this.isFocusable();
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isMovable && gp.getGameState().getMoveLeft() > 0) {
                        // bp.playerPanel.setNewRC(r, c);
                        gp.getGameState().doMove(gp.getChosenCard(), r, c);
                        gp.getGameState().getDeck().getUsedCard().add(gp.getChosenCard());
                        gp.getGameState().getDeck().getHand().remove(gp.getChosenCard());
                        if (gp.getChosenCard().getHasPower(0)) {
                            gp.getGameState().crossAttack();
                        }
                        if (gp.getChosenCard().getHasPower(1)) {
                            gp.getGameState().diagonalAttack();
                        }
                        if (gp.getChosenCard().getHasPower(2)) {
                            gp.getGameState().setShield(gp.getGameState().getShield() + 1);
                        }
                        if (gp.getChosenCard().getHasPower(3)) {
                            gp.getGameState().setMoveLeft(gp.getGameState().getMoveLeft() + 1);
                            gp.getGameState().getDeck().draw();
                        }
                        if (gp.getGameState().getMoveLeft() == 0) {
                            gp.getGameState().doDamage();
                            endTurn();
                        }

                        gp.setChosenCard(null);
                        bp.setTileEntered(-1, -1);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    isEntered = true;
                    gp.eastPanel.setObjEntered(gp.getGameState().getChessBoard().getTile(r, c));
                    setTileEntered(r, c);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isEntered = false;
                    setTileEntered(-1, -1);
                }
            });
            validate();
        }

        private void update() {
            if (tileEntered == null)
                this.isInRange = false;
            if (gp.getChosenCard() != null) {
                if (gp.getChosenCard().canAttack(this.r, this.c, gp.getGameState().getChessBoard())) {
                    this.isMovable = true;
                } else
                    this.isMovable = false;
            } else
                this.isMovable = false;
        }

        private void draw(final Graphics2D g2) {
            if (isMovable) {
                g2.setColor(ColorList.chosenBackground);
            } else {
                if (isEntered) {
                    g2.setColor(ColorList.lightBorderColor);
                } else {
                    if (isInRange) {
                        g2.setColor(((r + c) % 2 == 0) ? ColorList.darkRed : ColorList.lightRed);
                    } else {
                        g2.setColor(((r + c) % 2 == 0) ? ColorList.darkTileColor : ColorList.lightTileColor);
                    }
                }
            }
            int x = (int) Game.screenSize.getWidth() / 3 + 10 + c * ((int) Game.screenSize.getWidth() / 15 - 4);
            int y = (int) Game.screenSize.getHeight() / 12 + 10 + r * ((int) Game.screenSize.getWidth() / 15 - 4);
            g2.fillRect(x, y, (int) Game.screenSize.getWidth() / 15 - 4, (int) Game.screenSize.getWidth() / 15 - 4);
            if (isEntered) {
                g2.setColor(ColorList.lightBorderColor);
            } else {
                if (isInRange) {
                    g2.setColor(((r + c) % 2 == 0) ? ColorList.darkRed : ColorList.lightRed);
                } else {
                    g2.setColor(((r + c) % 2 == 0) ? ColorList.darkTileColor : ColorList.lightTileColor);
                }
            }
            x = (int) Game.screenSize.getWidth() / 3 + 15 + c * ((int) Game.screenSize.getWidth() / 15 - 4);
            y = (int) Game.screenSize.getHeight() / 12 + 15 + r * ((int) Game.screenSize.getWidth() / 15 - 4);
            g2.fillRect(x, y, (int) Game.screenSize.getWidth() / 15 - 14, (int) Game.screenSize.getWidth() / 15 - 14);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.setColor(Color.white);
            g2.drawString(Integer.toString(gp.getGameState().getChessBoard().calculateDamage(r, c)), x, y + ((int) Game.screenSize.getWidth() / 15 -15));
            if (gp.getGameState().getChessBoard().getTile(r, c).isBlighted()) {
                g2.drawImage(Game.imageList.getBlightImage(), x - 4, y - 6, (int) Game.screenSize.getWidth() / 15 - 4,
                        (int) Game.screenSize.getWidth() / 15 - 4, null);

            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        private void setInRange(final boolean isInRange) {
            this.isInRange = isInRange;
        }
    }

    private class PiecePanel extends JPanel {
        private final BoardPanel bp;
        private final MiniPiece piece;
        private int oldR, oldC, newR, newC;
        private int x, y;
        private int oldX, oldY, newX, newY;
        private float alpha = 1.0f;

        PiecePanel(final BoardPanel bp, final MiniPiece piece) {
            super();
            this.setPreferredSize(new Dimension(1, 1));
            this.piece = piece;
            this.bp = bp;
            this.oldR = piece.getRow();
            this.oldC = piece.getCol();
            this.newR = piece.getRow();
            this.newC = piece.getCol();
            this.oldX = this.x = this.newX = (int) Game.screenSize.getWidth() / 3 + 10
                    + piece.getCol() * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.oldY = this.y = this.newY = (int) Game.screenSize.getHeight() / 12 + 10
                    + piece.getRow() * ((int) Game.screenSize.getWidth() / 15 - 4);
        }

        // public void setNewRC(final int newR, final int newC) {
        // this.newR = newR;
        // this.newC = newC;
        // this.newX = (int) Game.screenSize.getWidth() / 3 + 10
        // + newC * ((int) Game.screenSize.getWidth() / 15 - 4);
        // this.newY = (int) Game.screenSize.getHeight() / 12 + 10
        // + newR * ((int) Game.screenSize.getWidth() / 15 - 4);
        // }

        public MiniPiece getPiece() {
            return piece;
        }

        public void update() {
            this.newR = piece.getRow();
            this.newC = piece.getCol();
            this.newX = (int) Game.screenSize.getWidth() / 3 + 10
                    + newC * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.newY = (int) Game.screenSize.getHeight() / 12 + 10
                    + newR * ((int) Game.screenSize.getWidth() / 15 - 4);
            if ((this.newX != this.oldX || this.newY != this.oldY) && isMoving == null) {
                isMoving = this;
            }
            if (isMoving == this) {
                if (this.oldR < this.newR) {
                    int SpeedY = (this.newY - this.oldY) / 20;
                    this.y += SpeedY;
                    if (this.y >= this.newY) {
                        this.y = this.newY;
                        this.oldY = this.newY;
                        this.oldR = this.newR;
                    }
                } else if (this.oldR > this.newR) {
                    int SpeedY = (this.newY - this.oldY) / 20;
                    this.y += SpeedY;
                    if (this.y <= this.newY) {
                        this.y = this.newY;
                        this.oldY = this.newY;
                        this.oldR = this.newR;
                    }
                }
                if (this.oldC < this.newC) {
                    int SpeedX = (this.newX - this.oldX) / 20;
                    this.x += SpeedX;
                    if (this.x >= this.newX) {
                        this.x = this.newX;
                        this.oldX = this.newX;
                        this.oldC = this.newC;
                    }
                } else if (this.oldC > this.newC) {
                    int SpeedX = (this.newX - this.oldX) / 20;
                    this.x += SpeedX;
                    if (this.x <= this.newX) {
                        this.x = this.newX;
                        this.oldX = this.newX;
                        this.oldC = this.newC;
                    }
                }
                if (this.oldX == this.newX && this.oldY == this.newY)
                    isMoving = null;
            }
        }

        public void draw(Graphics2D g2) {
            if (piece instanceof PlayerPiece) {
                g2.drawImage(Game.imageList.getPlayerImage(), this.x, this.y,
                        (int) Game.screenSize.getWidth() / 15 - 4,
                        (int) Game.screenSize.getWidth() / 15 - 4, null);
            } else {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                EnemyPiece enemyPiece = (EnemyPiece) piece;
                String pieceString = "";
                if (enemyPiece.isImmune())
                    pieceString += "i";
                pieceString += enemyPiece.toString();
                g2.drawImage(Game.imageList.getEnemyImage(pieceString), this.x, this.y,
                        (int) Game.screenSize.getWidth() / 15 - 4,
                        (int) Game.screenSize.getWidth() / 15 - 4, null);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                if (enemyPiece.isCurrentlyNimble()) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2.drawImage(Game.imageList.getNimbleImage(), this.x, this.y,
                            (int) Game.screenSize.getWidth() / 15 - 4,
                            (int) Game.screenSize.getWidth() / 15 - 4, null);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
        }
    }
    // private static final String ENEMY_ICON_PATH = "art\\pieces\\enemies\\";
    // private static final String PLAYER_ICON_PATH = "art\\pieces\\player\\";

    // private final List<TilePanel> boardTiles;
    // private EnemyPiece enemyPiece;
    // private final MiniTable miniTable;
    // private Card chosenCard = null;

    // public Card getChosenCard() {
    // return chosenCard;
    // }

    // public void setChosenCard(Card chosenCard) {
    // this.chosenCard = chosenCard;
    // }

    // BoardPanel(final MiniTable miniTable) {
    // super(new GridLayout(5, 5));
    // this.miniTable = miniTable;
    // setDoubleBuffered(true);
    // CompoundBorder boardBorder = new CompoundBorder(new
    // LineBorder(MiniTable.lightBorderColor, 4),
    // new LineBorder(MiniTable.darkBorderColor, 4));
    // this.setBackground(MiniTable.darkBorderColor);
    // this.setBorder(boardBorder);
    // this.boardTiles = new ArrayList<>();
    // for (int r = 0; r < 5; r++) {
    // for (int c = 0; c < 5; c++) {
    // final TilePanel tilePanel = new TilePanel(this, r, c);
    // this.boardTiles.add(tilePanel);
    // add(tilePanel);
    // }
    // }
    // validate();
    // }

    // public void drawBoard(MiniBoard board) {
    // removeAll();
    // for (TilePanel tilePanel : boardTiles) {
    // tilePanel.drawTile(board);
    // add(tilePanel);
    // }
    // validate();
    // repaint();
    // }

    // public void highlightDangerousTile(final int row, final int col, final
    // boolean highlight) {
    // if (highlight) {
    // if (miniTable.getGameState().getChessBoard().getTile(row, col).isOccupied())
    // {
    // MiniPiece piece = miniTable.getGameState().getChessBoard().getTile(row,
    // col).getPiece();
    // if (piece instanceof EnemyPiece) {
    // enemyPiece = (EnemyPiece) piece;
    // for (Integer cor : enemyPiece.getRange()) {
    // boardTiles.get(cor).redHighLight();
    // }
    // }
    // }
    // } else {
    // if (enemyPiece != null) {
    // for (Integer cor : enemyPiece.getRange()) {
    // boardTiles.get(cor).colorTile(miniTable.getGameState().getChessBoard());
    // }
    // }
    // enemyPiece = null;
    // }
    // }

    // public void skip() {
    // miniTable.getGameState().setMoveLeft(0);
    // miniTable.getGameState().getDeck().emptyHand();
    // miniTable.getGameState().doDamage();
    // miniTable.southPanel.redrawStatus();
    // miniTable.getGameState().addTurn();
    // if (miniTable.getGameState().getCurrentHealth() > 0)
    // enemyTurn();
    // }

    // private void enemyTurn() {

    // EnemyPiece enemyNotMoved = miniTable.getGameState().getChessBoard()
    // .getNotMovedPiece(miniTable.getGameState().getTurn());
    // if (enemyNotMoved != null) {
    // miniTable.getGameState().doMove(enemyNotMoved);
    // drawBoard(miniTable.getGameState().getChessBoard());
    // enemyNotMoved = miniTable.getGameState().getChessBoard()
    // .getNotMovedPiece(miniTable.getGameState().getTurn());
    // enemyTurn();
    // }
    // else{
    // playerTurn();
    // }
    // }

    // public void playerTurn() {
    // miniTable.getGameState().getDeck().fillHand(3);
    // miniTable.getGameState().setMoveLeft(3);
    // miniTable.southPanel.redrawStatus();
    // miniTable.westPanel.redraw();
    // miniTable.southPanel.redraw();
    // }

    // private class TilePanel extends JPanel {
    // private final int row, col;

    // TilePanel(final BoardPanel boardPanel, final int row, final int col) {
    // super(new BorderLayout(0, 0));
    // this.row = row;
    // this.col = col;
    // colorTile(miniTable.getGameState().getChessBoard());
    // placePieceIconOnTile(miniTable.getGameState().getChessBoard());
    // legalMovesHighlighter(miniTable.getGameState().getChessBoard());
    // addMouseListener(new MouseListener() {
    // @Override
    // public void mouseClicked(MouseEvent e) {
    // if (chosenCard != null) {
    // boolean isMoveLegal = false;
    // MiniMove legalMove = null;
    // for (MiniMove move :
    // chosenCard.legalMoves(miniTable.getGameState().getChessBoard(),
    // miniTable.getGameState().getChessBoard().getPlayerPiece())) {
    // if (move.getDestinationRow() == row && move.getDestinationCol() == col) {
    // isMoveLegal = true;
    // legalMove = move;
    // break;
    // }
    // }
    // if (isMoveLegal) {
    // miniTable.getGameState().setChessBoard(legalMove.execute());
    // miniTable.getGameState().getDeck().getHand().remove(chosenCard);
    // if (!chosenCard.getHasPower(3)) {
    // miniTable.getGameState().setMoveLeft(miniTable.getGameState().getMoveLeft() -
    // 1);
    // if (miniTable.getGameState().getMoveLeft() <= 0) {
    // miniTable.getGameState().getDeck().emptyHand();
    // miniTable.getGameState().doDamage();
    // miniTable.southPanel.redrawStatus();
    // miniTable.getGameState().addTurn();
    // if (miniTable.getGameState().getCurrentHealth() > 0)
    // enemyTurn();
    // }
    // } else {
    // miniTable.getGameState().getDeck().draw();
    // }
    // if (chosenCard.getHasPower(2))
    // miniTable.getGameState().setShield(miniTable.getGameState().getShield() + 1);
    // chosenCard = null;
    // }
    // }
    // SwingUtilities.invokeLater(new Runnable() {
    // @Override
    // public void run() {
    // boardPanel.drawBoard(miniTable.getGameState().getChessBoard());
    // miniTable.southPanel.redrawStatus();
    // miniTable.southPanel.redraw();
    // miniTable.westPanel.redraw();
    // }
    // });
    // }

    // @Override
    // public void mousePressed(MouseEvent e) {
    // // TODO Auto-generated method stub
    // }

    // @Override
    // public void mouseReleased(MouseEvent e) {
    // // TODO Auto-generated method stub
    // }

    // @Override
    // public void mouseEntered(MouseEvent e) {
    // boardPanel.highlightDangerousTile(row, col, true);
    // miniTable.eastPanel.redraw(miniTable.getGameState().getChessBoard().getTile(row,
    // col));
    // setBackground(MiniTable.lightBorderColor);
    // }

    // @Override
    // public void mouseExited(MouseEvent e) {
    // boardPanel.highlightDangerousTile(row, col, false);
    // colorTile(miniTable.getGameState().getChessBoard());
    // }

    // });
    // validate();
    // }

    // public void drawTile(MiniBoard board) {
    // removeAll();
    // colorTile(board);
    // legalMovesHighlighter(board);
    // placePieceIconOnTile(board);
    // validate();
    // repaint();
    // }

    // private void placePieceIconOnTile(final MiniBoard board) {
    // this.removeAll();
    // if (board.getTile(this.row, this.col).isOccupied()) {
    // if (board.getTile(row, col).getPiece() instanceof PlayerPiece) {
    // try {
    // final BufferedImage playerImage = ImageIO.read(new File(PLAYER_ICON_PATH
    // + board.getPlayerPiece().getPieceType().toString() + ".png"));
    // final ImageIcon icon = new ImageIcon(playerImage);
    // add(new JLabel(
    // new ImageIcon(
    // icon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 19,
    // (int) MiniTable.screenSize.getHeight() / 11, Image.SCALE_SMOOTH))),
    // BorderLayout.CENTER);
    // } catch (IOException exception) {
    // exception.printStackTrace();
    // }
    // } else {
    // EnemyPiece enemyPiece = (EnemyPiece) board.getTile(row, col).getPiece();
    // String filePath = ENEMY_ICON_PATH;
    // if (enemyPiece.isImmune())
    // filePath += "i";
    // filePath += enemyPiece.getPieceType().toString() + ".png";
    // try {
    // final BufferedImage enemyImage = ImageIO.read(new File(filePath));
    // final ImageIcon icon = new ImageIcon(enemyImage);
    // add(new JLabel(new ImageIcon(
    // icon.getImage().getScaledInstance((int) MiniTable.screenSize.getWidth() / 21,
    // (int) MiniTable.screenSize.getHeight() / 13, Image.SCALE_SMOOTH))),
    // BorderLayout.CENTER);
    // } catch (IOException exception) {
    // exception.printStackTrace();
    // }
    // }
    // }

    // }

    // private void colorTile(final MiniBoard board) {
    // if (board.getTile(row, col).isBlighted())
    // setBackground(((this.row + this.col) % 2 == 0) ? MiniTable.lightGreen :
    // MiniTable.darkGreen);
    // else
    // setBackground(((this.row + this.col) % 2 == 0) ? MiniTable.lightTileColor :
    // MiniTable.darkTileColor);
    // }

    // public void redHighLight() {
    // setBackground(((this.row + this.col) % 2 == 0) ? MiniTable.lightRed :
    // MiniTable.darkRed);
    // }

    // private void legalMovesHighlighter(final MiniBoard board) {
    // boolean highlight = false;
    // if (chosenCard != null) {
    // for (MiniMove move : chosenCard.legalMoves(board, board.getPlayerPiece())) {
    // if (move.getDestinationRow() == this.row && move.getDestinationCol() ==
    // this.col) {
    // highlight = true;
    // setBorder(new LineBorder(MiniTable.chosenBackground, 4));
    // }
    // }
    // }
    // if (!highlight) {
    // setBorder(BorderFactory.createEmptyBorder());
    // }
    // }
    // }
}
