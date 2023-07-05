package com.chess.engine.minigame.GUI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageList {
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

    public ImageList() throws IOException {
        playerImage = ImageIO.read(getClass().getResourceAsStream("/pieces/player/BA.png"));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/IF.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/BE.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/ZM.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/SW.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/iSW.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/AR.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/iAR.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/SH.png")));
        enemyImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/enemies/iSH.png")));
        cardImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/WP.png")));
        cardImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/WN.png")));
        cardImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/WB.png")));
        cardImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/WR.png")));
        cardImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/WQ.png")));
        cardImages.add(ImageIO.read(getClass().getResourceAsStream("/pieces/WK.png")));
        powerImages.add(ImageIO.read(getClass().getResourceAsStream("/other/power/+.png")));
        powerImages.add(ImageIO.read(getClass().getResourceAsStream("/other/power/x.png")));
        powerImages.add(ImageIO.read(getClass().getResourceAsStream("/other/power/s.png")));
        powerImages.add(ImageIO.read(getClass().getResourceAsStream("/other/power/c.png")));
        bagImage = ImageIO.read(getClass().getResourceAsStream("/other/bag.png"));
        coinImage = ImageIO.read(getClass().getResourceAsStream("/other/coin.png"));
        crystalImage = ImageIO.read(getClass().getResourceAsStream("/other/crystal.png"));
        gearImage = ImageIO.read(getClass().getResourceAsStream("/other/gear.png"));
        blightImage = ImageIO.read(getClass().getResourceAsStream("/other/blight.png"));
        skipImage = ImageIO.read(getClass().getResourceAsStream("/other/skip.png"));
        heartImage = ImageIO.read(getClass().getResourceAsStream("/other/heart.png"));
        bheartImage = ImageIO.read(getClass().getResourceAsStream("/other/bheart.png"));
        nimbleImage = ImageIO.read(getClass().getResourceAsStream("/other/nimble.png"));
        moveImage = ImageIO.read(getClass().getResourceAsStream("/other/power/c.png"));
        shieldImage = ImageIO.read(getClass().getResourceAsStream("/other/power/s.png"));
        dmgImage = ImageIO.read(getClass().getResourceAsStream("/other/dmg.png"));
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
