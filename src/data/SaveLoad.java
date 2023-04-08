package data;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.io.*;

public class SaveLoad {
    GamePanel gp;

    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }

    public Entity getObject(String itemName) {
        Entity obj = null;

        switch(itemName) {
            case "red potion":
                obj = new OBJ_RedPotion(gp);
            case "chest":
                obj = new OBJ_Chest(gp);
            case "sign":
                obj = new OBJ_Sign(gp);
            case "log cabin":
                obj = new BUILDING_LogCabin(gp);
            case "key":
                obj = new OBJ_Key(gp);
        }

        return obj;
    }

    public Entity getWeapon(String name) {
        Entity obj = null;
        switch(name) {
            case "Magician's Book":
                obj = new WEAPON_MagicBook(gp);
        }

        return obj;
    }
    public void saveGame() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
            DataStorage ds = new DataStorage();

            ds.playerLevel = gp.player.level;
            ds.playerMaxLife = gp.player.maxLife;
            ds.playerLife = gp.player.life;
            ds.playerExp = gp.player.exp;
            ds.playerMagicPoints = gp.player.magicPoints;
            ds.playerMaxMagicPoints = gp.player.maxMagicPoints;
            ds.playerKeys = gp.player.hasKey;

            //PLAYER INVENTORIES
            for(int i = 0; i < gp.player.inventory.size(); i++) {
                ds.inventory.add(gp.player.inventory.get(i).name);
                ds.itemAmount.add(gp.player.inventory.get(i).amount);
            }

            for(int i = 0; i < gp.player.weaponInventory.size(); i++) {
                ds.weaponInventory.add(gp.player.weaponInventory.get(i).name);
            }

            for(int i = 0; i < gp.player.currentParty.size(); i++) {
                ds.currentParty.add(gp.player.currentParty.get(i));
            }

            //OBJECTS ON MAP
           /* ds.mapObjectName = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[1].length];
            ds.mapObjectLootNames = new String[gp.maxMap][gp.obj[1].length];
            ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[1].length];

            for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
                for(int i = 0; i < gp.obj[1].length; i++) {
                    if(gp.obj[mapNum][i] == null) {
                        ds.mapObjectName[mapNum][i] = " ";
                    }

                    else {
                        ds.mapObjectName[mapNum][i] = gp.obj[mapNum][i].name;
                    }
                }
            }*/

            //Write the DataStorage object
            oos.writeObject(ds);
        }

        catch (Exception e) {
            System.out.print(e);
        }
    }

    public void loadGame() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            //Read the DataStorage object
            DataStorage ds = (DataStorage)ois.readObject();

            gp.player.level = ds.playerLevel;
            gp.player.life = ds.playerLife;
            gp.player.maxLife = ds.playerMaxLife;
            gp.player.magicPoints = ds.playerMagicPoints;
            gp.player.maxMagicPoints = ds.playerMaxMagicPoints;
            gp.player.exp = ds.playerExp;
            gp.player.hasKey = ds.playerKeys;

            //PLAYER INVENTORIES
            gp.player.inventory.clear();
            gp.player.weaponInventory.clear();

            for(int i = 0; i < ds.inventory.size(); i++) {
                gp.player.inventory.add(getObject(ds.inventory.get(i)));
                gp.player.inventory.get(i).amount = ds.itemAmount.get(i);
            }

            for(int i = 0; i < ds.weaponInventory.size(); i++) {
                gp.player.weaponInventory.add(getObject(ds.weaponInventory.get(i)));
            }


        }

        catch (Exception e) {
            System.out.print(e);
        }

    }
}
