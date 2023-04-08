package environment;

import entity.Particle;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rain {
    private List<Particle> particles = new ArrayList<>();
    private Random random = new Random();
    private GamePanel gp;

    public Rain(GamePanel gp) {
        this.gp = gp;
    }

    public void update() {
        int numParticles = 1000; // change the number of particles as needed
        for (int i = 0; i < numParticles; i++) {
            particles.add(createParticle());
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            particle.update();
            if (particle.isDead()) {
                particles.remove(particle);
                i--;
            }
        }
    }

    private Particle createParticle() {
        Color colour = getParticleColour();
        int size = getParticleSize();
        int speed = getParticleSpeed();
        int maxLife = getParticleMaxLife();
        int xd = 0;
        int yd = 1; // make particles fall downwards
        int x = random.nextInt(gp.getWidth());
        int y = random.nextInt(gp.getHeight());
        return new Particle(gp, colour, size, speed, maxLife, xd, yd, x, y);
    }

    private Color getParticleColour() {
        Color colour = new Color(0, 0, 255);
        return colour;
    }

    private int getParticleSize() {
        int size = 50; //pixel size
        return size;
    }

    private int getParticleSpeed() {
        int speed = 1;
        return speed;
    }

    private int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }

    public void draw(Graphics2D g2) {
        for (Particle particle : particles) {
            particle.draw(g2);
        }
    }
}