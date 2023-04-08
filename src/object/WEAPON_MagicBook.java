package object;

import entity.Entity;
import entity.Type;
import main.GamePanel;

public class WEAPON_MagicBook extends Entity {
    public WEAPON_MagicBook(GamePanel gp) {
        super(gp);
        name = "Magician's Book";
        type = Type.WEAPON;
        image = setup("/objects/magic_book", 64, 64);
        down1 = image;
        description = "Jeanne's " + name + ".\nIt has lots of basic spells";
        attackValue = 2;

    }
}
