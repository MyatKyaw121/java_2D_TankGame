package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class ChasingAITank extends SmartAITank{


    public ChasingAITank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }


    @Override
    public void move(GameWorld gameWorld){
        super.move(gameWorld);

        Entity playerTank=gameWorld.getEntity(Constants.PLAYER_TANK_ID);

        if(!playerTank.getOffRadar()){

            double dx=playerTank.getX()-super.getX();
            double dy=playerTank.getY()-super.getY();
            double totalSqaure=(dx*dx)+(dy*dy);
            double gap=Math.sqrt(totalSqaure);

            if(gap >=300)
            {
                super.moveForward(Constants.TANK_MOVEMENT_SPEED);
            }

        }
    }



}
