package main;

import entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class PartyManager extends Entity {
    Entity[][] npc;
    List<Entity[][]> currentParty = new ArrayList<>();

    public PartyManager(GamePanel gp, Entity[][] npc) {
        super(gp);
        this.npc = npc;
    }

    public void getCurrentParty() {
        for(Entity[][] npc : currentParty) {

        }

    }

    public void addToParty(Entity[][] npc) {
        //getCurrentParty();
        if(isEnrollable && !currentParty.contains(npc)) {
            currentParty.add(npc);
            /*onPath = true;
            isRoaming = true;*/

        }

    }
}
