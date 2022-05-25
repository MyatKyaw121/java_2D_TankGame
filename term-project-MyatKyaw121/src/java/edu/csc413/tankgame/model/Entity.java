package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

/**
 * A general concept for an entity in the Tank Game. This includes everything that can move or be interacted with, such
 * as tanks, shells, walls, power ups, etc.
 */
public abstract class Entity {

    private String id;
    private double x;
    private double y;
    private double angle;
    private int health;
    private int speedFactor;
    private boolean offRadar;

    public Entity(String id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        health=5;
        speedFactor=1;
        offRadar=false;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x){
        this.x=x;
    }

    public void setY(double y){
        this.y=y;
    }

    public double getY() {
        return y;
    }

    public abstract double getXBound();

    public abstract double getYBound();

    public double getAngle() {
        return angle;
    }


    protected void setSpeedFactor(int val){
        this.speedFactor=val;
    }

    public void increaseSpeed(){

    }
    public int getSpeedFactor(){
        return this.speedFactor;
    }

    protected void moveForward(double movementSpeed) {
        this.x += movementSpeed * Math.cos(angle)*this.speedFactor;
        this.y += movementSpeed * Math.sin(angle)*this.speedFactor;
    }

    protected void moveBackward(double movementSpeed) {
        x -= movementSpeed * Math.cos(angle)*this.speedFactor;
        y -= movementSpeed * Math.sin(angle)*this.speedFactor;
    }

    protected void turnLeft(double turnSpeed) {
        angle -= turnSpeed;
    }

    public String toString(){
        return this.getId() + this.getX() + this.getY() + this.getAngle();
    }

    protected void turnRight(double turnSpeed) {
        angle += turnSpeed;
    }

    public int getHealth(){
        return this.health;
    }

    public void setHealth(int health){
        this.health=health;
    }
    /** All entities can move, even if the details of their move logic may vary based on the specific type of Entity. */
    public abstract void move(GameWorld gameworld);
    public abstract void checkBounds(GameWorld gameWorld);


    public boolean getOffRadar(){
        return this.offRadar;
    }

    public void setOffRadar(boolean val){
        this.offRadar=val;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Entity)
        {
            Entity e1=(Entity) obj;
            return this.getId().equals(e1.getId());
        }
        return false;
    }
}

