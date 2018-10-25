package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.awt.Dialog;
import java.util.Map;

import javax.xml.soap.Text;

import Components.AnimationComponent;
import Components.BodyComponent;
import Components.DataComponent;
import Components.InteractiveComponent;
import Components.NPCComponent;
import Components.PlayerComponent;
import Components.SoundComponent;
import Components.StateComponent;
import Components.SteeringComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;
import Helpers.SteeringPresets;
import Managers.EntityManager;
import Observers.HUDObserver;
import Observers.NPCObserver;
import Observers.NPCSubject;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class NPCSystem extends IteratingSystem implements HUDObserver, NPCSubject {
    private RedBeardRun game;
    private Entity player;
    private World world;
    private Array<NPCObserver> observers;
    private Array<Entity> npcs;
    private boolean firstTime;
    private boolean dialogDisplayed = false;
    private boolean receivedMessage = false;
    private EntityManager entityManager;
    private int potionSpawnItemCount;
    private boolean playedSound = false;
    private Timer timer;
    private boolean generatedItems = false;
    private int random;
    private boolean addedEntity = false;
    private Entity talkingEntity;
    private PlayerComponent playerComponent;
    private int X;
    private int Y;
    private Entity entity;



    public NPCSystem(RedBeardRun game, Entity player, World world, EntityManager entityManager, Timer timer) {
        super(Family.all(NPCComponent.class).get());
        this.player = player;
        this.world = world;
        this.entityManager = entityManager;
        this.game = game;
        observers = new Array<NPCObserver>();
        npcs = new Array<Entity>();

        potionSpawnItemCount = 0;
        this.timer = timer;

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        NPCComponent npc = Mappers.npc.get(entity);
        BodyComponent body = Mappers.body.get(entity);
        SteeringComponent steering = Mappers.steering.get(entity);
        AnimationComponent animation = Mappers.animation.get(entity);
        BodyComponent playerbody = Mappers.body.get(player);
        PlayerComponent playerComponent = Mappers.player.get(player);
        StateComponent state = Mappers.state.get(entity);
        SteeringComponent playersteering = Mappers.steering.get(player);
        SoundComponent sound = Mappers.sound.get(entity);


        float distance = body.getBody().getPosition().dst(playerbody.getBody().getPosition());
        firstTime = playerComponent.isFirstTime();

        int health = npc.getHealth();

        if(state.getState()== StateComponent.STATE.LEAVING&&!npc.isPlayedLeavingSound()){

            if (npc.getType() == NPCComponent.TYPE.ROVING) {
                sound.addSound(SoundComponent.EVENT.ROVING_NPC_LEAVING);
            } else {
                sound.addSound(SoundComponent.EVENT.STATIC_NPC_LEAVING);
            }
            npc.setPlayedLeavingSound(true);
        }

        else if (health < 1) {
            if (state.getState() != StateComponent.STATE.LEAVING) {

                //  Gdx.app.log("Enemy System", "I should only be here once"+ state.getState() +" " + state.getDirection());

                if (steering != null) {
                    steering.steeringBehavior = null;
                    steering.currentMode = SteeringComponent.SteeringState.NONE;
                }

                state.setState(StateComponent.STATE.LEAVING);
                state.setDirection(StateComponent.DIRECTION.NONE);
                animation.setTime(0f);


            }
        }

        if (npc.isDead()) {
            //Gdx.app.log("NPC System: ", "NPC is Dead");
            body.setDead(true);
            notify(NPCObserver.NPCEvent.EXIT_CONVERSATION, 1);
            dialogDisplayed = false;
            receivedMessage = false;
            addedEntity = false;
        }
        if (state.getState() != StateComponent.STATE.LEAVING) {

            if (npc.getType() == NPCComponent.TYPE.STATIC) {

                if (distance < 5) {
                    if (firstTime) {
                        if(!addedEntity) {
                            notify(NPCObserver.NPCEvent.LOAD_CONVERSATION, 1);
                            if (state.getState() != StateComponent.STATE.TALKING) {
                                state.setDirection(StateComponent.DIRECTION.NONE);
                                state.setState(StateComponent.STATE.TALKING);
                                notify(NPCObserver.NPCEvent.SHOW_CONVERSATION, 1);
                                this.entity = entity;
                                addedEntity = true;
                            }

                        }

                    } else {
                        if(!addedEntity) {
                            notify(NPCObserver.NPCEvent.LOAD_CONVERSATION, 2);
                            if (state.getState() != StateComponent.STATE.TALKING) {
                                state.setDirection(StateComponent.DIRECTION.NONE);
                                state.setState(StateComponent.STATE.TALKING);
                                notify(NPCObserver.NPCEvent.SHOW_CONVERSATION, 2);
                                this.entity = entity;
                                addedEntity = true;
                            }

                        }

                    }
                    if (!npc.isPlayedTalkingSound()) {

                        sound.addSound(SoundComponent.EVENT.STATIC_NPC_TALKING);
                        npc.setPlayedTalkingSound(true);

                    }
                }
            }

                /*if (state.getState() == StateComponent.STATE.TALKING) {
                    npcs.add(entity);
                    if (!dialogDisplayed) {
                        if (firstTime) {
                            notify(NPCObserver.NPCEvent.SHOW_CONVERSATION, 1);
                            dialogDisplayed = !dialogDisplayed;
                        } else {
                            notify(NPCObserver.NPCEvent.SHOW_CONVERSATION, 2);
                            dialogDisplayed = !dialogDisplayed;

                        }
                    }

                }*/


            else if (npc.getType() == NPCComponent.TYPE.ROVING) {

                // steering.setMaxLinearAcceleration(50);
                // steering.setMaxLinearSpeed(75);

                // Gdx.app.log("NPC SYSTEM",  " Roving NPC distance " + distance + "current State " +steering.currentMode +" Health "+ health);


                switch (steering.currentMode) {
                    case NONE:
                        if (distance < 10 && state.getState() != StateComponent.STATE.TALKING) {
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;

                        }
                        break;
                    case WANDER:
                        if (distance < 5) {
                            steering.currentMode = SteeringComponent.SteeringState.ARRIVE;
                            break;

                        }
                        if (distance < 10 && health < 2) {
                            steering.currentMode = SteeringComponent.SteeringState.EVADE;
                            break;


                        }

                        if (steering.steeringBehavior != SteeringPresets.getWanderPrioritySteering(steering, world)) {
                            steering.steeringBehavior = SteeringPresets.getWanderPrioritySteering(steering, world);
                        }
                        Vector2 wanderTarget = SteeringPresets.getWanderTarget();
                        setWanderFacing(entity, wanderTarget);



                        break;
                    case EVADE:
                        if (distance > 8) {
                            health++;

                        }

                        if (health > 9) {
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;
                            break;
                        }

                        steering.steeringBehavior = SteeringPresets.getFlee(steering, playersteering);
                        setFacing(entity, player);
                        break;



                    case ARRIVE:


                        if (distance < 2) {
                            if(!addedEntity) {
                                notify(NPCObserver.NPCEvent.LOAD_CONVERSATION, 3);
                                if (state.getState() != StateComponent.STATE.TALKING) {
                                    state.setDirection(StateComponent.DIRECTION.NONE);
                                    state.setState(StateComponent.STATE.TALKING);
                                    steering.steeringBehavior = null;
                                    notify(NPCObserver.NPCEvent.SHOW_CONVERSATION, 3);
                                    this.entity = entity;
                                    addedEntity = true;
                                }
                            }
                            if(!npc.isPlayedTalkingSound()) {

                                sound.addSound(SoundComponent.EVENT.ROVING_NPC_TALKING);
                                npc.setPlayedTalkingSound(true);
                            }
                            break;
                        }

                        if (distance > 5) {
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;
                            break;
                        }



                        steering.steeringBehavior = SteeringPresets.getPrioritySteering(steering, playersteering, world);
                        setFacing(entity, player);

                        break;

                }

            }
            npc.setHealth(health);


        }
    }



    private void setWanderFacing(Entity seeker, Vector2 target) {
        StateComponent state = Mappers.state.get(seeker);
        SteeringComponent steering = Mappers.steering.get(seeker);
        Vector2 position = steering.getPosition();
        AnimationComponent animation = Mappers.animation.get(seeker);


        Vector2 pos = target;
        if (state.getState() != StateComponent.STATE.MOVING) {
            state.setState(StateComponent.STATE.MOVING);
            animation.setTime(0f);

        }
        if (Math.abs(pos.x - position.x) < Math.abs(pos.y - position.y)) {

            if (pos.y > position.y) {

                // state.setState(StateComponent.STATE.MOVING);
                if (state.getDirection() != StateComponent.DIRECTION.UP) {
                    state.setDirection(StateComponent.DIRECTION.UP);
                    animation.setTime(0f);
                }
            } else {
                if (state.getDirection() != StateComponent.DIRECTION.DOWN) {
                    //state.setState(StateComponent.STATE.MOVING);
                    state.setDirection(StateComponent.DIRECTION.DOWN);
                    animation.setTime(0f);
                }
            }
        } else {


            if (pos.x > position.x) {


                if (state.getDirection() != StateComponent.DIRECTION.RIGHT) {
                    state.setDirection(StateComponent.DIRECTION.RIGHT);
                    animation.setTime(0f);
                }


            } else {
                if (state.getDirection() != StateComponent.DIRECTION.LEFT) {
                    state.setDirection(StateComponent.DIRECTION.LEFT);
                    animation.setTime(0f);
                }
            }

        }
    }


    private void setFacing(Entity seeker, Entity target) {
        StateComponent state = Mappers.state.get(seeker);
        SteeringComponent steering = Mappers.steering.get(seeker);
        AnimationComponent animation = Mappers.animation.get(seeker);
        Vector2 position = steering.getPosition();

        SteeringComponent targetsteering = Mappers.steering.get(target);
        Vector2 pos = targetsteering.getPosition();
        if (Math.abs(pos.x - position.x) < Math.abs(pos.y - position.y)) {
            if (state.getState() != StateComponent.STATE.MOVING) {
                state.setState(StateComponent.STATE.MOVING);
                animation.setTime(0f);

            }
            if (pos.y > position.y) {

                // state.setState(StateComponent.STATE.MOVING);
                if (state.getDirection() != StateComponent.DIRECTION.UP) {
                    state.setDirection(StateComponent.DIRECTION.UP);
                    animation.setTime(0f);
                }
            } else {
                if (state.getDirection() != StateComponent.DIRECTION.DOWN) {
                    //state.setState(StateComponent.STATE.MOVING);
                    state.setDirection(StateComponent.DIRECTION.DOWN);
                    animation.setTime(0f);
                }
            }
        } else {
            if (state.getState() != StateComponent.STATE.MOVING) {
                state.setState(StateComponent.STATE.MOVING);
                animation.setTime(0f);
            }

            if (pos.x > position.x) {


                if (state.getDirection() != StateComponent.DIRECTION.RIGHT) {
                    state.setDirection(StateComponent.DIRECTION.RIGHT);
                    animation.setTime(0f);
                }


            } else {
                if (state.getDirection() != StateComponent.DIRECTION.LEFT) {
                    state.setDirection(StateComponent.DIRECTION.LEFT);
                    animation.setTime(0f);
                }
            }

        }

    }

    @Override
    public void onNotify(HUDEvent event, int value) {
        receivedMessage = false;
        playerComponent = Mappers.player.get(player);

        //Gdx.app.log("NPC System", " size of list "+ npcs.size);

        if(event == HUDEvent.USE_POTION) {
            // Gdx.app.log("NPC System", "Sent use Potion message: receivedMessage is:  " + receivedMessage);
            if (!receivedMessage) {
                // Gdx.app.log("NPC System", "health is currently: " + playerComponent.getHealth());
                playerComponent.addHealth(value);
                //Gdx.app.log("NPC System", "Bought potion health is now: " + playerComponent.getHealth());
                receivedMessage = true;
            }
            return;
        }




        StateComponent state = Mappers.state.get(entity);
        BodyComponent body = Mappers.body.get(entity);
        DataComponent data = Mappers.data.get(entity);
        AnimationComponent animation = Mappers.animation.get(entity);
        NPCComponent npc = Mappers.npc.get(entity);

        //  float group = data.getData("group");
        //Gdx.app.log("NPC System","group "+ group);


        X = (int)(body.getBody().getPosition().x* Figures.PPM);
        Y = (int)( body.getBody().getPosition().y*Figures.PPM);
        // Gdx.app.log("OnNotify","Event: "+ event);
        // Gdx.app.log("NPC SYSTEM before Switch","X: "+ X+" Y: "+ Y+" Group: "+ playerComponent.getCurrentGroup());
        switch(event){

            case EXIT_CONVERSATION:
                if(npc.getType()==NPCComponent.TYPE.ROVING) {
                    timer.scheduleTask(new Timer.Task() {

                        StateComponent state = Mappers.state.get(entity);
                        AnimationComponent animation = Mappers.animation.get(entity);

                        @Override
                        public void run() {
                            if (state.getState() != StateComponent.STATE.LEAVING) {

                                state.setState(StateComponent.STATE.LEAVING);
                                state.setDirection(StateComponent.DIRECTION.NONE);
                                animation.setTime(0f);
                                entity = null;
                            }


                        }
                    }, 6.0f);
                    this.entity = null;
                }
                else{
                    state.setState(StateComponent.STATE.LEAVING);
                    state.setDirection(StateComponent.DIRECTION.NONE);
                    animation.setTime(0f);
                    entity = null;

                }

                break;

            case BOUGHT_POTION:

                // Gdx.app.log("NPCSystem", "received message bought potion " + potionSpawnItemCount);
                if(!receivedMessage) {
                    playerComponent.addDragoons(value);
                    entityManager.spawnEntity("Potion", playerComponent.getCurrentGroup(),
                            TypeComponent.COL_INTERACTIVE, (int) playerComponent.getCameraX()+random, (int) playerComponent.getCameraY()+random);

                    receivedMessage = true;


                }
                break;




            case ACCEPT_QUEST:
                if(firstTime) {
                    timer.scheduleTask(new Timer.Task() {

                        StateComponent state = Mappers.state.get(entity);
                        AnimationComponent animation = Mappers.animation.get(entity);
                        @Override public void run() {
                            if(state.getState() != StateComponent.STATE.LEAVING) {

                                state.setState(StateComponent.STATE.LEAVING);
                                state.setDirection(StateComponent.DIRECTION.NONE);
                                animation.setTime(0f);
                                entity = null;
                            }

                            entityManager.spawnEntity("LSword", 8888,
                                    TypeComponent.COL_INTERACTIVE, (int)(X -16), Y-16);

                            entityManager.spawnEntity("Potion", playerComponent.getCurrentGroup(),
                                    TypeComponent.COL_INTERACTIVE, (int)(X + 16), Y-16);

                            firstTime = false;
                            playerComponent.setFirstTime(firstTime);


                        }
                    }, 6.0f);


                }
                else{
                    notify(NPCObserver.NPCEvent.EXIT_CONVERSATION,1);

                }

                break;

            case DENY_QUEST:
                timer.scheduleTask(new Timer.Task() {
                    @Override public void run() {
                        game.wonGame = false;
                        game.setScreen(RedBeardRun.SCREENTYPE.CREDITS);
                        entity = null;
                    }
                }, 6.0f);

                break;

            case BOUGHT_BOW:
                if(!receivedMessage) {
                    playerComponent.addDragoons(value);
                    playerComponent.addWeapon(InteractiveComponent.TYPE.BOW);


                        /*if(state.getState() != StateComponent.STATE.LEAVING) {
                            state.setState(StateComponent.STATE.LEAVING);
                            state.setDirection(StateComponent.DIRECTION.NONE);
                            animation.setTime(0f);
                        }*/

                    // notify(NPCObserver.NPCEvent.EXIT_CONVERSATION, 1);
                    receivedMessage = true;

                }
                break;
            case NONE:
                break;
        }



    }

    @Override
    public void addObserver(NPCObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NPCObserver observer) {
        observers.removeValue(observer, true);
    }

    @Override
    public void removeAllObservers() {
        for (NPCObserver observer : observers) {
            observers.removeValue(observer, true);
        }
    }

    @Override
    public void notify(NPCObserver.NPCEvent event, int value) {
        for (NPCObserver observer : observers) {
            observer.onNotify(event, value);
        }
    }
}
