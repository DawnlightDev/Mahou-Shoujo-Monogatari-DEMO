package main;

import entity.Entity;
import object.OBJ_Key;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    CollisionChecker cChecker;
    Graphics2D g2;
    Font font_40;
    Font font_60;
    BufferedImage keyImage, UIImage, inventoryImage, itemTab, weaponTab;
    BufferedImage backgrounds;

    //DIALOGUE SETTINGS
    public boolean messageOn = false;
    public String message = "";
    public String currentDialogue = "";
    private String[][] battleDialogue = new String[10][10];
    private int dialogueSet = 0;
    private int dialogueIndex = 0;
    public boolean selectingAction = false;
    private boolean playerAttacking = false;

    int messageCounter = 0;
    int charIndex = 0;
    String combinedText = "";

    //INVENTORY SETTINGS
    public int slotCol = 0;
    public int slotRow = 0;
    public boolean inventoryToggle = false;

    //ADDITIONAL SETTINGS
    public boolean gameFinished = false;
    public int subState = 0;
    public int commandNum = 0;
    public Entity npc;
    public Entity monster;

    public UI(GamePanel gp) {
        this.gp = gp;

        InputStream in = getClass().getResourceAsStream("/font/OldSchoolAdventures.ttf");

        try {
            font_40 = Font.createFont(Font.TRUETYPE_FONT, in);
            font_60 = new Font("Old School Adventures", Font.PLAIN, 12);
        }
        catch (FontFormatException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        OBJ_Key keyImg = new OBJ_Key(gp);
        keyImage = keyImg.UIimg;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(font_60);
        g2.setColor(Color.white);
        g2.drawImage(keyImage, gp.tileSize/4, 30, null);
        g2.drawString("x " + gp.player.hasKey, 50, 50);

        g2.setFont(font_40);
        g2.setColor(Color.white);

        //TITLE STATE
        if(gp.gameState == GameState.TITLE_STATE) {
            drawTitleScreen();
        }


        if(gp.gameState == GameState.PLAY_STATE) {
            //PLAY STATE STUFF LATER
        }
        if(gp.gameState == GameState.PAUSE_STATE) {
            if (gp.keyH.togglePressed) {
                gp.keyH.togglePressed = false;
                inventoryToggle = !inventoryToggle;
            }

            if (inventoryToggle) {
                gp.player.currentInventory = 1;
                drawWeaponInventory();
            }
            else {
                gp.player.currentInventory = 0;
                drawInventory();
            }
            drawCharacterStatus();

        }
        if(gp.gameState == GameState.DIALOGUE_STATE) {
            if(npc != null) {
                npc.dialogueSet = 0;
                drawDialogueScreen();
            }
        }
        if(gp.gameState == GameState.OPTIONS_STATE) {
            drawOptionsScreen();
        }

        if(gp.gameState == GameState.BATTLE_STATE) {
            drawBattleScreen();
        }
    }

    private void drawTitleScreen() {

        g2.setColor(new Color(87, 0, 49));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        //TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 72f));
        String text = "Mahou Shoujo \nMonogatari";
        int x = gp.tileSize * 4;
        int y = gp.tileSize * 2;

        //SHADOW
        g2.setColor(Color.black);
        for(String line : text.split("\n")) {
            g2.drawString(line, x+5, y+5);
            y += gp.tileSize * 2;
            x += gp.tileSize;
        }

        //MAIN COLOUR
        x = gp.tileSize * 4;
        y = gp.tileSize * 2;
        g2.setColor(Color.white);
        for(String line : text.split("\n")) {
            g2.drawString(line, x, y);
            y += gp.tileSize * 2;
            x += gp.tileSize;
        }

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 36f));
        g2.drawString("Demo Version 1.0", x+(int)(gp.tileSize*0.5), y-(int)(gp.tileSize*0.5));

        //MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 36f));

        text = "NEW GAME";
        x = gp.tileSize * 7;
        y += gp.tileSize * 2;
        if(commandNum == 0) {
            g2.setColor(Color.yellow);
        }
        g2.drawString(text, x, y);
        g2.setColor(Color.white);

        text = "LOAD GAME";
        y += gp.tileSize;
        if(commandNum == 1) {
            g2.setColor(Color.yellow);
        }
        g2.drawString(text, x, y);
        g2.setColor(Color.white);

        text = "EXIT GAME";
        y += gp.tileSize;
        if(commandNum == 2) {
            g2.setColor(Color.yellow);
        }
        g2.drawString(text, x, y);
        g2.setColor(Color.white);
    }

    private void drawCharacterStatus() {
        //FRAME
        final int frameX = (int)(gp.tileSize*1.5);
        final int frameY = gp.tileSize * 5;
        final int frameWidth = gp.tileSize * 3;
        final int frameHeight = gp.tileSize * 4;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12));

        int textX = frameX + 20;
        int textY = frameY + (gp.tileSize/2);
        final int lineHeight = 35;

        //NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("EXP", textX, textY);
        textY += lineHeight;
        g2.drawString("HP", textX, textY);
        textY += lineHeight;
        g2.drawString("MP", textX, textY);
        textY += gp.tileSize;
        g2.drawString("Weapon", textX, textY);

        //VALUES
        int tailX = (frameX + frameWidth) - 30;
        //Reset textY
        textY = frameY + (gp.tileSize/2);
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXForAlignRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.magicPoints + "/" + gp.player.maxMagicPoints);
        textX = getXForAlignRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        if(gp.player.currentWeapon != null) {
            g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-5, null);
        }

        //PLAYER PORTRAIT SLOT
        int portraitFrameX = (int)(gp.tileSize*1.5);
        int portraitFrameY = gp.tileSize;
        int portraitFrameWidth = gp.tileSize * 3;
        int portraitFrameHeight = gp.tileSize * 3;


        //DRAW PLAYER PORTRAIT
        g2.drawImage(gp.player.portrait, portraitFrameX, portraitFrameY, portraitFrameWidth, portraitFrameHeight, null);
    }
    private void drawInventory() {
        //DRAW BACKGROUND
        UIImage = setup("/UI/InventoryScreen");
        g2.drawImage(UIImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

        //DRAW INVENTORY TABS
        itemTab = setup("/UI/ItemTab2");
        weaponTab = setup("/UI/ItemTab");

        int tabX =(gp.tileSize*7)-25;
        int tabY = gp.tileSize/2;
        int tabWidth = gp.tileSize*2;
        int tabHeight = gp.tileSize/2;


        g2.drawImage(itemTab, tabX, tabY, tabWidth, tabHeight, null);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12));

        int textX = tabX+40;
        int textY = tabY+30;
        g2.drawString("Weapons", textX, textY);

        tabX = gp.tileSize*5+10;

        g2.drawImage(weaponTab, tabX, tabY, tabWidth, tabHeight, null);

        textX = tabX+40;
        g2.drawString("Items", textX, textY);
        //FRAME
        int frameX = gp.tileSize * 5;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 10;
        int frameHeight = gp.tileSize * 8;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        inventoryImage = setup("/UI/book2");
        g2.drawImage(inventoryImage, frameX-10, frameY, null);

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;

        //DRAW PLAYER INVENTORY ITEMS
        for(int i = 0; i < gp.player.inventory.size(); i++) {
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, gp.tileSize, gp.tileSize, null);
            slotX += slotSize;

            //ADJUST TO FIT INVENTORY SIZE
            if(i == 8 || i == 17 || i == 25 || i == 33) {
                slotX = slotXstart;
                slotY += slotSize;
            }

            //DISPLAY ITEM AMOUNT IN PLAYER'S INVENTORY
            if(gp.player.inventory.get(i).amount > 1) {
                g2.setFont(g2.getFont().deriveFont(18f));

                int amountX;
                int amountY;

                String s = "" + gp.player.inventory.get(i).amount;
                amountX = getXForAlignRightText(s, slotX - 10);
                amountY = slotY + gp.tileSize;

                //SHADOW
                g2.setColor(new Color(60, 60, 60));
                g2.drawString(s, amountX, amountY);

                //NUMBER
                g2.setColor(Color.white);
                g2.drawString(s, amountX - 2, amountY - 2);

            }
        }

        //CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        //DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*2;

        //DRAW DESCRIPTION TEXT
        textX = dFrameX + 20;
        textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            g2.setColor(Color.white);
            for(String line: gp.player.inventory.get(itemIndex).description.split("\n")) {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    private void drawWeaponInventory(){
        //DRAW BACKGROUND
        UIImage = setup("/UI/InventoryScreen");
        g2.drawImage(UIImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

        //DRAW INVENTORY TABS
        itemTab = setup("/UI/ItemTab");
        weaponTab = setup("/UI/ItemTab2");

        int tabX =(gp.tileSize*5)+10;
        int tabY = gp.tileSize/2;
        int tabWidth = gp.tileSize*2;
        int tabHeight = gp.tileSize/2;
        g2.drawImage(itemTab, tabX, tabY, tabWidth, tabHeight, null);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 12));

        int textX = tabX+40;
        int textY = tabY+30;
        g2.drawString("Items", textX, textY);

        tabX = gp.tileSize*7+10-25;
        g2.drawImage(weaponTab, tabX, tabY, tabWidth, tabHeight, null);

        textX = tabX+30;
        g2.drawString("Weapons", textX, textY);

        //FRAME
        int frameX = gp.tileSize * 5;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 10;
        int frameHeight = gp.tileSize * 8;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        inventoryImage = setup("/UI/book2");
        g2.drawImage(inventoryImage, frameX-10, frameY, null);

        //SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize + 3;

        //DRAW PLAYER INVENTORY ITEMS
        for(int i = 0; i < gp.player.weaponInventory.size(); i++) {
            //EQUIP CURSOR
            if(gp.player.weaponInventory.get(i) == gp.player.currentWeapon) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(gp.player.weaponInventory.get(i).down1, slotX, slotY, gp.tileSize, gp.tileSize, null);
            slotX += slotSize;

            //ADJUST TO FIT INVENTORY SIZE
            if(i == 8 || i == 17 || i == 25 || i == 33) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        //CURSOR
        int cursorX = slotXstart + (slotSize * slotCol);
        int cursorY = slotYstart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        //DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        //DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize*2;

        g2.setColor(Color.white);

        //DRAW DESCRIPTION TEXT
        textX = dFrameX + 20;
        textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.weaponInventory.size()) {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for(String line: gp.player.weaponInventory.get(itemIndex).description.split("\n")) {
                g2.setColor(Color.white);
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot() {
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }

    public void drawDialogueScreen() {
        if(gp.gameState == GameState.BATTLE_STATE) {
            //WINDOW
            int x = gp.tileSize*2;
            double y = gp.tileSize*9.5;
            int width = gp.screenWidth - (gp.tileSize*4);
            int height = gp.tileSize*2;

            drawSubWindow(x, (int) y, width, height);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24));
            g2.setColor(Color.white);
            x += gp.tileSize;
            y += gp.tileSize;

            if (currentDialogue != null) {
                for(String line : currentDialogue.split("\n")) {
                    g2.drawString(line, x, (int) y);
                    y += 40;
                }
            }
        }


        else if(gp.gameState == GameState.DIALOGUE_STATE) {
            //WINDOW
            int x = gp.tileSize*2;
            double y = gp.tileSize*7.5;
            int width = gp.screenWidth - (gp.tileSize*4);
            int height = gp.tileSize*3;

            drawSubWindow(x, (int) y, width, height);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24));
            g2.setColor(Color.white);
            x += gp.tileSize;
            y += gp.tileSize;

            if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {
                char[] characters = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();

                if(charIndex < characters.length) {
                    String s = String.valueOf(characters[charIndex]);
                    combinedText = combinedText + s;
                    currentDialogue = combinedText;
                    charIndex++;
                    gp.playSE(5);
                }

                if(gp.keyH.enterPressed) {
                    charIndex = 0;
                    combinedText = "";
                    if(gp.gameState == GameState.DIALOGUE_STATE) {
                        npc.dialogueIndex++;
                        gp.keyH.enterPressed = false;
                    }
                }
            }

            else {
                npc.dialogueIndex = 0;

                if(gp.gameState == GameState.DIALOGUE_STATE) {
                    if(gp.player.interactingWithChest) {
                        gp.player.interactingWithChest = false;
                        gp.player.direction = "down";
                        gp.player.spriteNum = 2;

                        if (gp.player.lootDisplayed) {
                            // Remove the loot from the obj array
                            gp.obj[gp.currentMap][gp.player.lootIndex] = null;
                            gp.player.lootIndex = -1;
                            gp.player.lootDisplayed = false;
                        }
                    }
                    gp.gameState = GameState.PLAY_STATE;
                }
            }

            if (currentDialogue != null) {
                for(String line : currentDialogue.split("\n")) {
                    g2.drawString(line, x, (int) y);
                    y += 40;
                }
            }

        }


    }

    private void drawOptionsScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(24f));

        //Sub Window
        int frameX = gp.tileSize * 6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0 -> optionsTop(frameX, frameY);
            case 1 -> optionsControls(frameX, frameY);
            case 2 -> optionsExitGameConfirmation(frameX, frameY);
            case 3 -> optionsSaveGameConfirmation(frameX, frameY);
        }

        gp.keyH.enterPressed = false;
    }

    private void optionsTop(int frameX, int frameY) {
        int textX;
        int textY;

        //TITLE
        g2.setColor(Color.white);
        String text = "Options";
        textX = gp.tileSize * 9;
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(18f));

        //Music Volume
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        if(commandNum == 0){
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
        }
        g2.drawString("Music Volume", textX, textY);
        g2.setColor(Color.white);

        //Sound Effects Volume
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        if(commandNum == 1){
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
        }
        g2.drawString("SFX Volume", textX, textY);
        g2.setColor(Color.white);

        //Controls
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        if(commandNum == 2){
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 1;
                commandNum = 0;
            }
        }
        g2.drawString("Controls", textX, textY);
        g2.setColor(Color.white);

        //Save Game
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        if(commandNum == 3) {
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 3;
                commandNum = 0;
            }
        }
        g2.drawString("Save Game", textX, textY);
        g2.setColor(Color.white);

        //Exit Game
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        if(commandNum == 4) {
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 2;
                commandNum = 0;
            }
        }
        g2.drawString("Exit Game", textX, textY);
        g2.setColor(Color.white);

        //Back to Game
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        if(commandNum == 5){
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                gp.gameState = GameState.PLAY_STATE;
                commandNum = 0;
            }
        }
        g2.drawString("Back", textX, textY);
        g2.setColor(Color.white);

        //Music Slider
        textX = frameX + (int)(gp.tileSize * 4.5);
        textY = frameY + gp.tileSize + 35;
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 140, 40);

        //SFX Slider
        textY += gp.tileSize;
        int volumeWidth = 24 * gp.sound.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 40);
        g2.drawRect(textX, textY, 140, 40);
    }

    private void optionsControls(int frameX, int frameY) {
        int textX;
        int textY;

        //TITLE
        g2.setColor(Color.white);
        String text = "Controls - Keyboard";
        textX = gp.tileSize * 7;
        textY = frameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(24f));
        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(18f));
        textX = frameX + (int)(gp.tileSize*0.5);
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY);
        textY += gp.tileSize;

        g2.drawString("Interact", textX, textY);
        textY += gp.tileSize;

        g2.drawString("Open Inventory", textX, textY);
        textY += gp.tileSize;

        g2.drawString("Toggle Inventory", textX, textY);
        textY += gp.tileSize;

        g2.drawString("Options", textX, textY);

        textX = frameX + (int)(gp.tileSize*3.5);
        textY = frameY + gp.tileSize*2;
        g2.drawString("WASD/Arrow Keys", textX, textY);
        textY += gp.tileSize;

        g2.drawString("ENTER", textX, textY);
        textY += gp.tileSize;

        g2.drawString("P", textX + 80, textY);
        textY += gp.tileSize;

        g2.drawString("1", textX+80, textY);
        textY += gp.tileSize;

        g2.drawString("ESCAPE", textX, textY);


        //Back
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        if(commandNum == 0){
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 2;
            }
        }
        g2.drawString("Back", textX, textY);
        g2.setColor(Color.white);
    }

    private void optionsExitGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "Quit the game and \nreturn to the title \nscreen?";

        g2.setColor(Color.white);

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //Yes option
        String text = "Yes";
        textX = gp.tileSize * 9;
        textY += gp.tileSize * 3;
        if(commandNum == 0) {
            //g2.drawString("->", textX-50, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 0;
                gp.gameState = GameState.TITLE_STATE;
            }
        }
        g2.drawString(text, textX, textY);
        g2.setColor(Color.white);

        //No option
        text = "No";
        textX = gp.tileSize * 9;
        textY += gp.tileSize;
        if(commandNum == 1) {
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 3;
            }
        }
        g2.drawString(text, textX + 10, textY);
        g2.setColor(Color.white);
    }

    private void optionsSaveGameConfirmation(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;

        currentDialogue = "Save your current \nadventure?";

        g2.setColor(Color.white);

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //Yes option
        String text = "Yes";
        textX = gp.tileSize * 9;
        textY += gp.tileSize * 3;
        if(commandNum == 0) {
            //g2.drawString("->", textX-50, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 0;
                gp.saveLoad.saveGame();
                commandNum = 5;
            }
        }
        g2.drawString(text, textX, textY);
        g2.setColor(Color.white);

        //No option
        text = "No";
        textX = gp.tileSize * 9;
        textY += gp.tileSize;
        if(commandNum == 1) {
            //g2.drawString("->", textX-40, textY);
            g2.setColor(Color.yellow);
            if(gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 3;
            }
        }
        g2.drawString(text, textX + 10, textY);
        g2.setColor(Color.white);
    }

    public void drawBattleScreen() {
        battleDialogue[0][0] = "Encountered a " + monster.name + "!";
        battleDialogue[1][0] = "What will Jeanne do?";
        /*battleDialogue[2][0] = "You can't attack without a weapon!";
        battleDialogue[3][0] = "Ran away from " + monster.name + "!";*/

        backgrounds = setup("/UI/backgrounds/ForestBackgroundPrototype");
        g2.drawImage(backgrounds, 0, 0, gp.screenWidth, gp.screenHeight, null);
        g2.drawImage(gp.player.down2, gp.tileSize * 2, (int) (gp.screenHeight - gp.tileSize*4), gp.tileSize * 3, gp.tileSize * 3, null);
        g2.drawImage(monster.down1, gp.screenWidth - gp.tileSize*3, (int) (gp.screenHeight - gp.tileSize*4), gp.tileSize*3, gp.tileSize*3, null);
        drawDialogueScreen();

        if(subState != 2) {
            currentDialogue = battleDialogue[dialogueSet][dialogueIndex];
            if (currentDialogue != null) {
                char[] characters = currentDialogue.toCharArray();
                if(charIndex < characters.length) {
                    String s = String.valueOf(characters[charIndex]);
                    combinedText = combinedText + s;
                    currentDialogue = combinedText;
                    charIndex++;
                    gp.playSE(5);
                }
            }


            if(gp.keyH.enterPressed) {
                charIndex = 0;
                combinedText = "";
                gp.keyH.enterPressed = false;
                dialogueSet++;

                if(battleDialogue[dialogueSet][0] == null) {
                    dialogueSet = 1;
                }

                currentDialogue = battleDialogue[dialogueSet][dialogueIndex];
                System.out.println(currentDialogue);
                currentDialogue = "";
                subState = 1;
                System.out.println("Dialogue Set: " + dialogueSet + " Dialogue Index: " + dialogueIndex);
            }
            drawActionsMenu();
            gp.keyH.enterPressed = false;
        }

    }

    public void drawActionsMenu() {
        //DRAW ACTIONS MENU
        if(subState == 1) {
            selectingAction = true;
            int frameX = gp.screenWidth - gp.tileSize*6;
            int frameY = gp.tileSize*8;
            int frameWidth = gp.tileSize*3;
            int frameHeight = (int) (gp.tileSize*2.5);

            drawSubWindow(frameX, frameY, frameWidth, frameHeight);

            int textX = frameX+15;
            int textY = frameY+15;

            g2.setColor(Color.white);

            //Attack option
            String text = "Attack";
            textY += gp.tileSize*0.5;
            if(commandNum == 0) {
                g2.setColor(Color.yellow);
                //selectingAction = false;
                //subState = 2;
            }


            g2.drawString(text, textX, textY);
            g2.setColor(Color.white);

            //USE ITEM option
            text = "Use Item";
            textY += gp.tileSize*0.5;
            if(commandNum == 1) {
                g2.setColor(Color.yellow);
            }
            g2.drawString(text, textX + 10, textY);
            g2.setColor(Color.white);

            //RUN option
            text = "Run";
            textY += gp.tileSize*0.5;
            if(commandNum == 2) {
                g2.setColor(Color.yellow);
            }
            g2.drawString(text, textX + 10, textY);
            g2.setColor(Color.white);
        }
    }


    public void runFromMonster() {
        gp.gameState = GameState.PLAY_STATE;
        selectingAction = false;
        dialogueSet = 0;
        dialogueIndex = 0;
        subState = 0;
        System.out.println("Dialogue reset to \n Dialogue Set: " + dialogueSet + " Dialogue Index: " + dialogueIndex);
    }

    public void useItem() {
    }

    public void attackState() {
        gp.keyH.enterPressed = false;
        if (gp.player.currentWeapon == null) {
            currentDialogue = "You can't attack without a weapon!";
            if (gp.keyH.enterPressed) {
                System.out.println("Enter key pressed!");
                selectingAction = false;
            }
        } else {
            switch(gp.player.currentWeapon.name) {
                case "Magic Book" -> {
                    currentDialogue = "What spell will you use?";
                    selectingAction = true;
                }
            }
        }

        //DRAW DIALOGUE TO SCREEN
        charIndex = 0;
        combinedText = "";
        char[] characters = currentDialogue.toCharArray();
        for(int i = 0; i < characters.length; i++) {
            String s = String.valueOf(characters[charIndex]);
            combinedText = combinedText + s;
            currentDialogue = combinedText;
            //System.out.println(combinedText);
            charIndex++;
            gp.playSE(5);


            //DELAY FOR DRAWING CHARACTERS
            try {
                Thread.sleep(50); // add a delay of 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(currentDialogue);
    }


    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(253, 218, 13);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    private int getXForAlignRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

    private BufferedImage setup(String ImagePath) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath + ".png"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
