package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.chess.engine.Party;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.WhitePlayer;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Collection<Move> whiteLegalMoves;
    private final Collection<Move> blackLegalMoves;
    private final Pawn enPassantPawn;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = findActivePiece(this.gameBoard, Party.WHITE);
        this.blackPieces = findActivePiece(this.gameBoard, Party.BLACK);

        this.whiteLegalMoves = legalMoves(this.whitePieces);
        this.blackLegalMoves = legalMoves(this.blackPieces);

        this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, blackLegalMoves, whiteLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
        this.enPassantPawn = builder.enPassantPawn;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public WhitePlayer getWhitePlayer() {
        return this.whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return this.blackPlayer;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    public Collection<Move> getAllLegalMove() {
        final Collection<Move> allLegalMoves = Stream
                .concat(this.whiteLegalMoves.stream(), this.blackLegalMoves.stream())
                .collect(Collectors.toList());
        return allLegalMoves;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int r = 0; r < BoardUtils.NUM_TILE_PER_ROW; r++) {
            for (int c = 0; c < BoardUtils.NUM_TILE_PER_COL; c++) {
                final String tileToString = this.gameBoard.get(r * BoardUtils.NUM_TILE_PER_ROW + c).toString();
                builder.append(String.format("%3s", tileToString));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private Collection<Move> legalMoves(final Collection<Piece> pieces) {
        final List<Move> legaMovesList = new ArrayList<>();

        for (final Piece piece : pieces) {
            legaMovesList.addAll(piece.legalMoves(this));
        }

        return null;
    }

    private static Collection<Piece> findActivePiece(final List<Tile> gameBoard, final Party party) {
        final List<Piece> activePieces = new ArrayList<>();
        for (Tile tile : gameBoard) {
            if (tile.isOccupied()) {
                Piece piece = tile.getPiece();
                if (piece.getPieceParty() == party)
                    activePieces.add(piece);
            }
        }

        return activePieces;
    }

    public Tile getTile(final int row, final int col) {
        return gameBoard.get(row * BoardUtils.NUM_TILE_PER_ROW + col);
    }

    private static List<Tile> createGameBoard(final Builder builder) {
        List<Tile> tiles = new ArrayList<>();
        for (int r = 0; r < BoardUtils.NUM_TILE_PER_ROW; r++) {
            for (int c = 0; c < BoardUtils.NUM_TILE_PER_COL; c++) {
                tiles.add(Tile.createTile(r, c, builder.boardConfig.get(r * BoardUtils.NUM_TILE_PER_ROW + c)));
            }
        }
        return tiles;
    }

    public static Board createStandardBoard() {
        final Builder builder = new Builder();

        // WHITE
        builder.setPiece(new Pawn(6, 0, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 1, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 2, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 3, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 4, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 5, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 6, Party.WHITE, true));
        builder.setPiece(new Pawn(6, 7, Party.WHITE, true));
        builder.setPiece(new Rook(7, 0, Party.WHITE, true));
        builder.setPiece(new Knight(7, 1, Party.WHITE, true));
        builder.setPiece(new Bishop(7, 2, Party.WHITE, true));
        builder.setPiece(new Queen(7, 3, Party.WHITE, true));
        builder.setPiece(new King(7, 4, Party.WHITE, true));
        builder.setPiece(new Bishop(7, 5, Party.WHITE, true));
        builder.setPiece(new Knight(7, 6, Party.WHITE, true));
        builder.setPiece(new Rook(7, 7, Party.WHITE, true));
        // BLACK
        builder.setPiece(new Pawn(1, 0, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 1, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 2, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 3, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 4, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 5, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 6, Party.BLACK, true));
        builder.setPiece(new Pawn(1, 7, Party.BLACK, true));
        builder.setPiece(new Rook(0, 0, Party.BLACK, true));
        builder.setPiece(new Knight(0, 1, Party.BLACK, true));
        builder.setPiece(new Bishop(0, 2, Party.BLACK, true));
        builder.setPiece(new Queen(0, 3, Party.BLACK, true));
        builder.setPiece(new King(0, 4, Party.BLACK, true));
        builder.setPiece(new Bishop(0, 5, Party.BLACK, true));
        builder.setPiece(new Knight(0, 6, Party.BLACK, true));
        builder.setPiece(new Rook(0, 7, Party.BLACK, true));

        builder.setMoveMaker(Party.WHITE);

        return builder.build();
    }

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Party nextMoveMaker;
        Pawn enPassantPawn;

        public Builder() {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getCorID(), piece);
            return this;
        }

        public Builder setMoveMaker(final Party nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = movedPawn;
        }

        public Board build() {
            return new Board(this);
        }

    }
}
