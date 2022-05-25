package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class SmartAITank extends Tank{

    public SmartAITank(String id, double x, double y, double angle){
        super(id,x,y,angle);
    }
    @Override
    public void move(GameWorld gameworld) {

        Entity playerTank=gameworld.getEntity(Constants.PLAYER_TANK_ID);

        double dx= playerTank.getX()-super.getX();
        double dy= playerTank.getY()-super.getY();
        double angleToPlayer=Math.atan2(dy,dx);

        double angleDifference=super.getAngle()-angleToPlayer;

        angleDifference -= Math.floor(angleDifference / Math.toRadians(360.0) + 0.5)
                        * Math.toRadians(360.0);

        if (angleDifference < -Math.toRadians(3.0)) {
            turnRight(Constants.TANK_TURN_SPEED);
        } else if (angleDifference > Math.toRadians(3.0)) {
            turnLeft(Constants.TANK_TURN_SPEED);
        }


        if(!super.isShellPresent())
        {
            super.fireShell(gameworld);
            super.setShellPresent(true);
            super.setReloadTime(200);
        }
        if(super.getReloadTime()!=0) {
            super.setReloadTime(super.getReloadTime() - 1);
        }else{
            super.setShellPresent(false);
            super.setReloadTime(200);
        }
    }


}
