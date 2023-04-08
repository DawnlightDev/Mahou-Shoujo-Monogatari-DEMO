package object;

import entity.Entity;
import entity.Type;
import main.GamePanel;
import main.GameState;

import java.awt.*;

public class OBJ_RedPotion extends Entity {
    GamePanel gp;
    int value = 20;
    public OBJ_RedPotion(GamePanel gp) {
        super(gp);
        this.gp = gp;
        name = "red potion";
        type = Type.CONSUMABLE;
        image = setup("/objects/red_potion",64, 64);
        down1 = image;
        description = "A " + name + ". It will heal " + value +  " HP when \nused.";
        stackable = true;

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
        dialogues[0][0] = "Jeanne used the " + name + "\n" + "Jeanne's party healed " + value + "HP.";
    }

    public void use(Entity entity) {
        gp.gameState = GameState.DIALOGUE_STATE;
        startDialogue(this, 0);
        entity.life += value;

        if(gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
    }
}
