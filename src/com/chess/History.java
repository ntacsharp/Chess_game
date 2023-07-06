package com.chess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.chess.engine.minigame.GUI.GamePanel;
import com.chess.engine.minigame.cards.Card;

public class History {
    GamePanel gp;

    public History(GamePanel gp) {
        this.gp = gp;
    }

    public void save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("history.txt"));
            bw.write(String.valueOf(gp.getGameState().getFloor()));
            bw.newLine();
            bw.write(String.valueOf(gp.getGameState().getMaxHealth()));
            bw.newLine();
            bw.write(String.valueOf(gp.getGameState().getCurrentHealth()));
            bw.newLine();
            bw.write(String.valueOf(gp.getGameState().getGold()));
            for (Card card : gp.getGameState().getDeck().getTotalDeck()) {
                bw.newLine();
                bw.write(card.toString());
            }
            bw.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void abandon() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("history.txt"));
            bw.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("history.txt"));
            String s = br.readLine();
            if(s != null){
                gp.getGameState().setTurn(Integer.parseInt(s));
                gp.getGameState().getDeck().emptyTotalDeck();
                s = br.readLine();
                gp.getGameState().setMaxHealth(Integer.parseInt(s));
                s = br.readLine();
                gp.getGameState().setCurrentHealth(Integer.parseInt(s));
                s = br.readLine();
                gp.getGameState().setGold(Integer.parseInt(s));
                for (int i = 0; i < 12; i++) {
                    s = br.readLine();
                    gp.getGameState().getDeck().addToTotalDeck(Card.parseCard(s));
                }
            }
            br.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
