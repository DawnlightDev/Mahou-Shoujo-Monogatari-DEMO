package entity;

import main.GamePanel;
import main.PartyManager;

import java.awt.*;
import java.util.Random;

public class NPC_Kylie extends Entity {
    public NPC_Kylie(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 4;
        isEnrollable = true;
        isRoaming = false;
        onPath = false;
        spriteNum = 2;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2;

        solidArea = new Rectangle();
        solidArea.x = 28;
        solidArea.y = 30;
        solidArea.width = 16;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getSprite();
        setDialogue();
    }

    private void getSprite() {
        up1 = setup("/NPC_Kylie/KylieUp2", 72, 72);
        up2 = setup("/NPC_Kylie/KylieUp1", 72, 72);
        up3 = setup("/NPC_Kylie/KylieUp3", 72, 72);
        down1 = setup("/NPC_Kylie/KylieFrame2", 72, 72);
        down2 = setup("/NPC_Kylie/KylieFrame1", 72, 72);
        down3 = setup("/NPC_Kylie/KylieFrame3", 72, 72);
        left1 = setup("/NPC_Kylie/KylieLeftWalk2", 72, 72);
        left2 = setup("/NPC_Kylie/KylieLeftWalk1", 72, 72);
        left3 = setup("/NPC_Kylie/KylieLeftWalk3", 72, 72);
        right1 = setup("/NPC_Kylie/KylieRightWalk2", 72, 72);
        right2 = setup("/NPC_Kylie/KylieRightWalk1", 72, 72);
        right3 = setup("/NPC_Kylie/KylieRightWalk3", 72, 72);
    }

    private void setDialogue() {
        dialogues[0][0] = "Hello Jeanne! \nWhat are you up to?";
        dialogues[0][1] = "Oh, you're out to defeat \nthe Winter Witch?";
        dialogues[0][2] = "Well, let me join you then!";
        dialogues[0][3] = "Kylie has joined your party.";
    }
    public void setAction() {

        if (onPath) {
            int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
            int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;

            searchPath(goalCol, goalRow);

            isRoaming = gp.player.isMoving;
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

    public void speak() {
        gp.ui.npc = this;
        facePlayer();
        startDialogue(this, dialogueSet);
        gp.player.addToParty(gp.Kylie);
        onPath = true;
        isRoaming = true;

        dialogueSet++;

        if(dialogues[dialogueSet][0] == null) {
            dialogueSet = 0;
        }
    }

}
