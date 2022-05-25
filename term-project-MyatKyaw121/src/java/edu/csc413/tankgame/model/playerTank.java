package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;

public class playerTank extends Tank{


    public playerTank(String id, double x, double y, double angle)
    {
        super( id,  x,  y,  angle);
    }


    @Override
    public void move(GameWorld gameworld) {
        KeyboardReader keyboard= KeyboardReader.instance();

        if(keyboard.upPressed())
        {
            super.moveForward(Constants.TANK_MOVEMENT_SPEED);
        }

        if(keyboard.downPressed())
        {
            super.moveBackward(Constants.TANK_MOVEMENT_SPEED);
        }

        if(keyboard.leftPressed())
        {
            super.turnLeft(Constants.TANK_TURN_SPEED);
        }

        if(keyboard.rightPressed())
        {
            super.turnRight(Constants.TANK_TURN_SPEED);
        }
        if(keyboard.spacePressed()){
               if(!super.isShellPresent())
               {
                    super.fireShell(gameworld);
                    super.setShellPresent(true);
                    super.setReloadTime(200);
                }
        }
        if(super.getReloadTime()!=0) {
            super.setReloadTime(super.getReloadTime() - 1);
        }else{
            super.setShellPresent(false);
            super.setReloadTime(200);
        }
    }





}
