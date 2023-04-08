package environment;

import main.GamePanel;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;
    //Rain rain;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setup() {
        lighting = new Lighting(gp);
        //rain = new Rain(gp);
    }

    public void update() {
        lighting.update();
        //rain.update();
    }

    public void draw(Graphics2D g2) {
        if(lighting != null) {
            lighting.draw(g2);
        }

        /*if(rain != null) {
            rain.draw(g2);
        }*/
    }
}
