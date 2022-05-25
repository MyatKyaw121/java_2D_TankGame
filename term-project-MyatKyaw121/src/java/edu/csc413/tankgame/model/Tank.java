package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

import java.util.HashMap;
import java.util.Map;

/** Entity class representing all tanks in the game. */
public abstract class Tank extends Entity {
    // TODO: Implement. A lot of what's below is relevant to all Entity types, not just Tanks. Move it accordingly to
    //       Entity class.


    private  int reloadTime;
    private boolean shellPresent;




    public Tank(String id, double x, double y, double angle) {
       super(id,x,y,angle);
        this.reloadTime=0;
        shellPresent=false;

    }


    @Override
    public void increaseSpeed(){
        super.setSpeedFactor(2);
    }


    protected boolean isShellPresent(){
        return this.shellPresent;
    }

    protected void setShellPresent(boolean value){
        this.shellPresent=value;
    }

    protected int getReloadTime(){
        return this.reloadTime;
    }


    protected void setReloadTime(int reloadTime){
        this.reloadTime=reloadTime;
    }



  

    // TODO: The methods below are provided so you don't have to do the math for movement. You should call these methods
    //       from the various subclasses of Entity in their implementations of move.




    public  double getXBound(){
       return  super.getX()+Constants.TANK_WIDTH;
    }

    public  double getYBound(){
        return super.getY()+Constants.TANK_HEIGHT;
    }




    protected double getShellX() {
        return getX() + Constants.TANK_WIDTH / 2 + 45.0 * Math.cos(getAngle()) - Constants.SHELL_WIDTH / 2;
    }

    protected double getShellY() {
        return getY() + Constants.TANK_HEIGHT / 2 + 45.0 * Math.sin(getAngle()) - Constants.SHELL_HEIGHT / 2;
    }

    protected double getShellAngle() {
        return getAngle();
    }

    protected void fireShell(GameWorld gameWorld){
        Shell shell=new Shell(getShellX(),getShellY(),getShellAngle());
        gameWorld.addFiredShells(super.getId(), shell.getId());
        gameWorld.addEntity(shell);
    }

    @Override
    public void checkBounds(GameWorld gameWorld) {

        if( super.getX() < Constants.TANK_X_LOWER_BOUND)
        {
            super.setX(Constants.TANK_X_LOWER_BOUND);
        }
        if(super.getY() <Constants.TANK_Y_LOWER_BOUND){
            super.setY(Constants.TANK_Y_LOWER_BOUND);
        }

        if(super.getX()>Constants.TANK_X_UPPER_BOUND)
        {
            super.setX(Constants.TANK_X_UPPER_BOUND);
        }

        if(super.getY()>Constants.TANK_Y_UPPER_BOUND)
        {
            super.setY(Constants.TANK_Y_UPPER_BOUND);
        }

    }

}
