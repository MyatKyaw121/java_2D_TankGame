package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class PowerUp extends Entity{


    public PowerUp(double x,double y){
        super("PowerUp",x,y,0);
    }


    @Override
    public double getXBound() {
        return super.getX()+ Constants.POWER_WIDTH;
    }

    @Override
    public double getYBound() {
        return  super.getY()+ Constants.POWER_HEIGHT;
    }

    @Override
    public void move(GameWorld gameworld) {

    }

    @Override
    public void checkBounds(GameWorld gameWorld) {

    }
}
