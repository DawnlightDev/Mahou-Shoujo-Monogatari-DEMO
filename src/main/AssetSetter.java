package main;

import entity.NPC_Kylie;
import monster.MON_Wolf;
import object.*;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {

        this.gp = gp;
    }

    public void setObject() {
        gp.obj[0][0] = new OBJ_Key(gp);
        gp.obj[0][0].worldX = 19 * gp.tileSize;
        gp.obj[0][0].worldY = 20 * gp.tileSize;

        gp.obj[0][1] = new OBJ_Chest(gp);
        gp.obj[0][1].setLoot(new OBJ_RedPotion(gp));
        gp.obj[0][1].worldX = 20 * gp.tileSize;
        gp.obj[0][1].worldY = 20 * gp.tileSize;

        gp.obj[0][2] = new OBJ_Sign(gp);
        gp.obj[0][2].worldX = 22*gp.tileSize;
        gp.obj[0][2].worldY = 13*gp.tileSize;

        gp.obj[0][3] = new OBJ_Chest(gp);
        gp.obj[0][3].setLoot(new OBJ_Key(gp));
        gp.obj[0][3].worldX = 30 * gp.tileSize;
        gp.obj[0][3].worldY = 20 * gp.tileSize;

        gp.obj[0][4] = new OBJ_Tree(gp, 32, 32);
        gp.obj[0][4].worldX = 19 * gp.tileSize;
        gp.obj[0][4].worldY = 22 * gp.tileSize;

        gp.obj[0][5] = new BUILDING_LogCabin(gp);
        gp.obj[0][5].worldX = gp.tileSize * 32;
        gp.obj[0][5].worldY = gp.tileSize * 7;

    }

    public void setNPC() {
        gp.Kylie[0][0] = new NPC_Kylie(gp);
        gp.Kylie[0][0].worldX = gp.tileSize * 21;
        gp.Kylie[0][0].worldY = gp.tileSize * 13;
    }

    public void setMonster() {
        gp.mon[0][0] = new MON_Wolf(gp);
        gp.mon[0][0].worldX = gp.tileSize*27;
        gp.mon[0][0].worldY = gp.tileSize*13;
    }
}
