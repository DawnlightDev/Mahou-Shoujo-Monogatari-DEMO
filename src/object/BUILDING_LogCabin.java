package object;

import entity.Entity;
import entity.Type;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;


public class BUILDING_LogCabin extends Entity {
    GamePanel gp;

    public BUILDING_LogCabin(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = Type.OBSTACLE;
        name = "log cabin";
        collision = true;

        // Set the solid area to cover the entire sprite
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = gp.tileSize*7;
        solidArea.height = gp.tileSize*3;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Create a rectangle for the doorway
        doorway.x = gp.tileSize*4;  // doorway starts at 5th tile across
        doorway.y = gp.tileSize*2;  // doorway is on 3rd tile down
        doorway.width = gp.tileSize;  // doorway is one tile wide
        doorway.height = gp.tileSize;  // doorway is two tiles tall

        getImage();
    }

    public void getImage() {
        down1 = setup("/buildings/logcabin", gp.tileSize*7, gp.tileSize*4);
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
            g2.drawImage(image, screenX, screenY, gp.tileSize*7, gp.tileSize*4, null);
            // Draw the original solidArea rectangle
            g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

            // Draw the doorway rectangle
            g2.setColor(Color.RED);
            g2.fillRect(screenX + doorway.x, screenY + doorway.y, doorway.width, doorway.height);
        }
    }

}
