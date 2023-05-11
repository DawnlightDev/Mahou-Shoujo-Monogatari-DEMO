package entity;

import main.GamePanel;
import main.UtilityTool;
import main.PartyManager;
import main.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    public int screenX;
    public int screenY;
    public String[] attacks = new String[4];
    GamePanel gp;

    //Sprite Settings
    public int worldX;
    public int worldY;
    public double speed;
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4,itemGet, portrait, UIimg;
    public String direction = "down";
    public int spriteCounter = 0;
    public int standCounter = 0;
    public int spriteNum = 1;
    public BufferedImage image, chestImage;
    public String name;

    //Sprite collision
    public boolean collision = false;
    public Rectangle solidArea = new Rectangle(0,0, 32, 32);
    public Rectangle doorway = new Rectangle(0,0,32,32);
    public boolean hasDoorway = false;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean isInteractable = false;

    //NPC Additional Settings
    public boolean isEnrollable = false;
    public boolean isRoaming = false;
    public int actionLockCounter = 0;
    public boolean isSpeaking = false;

    //Entity Dialogue Settings
    public String[][] dialogues = new String[20][20];
    public int dialogueIndex = 0;
    public int dialogueSet = 0;

    //Entity Type Settings
    public Type type;

    //Entity attributes
    public int maxLife;
    public int life;
    public int level;
    public int attack;
    public int defence;
    public int exp;
    public int nextLevelExp;
    public int magicPoints;
    public int maxMagicPoints;
    public Entity currentWeapon;
    public int maxWalletSize;
    public int currentMoneyAmount;

    //Item attributes
    public String description = "";
    public boolean stackable = false;
    public int amount = 1;
    public int attackValue = 0;

    //Chest Additional Settings
    public Entity loot;
    public boolean opened = false;
    public boolean interactingWithChest;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {

    }

    public void speak() {

    }

    public void interact() {

    }

    public void startDialogue(Entity entity, int setNum) {
        gp.gameState = GameState.DIALOGUE_STATE;
        gp.ui.npc = entity;
        dialogueSet = setNum;

        if(gp.gameState == GameState.DIALOGUE_STATE) {
            isSpeaking = true;
        }
        else {
            isSpeaking = false;
        }

        // Debugging: print the set dialogue set number
        System.out.println("Dialogue set: " + dialogueSet);
    }

    public void facePlayer() {

        switch (gp.player.direction) {
            case "up" -> direction = "down";
            case "down" -> direction = "up";
            case "left" -> direction = "right";
            case "right" -> direction = "left";
        }
    }

    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkPlayer(this);
        gp.cChecker.checkEntity(this, gp.mon);
        gp.cChecker.checkEntity(this, gp.npc);

        //IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(isRoaming) {
            if(!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

        //Animate the Entity sprite
            spriteCounter++;
            if(spriteCounter > 6) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }
                else if (spriteNum == 2) {
                    spriteNum = 3;
                }
                else if (spriteNum == 3) {
                    spriteNum = 4;
                }
                else if (spriteNum == 4) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }

    public BufferedImage setup(String ImagePath, int height, int width) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void setLoot(Entity loot) {

    }

    public void use(Entity entity) {

    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "up" -> {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    if (spriteNum == 3) {
                        image = up3;
                    }
                    if (spriteNum == 4) {
                        image = up2;
                    }
                }
                case "down" -> {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    if (spriteNum == 3) {
                        image = down3;
                    }
                    if (spriteNum == 4) {
                        image = down2;
                    }
                }
                case "left" -> {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    if (spriteNum == 3) {
                        image = left3;
                    }
                    if (spriteNum == 4) {
                        image = left2;
                    }
                }
                case "right" -> {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    if (spriteNum == 3) {
                        image = right3;
                    }
                    if (spriteNum == 4) {
                        image = right2;
                    }
                }
            }
            g2.drawImage(image, screenX, screenY, 72, 72, null);

            // COLLISION DEBUG
            //g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    public static boolean onPath = false;

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNode(startCol, startRow, goalCol, goalRow);

        if (gp.pFinder.search()) {
            //Next worldX and worldY
            int nextX = gp.pFinder.pathList.get(0).col * gp.tileSize;
            int nextY = gp.pFinder.pathList.get(0).row * gp.tileSize;

            //Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            } else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gp.tileSize) {
                direction = "down";
            } else if (enTopY >= nextY && enBottomY < nextY + gp.tileSize) {
                //left or right
                if (enLeftX > nextX) {
                    direction = "left";
                }
                if (enLeftX < nextX) {
                    direction = "right";
                }
            } else if (enTopY > nextY && enLeftX > nextX) {
                //up or left
                direction = "up";
                //checkCollision();

                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY > nextY && enLeftX < nextX) {
                //up or right
                direction = "up";

                if (collisionOn) {
                    direction = "right";
                }
            } else if (enTopY < nextY && enLeftX > nextX) {
                //down or left
                direction = "down";
                //checkCollision();

                if (collisionOn) {
                    direction = "left";
                }
            } else if (enTopY < nextY && enLeftX < nextX) {
                //down or right
                direction = "down";
                //checkCollision();

                if (collisionOn) {
                    direction = "right";
                }
            }

            /*int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

            if (nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
            }*/
        }

    }
}
