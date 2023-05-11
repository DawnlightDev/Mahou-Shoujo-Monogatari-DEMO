package object;

import entity.Entity;
import entity.Type;
import main.GamePanel;
import main.GameState;

import java.awt.*;

    public class OBJ_Sign extends Entity {
        GamePanel gp;
        public boolean interactionStarted = false;
        public OBJ_Sign(GamePanel gp) {
            super(gp);
            this.gp = gp;

            type = Type.OBSTACLE;
            name = "sign";
            image = setup("/objects/sign", 64, 64);
            down1 = image;
            collision = true;
            isInteractable = true;

            solidArea = new Rectangle();
            solidArea.x = 20;
            solidArea.y = 30;
            solidArea.width = 32;
            solidArea.height = 32;
            solidAreaDefaultX = solidArea.x;
            solidAreaDefaultY = solidArea.y;

            setDialogue();
        }

        public void setDialogue() {
            dialogues[0][0] = "Jeanne's House ->";

        }

        public void interact() {
            gp.gameState = GameState.DIALOGUE_STATE;
            startDialogue(this, 0);
        }

    }
