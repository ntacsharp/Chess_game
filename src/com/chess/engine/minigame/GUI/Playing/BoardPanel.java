package com.chess.engine.minigame.GUI.Playing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.chess.Game;
import com.chess.engine.minigame.GUI.ColorList;
import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.GUI.Sound;
import com.chess.engine.minigame.board.MiniTile;
import com.chess.engine.minigame.pieces.MiniPiece;
import com.chess.engine.minigame.pieces.enemy.EnemyPiece;
import com.chess.engine.minigame.pieces.player.PlayerPiece;

public class BoardPanel extends JPanel {
    private final GamePanel gp;
    private final List<TilePanel> tileList = new ArrayList<>();
    private PiecePanel playerPanel;
    private List<PiecePanel> enemyPanels = new ArrayList<>();
    private List<DeadPanel> deadEnemies = new ArrayList<>();
    private List<DamagePanel> damages = new ArrayList<>();
    private AttackPanel attackPanel = null;
    private MiniTile tileEntered = null;
    private PiecePanel isMoving = null;
    private int turn = 1, oldR = 4;
    private boolean isDmging = false, calculatedDmg = true;

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
        for (TilePanel tilePanel : tileList) {
            tilePanel.draw(g2);
        }
        playerPanel.draw(g2);
        if (attackPanel != null)
            attackPanel.draw(g2);
        for (PiecePanel enemyPanel : enemyPanels) {
            enemyPanel.draw(g2);
        }
        for (DeadPanel deadPanel : deadEnemies) {
            deadPanel.draw(g2);
        }
        for (DamagePanel damagePanel : damages) {
            damagePanel.draw(g2);
        }
    }

    public void update() {
        if (damages.isEmpty())
            isDmging = false;
        if (gp.getGameState().getMoveLeft() == 0 && !isDmging && isMoving == null) {
            if (!calculatedDmg) {
                gp.getGameState().doDamage();
            }
            calculatedDmg = true;
            EnemyPiece enemyPiece = gp.getGameState().getChessBoard().getNotMovedPiece(turn);
            if (enemyPiece != null) {
                for (PiecePanel piecePanel : enemyPanels) {
                    if (piecePanel.getPiece().equals(enemyPiece)) {
                        gp.getGameState().doMove(enemyPiece);
                        break;
                    }
                }
            } else {
                gp.getGameState().getDeck().fillHand(3);
                if (gp.getGameState().getChessBoard().getPlayerPiece().getRow() == 0)
                    gp.getGameState().getDeck().promote();
                gp.getGameState().setMoveLeft(2);
            }
        }

        for (TilePanel tilePanel : this.tileList) {
            tilePanel.update();
        }

        for (int i = 0; i < enemyPanels.size(); i++) {
            if (!gp.getGameState().getChessBoard().getEnemyPieces().contains(enemyPanels.get(i).getPiece())) {
                deadEnemies.add(new DeadPanel(enemyPanels.get(i).getPiece()));
                enemyPanels.remove(i);
                gp.getGameState().setGold(gp.getGameState().getGold() + 1);
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
        if (attackPanel != null)
            attackPanel.update();
        for (PiecePanel enemyPanel : enemyPanels) {
            enemyPanel.update();
        }
        for (int i = 0; i < deadEnemies.size(); i++) {
            deadEnemies.get(i).update();
        }
        for (int i = 0; i < damages.size(); i++) {
            damages.get(i).update();
        }
    }

    private void setTileEntered(final int r, final int c) {
        this.tileEntered = gp.getGameState().getChessBoard().getTile(r, c);
        if (tileEntered.isOccupied()) {
            MiniPiece piece = tileEntered.getPiece();
            if (piece instanceof EnemyPiece) {
                EnemyPiece enemyPiece = (EnemyPiece) piece;
                for (int cor : enemyPiece.getRange()) {
                    tileList.get(cor).setInRange(true);
                }
            }
        } else {
            tileEntered = null;
        }
    }

    public void endTurn() {
        isDmging = true;
        for (PiecePanel piecePanel : enemyPanels) {
            EnemyPiece enemyPiece = (EnemyPiece) piecePanel.getPiece();
            if (enemyPiece.canAttactk(gp.getGameState().getChessBoard().getPlayerPiece().getRow(),
                    gp.getGameState().getChessBoard().getPlayerPiece().getCol())) {
                damages.add(new DamagePanel(gp.getGameState().getChessBoard().getPlayerPiece().getRow(),
                        gp.getGameState().getChessBoard().getPlayerPiece().getCol(), enemyPiece.getRow(),
                        enemyPiece.getCol()));
            }
        }
        gp.getGameState().setMoveLeft(0);
        calculatedDmg = false;
        gp.setChosenCard(null);
        gp.getGameState().getDeck().emptyHand();
        gp.getGameState().setTurn(++turn);
    }

    private class TilePanel extends JPanel {
        private int r, c;
        private BoardPanel bp;
        private boolean isEntered = false;
        private boolean isInRange = false;
        private boolean isMovable = false;
        private final MouseListener msln = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isMovable && gp.getGameState().getMoveLeft() > 0) {
                        // bp.playerPanel.setNewRC(r, c);
                        oldR = gp.getGameState().getChessBoard().getPlayerPiece().getRow();
                        gp.getGameState().doMove(gp.getChosenCard(), r, c);
                        gp.getGameState().getDeck().getHand().remove(gp.getChosenCard());
                        gp.getGameState().getDeck().getUsedCard().add(gp.getChosenCard());
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
                        attackPanel = new AttackPanel(r, c, gp.getChosenCard().getHasPower(0),
                                gp.getChosenCard().getHasPower(1));
                        tileEntered = null;
                        gp.setChosenCard(null);
                        if (oldR != 0 && r == 0)
                            gp.getGameState().getDeck().promote();
                        if (gp.getGameState().isCleared()) {
                            gp.getGameState().setGold(gp.getGameState().getGold() + (13 - gp.getGameState().getTurn()));
                            if (gp.getGameState().getTurn() < 14)
                                gp.getGameState().setCurrentHealth(Math.min(gp.getGameState().getCurrentHealth() + 1,
                                        gp.getGameState().getMaxHealth()));
                            gp.setState(1);
                            gp.setTimeToChangeState(System.nanoTime() + 1000000000);
                        } else if (gp.getGameState().getMoveLeft() <= 0) {
                            endTurn();
                        }
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
                    tileEntered = null;
                }
            };

        TilePanel(final BoardPanel bp, final int r, final int c) {
            super(new FlowLayout());
            this.bp = bp;
            this.r = r;
            this.c = c;
            this.setPreferredSize(new Dimension(85, 85));
            validate();
        }

        private void update() {
            if (this.getMouseListeners().length == 0 && !gp.isPausing())
                this.addMouseListener(msln);
            if (this.getMouseListeners().length > 0 && gp.isPausing())
                this.removeMouseListener(msln);
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
            g2.drawString(Integer.toString(gp.getGameState().getChessBoard().calculateDamage(r, c)), x,
                    y + ((int) Game.screenSize.getWidth() / 15 - 15));
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
                int SpeedY = (this.newY - this.oldY) / 20;
                int SpeedX = (this.newX - this.oldX) / 20;
                this.y += SpeedY;
                this.x += SpeedX;
                if (this.oldR < this.newR) {
                    if (this.y >= this.newY) {
                        this.y = this.newY;
                        this.oldY = this.newY;
                        this.oldR = this.newR;
                    }
                } else if (this.oldR > this.newR) {
                    if (this.y <= this.newY) {
                        this.y = this.newY;
                        this.oldY = this.newY;
                        this.oldR = this.newR;
                    }
                }
                if (this.oldC < this.newC) {
                    if (this.x >= this.newX) {
                        this.x = this.newX;
                        this.oldX = this.newX;
                        this.oldC = this.newC;
                    }
                } else if (this.oldC > this.newC) {
                    if (this.x <= this.newX) {
                        this.x = this.newX;
                        this.oldX = this.newX;
                        this.oldC = this.newC;
                    }
                }
                if (this.oldX == this.newX && this.oldY == this.newY){
                    Game.sound.playSE("/sound/move.wav");
                    isMoving = null;
                }
                    
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

    private class DeadPanel extends JPanel {
        private int x, y;
        private final int maxY;
        private final MiniPiece piece;
        private boolean isNew = true;
        private float alpha = 0.7f;

        DeadPanel(final MiniPiece piece) {
            super();
            this.piece = piece;
            this.x = (int) Game.screenSize.getWidth() / 3 + 10
                    + piece.getCol() * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.y = (int) Game.screenSize.getHeight() / 12 + 10
                    + piece.getRow() * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.maxY = this.y - 100;
        }

        private void update() {
            if (this.isNew) {
                Game.sound.playSE("/sound/kill.wav");
                isNew = false;
            }
            if (this.y > this.maxY) {
                this.y -= 5;
                alpha -= 0.01;
            } else {
                deadEnemies.remove(this);
            }
        }

        private void draw(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(Game.imageList.getEnemyImage(piece.toString()), this.x, this.y,
                    (int) Game.screenSize.getWidth() / 15 - 4,
                    (int) Game.screenSize.getWidth() / 15 - 4, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    private class AttackPanel extends JPanel {
        private final int row, col;
        private final boolean cross, diagonal;
        private int size;
        private final int maxSize = (int) Game.screenSize.getWidth() / 5;
        private final int minSize = (int) Game.screenSize.getWidth() / 25;
        private final int spd = (maxSize - minSize) / 12;
        private final int x, y;

        AttackPanel(final int row, final int col, final boolean cross, final boolean diagonal) {
            super();
            this.row = row;
            this.col = col;
            this.cross = cross;
            this.diagonal = diagonal;
            this.size = minSize;
            this.x = (int) Game.screenSize.getWidth() / 3 + 55
                    + col * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.y = (int) Game.screenSize.getHeight() / 12 + 55
                    + row * ((int) Game.screenSize.getWidth() / 15 - 4);
        }

        private void update() {
            if (size < maxSize) {
                size += spd;
            } else {
                attackPanel = null;
            }
        }

        private void draw(Graphics2D g2) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            if (this.cross)
                g2.drawImage(Game.imageList.getPowerImage(0), this.x - size / 2, this.y - size / 2, size, size, null);
            if (this.diagonal)
                g2.drawImage(Game.imageList.getPowerImage(1), this.x - size / 2, this.y - size / 2, size, size, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    private class DamagePanel extends JPanel {
        private final int desX, desY;
        private int x, y;
        private final int spdX, spdY;

        DamagePanel(final int desR, final int desC, final int stR, final int stC) {
            super();
            this.desX = (int) Game.screenSize.getWidth() / 3 + 45
                    + desC * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.desY = (int) Game.screenSize.getHeight() / 12 + 45
                    + desR * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.x = (int) Game.screenSize.getWidth() / 3 + 45
                    + stC * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.y = (int) Game.screenSize.getHeight() / 12 + 45
                    + stR * ((int) Game.screenSize.getWidth() / 15 - 4);
            this.spdX = (desX - x) / 20;
            this.spdY = (desY - y) / 20;
        }

        private void update() {
            if ((this.desX - this.x) * this.spdX <= 0 && (this.desY - this.y) * this.spdY <= 0) {
                Game.sound.playSE("/sound/dmg.wav");
                damages.remove(this);

            } else {
                this.x += this.spdX;
                this.y += this.spdY;
            }
        }

        private void draw(Graphics2D g2) {
            g2.drawImage(Game.imageList.getDmgImage(), this.x, this.y, 15, 15, null);
        }
    }
}