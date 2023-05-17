package com.chess.GUI;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.GUI.Table.MoveLog;

public class TakenPiecesPanel extends JPanel {
    private static final EtchedBorder TAKEN_PIECES_PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Dimension TAKEN_PIECES_PANEL_DIMENSION = new Dimension(40, 80);
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private JPanel upperPanel;
    private final JPanel lowerPanel;

    public TakenPiecesPanel() {
        super(new BorderLayout());
        setBackground(new Color(248, 219, 167));
        setBorder(TAKEN_PIECES_PANEL_BORDER);
        this.upperPanel = new JPanel(new GridLayout(8, 1));
        this.lowerPanel = new JPanel(new GridLayout(8, 1));
        this.upperPanel.setBackground(PANEL_COLOR);
        this.lowerPanel.setBackground(PANEL_COLOR);
        this.add(upperPanel, BorderLayout.NORTH);
        this.add(lowerPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_PANEL_DIMENSION);
    }

    public void redo(final MoveLog moveLog) {
        this.upperPanel.removeAll();
        this.lowerPanel.removeAll();

        JPanel tmpPanel = new JPanel(new GridLayout(1, 2));
        int tmp;

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for (Move move : moveLog.getMoves()) {
            if (move.isAttackingMove()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceParty().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else
                    whiteTakenPieces.add(takenPiece);
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece a, Piece b) {
                if (a.getPieceValue() > b.getPieceValue())
                    return 1;
                if (a.getPieceValue() < b.getPieceValue())
                    return -1;
                return 0;
            }
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece a, Piece b) {
                if (a.getPieceValue() > b.getPieceValue())
                    return 1;
                if (a.getPieceValue() < b.getPieceValue())
                    return -1;
                return 0;
            }
        });

        tmp = 0;
        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                final BufferedImage bufferedImage = ImageIO.read(new File(Table.PIECE_ICON_PATH
                        + takenPiece.getPieceParty().toString().substring(0, 1)
                        + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(bufferedImage);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 25, icon.getIconWidth() - 20, Image.SCALE_SMOOTH)));
                if (tmp == 0) {
                    tmpPanel = new TmpPanel();
                    tmpPanel.add(imageLabel);
                } else {
                    tmpPanel.add(imageLabel);
                    this.lowerPanel.add(tmpPanel);
                }
                tmp = 1 - tmp;
                // this.lowerPanel.add(imageLabel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (tmp == 1)
            this.lowerPanel.add(tmpPanel);

        tmp = 0;
        for (final Piece takenPiece : blackTakenPieces) {
            try {
                final BufferedImage bufferedImage = ImageIO.read(new File(Table.PIECE_ICON_PATH
                        + takenPiece.getPieceParty().toString().substring(0, 1)
                        + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(bufferedImage);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 25, icon.getIconWidth() - 20, Image.SCALE_SMOOTH)));
                if (tmp == 0) {
                    tmpPanel = new TmpPanel();
                    tmpPanel.add(imageLabel);
                } else {
                    tmpPanel.add(imageLabel);
                    this.upperPanel.add(tmpPanel);
                }
                tmp = 1 - tmp;
                // this.upperPanel.add(imageLabel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (tmp == 1)
            this.upperPanel.add(tmpPanel);

        validate();
    }

    private static final class TmpPanel extends JPanel {
        public TmpPanel() {
            this.setLayout(new GridLayout(1, 2));
            this.setBackground(PANEL_COLOR);
        }
    }
}
