package edu.csc413.tankgame.model;

import java.util.*;

/**
 * GameWorld holds all of the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */
public class GameWorld {
    // TODO: Implement. There's a lot of information the GameState will need to store to provide contextual information.
    //       Add whatever instance variables, constructors, and methods are needed.
    private final List<Entity> entities;
    private final List<String>  removedIDs;
    private final Map<String,String>firedShells;
    public GameWorld() {
        // TODO: Implement.
        entities=new ArrayList<>();
        removedIDs=new ArrayList<>();
        firedShells=new HashMap<>();

    }




    /** Returns a list of all entities in the game. */
    public List<Entity> getEntities() {
        return entities;
    }

    /** Adds a new entity to the game. */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /** Returns the Entity with the specified ID. */
    public Entity getEntity(String id) {

        Entity result=null;
        for(Entity entity:this.entities)
        {
            if(entity.getId().equals(id))
            {
                result=entity;
            }
        }
        return result;

    }



    public void addFiredShells(String tankID,String shellId){
        this.firedShells.put(tankID,shellId);
    }

    public boolean containsFiredShells(String tankID,String shellID){
        if(this.firedShells.containsKey(tankID))
            return this.firedShells.get(tankID).equals(shellID);

        return false;
    }


    public void reset(){
        this.entities.clear();
        this.removedIDs.clear();
        this.firedShells.clear();
    }


    public List<String> getRemovedIDs(){
        return this.removedIDs;
    }


    public void resetRemovedIDs(){
        this.removedIDs.clear();
    }


    /** Removes the entity with the specified ID from the game. */
    public void removeEntity(String id) {
        // TODO: Implement.
        int pos=-1;
        boolean found=false;


        for(int i=entities.size()-1;i>=0 && !found ;i--)
        {
            if(entities.get(i).getId().equals(id))
            {
                pos=i;
                found=true;
            }
        }

        if(pos!=-1) {
            String local=entities.get(pos).getId();
            entities.remove(pos);
            removedIDs.add(local);
        }

    }
}

