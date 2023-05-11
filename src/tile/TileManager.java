package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;
    boolean drawPath = true;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[100];
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/samplemap.txt", 0);
    }

    public void getTileImage() {
        //Static tiles
        setup(0, "Bridge1", false);
        setup(1, "Flower1", false);
        setup(2, "Flower2", false);
        setup(3, "ForestBorder1", true);
        setup(4, "ForestBorder2", true);
        setup(5, "ForestBorder3", true);
        setup(6, "ForestBorder4", true);
        setup(7, "ForestBorder5", true);
        setup(8, "ForestBorderRight1", true);
        setup(9, "ForestBorderRight2", true);
        setup(10, "ForestEnd1", true);
        setup(11, "ForestEnd2", true);
        setup(12, "GRASS1ALT", false);
        setup(13, "Path1", false);
        setup(14, "Path2", false);
        setup(15, "Path3", false);
        setup(16, "River1", true);
        setup(17, "River2", true);
        setup(18, "River3", true);
        setup(19, "Sign", true);
        setup(20, "Tree1", true);
        setup(21, "Tree2", true);
        setup(22, "Tree3", false);
        setup(23, "Tree4", false);
        setup(24, "TreeTop1", true);
        setup(25, "TreeTop2", true);
        setup(26, "TreeTop3", true);
        setup(27, "TreeTop4", true);
        setup(28, "TreeTop5", true);
        setup(29, "TreeTopCornerBottomLeft", true);
        setup(30, "TreeTopCornerBottomRight", true);
        setup(31, "TreeTopCornerTopRight", true);
    }

    public void setup(int index, String ImagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            //Static Tiles
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" +  ImagePath + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while(col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }

                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            //Stop moving the camera at the map's edge
            if(gp.player.screenX > gp.player.worldX) {
                screenX = worldX;
            }

            if(gp.player.screenY > gp.player.worldY) {
                screenY = worldY;
            }

            int rightOffset = gp.screenWidth - gp.player.screenX;
            if(rightOffset > gp.worldWidth - gp.player.worldX) {
                screenX = gp.screenWidth - (gp.worldWidth - worldX);
            }

            int bottomOffset = gp.screenHeight - gp.player.screenY;
            if(bottomOffset > gp.worldHeight - gp.player.worldY) {
                screenY = gp.screenHeight - (gp.worldHeight - worldY);
            }

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                    && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            else if(gp.player.screenX > gp.player.worldX || gp.player.screenY > gp.player.worldY
                    ||bottomOffset > gp.worldHeight - gp.player.worldY ||rightOffset > gp.worldWidth - gp.player.worldX ) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }

            for (int col = 0; col < gp.maxWorldCol; col++) {
                for (int row = 0; row < gp.maxWorldRow; row++) {
                    int num = mapTileNum[gp.currentMap][col][row];
                }

            }
        }


        //DEBUG FOR PATHFINDING
        if(drawPath == true) {
            g2.setColor(new Color(255, 0, 0, 70));

            for(int i = 0; i < gp.pFinder.pathList.size(); i++) {
                int worldX = gp.pFinder.pathList.get(i).col * gp.tileSize;
                int worldY = gp.pFinder.pathList.get(i).row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
        }
    }
}
