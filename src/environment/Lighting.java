package environment;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;
    int dayCounter;
    float filterAlpha = 0f;

    //Day state
    final int day = 0;
    final int dusk = 1;
    final int night = 2;
    final int dawn = 3;
    int dayState = day;

    public Lighting(GamePanel gp) {
        this.gp = gp;
        setLightSource(350);
    }

    public void setLightSource(int circleSize) {
        //Create buffered image
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();

        //Get centre x and y of lighting
        int centreX = gp.player.screenX + (gp.tileSize)/2;
        int centreY = gp.player.screenY + (gp.tileSize)/2;

        //Create a gradient effect within the light circle
        Color color[] = new Color[4];
        float fraction[] = new float[4];

        color[0] = new Color(0f, 0f, 78/255f, 0f);
        color[1] = new Color(0f, 0f, 78/255f, 0.25f);
        color[2] = new Color(0f, 0f, 78/255f, 0.5f);
        color[3] = new Color(0f, 0f, 78/255f, 0.7f);

        fraction[0] = 0f;
        fraction[1] = 0.25f;
        fraction[2] = 0.5f;
        fraction[3] = 0.7f;

        //Create a gradation paint settings for the light circle
        RadialGradientPaint gPaint = new RadialGradientPaint(centreX, centreY, (circleSize/2), fraction, color);

        //Set the gradient
        g2.setPaint(gPaint);

        g2.fillRect(0,0, gp.screenWidth, gp.screenHeight);

        g2.dispose();
    }

    public void update() {
        //Check the state of day
        if(dayState == day) {
            dayCounter++;

            if(dayCounter > 600000) {
                dayState = dusk;
                dayCounter = 0;
            }

        }

        if(dayState == dusk) {
            filterAlpha += 0.001f;

            if(filterAlpha > 1f) {
                filterAlpha = 1f;
                dayState = night;
            }
        }

        if(dayState == night) {
            dayCounter++;

            if(dayCounter > 600000) {
                dayState = dawn;
                dayCounter = 0;
            }
        }

        if(dayState == dawn) {
            filterAlpha -= 0.001f;

            if(filterAlpha < 0f) {
                filterAlpha = 0f;
                dayState = day;
            }
        }
    }

    public void draw(Graphics2D g2) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        g2.drawImage(darknessFilter, 0, 0, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
        /*String situation = "";

        switch(dayState) {
            case day: situation = "day"; break;
            case dusk: situation = "dusk"; break;
            case night: situation = "night"; break;
            case dawn: situation = "dawn"; break;

        }

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString(situation, 800, 700);*/
    }
}
