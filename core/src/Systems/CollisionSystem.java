package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Components.AnimationComponent;
import Components.BodyComponent;
import Components.CollisionComponent;
import Components.DataComponent;
import Components.EnemyComponent;
import Components.InteractiveComponent;
import Components.NPCComponent;
import Components.PlayerComponent;
import Components.SoundComponent;
import Components.StateComponent;
import Components.SteeringComponent;
import Components.TextureComponent;
import Components.TouchComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;
import Managers.EntityManager;
import Managers.MyAssetManager;
import Observers.StatusObserver;
import Observers.StatusSubject;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class CollisionSystem extends IteratingSystem implements StatusSubject{

    private PooledEngine engine;
    private World world;
    private EntityManager entityManager;
    private MyAssetManager myAssetManager;
    private TextureRegion cutBush;
    private TextureAtlas atlas;
    private Array<StatusObserver> observers;
    private Entity player;
    private RedBeardRun game;

    public CollisionSystem(RedBeardRun redBeardRun, PooledEngine engine, World world, EntityManager entityManager, MyAssetManager myAssetManager, Entity player) {
        super(Family.all(CollisionComponent.class).get());
        this.engine = engine;
        this.world = world;
        this.entityManager = entityManager;
        this.myAssetManager = myAssetManager;
        this.player = player;
        this.game = redBeardRun;
        observers = new Array<StatusObserver>();
        atlas = myAssetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas");
        cutBush = atlas.findRegion("CutDown_Bush");
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // get collision for this entity
        CollisionComponent collision = Mappers.collision.get(entity);
        //get collided entity
        Entity collidedEntity = collision.getCollisionEntity();


        TypeComponent thisType = entity.getComponent(TypeComponent.class);
        String TAG;

        PlayerComponent playerComponent = Mappers.player.get(entity);
        StateComponent state = Mappers.state.get(entity);
        BodyComponent body = Mappers.body.get(entity);
        SteeringComponent steering = Mappers.steering.get(entity);
        SoundComponent sound = Mappers.sound.get(entity);

        InteractiveComponent interactive;


        InteractiveComponent interactiveCollided;
        NPCComponent npcCollided ;
        SteeringComponent steeringCollided;
        StateComponent stateCollided ;
        BodyComponent bodyCollided ;
        TextureComponent textureCollided ;

        DataComponent dataCollided ;


       // Gdx.app.log("COLLISION SYSTEM", thisType.getType() + " collided "+ !(collidedEntity ==null));

        if (thisType.getType() == TypeComponent.COL_PLAYER) {
            TAG = "PLAYER COLLISION";
            if (collidedEntity != null) {
                int health;
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    //TODO rewrite mappers so they are accessible by all cases without having to change variable names
                    switch (type.getType()) {

                        case TypeComponent.COL_WEAPON:
                        case TypeComponent.COL_ENEMY:
                            //do player hit enemy thing
                            Gdx.app.log(TAG, "Enemy");

                            //playerComponent = Mappers.player.get(entity);
                           playerComponent.addHealth(-15);
                           notify(StatusObserver.StatusEvent.UPDATED_HEALTH,-15);
                           sound.addSound(SoundComponent.EVENT.PLAYER_HIT);

                            /*if(playerComponent.getHealth()<1) {
                                sound.addSound(SoundComponent.EVENT.PLAYER_DYING);
                            }*/

                            /*EnemyComponent enemy= Mappers.enemy.get(collidedEntity);
                            StateComponent enemystate = Mappers.state.get(collidedEntity);
                            health = enemy.getHealth();
                            enemy.setHealth(health--);
                            int aggressive = enemy.getAggressive();
                            enemy.setAggressive(aggressive--);

                            if(health<1){
                                enemystate.setState(StateComponent.STATE.LEAVING);
                                enemystate.setDirection(StateComponent.DIRECTION.NONE);
                            }*/
                            break;

                        case TypeComponent.COL_LEVEL:
                            Gdx.app.log(TAG, "Level");
                            break;

                       /* case TypeComponent.COL_ENEMY_WEAPON:
                            Gdx.app.log(TAG, "Enemy Weapon");
                            break;*/

                        case TypeComponent.COL_FRIENDLY:
                            Gdx.app.log(TAG, "Friendly");
                        /*  npcCollided = Mappers.npc.get(collidedEntity);
                          stateCollided = Mappers.state.get(collidedEntity);
                            //stateCollided = Mappers.state.get(collidedEntity);
                            npcCollided.addHealth(-10);*/
                            //Gdx.app.log(TAG,"health before "+ health);


                           /* if(npcCollided.isDead()){
                                if(stateCollided.getState() != StateComponent.STATE.LEAVING) {
                                    stateCollided.setState(StateComponent.STATE.LEAVING);
                                    stateCollided.setDirection(StateComponent.DIRECTION.NONE);
                                    //sound.addSound(SoundComponent.EVENT.PLAYER_HIT);
                                  //  Gdx.app.log(TAG, "Current State "+ npcstate.getState() + " current Direction "+ npcstate.getDirection());
                                }

                            }*/
                            break;

                        case TypeComponent.COL_INTERACTIVE:

                            interactiveCollided = Mappers.interactive.get(collidedEntity);



                            switch(interactiveCollided.getType()){
                                case LSWORD:
                                    Gdx.app.log(TAG, "Lsword item");
                                    notify(StatusObserver.StatusEvent.UPDATED_WEAPONS,1);
                                    playerComponent.addWeapon(interactiveCollided.getType());
                                    interactiveCollided.setDead(true);
                                    sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);
                                    break;
                                case HSWORD:
                                    Gdx.app.log(TAG, "Hsword item");
                                    notify(StatusObserver.StatusEvent.UPDATED_WEAPONS,2);


                                    playerComponent.addWeapon(interactiveCollided.getType());
                                    interactiveCollided.setDead(true);
                                    sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);
                                    break;
                                case BOW:
                                    Gdx.app.log(TAG, "Bow item");
                                    notify(StatusObserver.StatusEvent.UPDATED_WEAPONS,3);
                                    playerComponent.addWeapon(interactiveCollided.getType());
                                    interactiveCollided.setDead(true);
                                    sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);
                                    break;
                                case HEART:
                                    Gdx.app.log(TAG, "Heart item");
                                    playerComponent.addHealth(15);
                                    notify(StatusObserver.StatusEvent.UPDATED_HEALTH,15);

                                    sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);
                                    interactiveCollided.setDead(true);

                                    break;
                                case TOUCH:
                                    Gdx.app.log(TAG, "Touch item");
                                    state.setState(StateComponent.STATE.IDLE);
                                    steering.steeringBehavior = null;
                                    bodyCollided = Mappers.body.get(collidedEntity);
                                    bodyCollided.setActive(false);
                                    break;
                                case PORTAL:
                                    Gdx.app.log(TAG, "Portal");
                                    //  BodyComponent playerbody = Mappers.body.get(entity);
                                    //  PlayerComponent playerComponent = Mappers.player.get(entity);
                                    dataCollided= Mappers.data.get(collidedEntity);
                                    float x = dataCollided.getData("x");
                                    float y = dataCollided.getData("y");
                                    float currentGroup = dataCollided.getData("current");
                                    float pastGroup = dataCollided.getData("past");
                                    float lerp = dataCollided.getData("lerp");
                                    float area;
                                    float cameraX = dataCollided.getData("cameraX");
                                    float cameraY = dataCollided.getData("cameraY");

                                  //  Gdx.app.log("Portal", "current "+ currentGroup + " Past "+ pastGroup);
                                    //playerComponent.setLoadHealth(playerComponent.getHealth());
                                    if(currentGroup == 0 ||currentGroup == 1 ||
                                        currentGroup == 2|| currentGroup == 3||
                                        currentGroup == 7|| currentGroup == 8||
                                        currentGroup == 9|| currentGroup == 10||
                                        currentGroup == 14|| currentGroup == 15||
                                        currentGroup == 16|| currentGroup == 25) {
                                        game.loadData.clear();
                                        game.loadData.putFloat("health", playerComponent.getHealth());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("pastGroup", playerComponent.getPastGroup());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("currentGroup", playerComponent.getCurrentGroup());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("cameraX", playerComponent.getCameraX());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("cameraY", playerComponent.getCameraY());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("X", playerComponent.getX());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("Y", playerComponent.getY());
                                        //game.loadData.flush();

                                        game.loadData.putFloat("dragoons", playerComponent.getDragoons());
                                        if (playerComponent.getWeapons().size > 0) {

                                            if (playerComponent.checkWeapon(InteractiveComponent.TYPE.LSWORD)) {

                                                game.loadData.putBoolean("liteSword", true);
                                                //game.loadData.flush();
                                            }
                                            if (playerComponent.checkWeapon(InteractiveComponent.TYPE.HSWORD)) {
                                                game.loadData.putBoolean("heavySword", true);
                                                //game.loadData.flush();
                                            }
                                            if (playerComponent.checkWeapon(InteractiveComponent.TYPE.BOW)) {
                                                game.loadData.putBoolean("bow", true);
                                                //game.loadData.flush();
                                            }
                                        }

                                        game.loadData.putFloat("potions", playerComponent.getPotions());
                                        game.loadData.putBoolean("firstTime", playerComponent.isFirstTime());
                                        game.loadData.flush();
                                    }

                                    playerComponent.setPastGroup(pastGroup);
                                    playerComponent.setCurrentGroup(currentGroup);
                                    playerComponent.setCameraX(cameraX);
                                    playerComponent.setCameraY(cameraY);
                                    playerComponent.setLerp(lerp);
                                    playerComponent.setX(x);
                                    playerComponent.setY(y);
                                    playerComponent.setSpawnPastGroupEntities(false);
                                    playerComponent.setDragoons(playerComponent.getDragoons());

                                    if(lerp == 0f){
                                        body.getBody().setTransform(x/Figures.PPM,y/Figures.PPM,body.getBody().getAngle());
                                    }
                                    else {
                                        playerComponent.setPause(true);
                                    }


                                    state.setState(StateComponent.STATE.IDLE);
                                    steering.steeringBehavior = null;

                                    //adding sound for specific portals
                                    if(currentGroup == 10 ||currentGroup == 5| currentGroup ==26){
                                        sound.addSound(SoundComponent.EVENT.SECRET_AREA_DISCOVERY);
                                    }


                                    notify(StatusObserver.StatusEvent.UPDATED_LOCATION,(int)currentGroup);
                                    break;
                                case POTION:
                                    Gdx.app.log(TAG, "Potion item");
                                   // playerComponent.addHealth(60);
                                    playerComponent.addPotion();
                                    notify(StatusObserver.StatusEvent.UPDATED_POTIONS,1);
                                    sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);
                                    interactiveCollided.setDead(true);
                                    break;
                                case DRAGOON10:
                                    Gdx.app.log(TAG, "Dragoon10 item");
                                    playerComponent.addDragoons(1);
                                    notify(StatusObserver.StatusEvent.UPDATED_DRAGOONS, 1);
                                    sound.addSound(SoundComponent.EVENT.COIN_PICKUP);
                                    interactiveCollided.setDead(true);
                                    break;

                                case DRAGOON25:
                                    Gdx.app.log(TAG, "Dragoon25 item");
                                    playerComponent.addDragoons(5);
                                    notify(StatusObserver.StatusEvent.UPDATED_DRAGOONS,5);
                                    sound.addSound(SoundComponent.EVENT.COIN_PICKUP);
                                    interactiveCollided.setDead(true);
                                    break;
                                case BRICK:
                                    Gdx.app.log(TAG,"Brick Destroyable");
                                    break;
                                case ORB:
                                    body.setDead(true);
                                    game.wonGame = true;
                                    game.setScreen(RedBeardRun.SCREENTYPE.CREDITS);
                                    break;


                            }

                         /*   if(interactiveCollided.getType()== InteractiveComponent.TYPE.LITESWORD){
                                notify(StatusObserver.StatusEvent.UPDATED_WEAPONS,1);
                            }
                            else if(interactiveCollided.getType()== InteractiveComponent.TYPE.HEAVYSWORD){
                                notify(StatusObserver.StatusEvent.UPDATED_WEAPONS,2);
                            }
                            else if(interactiveCollided.getType()== InteractiveComponent.TYPE.BOW){
                                notify(StatusObserver.StatusEvent.UPDATED_WEAPONS,3);
                            }

                            break;

                        case TypeComponent.COL_POTION:
                            Gdx.app.log(TAG, "Potion item");

                            interactiveCollided = Mappers.interactive.get(collidedEntity);
                            interactiveCollided.setDead(true);

                           // playerComponent = Mappers.player.get(entity);
                            playerComponent.addHealth(60);
                            notify(StatusObserver.StatusEvent.UPDATED_POTIONS,1);
                            sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);

                            break;
                        case TypeComponent.COL_HEART:
                            Gdx.app.log(TAG, "Heart item");

                            interactiveCollided = Mappers.interactive.get(collidedEntity);
                            interactiveCollided.setDead(true);


                            playerComponent.addHealth(15);
                            notify(StatusObserver.StatusEvent.UPDATED_HEALTH,15);
                            sound.addSound(SoundComponent.EVENT.ITEM_PICKUP);

                            break;

                        case TypeComponent.COL_DRAGOONS:
                            Gdx.app.log(TAG, "dragoons");
                            interactiveCollided = Mappers.interactive.get(collidedEntity);

                            if(interactiveCollided.getType() == InteractiveComponent.TYPE.DRAGOON10){
                                playerComponent.addDragoons(1);
                                notify(StatusObserver.StatusEvent.UPDATED_DRAGOONS, 1);
                                //Gdx.app.log(TAG,playerComponent.getDragoons()+"");

                            }

                            else if (interactiveCollided.getType() == InteractiveComponent.TYPE.DRAGOON25){
                                playerComponent.addDragoons(5);
                                notify(StatusObserver.StatusEvent.UPDATED_DRAGOONS,5);
                            }
                            sound.addSound(SoundComponent.EVENT.COIN_PICKUP);
                            interactiveCollided.setDead(true);

                            break;
                        case TypeComponent.COL_DESTROYABLE:
                            Gdx.app.log(TAG, "Destroyable");
                           *//* textureCollided= Mappers.texture.get(collidedEntity);
                            dataCollided = Mappers.data.get(collidedEntity);
                            bodyCollided = Mappers.body.get(collidedEntity);
                            textureCollided.setRegion(cutBush);
                            bodyCollided.setActive(false);
                            sound.addSound(SoundComponent.EVENT.DESTROYED_PLANT);

                            float group = dataCollided.getData("group");

                            int destroyableX = (int)bodyCollided.getBody().getPosition().x;
                            int destroyableY = (int) bodyCollided.getBody().getPosition().y;

                            entityManager.spawnEntity("Dragoon10",group,
                                    TypeComponent.COL_DRAGOONS,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                           // sound.addSound(SoundComponent.EVENT.COIN_DROP);
*//*
                            break;

                        case TypeComponent.COL_TOUCH:
                            Gdx.app.log(TAG, "Touch Location");
                            *//*TouchComponent touch = Mappers.touch.get(collidedEntity);
                            BodyComponent body = Mappers.body.get(collidedEntity);*//*


                            state.setState(StateComponent.STATE.IDLE);
                            steering.steeringBehavior = null;

                            break;
                        case TypeComponent.COL_PORTAL:
                            Gdx.app.log(TAG, "Portal");
                          //  BodyComponent playerbody = Mappers.body.get(entity);
                          //  PlayerComponent playerComponent = Mappers.player.get(entity);
                          dataCollided= Mappers.data.get(collidedEntity);
                            float x = dataCollided.getData("x");
                            float y = dataCollided.getData("y");
                            float currentGroup = dataCollided.getData("current");
                            float pastGroup = dataCollided.getData("past");
                            float lerp = dataCollided.getData("lerp");
                            float area;
                            float cameraX = dataCollided.getData("cameraX");
                            float cameraY = dataCollided.getData("cameraY");

                            Gdx.app.log("Portal", "current "+ currentGroup + " Past "+ pastGroup);
                            playerComponent.setLoadHealth(playerComponent.getHealth());
                            playerComponent.setPastGroup(pastGroup);
                            playerComponent.setCurrentGroup(currentGroup);
                            playerComponent.setCameraX(cameraX);
                            playerComponent.setCameraY(cameraY);
                            playerComponent.setLerp(lerp);
                            playerComponent.setX(x);
                            playerComponent.setY(y);
                            playerComponent.setSpawnPastGroupEntities(false);
                            playerComponent.setDragoons(playerComponent.getDragoons());


                        if(lerp == 0f){
                            body.getBody().setTransform(x/Figures.PPM,y/Figures.PPM,body.getBody().getAngle());
                        }
                        else {
                            playerComponent.setPause(true);
                        }


                            state.setState(StateComponent.STATE.IDLE);
                            steering.steeringBehavior = null;

                            //adding sound for specific portals
                            if(currentGroup == 11){
                                sound.addSound(SoundComponent.EVENT.SECRET_AREA_DISCOVERY);
                            }
                            if(currentGroup == 12){
                                sound.addSound(SoundComponent.EVENT.DUNGEON_SONG);
                            }
                            if(currentGroup == 7){
                                sound.addSound(SoundComponent.EVENT.GAME_SONG);
                            }

                        notify(StatusObserver.StatusEvent.UPDATED_LOCATION,(int)currentGroup);

                            break;
*/
                        case TypeComponent.COL_OTHER:
                            //do player hit other thing
                           Gdx.app.log(TAG,"Other");
                            break;

                        default:
                            Gdx.app.log(TAG,"Unclear");
                    }

                }
            }
        }

        if (thisType.getType() == TypeComponent.COL_WEAPON) {
            TAG = "WEAPON COLLISION";
            if (collidedEntity != null) {
                sound = Mappers.sound.get(collidedEntity);
                int health;
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    //TODO rewrite mappers so they are accessible by all cases without having to change variable names
                    switch (type.getType()) {
                        case TypeComponent.COL_ENEMY:


                            //sound.addSound(SoundComponent.EVENT.ENEMY_HIT);


                           EnemyComponent enemy= Mappers.enemy.get(collidedEntity);
                           stateCollided = Mappers.state.get(collidedEntity);
                            AnimationComponent animation = Mappers.animation.get(collidedEntity);
                            steeringCollided = Mappers.steering.get(collidedEntity);
                           // Gdx.app.log(TAG, "Enemy: "+ enemy.getType() +" current health "+ enemy.getHealth());
                           // StateComponent enemystate = Mappers.state.get(collidedEntity);
                            interactive = Mappers.interactive.get(entity);

                            if(interactive.getType()==InteractiveComponent.TYPE.LITESWORD) {
                                enemy.addHealth(-15);
                                enemy.addAggressive(-1);
                            }
                            else if(interactive.getType()==InteractiveComponent.TYPE.HEAVYSWORD){
                                enemy.addHealth(-30);
                                enemy.addAggressive(-2);
                            }
                            else if(interactive.getType()==InteractiveComponent.TYPE.ARROW){
                               // Gdx.app.log(TAG, "health "+enemy.getHealth());
                               // Gdx.app.log(TAG, "Arrow hit enemy");
                                enemy.addHealth(-10);
                                enemy.addAggressive(-5);
                            }

                            if(enemy.getType()== EnemyComponent.TYPE.DRAGON){
                                //Gdx.app.log(TAG, "Dragon health "+enemy.getHealth());
                                if(stateCollided.getState()!= StateComponent.STATE.HIT) {
                                    stateCollided.setState(StateComponent.STATE.HIT);
                                    animation.setTime(0f);
                                    sound.addSound(SoundComponent.EVENT.DRAGON_HIT);


                                }
                            }

                            if(enemy.getType()!= EnemyComponent.TYPE.DRAGON) {
                                if(stateCollided.getState()!= StateComponent.STATE.HIT) {
                                    stateCollided.setState(StateComponent.STATE.HIT);
                                    stateCollided.setDirection(StateComponent.DIRECTION.NONE);
                                    steeringCollided.steeringBehavior = null;
                                    steeringCollided.currentMode = SteeringComponent.SteeringState.HIT;
                                    animation.setTime(0f);
                                    sound.addSound(SoundComponent.EVENT.ENEMY_HIT);


                                }

                            }


                           /* int aggressive = enemy.getAggressive();
                            enemy.setAggressive(aggressive--);*/

                           /* if(health<1){
                                enemystate.setState(StateComponent.STATE.LEAVING);
                                enemystate.setDirection(StateComponent.DIRECTION.NONE);
                                sound.addSound(SoundComponent.EVENT.ENEMY_DYING);
                            }*/

                          // Gdx.app.log(TAG,enemy.getType().toString()+" "+ stateCollided.getState()+" "+ stateCollided.getDirection());
                            break;

                        case TypeComponent.COL_LEVEL:
                            Gdx.app.log(TAG, "Level");
                            break;

                       /* case TypeComponent.COL_ENEMY_WEAPON:
                            Gdx.app.log(TAG, "Enemy Weapon");
                            break;*/

                        case TypeComponent.COL_FRIENDLY:
                            Gdx.app.log(TAG, "Friendly");
                            npcCollided = Mappers.npc.get(collidedEntity);
                           // stateCollided = Mappers.state.get(collidedEntity);
                            //stateCollided = Mappers.state.get(collidedEntity);
                            npcCollided.addHealth(-5);

                            //Gdx.app.log(TAG,"health before "+ health);

/*
                            if(npcCollided.isDead()){
                                if(stateCollided.getState() != StateComponent.STATE.LEAVING) {
                                    stateCollided.setState(StateComponent.STATE.LEAVING);
                                    stateCollided.setDirection(StateComponent.DIRECTION.NONE);
                                    //sound.addSound(SoundComponent.EVENT.PLAYER_HIT);
                                    //  Gdx.app.log(TAG, "Current State "+ npcstate.getState() + " current Direction "+ npcstate.getDirection());
                                }*/

                           // }
                            break;


                        case TypeComponent.COL_INTERACTIVE:
                            sound = Mappers.sound.get(entity);
                           // Gdx.app.log(TAG, "Interactive");
                            textureCollided= Mappers.texture.get(collidedEntity);
                            dataCollided = Mappers.data.get(collidedEntity);
                            bodyCollided = Mappers.body.get(collidedEntity);
                            interactiveCollided = Mappers.interactive.get(collidedEntity);
                            //Gdx.app.log(TAG,interactiveCollided.getType().toString());

                            switch(interactiveCollided.getType()){
                                case DESTROYABLE:
                                    Gdx.app.log(TAG, "Destroyable item");
                                    type.setType(TypeComponent.COL_OTHER);
                                   textureCollided.setRegion(cutBush);
                                   bodyCollided.setActive(false);
                                    sound.addSound(SoundComponent.EVENT.DESTROYED_PLANT);

                                    float group = dataCollided.getData("group");
                                    float random = MathUtils.random(0,100);

                                    int destroyableX = (int)bodyCollided.getBody().getPosition().x;
                                    int destroyableY = (int) bodyCollided.getBody().getPosition().y;


                                    if(random < 40){
                                        //do nothing
                                        return;
                                    }
                                    else if(random <75){
                                        entityManager.spawnEntity("Dragoon10",group,
                                                TypeComponent.COL_INTERACTIVE,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                                        sound.addSound(SoundComponent.EVENT.COIN_DROP);

                                    }
                                    else if (random < 95){
                                        entityManager.spawnEntity("Heart",group,
                                                TypeComponent.COL_INTERACTIVE,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                                        sound.addSound(SoundComponent.EVENT.COIN_DROP);
                                    }
                                    else if (random < 100){
                                        entityManager.spawnEntity("Dragoon25",group,
                                                TypeComponent.COL_INTERACTIVE,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                                        sound.addSound(SoundComponent.EVENT.COIN_DROP);
                                    }
                                    break;


                                case BOW:
                                    break;

                            }



                           /* textureCollided.setRegion(cutBush);
                            bodyCollided.setActive(false);
                            sound.addSound(SoundComponent.EVENT.DESTROYED_PLANT);

                            float group = dataCollided.getData("group");
                            float random = MathUtils.random(0,100);

                            int destroyableX = (int)bodyCollided.getBody().getPosition().x;
                            int destroyableY = (int) bodyCollided.getBody().getPosition().y;



                            if(random < 40){
                                //do nothing
                                return;
                            }
                            else if(random <75){
                                entityManager.spawnEntity("Dragoon10",group,
                                        TypeComponent.COL_INTERACTIVE,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                                sound.addSound(SoundComponent.EVENT.COIN_DROP);

                            }
                            else if (random < 95){
                                entityManager.spawnEntity("Heart",group,
                                        TypeComponent.COL_INTERACTIVE,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                                sound.addSound(SoundComponent.EVENT.COIN_DROP);
                            }
                            else if (random < 100){
                                entityManager.spawnEntity("Dragoon25",group,
                                        TypeComponent.COL_INTERACTIVE,(int)(destroyableX*Figures.PPM),(int) (destroyableY*Figures.PPM));
                                sound.addSound(SoundComponent.EVENT.COIN_DROP);
                            }

*/

                            // sound.addSound(SoundComponent.EVENT.COIN_DROP);

                            break;

                      /*  case TypeComponent.COL_TOUCH:
                            Gdx.app.log(TAG, "Touch Location");
                            *//*TouchComponent touch = Mappers.touch.get(collidedEntity);
                            BodyComponent body = Mappers.body.get(collidedEntity);*//*


                         *//*   state.setState(StateComponent.STATE.IDLE);
                            steering.steeringBehavior = null;*//*

                            break;
                        case TypeComponent.COL_PORTAL:
                            Gdx.app.log(TAG, "Portal");

                            break;*/

                        case TypeComponent.COL_OTHER:
                            //do player hit other thing
                            Gdx.app.log(TAG,"Other");
                            break;

                        default:
                            Gdx.app.log(TAG,"Unclear");
                    }

                }
            }
        }

        if (thisType.getType() == TypeComponent.COL_ENEMY) {
            TAG = "Enemy Collision";
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.getType()) {
                        case TypeComponent.COL_ENEMY:

                            Gdx.app.log(TAG, "Enemy");

                            break;
                        case TypeComponent.COL_PLAYER:
                            Gdx.app.log(TAG, "Player");
                            playerComponent = Mappers.player.get(player);
                            playerComponent.addHealth(-15);
                            notify(StatusObserver.StatusEvent.UPDATED_HEALTH,-15);



                            break;

                        case TypeComponent.COL_LEVEL:
                            Gdx.app.log(TAG, "Level");
                            break;

                        case TypeComponent.COL_FRIENDLY:
                            Gdx.app.log(TAG, "Friendly");
                            break;

                        case TypeComponent.COL_WEAPON:
                            Gdx.app.log(TAG, "Weapon item");

                            EnemyComponent enemy= Mappers.enemy.get(entity);
                            stateCollided = Mappers.state.get(entity);
                            steeringCollided = Mappers.steering.get(entity);
                            AnimationComponent animation = Mappers.animation.get(entity);
                           // Gdx.app.log(TAG, "Enemy: "+ enemy.getType() +" current health "+ enemy.getHealth());
                            // StateComponent enemystate = Mappers.state.get(collidedEntity);
                            interactive = Mappers.interactive.get(collidedEntity);

                            if(interactive.getType()==InteractiveComponent.TYPE.LITESWORD) {
                                enemy.addHealth(-15);
                                enemy.addAggressive(-1);
                            }
                            else if(interactive.getType()==InteractiveComponent.TYPE.HEAVYSWORD){
                                enemy.addHealth(-30);
                                enemy.addAggressive(-2);
                            }
                            else if(interactive.getType()==InteractiveComponent.TYPE.ARROW){
                               // Gdx.app.log(TAG, "health "+enemy.getHealth());
                               // Gdx.app.log(TAG, "Enemy hit Arrow");
                                enemy.addHealth(-10);
                                enemy.addAggressive(-5);
                            }

                            if(enemy.getType()== EnemyComponent.TYPE.DRAGON){
                               // Gdx.app.log(TAG, "health "+enemy.getHealth());
                                if(stateCollided.getState()!= StateComponent.STATE.HIT) {
                                    stateCollided.setState(StateComponent.STATE.HIT);
                                    animation.setTime(0f);
                                    sound.addSound(SoundComponent.EVENT.DRAGON_HIT);


                                }
                            }

                            if(enemy.getType()!= EnemyComponent.TYPE.DRAGON) {
                                if(stateCollided.getState()!= StateComponent.STATE.HIT) {
                                    stateCollided.setState(StateComponent.STATE.HIT);
                                    stateCollided.setDirection(StateComponent.DIRECTION.NONE);
                                    steeringCollided.steeringBehavior = null;
                                    steeringCollided.currentMode = SteeringComponent.SteeringState.HIT;
                                    animation.setTime(0f);
                                    sound.addSound(SoundComponent.EVENT.ENEMY_HIT);


                                }

                            }
                           // Gdx.app.log(TAG,enemy.getType().toString()+" "+ stateCollided.getState()+" "+ stateCollided.getDirection());
                            break;

                      /*  case TypeComponent.COL_ENEMY_WEAPON:
                            Gdx.app.log(TAG, "Enemy Weapon");
                            break;



                        case TypeComponent.COL_POTION:
                            Gdx.app.log(TAG, "Potion item");
                            break;

                        case TypeComponent.COL_DRAGOONS:
                            Gdx.app.log(TAG, "dragoons");
                            break;

                        case TypeComponent.COL_TOUCH:
                            Gdx.app.log(TAG, "Touch Location");

                            break;
                        case TypeComponent.COL_PORTAL:
                            Gdx.app.log(TAG, "Portal");
                           break;

                        case TypeComponent.COL_PLAYER_WEAPON:
                            Gdx.app.log(TAG, "Player Weapon");
                            break;*/


                        case TypeComponent.COL_OTHER:
                            //do player hit other thing
                            Gdx.app.log(TAG,"Other");
                            break;

                        default:
                            Gdx.app.log(TAG,"Unclear");
                    }

                }
            }
        }

        if (thisType.getType() == TypeComponent.COL_FRIENDLY) {
            TAG = "NPC Collision";
            if (collidedEntity != null) {
                TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
                if (type != null) {
                    switch (type.getType()) {
                        case TypeComponent.COL_ENEMY:
                            //do player hit enemy thing
                            Gdx.app.log(TAG, "Enemy");

                            break;
                        case TypeComponent.COL_PLAYER:
                            Gdx.app.log(TAG, "Player");

                            break;

                        case TypeComponent.COL_LEVEL:
                            Gdx.app.log(TAG, "Level");
                            break;

                       /* case TypeComponent.COL_ENEMY_WEAPON:
                            Gdx.app.log(TAG, "Enemy Weapon");
                            break;*/

                        case TypeComponent.COL_FRIENDLY:
                            Gdx.app.log(TAG, "Friendly");
                            break;

                        case TypeComponent.COL_WEAPON:
                            Gdx.app.log(TAG, "Weapon item");
                            break;

                      /*  case TypeComponent.COL_POTION:
                            Gdx.app.log(TAG, "Potion item");
                            break;

                        case TypeComponent.COL_DRAGOONS:
                            Gdx.app.log(TAG, "dragoons");
                            break;

                        case TypeComponent.COL_TOUCH:
                            Gdx.app.log(TAG, "Touch Location");

                            break;
                        case TypeComponent.COL_PORTAL:
                            Gdx.app.log(TAG, "Portal");
                            break;
*/


                        case TypeComponent.COL_OTHER:
                            //do player hit other thing
                            Gdx.app.log(TAG,"Other");
                            break;

                        default:
                            Gdx.app.log(TAG,"Unclear");
                    }

                }
            }
        }

        collision.setCollisionEntity(null);


    }

    @Override
    public void addObserver(StatusObserver observer) {
        observers.add(observer);

    }

    @Override
    public void removeObserver(StatusObserver observer) {
        observers.removeValue(observer,true);

    }

    @Override
    public void removeAllObservers() {
        for(StatusObserver observer: observers){
            observers.removeValue(observer,true);
        }

    }

    @Override
    public void notify(StatusObserver.StatusEvent event, int value) {
        for(StatusObserver observer: observers){
            observer.onNotify(event, value);
        }

    }

}
