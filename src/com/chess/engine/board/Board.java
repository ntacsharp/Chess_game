package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.chess.engine.Party;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;

public class Board {
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePiece;
    private final Collection<Piece> blackPiece;

    private Board (Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePiece = findActivePiece(this.gameBoard, Party.WHITE);
        this.blackPiece = findActivePiece(this.gameBoard, Party.BLACK);
    }

    private static Collection<Piece> findActivePiece(final List<Tile> gameBoard, final Party party) {
        final List<Piece> activePieces = new ArrayList<>();
        for(Tile tile: gameBoard){
            if(tile.isOccupied()){
                Piece piece = tile.getPiece();
                if(piece.getPieceParty() == party) activePieces.add(piece);
            }
        }

        return activePieces;
    }

    public Tile getTile(final int row, final int col){
        return gameBoard.get(row * BoardUtils.NUM_TILE_PER_ROW + col);
    }
    
    private static List<Tile> createGameBoard(final Builder builder){
        List<Tile> tiles = new ArrayList<>();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                tiles.add(Tile.createTile(r, c, builder.boardConfig.get(r * 8 + c)));
            }
        }
        return tiles;
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();

        //WHITE
        builder.setPiece(new Pawn(6, 0, Party.WHITE));
        builder.setPiece(new Pawn(6, 1, Party.WHITE));
        builder.setPiece(new Pawn(6, 2, Party.WHITE));
        builder.setPiece(new Pawn(6, 3, Party.WHITE));
        builder.setPiece(new Pawn(6, 4, Party.WHITE));
        builder.setPiece(new Pawn(6, 5, Party.WHITE));
        builder.setPiece(new Pawn(6, 6, Party.WHITE));
        builder.setPiece(new Pawn(6, 7, Party.WHITE));
        builder.setPiece(new Rook(7, 0, Party.WHITE));
        builder.setPiece(new Knight(7, 1, Party.WHITE));
        builder.setPiece(new Bishop(7, 2, Party.WHITE));
        builder.setPiece(new Queen(7, 3, Party.WHITE));
        builder.setPiece(new King(6, 4, Party.WHITE));
        builder.setPiece(new Bishop(6, 5, Party.WHITE));
        builder.setPiece(new Knight(6, 6, Party.WHITE));
        builder.setPiece(new Rook(6, 7, Party.WHITE));
        //BLACK
        builder.setPiece(new Pawn(1, 0, Party.BLACK));
        builder.setPiece(new Pawn(1, 1, Party.BLACK));
        builder.setPiece(new Pawn(1, 2, Party.BLACK));
        builder.setPiece(new Pawn(1, 3, Party.BLACK));
        builder.setPiece(new Pawn(1, 4, Party.BLACK));
        builder.setPiece(new Pawn(1, 5, Party.BLACK));
        builder.setPiece(new Pawn(1, 6, Party.BLACK));
        builder.setPiece(new Pawn(1, 7, Party.BLACK));
        builder.setPiece(new Rook(0, 0, Party.BLACK));
        builder.setPiece(new Knight(0, 1, Party.BLACK));
        builder.setPiece(new Bishop(0, 2, Party.BLACK));
        builder.setPiece(new Queen(0, 3, Party.BLACK));
        builder.setPiece(new King(0, 4, Party.BLACK));
        builder.setPiece(new Bishop(0, 5, Party.BLACK));
        builder.setPiece(new Knight(0, 6, Party.BLACK));
        builder.setPiece(new Rook(0, 7, Party.BLACK));

        builder.setMoveMaker(Party.WHITE);

        return builder.build();
    }


    public static class Builder{
        Map<Integer, Piece> boardConfig;
        Party nextMoveMaker;
        
        public Builder(){
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getCorID(), piece);
            return this;
        }

        public Builder setMoveMaker(final Party nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }


    }
}
