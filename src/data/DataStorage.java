package data;

import entity.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    //PLAYER STATS
    int playerLevel;
    int playerMaxLife;
    int playerLife;
    int playerExp;
    int playerMagicPoints;
    int playerMaxMagicPoints;
    int playerKeys;


    //PLAYER INVENTORIES
    ArrayList<String> inventory;
    ArrayList<String> weaponInventory;
    ArrayList<Integer> itemAmount;
    ArrayList<Entity[][]> currentParty;

    //OBJECTS ON MAP
    String mapObjectName[][];
    int mapObjectWorldX[][];
    int mapObjectWorldY[][];
    String mapObjectLootNames[][];
    boolean mapObjectOpened[][];
}
