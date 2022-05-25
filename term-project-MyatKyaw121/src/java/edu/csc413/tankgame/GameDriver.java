package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;

import java.awt.event.ActionEvent;
import java.util.*;

public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    private final List<String> checkedItems;
    private final GameWorld gameworld;
    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        gameworld=new GameWorld();
        checkedItems=new ArrayList<>();
    }



    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }



    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            resetGame();
        };
        new Thread(gameRunner).start();
    }

    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame() {
        // TODO: Implement.

        List<WallInformation> wallInformations=WallInformation.readWalls();
        List<Wall>walls=new ArrayList<>();

        for(WallInformation w:wallInformations){
            walls.add(new Wall(w.getX(),w.getY()));
        }


        for(int i=0;i<walls.size();i++ ){
            gameworld.addEntity(walls.get(i));
            runGameView.addSprite(walls.get(i).getId(),wallInformations.get(i).getImageFile(),walls.get(i).getX(),walls.get(i).getY(),0);
        }


        playerTank pTank=new playerTank(Constants.PLAYER_TANK_ID,
                Constants.PLAYER_TANK_INITIAL_X,
                Constants.PLAYER_TANK_INITIAL_Y,
                Constants.PLAYER_TANK_INITIAL_ANGLE);

        ChasingAITank chasingAI=new ChasingAITank(Constants.AI_TANK_1_ID,
                Constants.AI_TANK_1_INITIAL_X,
                Constants.AI_TANK_1_INITIAL_Y,
                Constants.AI_TANK_1_INITIAL_ANGLE);
        SmartAITank AITank=new SmartAITank(Constants.AI_TANK_2_ID,
                Constants.AI_TANK_2_INITIAL_X,
                Constants.AI_TANK_2_INITIAL_Y,
                Constants.AI_TANK_2_INITIAL_ANGLE
        );


        PowerUp Pu=new PowerUp(Constants.AI_TANK_2_INITIAL_X+200,
                               Constants.AI_TANK_2_INITIAL_Y+200);



        this.gameworld.addEntity(pTank);
        this.gameworld.addEntity(chasingAI);
        this.gameworld.addEntity(AITank);
        this.gameworld.addEntity(Pu);


        runGameView.addSprite(pTank.getId(),runGameView.PLAYER_TANK_IMAGE_FILE,
                pTank.getX(),pTank.getY(),pTank.getAngle());

        runGameView.addSprite(chasingAI.getId(),
                "ai-tank.png",
                chasingAI.getX(),
                chasingAI.getY(),
                chasingAI.getAngle());
        runGameView.addSprite(AITank.getId(),"ai-tank.png",
                AITank.getX(),
                AITank.getY(),
                AITank.getAngle());
        runGameView.addSprite(Pu.getId(), "Power-up.png",
                              Pu.getX(),
                              Pu.getY(),
                              Pu.getAngle());

        runGameView.setTankInfos(1,"Player Tank Health:"+pTank.getHealth());
        runGameView.setTankInfos(2,"AI Tank1 Health:"+chasingAI.getHealth());
        runGameView.setTankInfos(3,"AI Tank2 Health:"+AITank.getHealth());
    }

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */

    private boolean updateGame() {


        KeyboardReader keyboard= KeyboardReader.instance();
        if(!keyboard.escapePressed() && gameworld.getEntity(Constants.PLAYER_TANK_ID) != null
            &&  (gameworld.getEntity(Constants.AI_TANK_1_ID)!=null ||gameworld.getEntity(Constants.AI_TANK_2_ID)!=null))
         {
            ArrayList<Entity> OriginalEntities = new ArrayList<>(gameworld.getEntities());
            for (Entity entity : OriginalEntities) {
                entity.move(gameworld);
            }


            for (int i = OriginalEntities.size(); i < gameworld.getEntities().size(); i++) {
                Entity e = gameworld.getEntities().get(i);
                runGameView.addSprite(e.getId(), RunGameView.SHELL_IMAGE_FILE, e.getX(), e.getY(), e.getAngle());
            }


            for (Entity entity : OriginalEntities) {
                entity.checkBounds(gameworld);
            }


            for (int i = OriginalEntities.size(); i < gameworld.getEntities().size(); i++) {
                Entity e = gameworld.getEntities().get(i);
                e.checkBounds(gameworld);
            }

            for (int i = 0; i < gameworld.getRemovedIDs().size(); i++) {
                runGameView.removeSprite(gameworld.getRemovedIDs().get(i));
            }
            gameworld.resetRemovedIDs();


            this.gameworld.getEntities()
                    .stream()
                    .forEach(e -> runGameView.setSpriteLocationAndAngle(e.getId(),
                            e.getX(),
                            e.getY(),
                            e.getAngle()));


            for (int i = 0; i < gameworld.getEntities().size(); i++) {
                Entity e1 = gameworld.getEntities().get(i);
                for (int j = 0; j < gameworld.getEntities().size(); j++) {
                    Entity e2 = gameworld.getEntities().get(j);
                    if (this.entitiesOverlap(e1, e2) && (!e1.equals(e2))) {
                        String s1=e1.getId()+e2.getId();
                        String s2=e2.getId()+e1.getId();
                        if (!checkedItems.isEmpty()) {
                            if (!(checkedItems.contains(s1) && checkedItems.contains(s2))) {
                                handleCollision(e1, e2);
                                checkedItems.add(s1);
                                checkedItems.add(s2);
                            }
                        } else {

                            handleCollision(e1, e2);
                            checkedItems.add(s1);
                            checkedItems.add(s2);
                        }
                    }
                }
            }

            UpdateGameUI();
            checkedItems.clear();
            return true;
        }



        if(gameworld.getEntity(Constants.PLAYER_TANK_ID)==null)
            System.out.println("Player tank destroyed");
        if(gameworld.getEntity(Constants.AI_TANK_1_ID)==null)
            System.out.println("AI tank 1  destroyed");
        if(gameworld.getEntity(Constants.AI_TANK_2_ID)==null)
            System.out.println("AI  tank 2 destroyed");

        return false;
    }


    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        // TODO: Implement.
        runGameView.reset();
        this.gameworld.reset();
    }




    private void handleCollision(Entity e1,Entity e2){

        double leftVal=e1.getXBound()-e2.getX();
        double rightVal=e2.getXBound()-e1.getX();
        double upVal=e1.getYBound()-e2.getY();
        double downVal=e2.getYBound()-e1.getY();
        double small=rightVal;
        int pos=1;
        boolean vertical=false;
        boolean horizontal=true;




            if(leftVal<small){
                small=leftVal;
                pos=2;
                horizontal=true;
            }

            if(downVal<small){
                small=downVal;
                pos=3;
                vertical=true;
           }

            if(upVal<small){
                small=upVal;
                pos=4;
                vertical=true;
            }

        if(e1 instanceof  Tank && e2 instanceof  Tank){
            double val=small/2;
            if(vertical)
            {
                if(pos==3){
                    e2.setY(e2.getY()-val);
                    e1.setY(e1.getY()+val);
                }else{
                    e2.setY(e2.getY()+val);
                    e1.setY(e1.getY()-val);
                }

            }else if (horizontal){
                if(pos==1){
                    e1.setX(e1.getX()+val);
                    e2.setX(e2.getX()-val);
                }else{
                    e1.setX(e1.getX()-val);
                    e2.setX(e2.getX()+val);
                }

            }

        }else if (e1 instanceof  Tank && e2 instanceof  Wall){

            double val=(small);
            if(vertical)
            {
                if(pos==3){
                    e1.setY(e1.getY()+val);
                }else{
                    e1.setY(e1.getY()-val);
                }
            }else if (horizontal){
                if(pos==1){
                    e1.setX(e1.getX()+val);
                }else{
                    e1.setX(e1.getX()-val);
                }
            }

        }else if ( e1 instanceof Tank && e2 instanceof Shell){

            if(!this.shellChecker(e1.getId(),e2.getId())){

                int health=e1.getHealth();
                if(health==1){
                    this.removeFromGame(e1.getId());
                    runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY
                    ,e1.getX(),e1.getY());
                }
                else {
                    e1.setHealth(e1.getHealth()-1);
                }
                this.removeFromGame(e2.getId());
                runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY
                        ,e2.getX(),e2.getY());

            }

        }else if (e1 instanceof  Wall && e2 instanceof Tank){

            double val=(small);
            if(vertical)
            {
                if(pos==3){
                    e2.setY(e2.getY()-val);
                }else{
                    e2.setY(e2.getY()+val);
                }
            }else if (horizontal){
                if(pos==1){
                    e2.setX(e2.getX()-val);
                }else{
                    e2.setX(e2.getX()+val);
                }
            }


        }else if (e1 instanceof Wall && e2 instanceof Wall){
                    // do nothing
        }else if (e1 instanceof  Wall && e2 instanceof Shell){

            int health=e1.getHealth();
            if(health==1){
                this.removeFromGame(e1.getId());
                runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY
                        ,e1.getX(),e1.getY());
            }
            else {
              e1.setHealth(e1.getHealth()-1);
            }

            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY
                    ,e2.getX(),e2.getY());
            this.removeFromGame(e2.getId());


        }else if (e1 instanceof Shell && e2 instanceof Tank){

            if(!this.shellChecker(e2.getId(),e1.getId())){

                int health=e2.getHealth();
                if(health==1){
                    this.removeFromGame(e2.getId());
                    runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY
                            ,e2.getX(),e2.getY());
                }
                else {
                   e2.setHealth(e2.getHealth()-1);
                }

                this.removeFromGame(e1.getId());
                runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY
                        ,e1.getX(),e1.getY());
            }


        }else if (e1 instanceof Shell && e2 instanceof Wall){

            int health=e2.getHealth();
            if(health==1) {
                this.removeFromGame(e2.getId());
                runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY
                        ,e2.getX(),e2.getY());
            }
            else {
                e2.setHealth(e2.getHealth()-1);
            }


            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION,RunGameView.SHELL_EXPLOSION_FRAME_DELAY
                    ,e1.getX(),e1.getY());
            this.removeFromGame(e1.getId());


        }else if (e1 instanceof Shell && e2 instanceof Shell){

                this.removeFromGame(e1.getId());
                this.removeFromGame(e2.getId());
            runGameView.addAnimation(RunGameView.BIG_EXPLOSION_ANIMATION,RunGameView.BIG_EXPLOSION_FRAME_DELAY
                    ,((e1.getX()+e2.getX())/2),((e1.getY()+e2.getY())/2));


        }else if (e1 instanceof PowerUp && e2 instanceof  Tank){

            this.removeFromGame(e1.getId());
            e2.increaseSpeed();
            e2.setOffRadar(true);
        }else if (e1 instanceof Tank && e2 instanceof PowerUp){

            this.removeFromGame(e2.getId());
             e1.increaseSpeed();
             e1.setOffRadar(true);
        }

    }



    private boolean shellChecker(String tankID,String shellId){
        return this.gameworld.containsFiredShells(tankID,shellId);
    }


    private void removeFromGame(String id){
        runGameView.removeSprite(id);
        gameworld.removeEntity(id);
    }







    private void UpdateGameUI(){
        if(gameworld.getEntity(Constants.PLAYER_TANK_ID)==null){
            runGameView.setTankInfos(1,"Player Tank Destroyed");
        }else{
            String local="Player Tank Health:"+ gameworld.getEntity(Constants.PLAYER_TANK_ID).getHealth();
            if(gameworld.getEntity(Constants.PLAYER_TANK_ID).getSpeedFactor()==2)
            {
                local+=" ( Doubled speed and offRadar!)";
            }
            runGameView.setTankInfos(1,local);
        }

        if(gameworld.getEntity(Constants.AI_TANK_1_ID)==null){
            runGameView.setTankInfos(2,"AI Tank1 Destroyed");
        }else{
            String local="AI Tank1 Health:"+ gameworld.getEntity(Constants.AI_TANK_1_ID).getHealth();
            if(gameworld.getEntity(Constants.AI_TANK_1_ID).getSpeedFactor()==2)
            {
                local+= " (Doubled speed)!";
            }
            runGameView.setTankInfos(2,local);
        }

        
        if(gameworld.getEntity(Constants.AI_TANK_2_ID)==null){
            runGameView.setTankInfos(3,"AI Tank2 Destroyed");
        }else{
            runGameView.setTankInfos(3,"AI Tank2 Health:"+ gameworld.getEntity(Constants.AI_TANK_2_ID).getHealth());
        }

    }




    private boolean entitiesOverlap(Entity e1, Entity e2){
        if(e1.getX()>e2.getXBound() || e1.getXBound()<e2.getX() || e1.getY()>e2.getYBound() ||
                e1.getYBound()<e2.getY() )
        {
            return false;
        }
        return true;
    }




    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}

