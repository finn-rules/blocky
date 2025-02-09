package com.gamewerks.blocky.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.gamewerks.blocky.engine.PieceKind;

public class Loader { 
    public static boolean[][][] loadRotationData(PieceKind piece) throws IOException {
        boolean[][][] data = new boolean[4][][];
        File file = new File(Constants.DATA_PATH, piece.toString() + ".data");
        Scanner in = new Scanner(file);
        for (int i = 0; i < 4; i++) {
            data[i] = readRotation(in);
            if (in.hasNextLine()) {
                in.nextLine();
            }
        }
        in.close();
        return data;
    }
    
    public static HashMap loadAllRotationData() throws IOException {
        HashMap ret = new HashMap();
        for (int i = 0; i < PieceKind.ALL.length; i++) {
            PieceKind piece = PieceKind.ALL[i];
            ret.put(piece, loadRotationData(piece));
        }
        return ret;
    }
}
