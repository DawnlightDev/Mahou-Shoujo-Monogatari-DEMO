package tiles_interactive;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class InteractiveTile extends Entity {
    private final int col;
    private final int row;
    GamePanel gp;
    public boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
        this.col = col;
        this.row = row;
    }

    public void update() {
        //Animate the Interactive Tile (if necessary)
        spriteCounter++;
        if(spriteCounter > 60) {
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

    public BufferedImage setup(String ImagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath + ".png"));
            image = uTool.scaleImage(image, 32, 32);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
