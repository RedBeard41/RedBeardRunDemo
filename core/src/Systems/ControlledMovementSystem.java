package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import Components.BodyComponent;
import Components.InteractiveComponent;
import Components.ParticleEffectComponent;
import Components.PlayerComponent;
import Components.PlayerControlComponent;
import Components.PositionComponent;
import Components.SoundComponent;
import Components.StateComponent;
import Components.SteeringComponent;
import Components.TouchComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.GameInput;
import Helpers.Mappers;
import Helpers.SteeringPresets;
import Managers.EntityManager;
import Observers.HUDObserver;
import Screens.MainGameScreen;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class ControlledMovementSystem extends IteratingSystem {
    public static final String TAG = ControlledMovementSystem.class.getSimpleName();

    private GameInput input;
    private GestureDetector detector;
    private World world;
    private EntityManager entityManager;
    private Timer timer;
    private RedBeardRun game;
    private MainGameScreen screen;
    private PlayerControlComponent control;
    private Entity touchPoint;
    private Entity player;
    private PlayerComponent playerComponent;


    public ControlledMovementSystem(RedBeardRun game, GameInput input,
                                    GestureDetector detector,World world, EntityManager entityManager, Timer timer) {
        super(Family.all(PlayerComponent.class).exclude(ParticleEffectComponent.class).get());

        this.game = game;
        this.input = input;
        this.world = world;
        this.detector = detector;
        this.entityManager = entityManager;
        this.timer = timer;

        touchPoint = entityManager.spawnTouch(TypeComponent.COL_INTERACTIVE,0,0);
    }



    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SteeringComponent steering = Mappers.steering.get(entity);
        StateComponent state = Mappers.state.get(entity);
        control = Mappers.control.get(entity);
        playerComponent = Mappers.player.get(entity);
        BodyComponent body = Mappers.body.get(entity);
        SoundComponent sound = Mappers.sound.get(entity);

        Vector2 position = steering.getPosition();
        Vector3 touch;
        float deltaX;
        float deltaY;

        if(playerComponent.getHealth()<1){
            player = entity;
            if(!playerComponent.isPlayedDyingSound()) {
                sound.addSound(SoundComponent.EVENT.PLAYER_DYING);
                playerComponent.setPlayedDyingSound(true);
                steering.steeringBehavior = null;
                state.setState(StateComponent.STATE.IDLE);
                state.setDirection(StateComponent.DIRECTION.DOWN);
            }
            /*Timer.Task task = timer.scheduleTask(new Timer.Task() {

                @Override
                public void run() {
                    BodyComponent body = Mappers.body.get(player);


                    body.setDead(true);


                }
            }, .4f);*/

            if(playerComponent.isPlayedDyingSound()) {

                game.setScreen(RedBeardRun.SCREENTYPE.CREDITS);
            }
            return;
        }

        if(control.getTimeSinceLastMeleeAttack()>0){
            float currentTime = control.getTimeSinceLastMeleeAttack();
            currentTime -=deltaTime;
            control.setTimeSinceLastMeleeAttack(currentTime);

        }

        if(control.getTimeSinceLastRangeAttack()>0){
            float currentTime = control.getTimeSinceLastRangeAttack();
            currentTime -=deltaTime;
            control.setTimeSinceLastRangeAttack(currentTime);

        }



        if(detector.isLongPressed(.5f)){
            control.setControl(PlayerControlComponent.CONTROL.HOLD);

        }

        if(detector.isPanning()){
            control.setControl(PlayerControlComponent.CONTROL.RANGE);
        }

       // Gdx.app.log(TAG,"Current Control: "+control.getControl());


        switch(control.getControl()){
            case NONE:
                if(!input.getTouch().epsilonEquals(Vector3.Zero,.1f)) {
                    input.resetTouch();
                }
                body.getBody().setLinearDamping(3f);

                break;

            case TAP:

                touch = input.getTouch();
                BodyComponent touchBody = Mappers.body.get(touchPoint);
                touchBody.getBody().setTransform(touch.x/Figures.PPM,touch.y/Figures.PPM,0);
                touchBody.setActive(true);

                //Gdx.app.log("CMS", "player position: "+position.x+", "+ position.y+" touch position: "+touch.x+", "+ touch.y);
                /*Entity target =  entityManager.spawnEntity("Touch", 1111,
                        TypeComponent.COL_INTERACTIVE, (int)touch.x,(int)touch.y);*/

                /*TouchComponent touchComponent = Mappers.touch.get(touchPoint);
                touchComponent.setCurrentTarget(true);*/

                SteeringComponent targetsteering = Mappers.steering.get(touchPoint);

                Vector2 pos = targetsteering.getPosition();
                //Gdx.app.log(TAG, "Before Max Linear Speed: "+ steering.getMaxLinearSpeed() +" max linear acceleration; "+ steering.getMaxLinearAcceleration());
                steering.setMaxLinearSpeed(800);
                steering.setMaxLinearAcceleration(750);
                //Gdx.app.log(TAG, "Before Max Linear Speed: "+ steering.getMaxLinearSpeed() +" max linear acceleration; "+ steering.getMaxLinearAcceleration());
                steering.steeringBehavior = SteeringPresets.getPlayerPrioritySteering(steering,targetsteering,world);
                //Gdx.app.log(TAG, "Registered Tap and applied steering");





                control.setControl(PlayerControlComponent.CONTROL.NONE);
                if (state.getState() != StateComponent.STATE.MOVING) {
                    state.setState(StateComponent.STATE.MOVING);

                }

                if (Math.abs(pos.x - position.x) < Math.abs(pos.y - position.y)) {

                    if (pos.y > position.y) {

                        // state.setState(StateComponent.STATE.MOVING);
                        if (state.getDirection() != StateComponent.DIRECTION.UP) {
                            state.setDirection(StateComponent.DIRECTION.UP);
                        }
                    } else {
                        if (state.getDirection() != StateComponent.DIRECTION.DOWN) {
                            //state.setState(StateComponent.STATE.MOVING);
                            state.setDirection(StateComponent.DIRECTION.DOWN);
                        }
                    }
                }
                else {


                    if (pos.x > position.x) {


                        if (state.getDirection() != StateComponent.DIRECTION.RIGHT) {
                            state.setDirection(StateComponent.DIRECTION.RIGHT);
                        }


                    } else {
                        if (state.getDirection() != StateComponent.DIRECTION.LEFT) {
                            state.setDirection(StateComponent.DIRECTION.LEFT);
                        }
                    }



                }
                 break;

            case DOUBLETAP:
                //todo try to set up to only do attack when tapped on an entity.
                /*if(!playerComponent.checkWeapon(InteractiveComponent.TYPE.HSWORD)
                    &&!playerComponent.checkWeapon(InteractiveComponent.TYPE.LSWORD)){
                    //control.setControl(PlayerControlComponent.CONTROL.NONE);
                    break;
                }*/

                body.getBody().setLinearDamping(100f);
                steering.steeringBehavior = null;

                if(playerComponent.checkWeapon(InteractiveComponent.TYPE.HSWORD)){
                    if(control.getTimeSinceLastMeleeAttack()<=0) {
                        if(state.getState()!= StateComponent.STATE.IDLE) {
                            state.setState(StateComponent.STATE.IDLE);
                        }

                        int currentX = (int) body.getBody().getPosition().x;
                        int currentY = (int) body.getBody().getPosition().y;

                        final Entity heavySword = entityManager.spawnEntity("HeavySword", 7777,
                                TypeComponent.COL_WEAPON, (int) (currentX * Figures.PPM), (int) (currentY * Figures.PPM));
                        sound.addSound(SoundComponent.EVENT.PLAYER_ATTACK_SWORD);
                        control.setTimeSinceLastMeleeAttack(control.getTimeBetweenMeleeAttacks());

                        Timer.Task task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                BodyComponent swordBody = Mappers.body.get(heavySword);

                                swordBody.setDead(true);
                            }
                        }, .3f);
                    }




                }
                else if(playerComponent.checkWeapon(InteractiveComponent.TYPE.LSWORD)){
                    if(control.getTimeSinceLastMeleeAttack()<=0) {
                        if(state.getState()!= StateComponent.STATE.IDLE) {
                            state.setState(StateComponent.STATE.IDLE);
                        }

                        int currentX = (int) body.getBody().getPosition().x;
                        int currentY = (int) body.getBody().getPosition().y;

                        final Entity liteSword = entityManager.spawnEntity("LiteSword", 7777,
                                TypeComponent.COL_WEAPON, (int) (currentX * Figures.PPM), (int) (currentY * Figures.PPM));
                        sound.addSound(SoundComponent.EVENT.PLAYER_ATTACK_SWORD);
                        control.setTimeSinceLastMeleeAttack(control.getTimeBetweenMeleeAttacks());

                        Timer.Task task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                BodyComponent swordBody = Mappers.body.get(liteSword);

                                swordBody.setDead(true);
                            }
                        }, .6f);
                    }




                }



               // body.getBody().setLinearDamping(3f);
                control.setControl(PlayerControlComponent.CONTROL.NONE);
                break;

            case RANGE:

                if(control.getTimeSinceLastRangeAttack()>0){
                    control.setControl(PlayerControlComponent.CONTROL.NONE);
                    break;
                }

                body.getBody().setLinearDamping(100f);
                steering.steeringBehavior = null;

                touch = input.getTouch();
                deltaX = input.getDeltaX();
                deltaY = input.getDeltaY();
                //Vector3 touch2 = input.getTouch2();
               // Gdx.app.log(TAG,deltaX+" "+ deltaY);
                if (Math.abs(deltaX) < Math.abs(deltaY)) {

                    if (deltaY < 0) {

                        // state.setState(StateComponent.STATE.MOVING);
                        if (state.getDirection() != StateComponent.DIRECTION.UP) {
                            state.setDirection(StateComponent.DIRECTION.UP);
                        }
                    } else {
                        if (state.getDirection() != StateComponent.DIRECTION.DOWN) {
                            //state.setState(StateComponent.STATE.MOVING);
                            state.setDirection(StateComponent.DIRECTION.DOWN);
                        }
                    }
                }
                else {


                    if (deltaX > 0) {


                        if (state.getDirection() != StateComponent.DIRECTION.RIGHT) {
                            state.setDirection(StateComponent.DIRECTION.RIGHT);
                        }


                    } else {
                        if (state.getDirection() != StateComponent.DIRECTION.LEFT) {
                            state.setDirection(StateComponent.DIRECTION.LEFT);
                        }
                    }



                }


                if(playerComponent.checkWeapon(InteractiveComponent.TYPE.BOW)) {
                    if (control.getTimeSinceLastRangeAttack() <= 0) {
                        int currentX = (int) (body.getBody().getPosition().x * Figures.PPM);
                        int currentY = (int) (body.getBody().getPosition().y * Figures.PPM);

                        final Entity arrow = entityManager.spawnEntity("Arrow", 7777,
                                TypeComponent.COL_WEAPON, currentX , currentY);
                        sound.addSound(SoundComponent.EVENT.PLAYER_ATTACK_RANGE);
                        control.setTimeSinceLastRangeAttack(control.getTimeBetweenRangeAttacks());

                        Timer.Task task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                BodyComponent swordBody = Mappers.body.get(arrow);

                                swordBody.setDead(true);
                            }
                        }, 2.0f);
                    }
                    control.setTimeSinceLastRangeAttack(control.getTimeBetweenRangeAttacks());
                }







                //body.getBody().setLinearDamping(3f);
                control.setControl(PlayerControlComponent.CONTROL.NONE);
                break;

            case HOLD:

                if(!playerComponent.checkWeapon(InteractiveComponent.TYPE.HSWORD)||
                        control.getTimeSinceLastMeleeAttack()>0){
                    control.setControl(PlayerControlComponent.CONTROL.NONE);
                    break;
                }

                steering.steeringBehavior = null;

                if(playerComponent.checkWeapon(InteractiveComponent.TYPE.HSWORD)){
                    if(control.getTimeSinceLastMeleeAttack()<=0) {
                        if(state.getState()!= StateComponent.STATE.IDLE) {
                            state.setState(StateComponent.STATE.IDLE);
                        }

                        int currentX = (int) (body.getBody().getPosition().x+8/Figures.PPM);
                        int currentY = (int) (body.getBody().getPosition().y+8/Figures.PPM);

                        body.getBody().setLinearDamping(1000f);


                        if(state.getDirection()!= StateComponent.DIRECTION.DOWN){
                            state.setDirection(StateComponent.DIRECTION.DOWN);
                            //body.getBody().setTransform(currentX,currentY,0f);

                        }

                        for(int i = 0; i<4;i++){

                            final Entity heavySword = entityManager.spawnEntity("HeavySword360", 7777,
                                    TypeComponent.COL_WEAPON, (int) (currentX * Figures.PPM), (int) (currentY * Figures.PPM));
                            sound.addSound(SoundComponent.EVENT.PLAYER_ATTACK_SWORD);


                            Timer.Task task = timer.scheduleTask(new Timer.Task() {

                                @Override
                                public void run() {
                                    BodyComponent swordBody = Mappers.body.get(heavySword);

                                    swordBody.setDead(true);
                                }
                            }, .3f);



                            switch(state.getDirection()){
                                case UP:
                                    state.setDirection(StateComponent.DIRECTION.DOWN);
                                   // body.getBody().setTransform(currentX,currentY,-90f);
                                    break;
                                case RIGHT:
                                    state.setDirection(StateComponent.DIRECTION.UP);
                                   // body.getBody().setTransform(currentX,currentY,-180f);
                                    break;
                                case DOWN:
                                    state.setDirection(StateComponent.DIRECTION.LEFT);
                                  //  body.getBody().setTransform(currentX,currentY,-270f);
                                    break;
                                case LEFT:
                                    state.setDirection(StateComponent.DIRECTION.RIGHT);
                                   // body.getBody().setTransform(currentX,currentY,0f);
                                    break;
                            }
                        }
                        control.setTimeSinceLastMeleeAttack(control.getTimeBetweenMeleeAttacks());


                    }




                }
                //body.getBody().setLinearDamping(3f);
                control.setControl(PlayerControlComponent.CONTROL.NONE);
                break;
        }




    }
}
