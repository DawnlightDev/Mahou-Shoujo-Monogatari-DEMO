package entity;

import main.GamePanel;
import main.KeyHandler;
import monster.MON_Wolf;
import object.OBJ_RedPotion;
import object.WEAPON_MagicBook;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;
    public boolean lootDisplayed = false;
    public int lootIndex = -1;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public ArrayList<Entity> weaponInventory = new ArrayList<>();
    public ArrayList<Entity[][]> currentParty = new ArrayList<>();
    public final int maxInventorySize = 48;
    public int currentInventory = 0; //0 for Items, 1 for Weapons
    public boolean isMoving;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

        spriteNum = 2;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2;

        solidArea = new Rectangle();
        solidArea.x = 28;
        solidArea.y = 30;
        solidArea.width = 16;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerSprite();
        setItems();
    }

    private void setDefaultValues() {
        worldX = gp.tileSize * 19;
        worldY = gp.tileSize * 13;
        speed = 4;
        direction = "down";

        //DEFAULT STATS
        level = 1;
        maxLife = 10;
        life = maxLife;
        exp = 0;
        nextLevelExp = 10;
        maxMagicPoints = 10;
        magicPoints = maxMagicPoints;
        attack = getAttack();
        defence = getDefence();

        currentWeapon = null;
    }

    private int getAttack() {
        if(currentWeapon != null) {
            return attack = (level * currentWeapon.attackValue)/2;
        }

        return attack;
    }

    public int getDefence() {
        return defence = (level * maxLife)/2;
    }

    private void setItems() {
        inventory.add(new OBJ_RedPotion(gp));
        weaponInventory.add(new WEAPON_MagicBook(gp));
    }

    private void getPlayerSprite() {
        up1 = setup("/player/UPWALKF2", 72, 72);
        up2 = setup("/player/UPWALKF1", 72, 72);
        up3 = setup("/player/UPWALKF3", 72, 72);;
        down1 = setup("/player/0Frame2", 72, 72);
        down2 = setup("/player/0Frame1", 72, 72);
        down3 = setup("/player/0Frame3", 72, 72);
        left1 = setup("/player/LEFTWALK2", 72, 72);
        left2 = setup("/player/LEFTWALK1", 72, 72);
        left3 = setup("/player/LEFTWALK3", 72, 72);
        right1 = setup("/player/RIGHTWALK2", 72, 72);
        right2 = setup("/player/RIGHTWALK1", 72, 72);
        right3 = setup("/player/RIGHTWALK3", 72, 72);
        itemGet = setup("/player/treasureGet2", 72, 72);
        portrait = setup("/player/portraitproto", gp.tileSize*3, gp.tileSize*3);

    }

    public void update() {
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if(keyH.upPressed) {
                isMoving = true;
                direction = "up";
            }

            else if(keyH.downPressed) {
                isMoving = true;
                direction = "down";
            }

            else if(keyH.leftPressed) {
                isMoving = true;
                direction = "left";
            }

            else if(keyH.rightPressed) {
                isMoving = true;
                direction = "right";
            }

            else if(keyH.enterPressed) {
                gp.keyH.enterPressed = true;
            }

            //CHECK TILE FOR COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            obtainObject(objIndex);

            //CHECK NPC COLLISION
            if(!NPC_Kylie.onPath) {
                int npcIndex = gp.cChecker.checkEntity(this, gp.Kylie);
                interactNPC(npcIndex);
            }

            //CHECK MONSTER COLLISION
            int monIndex = gp.cChecker.checkEntity(this, gp.mon);

            //CHECK EVENT

            //IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            gp.keyH.enterPressed = false;

            //Animate the Player character sprite
            if(!gp.keyH.enterPressed) {
                spriteCounter++;
                if (interactingWithChest) {
                    direction = "itemGet";
                    spriteCounter = 0;
                }

                else {
                    if (spriteCounter > 6) {
                        if (spriteNum == 1) {
                            spriteNum = 2;
                        } else if (spriteNum == 2) {
                            spriteNum = 3;
                        } else if (spriteNum == 3) {
                            spriteNum = 4;
                        } else if (spriteNum == 4) {
                            spriteNum = 1;
                        }
                        spriteCounter = 0;
                    }

                    else if(!gp.keyH.upPressed && !gp.keyH.downPressed && !gp.keyH.leftPressed && !gp.keyH.rightPressed) {
                        standCounter++;

                        if(standCounter == 20) {
                            spriteNum = 2;
                            standCounter = 0;
                        }
                    }
                }
            }
        }

        else {
            isMoving = false;
        }

    }

    public void obtainObject(int i) {
        if(i != 999) {
            String objectName = gp.obj[gp.currentMap][i].name;

            switch (objectName) {
                case "key" -> {
                    gp.playSE(4);
                    hasKey++;
                    gp.obj[gp.currentMap][i] = null;
                }

                case "red potion" -> {
                    gp.playSE(4);
                    canObtainObject(gp.obj[gp.currentMap][i]);
                    gp.obj[gp.currentMap][i] = null;
                }
            }

            if(gp.obj[gp.currentMap][i] != null) {
                if(gp.obj[gp.currentMap][i].type == Type.OBSTACLE) {
                    if(keyH.enterPressed) {
                        gp.obj[gp.currentMap][i].interact();
                    }
                }
            }

            else {
                if(canObtainObject(gp.obj[gp.currentMap][i])) {
                    inventory.add(gp.obj[gp.currentMap][i]);
                }
            }
        }
    }

    public boolean canObtainObject(Entity loot) {
        boolean canObtain = false;
        if(loot != null) {
            if(loot.stackable) {
                int index = searchItemInInventory(loot.name);

                if(index != 999) {
                    inventory.get(index).amount++;
                    canObtain = true;
                }

                else {
                    if(inventory.size() != maxInventorySize) {
                        inventory.add(loot);
                        canObtain = true;
                    }
                }
            }

            else {
                if(inventory.size() != maxInventorySize) {
                    inventory.add(loot);
                    canObtain = true;
                }
            }
        }

        return canObtain;
    }

    public boolean canObtainLoot(Entity loot) {
        if (Objects.equals(loot.name, "key")) {
            hasKey++;
            return true;
        }

        if(Objects.equals(loot.name, "red potion")) {
            int index = searchItemInInventory("red potion");
            if (index != 999) {
                // red potion already exists in inventory
                inventory.get(index).amount++;
            }

            else {
                // red potion does not exist in inventory
                inventory.add(new OBJ_RedPotion(gp));
            }
            return true;
        }

        else {
            return false;
        }
    }

    private int searchItemInInventory(String itemName) {
        int itemIndex = 999;

        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }

        return itemIndex;
    }

    public void displayLoot(Entity loot) {
        // Set the position of the loot object to be slightly above the player
        loot.worldX = worldX - 16;
        loot.worldY = worldY - 48;

        // Find an empty slot in the obj array to add the loot object to
        for (int i = 0; i < gp.obj[gp.currentMap].length; i++) {
            if (gp.obj[gp.currentMap][i] == null) {
                lootIndex = i;
                break;
            }
        }

        // If an empty slot was found, add the loot object to the obj array
        if (lootIndex != -1) {
            gp.obj[gp.currentMap][lootIndex] = loot;
        }

        lootDisplayed = true;
    }




    private void interactNPC(int i) {
        if(i != 999) {
            if(gp.keyH.enterPressed) {
                gp.Kylie[gp.currentMap][i].speak();
            }
        }
    }

    public void addToParty(Entity[][] npc) {
        //getCurrentParty();
        if(isEnrollable && !currentParty.contains(npc)) {
            currentParty.add(npc);
            System.out.print("Added " + npc + "to your party!");
            System.out.print(currentParty);

        }
    }

    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if(selectedItem.type == Type.CONSUMABLE) {
                selectedItem.use(this);
                if(selectedItem.amount == 1) {
                    inventory.remove(itemIndex);
                }
                else {
                    selectedItem.amount--;
                }
            }
        }
    }

    public void selectWeapon() {
        int itemIndex = gp.ui.getItemIndexOnSlot();

        if(itemIndex < weaponInventory.size()) {
            Entity selectedWeapon = weaponInventory.get(itemIndex);

            if(selectedWeapon.type == Type.WEAPON) {
                currentWeapon = selectedWeapon;
                attack = getAttack();
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int spriteSize = 72;

        switch (direction) {
            case "up" -> {
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                if (spriteNum == 3) {
                    image = up3;
                }
                if (spriteNum == 4) {
                    image = up2;
                }
            }
            case "down" -> {
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down3;
                }
                if (spriteNum == 4) {
                    image = down2;
                }
                if (spriteNum == 5) {
                    image = itemGet;
                }
            }
            case "left" -> {
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                if (spriteNum == 4) {
                    image = left2;
                }
            }
            case "right" -> {
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }
                if (spriteNum == 4) {
                    image = right2;
                }
            }
            case "itemGet" -> {
                image = itemGet;
            }
        }

        int x = screenX;
        int y = screenY;
        if(screenX > worldX) {
            x = worldX;
        }

        if(screenY > worldY) {
            y = worldY;
        }

        int rightOffset = gp.screenWidth - screenX;
        if(rightOffset > gp.worldWidth - worldX) {
            x = gp.screenWidth - (gp.worldWidth - worldX);
        }

        int bottomOffset = gp.screenHeight - screenY;
        if(bottomOffset > gp.worldHeight - worldY) {
            y = gp.screenHeight - (gp.worldHeight - worldY);
        }
        g2.drawImage(image, x, y, spriteSize, spriteSize, null);
        //g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height); // Draw collision box

    }
}
