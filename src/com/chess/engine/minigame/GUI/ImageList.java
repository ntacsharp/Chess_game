package com.chess.engine.minigame.GUI;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageList {
    private static final String OTHER_ICON_PATH = "art\\other\\";
    private static final String POWER_ICON_PATH = "art\\other\\power\\";
    private static final String PIECE_ICON_PATH = "art\\pieces\\";
    private static final String ENEMY_ICON_PATH = "art\\pieces\\enemies\\";
    private static final String PLAYER_ICON_PATH = "art\\pieces\\player\\";

    private static final List<BufferedImage> enemyImages = new ArrayList<>();
    private static final List<BufferedImage> cardImages = new ArrayList<>();
    private static final List<BufferedImage> powerImages = new ArrayList<>();
    private final BufferedImage playerImage;
    private final BufferedImage bagImage;
    private final BufferedImage coinImage;
    private final BufferedImage crystalImage;
    private final BufferedImage gearImage;
    private final BufferedImage blightImage;
    private final BufferedImage skipImage;
    private final BufferedImage heartImage;
    private final BufferedImage bheartImage;
    private final BufferedImage moveImage;
    private final BufferedImage shieldImage;
    private final BufferedImage nimbleImage;
    private final BufferedImage dmgImage;
    private final BufferedImage overImage;

    public ImageList() throws IOException {
        playerImage = ImageIO.read(new File(PLAYER_ICON_PATH + "BA.png"));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "IF.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "BE.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "ZM.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "SW.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "iSW.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "AR.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "iAR.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "SH.png")));
        enemyImages.add(ImageIO.read(new File(ENEMY_ICON_PATH + "iSH.png")));
        cardImages.add(ImageIO.read(new File(PIECE_ICON_PATH + "WP.png")));
        cardImages.add(ImageIO.read(new File(PIECE_ICON_PATH + "WN.png")));
        cardImages.add(ImageIO.read(new File(PIECE_ICON_PATH + "WB.png")));
        cardImages.add(ImageIO.read(new File(PIECE_ICON_PATH + "WR.png")));
        cardImages.add(ImageIO.read(new File(PIECE_ICON_PATH + "WQ.png")));
        cardImages.add(ImageIO.read(new File(PIECE_ICON_PATH + "WK.png")));
        powerImages.add(ImageIO.read(new File(POWER_ICON_PATH + "+.png")));
        powerImages.add(ImageIO.read(new File(POWER_ICON_PATH + "x.png")));
        powerImages.add(ImageIO.read(new File(POWER_ICON_PATH + "s.png")));
        powerImages.add(ImageIO.read(new File(POWER_ICON_PATH + "c.png")));
        bagImage = ImageIO.read(new File(OTHER_ICON_PATH + "bag.png"));
        coinImage = ImageIO.read(new File(OTHER_ICON_PATH + "coin.png"));
        crystalImage = ImageIO.read(new File(OTHER_ICON_PATH + "crystal.png"));
        gearImage = ImageIO.read(new File(OTHER_ICON_PATH + "gear.png"));
        blightImage = ImageIO.read(new File(OTHER_ICON_PATH + "blight.png"));
        skipImage = ImageIO.read(new File(OTHER_ICON_PATH + "skip.png"));
        heartImage = ImageIO.read(new File(OTHER_ICON_PATH + "heart.png"));
        bheartImage = ImageIO.read(new File(OTHER_ICON_PATH + "bheart.png"));
        nimbleImage = ImageIO.read(new File(OTHER_ICON_PATH + "nimble.png"));
        moveImage = ImageIO.read(new File(POWER_ICON_PATH + "c.png"));
        shieldImage = ImageIO.read(new File(POWER_ICON_PATH + "s.png"));
        dmgImage = ImageIO.read(new File(OTHER_ICON_PATH + "dmg.png"));
        overImage = ImageIO.read(new File(OTHER_ICON_PATH + "over.png"));
    }

    public BufferedImage getEnemyImage(final String name) {
        switch (name) {
            case ("IF"):
                return enemyImages.get(0);
            case ("BE"):
                return enemyImages.get(1);
            case ("ZM"):
                return enemyImages.get(2);
            case ("SW"):
                return enemyImages.get(3);
            case ("iSW"):
                return enemyImages.get(4);
            case ("AR"):
                return enemyImages.get(5);
            case ("iAR"):
                return enemyImages.get(6);
            case ("SH"):
                return enemyImages.get(7);
            case ("iSH"):
                return enemyImages.get(8);
            default:
                return null;
        }
    }

    public BufferedImage getCardImage(final String name) {
        switch (name) {
            case ("P"):
                return cardImages.get(0);
            case ("N"):
                return cardImages.get(1);
            case ("B"):
                return cardImages.get(2);
            case ("R"):
                return cardImages.get(3);
            case ("Q"):
                return cardImages.get(4);
            case ("K"):
                return cardImages.get(5);
            default:
                return null;
        }
    }

    public BufferedImage getPowerImage(final int i) {
        return powerImages.get(i);
    }

    public BufferedImage getShieldImage() {
        return shieldImage;
    }

    public BufferedImage getNimbleImage() {
        return nimbleImage;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }

    public BufferedImage getDmgImage() {
        return dmgImage;
    }

    public BufferedImage getBagImage() {
        return bagImage;
    }

    public BufferedImage getOverImage() {
        return overImage;
    }

    public BufferedImage getCoinImage() {
        return coinImage;
    }

    public BufferedImage getCrystalImage() {
        return crystalImage;
    }

    public BufferedImage getGearImage() {
        return gearImage;
    }

    public BufferedImage getBlightImage() {
        return blightImage;
    }

    public BufferedImage getSkipImage() {
        return skipImage;
    }

    public BufferedImage getHeartImage() {
        return heartImage;
    }

    public BufferedImage getBheartImage() {
        return bheartImage;
    }

    public BufferedImage getMoveImage() {
        return moveImage;
    }
}
