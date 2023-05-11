package object;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class OBJ_Key extends Entity {
    public OBJ_Key(GamePanel gp) {
        super(gp);
        name = "key";
        image = setup("/objects/key", 64, 64);
        UIimg = setup("/objects/key", 32, 32);
        down1 = image;
        description = "A " + name + ". It seems to unlock \nsomething...";
        isInteractable = false;

        solidArea = new Rectangle();
        solidArea.x = 20;
        solidArea.y = 30;
        solidArea.width = 32;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
