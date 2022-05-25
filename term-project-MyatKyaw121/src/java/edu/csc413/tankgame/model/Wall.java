package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class Wall extends Entity{


    private static int unique_ID=0;

    public Wall( double x, double y){
        super("wall-"+unique_ID,x,y,0);
        unique_ID++;
    }


    @Override
    public double getXBound() {
        return super.getX()+ Constants.WALL_WIDTH;
    }

    @Override
    public double getYBound() {
        return super.getY()+Constants.WALL_HEIGHT;
    }

    @Override
    public void move(GameWorld gameworld) {

    }

    @Override
    public void checkBounds(GameWorld gameWorld) {

    }
}
