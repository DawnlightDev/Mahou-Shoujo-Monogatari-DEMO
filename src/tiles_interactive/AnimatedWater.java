package tiles_interactive;
import main.GamePanel;

public class AnimatedWater extends InteractiveTile {
    GamePanel gp;

    public AnimatedWater(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.worldX = gp.tileSize * col;
        this.worldY = gp.tileSize * row;

        direction = "down";
        collisionOn = true;

        down1 = setup("/tiles/River1");
        down2 = setup("/tiles/River2");
        down3 = setup("/tiles/River3");
    }
}
