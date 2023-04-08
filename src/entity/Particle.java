package entity;

import main.GamePanel;

import java.awt.*;

public class Particle extends Entity {
    Color colour;
    int size;
    int xd;
    int yd;
    int x;
    int y;

    public Particle(GamePanel gp, Color colour, int size, int speed, int maxLife, int xd, int yd, int x, int y) {
        super(gp);
        this.colour = colour;
        this.size = size;
        this.speed = speed;
        this.maxLife = maxLife;
        this.xd = xd;
        this.yd = yd;
        this.x = x;
        this.y = y;
    }

    public boolean isDead() {
        return life <= 0;
    }

    @Override
    public void update() {
        super.update();
        x += xd * speed;
        y += yd * speed;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(colour);
        g2.fillOval(x, y, size, size);
    }
}