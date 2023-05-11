package monster;

import entity.Entity;
import entity.Type;
import main.GamePanel;

import java.util.Random;

public class MON_Wolf extends Entity {
    GamePanel gp;
    public MON_Wolf(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "wolf";
        speed = 3;
        maxLife = 10;
        life = maxLife;

        isRoaming = true;
        type = Type.MONSTER;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2;

        solidArea.x = 21;
        solidArea.y = 10;
        solidArea.width = 32;
        solidArea.height = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("/MON_Wolf/wolfframe1", 64, 64);
        up2 = setup("/MON_Wolf/wolfframe2", 64, 64);
        up3 = setup("/MON_Wolf/wolfframe3", 64, 64);
        up4 = setup("/MON_Wolf/wolfframe4", 64, 64);
        down1 = setup("/MON_Wolf/wolfframe1", 64, 64);
        down2 = setup("/MON_Wolf/wolfframe2", 64, 64);
        down3 = setup("/MON_Wolf/wolfframe3", 64, 64);
        down4 = setup("/MON_Wolf/wolfframe4", 64, 64);
        left1 = setup("/MON_Wolf/wolfframe1", 64, 64);
        left2 = setup("/MON_Wolf/wolfframe2", 64, 64);
        left3 = setup("/MON_Wolf/wolfframe3", 64, 64);
        left4 = setup("/MON_Wolf/wolfframe4", 64, 64);
        right1 = setup("/MON_Wolf/wolfframe1", 64, 64);
        right2 = setup("/MON_Wolf/wolfframe2", 64, 64);
        right3 = setup("/MON_Wolf/wolfframe3", 64, 64);
        right4 = setup("/MON_Wolf/wolfframe4", 64, 64);
        portrait = setup("/MON_Wolf/WolfTrioPrototype", gp.tileSize*3, gp.tileSize*3);

    }

/*    @Override
    public void update() {
        if(type == Type.MONSTER) {
            int xDistance = Math.abs(worldX - gp.player.worldX);
            int yDistance = Math.abs(worldY - gp.player.worldY);
            int tileDistance = (xDistance + yDistance)/gp.tileSize;

            if(!onPath && tileDistance < 5) {
                int i = new Random().nextInt(100)+1;
                if(i > 50) {
                    onPath = true;
                }
            }

            if(onPath && tileDistance > 20) {
                onPath = false;
            }
        }
        super.update();
    }*/
    public void setAction() {
        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;

            searchPath(goalCol, goalRow);
        }
        else {
            actionLockCounter++;

            if(actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; //Pick a number from 1-100

                if(i <= 25) {
                    direction = "up";
                }
                else if(i > 25 && i <= 50) {
                    direction = "down";
                }
                else if(i > 50 && i <= 75) {
                    direction = "left";
                }
                else if(i > 75 && i <= 100) {
                    direction = "right";
                }

                actionLockCounter = 0;
            }
        }

    }
}
