package com.gamewerks.blocky.engine;

import com.gamewerks.blocky.util.Constants;
import com.gamewerks.blocky.util.Position;

public class BlockyGame {
    private static final int LOCK_DELAY_LIMIT = 30;
    
    private Board board;
    private Piece activePiece;
    private Direction movement;
    
    private int lockCounter;
    // keep track of piece index
    private int index = 0;
    PieceKind[] shuffled = { PieceKind.I, J, L, O, S, T, Z };
    
    public BlockyGame() {
        board = new Board();
        movement = Direction.NONE;
        lockCounter = 0;
        shuffle(shuffled);
        trySpawnBlock();
    }
    
    private void trySpawnBlock() {
        if (activePiece == null) {
            if(index < 7) {
            activePiece = new Piece(shuffled[index], new Position(0, Constants.BOARD_WIDTH / 2 - 2));
            } else {
                shuffle(shuffled);
                index = 0;
                activePiece = new Piece(shuffled[index], new Position(0, Constants.BOARD_WIDTH / 2 - 2));
            }
            // increment index.
            index++;
            if (board.collides(activePiece)) {
                System.exit(0);
            }
        }
    }
    
    public static void shuffle(PieceKind[] arr) {
       for (int i = arr.length - 1; i >= 0; i--) {
            int randomIndex = (int)(Math.random() * arr.length);
            PieceKind temp = arr[i];
            arr[i] = arr[randomIndex];
            arr[randomIndex] = temp; 
        }
    }
    
    private void processMovement() {
        Position nextPos;
        switch(movement) {
        case NONE:
            nextPos = activePiece.getPosition();
            break;
        case LEFT:
            nextPos = activePiece.getPosition().add(0, -1);
            break;
        case RIGHT:
            nextPos = activePiece.getPosition().add(0, 1);
            break;
        default:
            throw new IllegalStateException("Unrecognized direction: " + movement.name());
        }
        if (!board.collides(activePiece.getLayout(), nextPos)) {
            activePiece.moveTo(nextPos);
        }
    }
    
    private void processGravity() {
        Position nextPos = activePiece.getPosition().add(1, 0);
        if (!board.collides(activePiece.getLayout(), nextPos)) {
            lockCounter = 0;
            activePiece.moveTo(nextPos);
        } else {
            if (lockCounter < LOCK_DELAY_LIMIT) {
                lockCounter += 1;
            } else {
                board.addToWell(activePiece);
                lockCounter = 0;
                System.out.println("Attempted to place block.");
                activePiece = null;
            }
        }
    }
    
    private void processClearedLines() {
        board.deleteRows(board.getCompletedRows());
    }
    
    public void step() {
        trySpawnBlock();
        processMovement();
        processGravity();
        // printWell(board.getWell());
        processClearedLines();
    }
    
    public boolean[][] getWell() {
        return board.getWell();
    }
    
    public void printWell(boolean[][] arr) {
        for(int i = 0; i < 22; i++) {
            for(int j = 0; j < 10; j++) {
                System.out.print(arr[i][j] + " ");
            }
        System.out.println();
        }
    }
    public Piece getActivePiece() { return activePiece; }
    public void setDirection(Direction movement) { this.movement = movement; }
    public void rotatePiece(boolean dir) { activePiece.rotate(dir); }
}

