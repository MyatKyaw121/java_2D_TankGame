package edu.csc413.tankgame.model;


import edu.csc413.tankgame.Constants;

public class Shell extends Entity{

private static int unique_ID=0;
    public Shell(double x, double y, double angle){
        super("shell-"+unique_ID,x,y,angle);
        unique_ID++;
    }



    @Override
    public void move(GameWorld gameworld) {
        super.moveForward(Constants.SHELL_MOVEMENT_SPEED);
    }

    @Override
    public void checkBounds(GameWorld gameWorld) {
        if(super.getX()<Constants.SHELL_X_LOWER_BOUND ||
                super.getX()>Constants.SHELL_X_UPPER_BOUND||
                super.getY()<Constants.SHELL_Y_LOWER_BOUND||
                super.getY()>Constants.SHELL_Y_UPPER_BOUND){
            gameWorld.removeEntity(super.getId());

        }

    }

    public  double getXBound(){
        return  super.getX()+Constants.SHELL_WIDTH;
    }

    public  double getYBound(){
        return super.getY()+Constants.SHELL_HEIGHT;
    }




}
