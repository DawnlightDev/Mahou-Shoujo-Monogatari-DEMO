package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, optionsPressed, togglePressed;
    public boolean isTransitioning = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLE STATE
        if(gp.gameState == GameState.TITLE_STATE) {
            titleState(code);
        }
        //PLAY STATE
        if (gp.gameState == GameState.PLAY_STATE) {
            playState(code);
        }
        //PAUSE STATE
        else if (gp.gameState == GameState.PAUSE_STATE) {
            pauseState(code);
        }

        //DIALOGUE STATE
        else if(gp.gameState == GameState.DIALOGUE_STATE) {
            dialogueState(code);
        }

        //OPTIONS MENU
        else if(gp.gameState == GameState.OPTIONS_STATE) {
            optionsState(code);
        }

        //BATTLE STATE
        else if(gp.gameState == GameState.BATTLE_STATE) {
            battleState(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;

        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;

        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;

        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;

        }

        /*if(code == KeyEvent.VK_SHIFT) {
            sprintPressed = false;
        }*/

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }

        if(code == KeyEvent.VK_1) {
            togglePressed = false;
        }

    }

    public void titleState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
            if(gp.ui.commandNum == 0) {
                gp.fadeIn();
                gp.gameState = GameState.PLAY_STATE;
                gp.fadeOut();
            }
            if(gp.ui.commandNum == 1) {
                //Add later
                gp.saveLoad.loadGame();
                gp.gameState = GameState.PLAY_STATE;
            }
            if(gp.ui.commandNum == 2) {
                System.exit(0);
            }
        }

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2;
            }
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }
    }

    public void playState(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;

        }

        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;

        }

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;

        }

        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;

        }

            /*if (code == KeyEvent.VK_SHIFT) {
                sprintPressed = true;
            }*/

        if (code == KeyEvent.VK_P) {
            if (gp.gameState == GameState.PLAY_STATE) {
                gp.playSE(3);
                gp.gameState = GameState.PAUSE_STATE;
            }
        }

        if(code == KeyEvent.VK_ESCAPE) {
            if(gp.gameState == GameState.PLAY_STATE) {
                gp.gameState = GameState.OPTIONS_STATE;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    public void pauseState (int code) {
        if(code == KeyEvent.VK_P) {
            gp.gameState = GameState.PLAY_STATE;
            gp.playSE(6);
        }

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if(gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSE(1);
            }
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_RIGHT) {
            if(gp.ui.slotCol != 8) {
                gp.ui.slotCol++;
                gp.playSE(1);
            }
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if(gp.ui.slotRow != 6) {
                gp.ui.slotRow++;
                gp.playSE(1);
            }
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_LEFT) {
            if(gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.playSE(1);
            }
        }

        if(code == KeyEvent.VK_1) {
            togglePressed = true;
            gp.playSE(7);
        }

        if(code == KeyEvent.VK_ENTER) {
            if(gp.player.currentInventory == 0) {
                gp.player.selectItem();
            }

            else if(gp.player.currentInventory == 1) {
                gp.player.selectWeapon();
            }
        }
    }

    public void dialogueState(int code) {
        //DIALOGUE STATE
        if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
        }
    }

    public void optionsState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameState.PLAY_STATE;
        }

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState) {
            case 0 -> maxCommandNum = 5;
            case 2 -> maxCommandNum = 1;
        }

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }

        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if(gp.ui.subState == 0) {
                /*if(gp.ui.commandNum == 0 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(1);
                }*/

                if(gp.ui.commandNum == 1 && gp.sound.volumeScale >= 0) {
                    gp.sound.volumeScale--;
                    gp.playSE(1);
                }

                if(gp.sound.volumeScale < 0) {
                    gp.sound.volumeScale = 0;
                }

                else if(gp.sound.volumeScale > 6) {
                    gp.sound.volumeScale = 6;
                }
            }
        }

        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if(gp.ui.subState == 0) {
                /*if(gp.ui.commandNum == 0 && gp.music.volumeScale < 0) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(1);
                }*/

                if(gp.ui.commandNum == 1 && gp.sound.volumeScale >= 0) {
                    gp.sound.volumeScale++;
                    gp.playSE(1);
                }

                if(gp.sound.volumeScale < 0) {
                    gp.sound.volumeScale = 0;
                }

                else if(gp.sound.volumeScale > 6) {
                    gp.sound.volumeScale = 6;
                }
            }
        }
    }

    public void battleState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;

            if(enterPressed && gp.ui.selectingAction) {
                switch(gp.ui.commandNum) {
                    case 0 -> {
                        gp.ui.subState = 2;
                        gp.ui.selectingAction = false;
                        gp.ui.attackState();
                    }

                    case 1 -> {
                        gp.ui.useItem();
                    }

                    case 2 -> {
                        enterPressed = false;
                        gp.ui.selectingAction = false;
                        gp.ui.currentDialogue = "Got away from " + gp.ui.monster.name + "!";

                        if(enterPressed) {
                            System.out.print(enterPressed);
                            gp.ui.runFromMonster();
                            enterPressed = false;
                        }
                    }
                }
            }

            if(enterPressed && !gp.ui.selectingAction) {
                switch(gp.ui.subState) {
                    case 2 -> {
                        gp.ui.subState = 1;
                    }

                }
            }

            if(enterPressed && gp.ui.selectingAction && gp.ui.subState == 2) {
                switch(gp.ui.commandNum) {
                    case 0 -> {
                        gp.ui.currentDialogue = "Jeanne casts " + gp.player.currentWeapon.attacks[0] + "!";
                        enterPressed = false;
                        if(enterPressed) {
                            gp.ui.currentDialogue = gp.ui.monster.name + " took " + gp.player.getAttack() + " damage!";
                        }
                    }

                    case 1 -> {
                        gp.ui.currentDialogue = "Jeanne casts " + gp.player.currentWeapon.attacks[1] + "!";
                    }

                    case 2 -> {
                        gp.ui.currentDialogue = "Jeanne casts " + gp.player.currentWeapon.attacks[2] + "!";
                    }

                    case 3-> {
                        gp.ui.currentDialogue = "Jeanne casts " + gp.player.currentWeapon.attacks[3] + "!";
                    }
                }
            }
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState) {
            case 1 -> maxCommandNum = 2;
            case 2 -> maxCommandNum = 3;
        }

        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }

        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
    }
}
