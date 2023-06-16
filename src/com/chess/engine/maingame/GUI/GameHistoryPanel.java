package com.chess.engine.maingame.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.chess.engine.maingame.GUI.Table.MoveLog;
import com.chess.engine.maingame.board.Board;
import com.chess.engine.maingame.board.Move;

public class GameHistoryPanel extends JPanel {
    private static final Dimension GAME_HISTORY_PANEL_DIMENSION = new Dimension(100, 40);

    private final DataModel model;
    private final JScrollPane scrollPane;

    GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(GAME_HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void redo(Board board, MoveLog moveLog) {
        int chosenRow = 0;
        this.model.clear();
        for (final Move move : moveLog.getMoves()) {
            final String moveString = move.toString();
            if (move.getMovePiece().getPieceParty().isBlack()) {
                this.model.setValueAt(moveString, chosenRow, 1);
                chosenRow++;
            } else {
                this.model.setValueAt(moveString, chosenRow, 0);
            }
        }
        if (moveLog.getMoves().size() > 0) {
            final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1);
            final String moveString = lastMove.toString();

            if (lastMove.getMovePiece().getPieceParty().isBlack()) {
                this.model.setValueAt(moveString + check(board), chosenRow - 1, 1);
            } else {
                this.model.setValueAt(moveString + check(board), chosenRow, 0);
            }
        }
        final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    private String check(final Board board) {
        if (board.getCurrentPlayer().isCheckmated()) {
            return "#";
        } else if (board.getCurrentPlayer().isChecked()) {
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel {

        private final List<Row> values;
        private static final String[] NAMES = { "White", "Black" };

        DataModel() {
            this.values = new ArrayList<>();
        }

        public void clear() {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() {
            if (this.values == null)
                return 0;
            return this.values.size();
        }

        @Override
        public int getColumnCount() {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int col) {
            final Row chosenRow = this.values.get(row);
            if (col == 0)
                return chosenRow.getWhiteMove();
            else
                return chosenRow.getBlackMove();
        }

        @Override
        public void setValueAt(final Object obj, final int row, final int col) {
            final Row chosenRow;
            if (row >= this.values.size()) {
                chosenRow = new Row();
                this.values.add(chosenRow);
            } else {
                chosenRow = this.values.get(row);
            }
            if (col == 0) {
                chosenRow.setWhiteMove((String) obj);
                fireTableRowsInserted(row, row);
            } else {
                chosenRow.setBlackMove((String) obj);
                fireTableCellUpdated(row, col);
            }

        }

        @Override
        public Class<?> getColumnClass(final int col) {
            return Move.class;
        }

        @Override
        public String getColumnName(final int col) {
            return NAMES[col];
        }
    }

    private static class Row {
        private String whiteMove;
        private String blackMove;

        Row() {

        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public void setWhiteMove(String whiteMove) {
            this.whiteMove = whiteMove;
        }

        public String getBlackMove() {
            return blackMove;
        }

        public void setBlackMove(String blackMove) {
            this.blackMove = blackMove;
        }

    }
}
