package com.chess.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTrans;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Table {
    private static final Dimension FRAME_DIMENSION = new Dimension(700, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);

    private static final Color lightTileColor = new Color(235, 236, 208);
    private static final Color darkTileColor = new Color(119, 149, 86);
    private static final Color lightHighlight = new Color(240, 217, 181);
    private static final Color darkHighlight = new Color(181, 136, 99);

    public static final String PIECE_ICON_PATH = "art/pieces/";

    private Board chessBoard;
    private JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private BoardPanel boardPanel;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece movedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    private final MoveLog moveLog;

    public Table() {
        this.chessBoard = Board.createStandardBoard();
        this.gameFrame = new JFrame("Chess game");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar menubar = setUpMenuBar();
        this.gameFrame.setJMenuBar(menubar);
        this.gameFrame.setSize(FRAME_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.moveLog = new MoveLog();

    }

    private JMenuBar setUpMenuBar() {
        final JMenuBar menubar = new JMenuBar();
        menubar.add(createFileMenu());
        menubar.add(createViewMenu());
        return menubar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGM = new JMenuItem("Load PGM File");
        openPGM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGM File!");
            }
        });
        fileMenu.add(openPGM);

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

    private JMenu createViewMenu() {
        final JMenu viewMenu = new JMenu("View");

        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.oppositeDirection();
                boardPanel.drawBoard(chessBoard);
            }
        });
        viewMenu.add(flipBoardMenuItem);

        viewMenu.addSeparator();

        final JCheckBoxMenuItem highlightLegalMovesCheckbox = new JCheckBoxMenuItem("Highlight legal moves", false);

        highlightLegalMovesCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = highlightLegalMovesCheckbox.isSelected();
            }
        });
        viewMenu.add(highlightLegalMovesCheckbox);
        return viewMenu;
    }

    public enum BoardDirection {
        NORMAL {

            @Override
            List<TilePanel> flip(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection oppositeDirection() {
                return FLIPPED;
            }

        },
        FLIPPED {

            @Override
            List<TilePanel> flip(List<TilePanel> boardTiles) {
                Collections.reverse(boardTiles);
                return boardTiles;
            }

            @Override
            BoardDirection oppositeDirection() {
                return NORMAL;
            }

        };

        abstract List<TilePanel> flip(final List<TilePanel> boardTiles);

        abstract BoardDirection oppositeDirection();
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    final TilePanel tilePanel = new TilePanel(this, r, c);
                    this.boardTiles.add(tilePanel);
                    add(tilePanel);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(Board board) {
            removeAll();
            for (TilePanel tilePanel : boardDirection.flip(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        public boolean removeMove(Move move) {
            return this.moves.remove(move);
        }
    }

    public class TilePanel extends JPanel {
        private final int row, col;

        TilePanel(final BoardPanel boardPanel, final int row, final int col) {
            super(new GridBagLayout());
            this.row = row;
            this.col = col;
            setPreferredSize(TILE_PANEL_DIMENSION);
            colorTile();
            placePieceIconOnTile(chessBoard);

            addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        movedPiece = null;

                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(row, col);
                            if (sourceTile.isOccupied()) {
                                movedPiece = sourceTile.getPiece();
                                if (movedPiece.getPieceParty() != chessBoard.getCurrentPlayer().getParty()) {
                                    sourceTile = null;
                                    movedPiece = null;
                                } else {
                                    for (Move move2 : movedPiece.legalMoves(chessBoard)) {
                                        System.out.println(move2.toString());
                                    }
                                }
                            } else {

                                sourceTile = null;
                            }

                        } else {
                            if (row != sourceTile.getRow() || col != sourceTile.getCol()) {
                                destinationTile = chessBoard.getTile(row, col);
                                final Move move = Move.MoveFactory.findMove(chessBoard,
                                        sourceTile.getCor(),
                                        destinationTile.getCor());
                                // System.out.println(move.toString());
                                final MoveTrans transition = chessBoard.getCurrentPlayer().moveTrans(move);
                                if (transition.getMoveStatus().isDone()) {
                                    // System.out.println(sourceTile.getCor() + " " + destinationTile.getCor());
                                    chessBoard = transition.getBoard();
                                    moveLog.addMove(move);
                                    sourceTile = null;
                                    destinationTile = null;
                                    movedPiece = null;
                                } else {
                                    if (destinationTile.isOccupied()) {
                                        movedPiece = destinationTile.getPiece();
                                        if (movedPiece.getPieceParty() != chessBoard.getCurrentPlayer().getParty()) {
                                            sourceTile = null;
                                            movedPiece = null;
                                            destinationTile = null;
                                        }
                                    } else {
                                        sourceTile = null;
                                        destinationTile = null;
                                        movedPiece = null;
                                    }

                                }
                            }
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            takenPiecesPanel.redo(moveLog);
                            boardPanel.drawBoard(chessBoard);
                        }
                    });
                }

                @Override
                public void mouseExited(final MouseEvent e) {
                }

                @Override
                public void mouseEntered(final MouseEvent e) {
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                }

            });
            validate();
        }

        public void drawTile(Board board) {
            colorTile();
            placePieceIconOnTile(board);
            legalMovesHighlighter(board);
            validate();
            repaint();
        }

        private void placePieceIconOnTile(final Board board) {
            this.removeAll();
            if (board.getTile(this.row, this.col).isOccupied()) {

                try {
                    final BufferedImage bufferedImage = ImageIO.read(new File(PIECE_ICON_PATH
                            + board.getTile(this.row, this.col).getPiece().getPieceParty().toString().substring(0, 1)
                            + board.getTile(this.row, this.col).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(bufferedImage)));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        private void colorTile() {
            setBackground(((this.row + this.col) % 2 == 0) ? lightTileColor : darkTileColor);
        }

        private void legalMovesHighlighter(final Board board) {
            if (highlightLegalMoves) {
                for (Move move : calculateLegalMoves(board)) {
                    if (move.getDestinationRow() == this.row && move.getDestinationCol() == this.col) {
                        if (move.isAttackingMove()) {
                            setBackground(((this.row + this.col) % 2 == 0) ? lightHighlight : darkHighlight);
                        } else {
                            try {
                                BufferedImage icon = ImageIO.read(new File("art/other/green_dot.png"));
                                add(new JLabel(new ImageIcon(icon)));
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                }
            }
        }

        private Collection<Move> calculateLegalMoves(final Board board) {
            if (movedPiece != null) {
                if (!movedPiece.isKing())
                    return movedPiece.legalMoves(board);
                else {
                    Collection<Move> legalMoves = movedPiece.legalMoves(board);
                    legalMoves.addAll(board.getCurrentPlayer().getCastleMoves());
                    return legalMoves;
                }
            }
            return Collections.emptyList();
        }
    }
}
