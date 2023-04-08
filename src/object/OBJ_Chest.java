package object;

import entity.Entity;
import entity.Type;
import main.GamePanel;
import main.GameState;

import java.awt.*;

public class OBJ_Chest extends Entity {
    GamePanel gp;
    public OBJ_Chest(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = Type.OBSTACLE;
        name = "chest";
        image = setup("/objects/Chest1", 64, 64);
        chestImage = setup("/objects/Chest2", 64, 64);
        down1 = image;
        collision = true;
        opened = false;

        solidArea = new Rectangle();
        solidArea.x = 20;
        solidArea.y = 30;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void setLoot(Entity loot) {
        this.loot = loot;
        setDialogue();
    }

    public void setDialogue() {
        dialogues[0][0] = "You found a " + loot.name + "!" + "\nBut you can't carry any more items.";
        dialogues[1][0] = "You found a " + loot.name + "!";
        dialogues[2][0] = "There is nothing inside...";
    }

    public void interact() {
            gp.gameState = GameState.DIALOGUE_STATE;

            if(!opened) {
                if(!gp.player.interactingWithChest) {
                    gp.player.interactingWithChest = true;

                    if (!gp.player.canObtainLoot(loot)) {
                        startDialogue(this, 0);
                    }

                    else {
                        startDialogue(this, 1);
                        gp.player.displayLoot(loot);
                        down1 = chestImage;
                        opened = true;
                        gp.playSE(0);
                    }

                }
            }
            else {
                startDialogue(this, 2);
            }

        // Debugging: print the retrieved dialogue text
        System.out.println("Dialogues: " + dialogues[dialogueSet][dialogueIndex]);
    }

}
