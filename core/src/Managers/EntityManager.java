package Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Queue;


import java.util.LinkedList;
import java.util.Random;

import Components.AnimationComponent;
import Components.BodyComponent;
import Components.CollisionComponent;
import Components.DataComponent;
import Components.EnemyComponent;
import Components.InteractiveComponent;
import Components.NPCComponent;
import Components.ParticleEffectComponent;
import Components.PlayerComponent;
import Components.PlayerControlComponent;
import Components.PositionComponent;
import Components.ProjectileComponent;
import Components.RenderableComponent;
import Components.SoundComponent;
import Components.StateComponent;
import Components.SteeringComponent;
import Components.TextureComponent;
import Components.TouchComponent;
import Components.TypeComponent;
import Helpers.BodyGenerator;
import Helpers.Figures;
import Helpers.Mappers;
import edu.fullsail.gabriel_walters.RedBeardRun;

public class EntityManager {

    private static final String TAG = EntityManager.class.getSimpleName();
    private RedBeardRun game;

    private World world;
    private SpriteBatch batch;
    private PooledEngine engine;
    private BodyGenerator generator;
    private TextureAtlas atlas;
    private MyAssetManager assetManager;
    private ParticleEffectsManager particleEffectsManager;

    //Animations
    private Animation redBeardDown;
    private Animation redBeardUp;
    private Animation redBeardLeft;
    private Animation redBeardRight;

    private Animation rovingNPCUp;
    private Animation rovingNPCDown;
    private Animation rovingNPCLeft;
    private Animation rovingNPCRight;
    private Animation rovingNPCTalking;
    private Animation rovingNPCLeaving;

    private Animation skeletonUp;
    private Animation skeletonDown;
    private Animation skeletonRight;
    private Animation skeletonLeft;
    private Animation skeletonHit;
    private Animation skeletonLeaving;

    private Animation snakeUp;
    private Animation snakeDown;
    private Animation snakeRight;
    private Animation snakeLeft;
    private Animation snakeHit;
    private Animation snakeLeaving;
    private Animation orb;

    private Animation staticNPCTalking;
    private Animation staticNPCLeaving;
    private Animation staticNPCIdle;
    private TextureRegion plant1;
    private TextureRegion plant2;
    private TextureRegion heart;
    private TextureRegion dragoon10;
    private TextureRegion dragoon25;
    private TextureRegion potion;
    private TextureRegion liteSwordUp;
    private TextureRegion liteSwordDown;
    private TextureRegion liteSwordRight;
    private TextureRegion liteSwordLeft;
    private TextureRegion heavySwordUp;
    private TextureRegion heavySwordDown;
    private TextureRegion heavySwordRight;
    private TextureRegion heavySwordLeft;
    private TextureRegion fireball;
    private TextureRegion bricks;

    private Animation arrowUp;
    private Animation arrowDown;
    private Animation arrowRight;
    private Animation arrowLeft;

    private TextureRegion bow;


    //Dragon
    private Animation dargonEntering;
    private Animation dargonLeaving;
    private Animation dargonHit;
    private Animation dargonPrepareToAttack;
    private Animation dargonAttack;
    private Animation dargonPush;
    private EnemyComponent dragonEnemyComponent;


    private Queue<Entity> touchPoints;
    private int maxEntities;
    private int currentEntityCount;
    private Array<Entity> attachedEntities;



    private static Entity target;
    private static Entity player;

    public EntityManager(RedBeardRun redBeardRun, World world, MyAssetManager assetManager, SpriteBatch batch, PooledEngine engine) {
        this.game = redBeardRun;
        this.world = world;
        this.batch = batch;
        this.engine = engine;
        generator = new BodyGenerator(world);
        this.assetManager = assetManager;
        particleEffectsManager = new ParticleEffectsManager();
        touchPoints = new Queue<Entity>();
        attachedEntities = new Array<>();
        atlas = assetManager.getTextureAsset("sprites/Output/RedBeardTest.atlas");


        particleEffectsManager.createPrototype(assetManager.getParticleAsset("ParticleEffects/fire.party"),ParticleEffectsManager.FIRE,1/4f);
       /* particleEffectsManager.addParticleEffect(ParticleEffectsManager.FIRE,
                assetManager.getParticleAsset("ParticleEffects/fire.party"),1/32f,3,10);*/

        particleEffectsManager.createPrototype(assetManager.getParticleAsset("ParticleEffects/smoke.party"),ParticleEffectsManager.SMOKE,1/4f);
        /*particleEffectsManager.addParticleEffect(ParticleEffectsManager.SMOKE,
                assetManager.getParticleAsset("ParticleEffects/smoke.party"),1/128f);*/

        particleEffectsManager.addParticleEffect(ParticleEffectsManager.FIRE);
        particleEffectsManager.addParticleEffect(ParticleEffectsManager.SMOKE);

        fireball = atlas.findRegion("Fireball");
        plant1 = atlas.findRegion("plant1");
        plant2 = atlas.findRegion("plant2");
        heart = atlas.findRegion("heart");
        dragoon10 = atlas.findRegion("Dragoon10");
        dragoon25 = atlas.findRegion("Dragoon25");
        potion = atlas.findRegion("potion");
        liteSwordUp = atlas.findRegion("liteSwordUp");
        liteSwordDown = atlas.findRegion("liteSwordDown");
        liteSwordLeft = atlas.findRegion("liteSwordLeft");
        liteSwordRight = atlas.findRegion("liteSwordRight");
        heavySwordUp = atlas.findRegion("heavySwordUp");
        heavySwordDown = atlas.findRegion("heavySwordDown");
        heavySwordLeft = atlas.findRegion("heavySwordLeft");
        heavySwordRight = atlas.findRegion("heavySwordRight");
        bow = atlas.findRegion("Bow");
        bricks = atlas.findRegion("Bricks");


        dargonEntering = new Animation(.10f,atlas.findRegions("DargonEntrance"));
        dargonPush = new Animation(.10f,atlas.findRegions("DargonPush"));
        dargonPrepareToAttack = new Animation(.08f,atlas.findRegions("DargonPrepareToAttack"));
        dargonAttack = new Animation(.10f,atlas.findRegions("DargonAttack"));
        dargonHit = new Animation(.20f,atlas.findRegions("DargonHit"));
        dargonLeaving = new Animation(.25f,atlas.findRegions("DargonLeaving"));

        arrowDown = new Animation(.20f, atlas.findRegions("ArrowDown"));
        arrowUp = new Animation(.20f, atlas.findRegions("ArrowUp"));
        arrowLeft = new Animation(.20f, atlas.findRegions("ArrowLeft"));
        arrowRight = new Animation(.20f, atlas.findRegions("ArrowRight"));


        orb = new Animation(0.1f,atlas.findRegions("Orb"));

        redBeardDown = new Animation(0.25f,atlas.findRegions("RedBeardDown"));
        redBeardUp = new Animation(0.25f,atlas.findRegions("RedBeardUp"));
        redBeardLeft = new Animation(0.25f,atlas.findRegions("RedBeardLeft"));
        redBeardRight = new Animation(0.25f,atlas.findRegions("RedBeardRight"));

        rovingNPCDown = new Animation(0.25f,atlas.findRegions("RovingNPCDown"));
        rovingNPCUp = new Animation(0.25f,atlas.findRegions("RovingNPCUp"));
        rovingNPCLeft = new Animation(0.25f,atlas.findRegions("RovingNPCLeft"));
        rovingNPCRight = new Animation(0.25f,atlas.findRegions("RovingNPCRight"));
        rovingNPCTalking = new Animation(0.25f,atlas.findRegions("RovingNPCTalking"));
        rovingNPCLeaving = new Animation(0.25f,atlas.findRegions("RovingNPCLeaving"));

        skeletonDown = new Animation(0.25f,atlas.findRegions("SkeletonDown"));
        skeletonUp = new Animation(0.25f,atlas.findRegions("SkeletonUp"));
        skeletonLeft = new Animation(0.25f,atlas.findRegions("SkeletonLeft"));
        skeletonRight = new Animation(0.25f,atlas.findRegions("SkeletonRight"));
        skeletonLeaving = new Animation(0.25f,atlas.findRegions("SkeletonLeaving"));
        skeletonHit = new Animation(.25f,atlas.findRegions("SkeletonHit"));

        snakeDown = new Animation(0.25f,atlas.findRegions("SnakeDown"));
        snakeUp = new Animation(0.25f,atlas.findRegions("SnakeUp"));
        snakeLeft = new Animation(0.25f,atlas.findRegions("SnakeLeft"));
        snakeRight = new Animation(0.25f,atlas.findRegions("SnakeRight"));
        snakeLeaving = new Animation(0.25f,atlas.findRegions("SnakeLeaving"));
        snakeHit = new Animation(.25f,atlas.findRegions("SnakeHit"));

        staticNPCLeaving = new Animation(0.25f,atlas.findRegions("StaticNPCLeaving"));
        staticNPCTalking = new Animation(0.25f,atlas.findRegions("StaticNPCTalking"));
        staticNPCIdle = new Animation(0.25f,atlas.findRegion("StaticNPCTalking"));

    }



    public void spawnGroup(TiledMap map, float groupNumber){

        currentEntityCount = 0;
        for(MapObject object:  map.getLayers().get("MAP_SPAWN_LAYER").getObjects()){
            String entityName = object.getProperties().get("Spawn", String.class);
            int spawnPointX = object.getProperties().get("x", Float.class).intValue();
            int spawnPointY = object.getProperties().get("y", Float.class).intValue();

            float group = object.getProperties().get("group", Float.class);
            // = object.getProperties().get("Type", String.class);
            short type;
            if(entityName.equalsIgnoreCase("Snake")||entityName.equalsIgnoreCase("Skeleton")||entityName.equalsIgnoreCase("Dragon")){
                type = TypeComponent.COL_ENEMY;
            }
            else if(entityName.equalsIgnoreCase("RovingNPC")||entityName.equalsIgnoreCase("StaticNPC")){
                type = TypeComponent.COL_FRIENDLY;
            }

            else if(entityName.equalsIgnoreCase("Player")){
                type = TypeComponent.COL_PLAYER;
                // Gdx.app.log("Type Component is set correctly","");
            }
            else if(entityName.equalsIgnoreCase("Plant1")||entityName.equalsIgnoreCase("Plant2")||
                    entityName.equalsIgnoreCase("Potion")||entityName.equalsIgnoreCase("Heart")||
                    entityName.equalsIgnoreCase("LSword")||entityName.equalsIgnoreCase("HSword")||
                    entityName.equalsIgnoreCase("Bow")||entityName.equalsIgnoreCase("Orb")||
                    entityName.equalsIgnoreCase("Touch")||entityName.equalsIgnoreCase("Dragoon10")||
                    entityName.equalsIgnoreCase("Dragoon25")){

                type = TypeComponent.COL_INTERACTIVE;
            }

            else if(entityName.equalsIgnoreCase("Bricks")&&groupNumber ==27){
                type = TypeComponent.COL_INTERACTIVE;
                attachedEntities.add(spawnEntity(entityName, groupNumber, type, spawnPointX, spawnPointY));
                Gdx.app.log("Entity Manager","current attached entities size: "+ attachedEntities.size);
            }


            else if(entityName.equalsIgnoreCase("LiteSword")||entityName.equalsIgnoreCase("HeavySword")||entityName.equalsIgnoreCase("Arrow")){
                type = TypeComponent.COL_WEAPON;
            }


            else{
                type = TypeComponent.COL_OTHER;
            }
            if(groupNumber == group&&!entityName.equalsIgnoreCase("Bricks")) {
                // Gdx.app.log("Spawn Player",""+(groupNumber==group));
                spawnEntity(entityName, groupNumber, type, spawnPointX, spawnPointY);
                currentEntityCount++;


            }

        }
        if(dragonEnemyComponent != null) {
            dragonEnemyComponent.setAttachedEntities(attachedEntities);
            Gdx.app.log(TAG, "dragon enemy component: "+ dragonEnemyComponent.getAttachedEntities().size);
        }
       // Gdx.app.log(TAG, "Current Entity count for Area: "+ currentEntityCount);
    }

    public Entity createProjectile(String entityName, short type,  int x, int y, float xVel, float yVel){
        Entity entity = engine.createEntity();
        //addBodyComponent(entity, entityName, type,  x,  y,xVel,yVel);
        addProjectileBodyComponent(entity, entityName, type,  x,  y,xVel,yVel);
        addPositionComponent(entity,x, y );
        addTypeComponent(entity, type);
        //addTextureComponent(entity,entityName);
        addCollisionComponent(entity);
      //  addRenderableComponent(entity,entityName);
        engine.addEntity(entity);
        return entity;
    }

    public Entity makeParticleEffect( int type, float x, float y){
        Entity entity = engine.createEntity();
        ParticleEffectComponent particleEffectComponent = engine.createComponent(ParticleEffectComponent.class);
        particleEffectComponent.setParticleEffect(particleEffectsManager.getPooledParticleEffect(type));
        particleEffectComponent.getParticleEffect().setPosition(x,y);
        entity.add(particleEffectComponent);
        engine.addEntity(entity);
        return entity;
    }

    public Entity makeParticleEffect(Entity entity, int type, BodyComponent body){
        makeParticleEffect(entity, type, body,0,0);
        return entity;
    }

    public Entity makeParticleEffect(Entity entity, int type, BodyComponent body, float xOffset, float yOffset ){
       // Entity entity = engine.createEntity();
        ParticleEffectComponent particle = engine.createComponent(ParticleEffectComponent.class);
        particle.setParticleEffect(particleEffectsManager.getPooledParticleEffect(type));
        particle.getParticleEffect().setPosition(body.getBody().getPosition().x * Figures.PPM,
                body.getBody().getPosition().y * Figures.PPM);
        //particle.getParticleEffect().getEmitters().first().setAttached(true);
        particle.setxOffset(xOffset);
        particle.setyOffset(yOffset);
        particle.setAttached(true);
        particle.setAttachedBody(body.getBody());
        entity.add(particle);
        //engine.addEntity(entity);
        return entity;
    }

    public Entity spawnTouch(short type,int x, int y){
        Entity entity = engine.createEntity();

        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        typeComponent.setType(type);

        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        positionComponent.setX(x);
        positionComponent.setY(y);

        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        FixtureDef fdef = new FixtureDef();

        fdef.isSensor = true;
        fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
        fdef.filter.maskBits = TypeComponent.COL_PLAYER;

        bodyComponent.setBody(generator.generateBody(entity,
                x, y, 0, fdef));

        bodyComponent.setActive(false);
        bodyComponent.getBody().setUserData(entity);

        SteeringComponent steeringComponent = engine.createComponent(SteeringComponent.class);
        steeringComponent.setBody(bodyComponent.getBody());
        steeringComponent.steeringBehavior = null;
        steeringComponent.currentMode = SteeringComponent.SteeringState.NONE;

        InteractiveComponent interactiveComponent = engine.createComponent(InteractiveComponent.class);
        interactiveComponent.setType(InteractiveComponent.TYPE.TOUCH);

        // addDataComponent(entity, entityName, groupNumber);
        // addTouchComponent(entity);
        //addInteractiveComponent(entity, type, entityName);

        entity.add(typeComponent);
        entity.add(positionComponent);
        entity.add(bodyComponent);
        entity.add(steeringComponent);
        entity.add(interactiveComponent);

        return entity;
    }

    public Entity spawnEntity(String entityName, float groupNumber, short type, int x, int y){
       // Gdx.app.log(TAG,"SPawn ENTITY: "+ entityName+" "+ groupNumber+" "+ type );
        Entity entity = engine.createEntity();
        //todo change to the entityName instead of the type
        switch(entityName){
            case "Skeleton":
            case "Snake":
            case "Dragon":
                addAnimationComponent(entity, entityName);
                addStateComponent(entity, entityName);
                addCollisionComponent(entity);
                addRenderableComponent(entity, entityName);
                addTextureComponent(entity, entityName);
                addTypeComponent(entity, type);
                addPositionComponent(entity, x, y);
                addBodyComponent(entity, entityName, type, x, y,0,0);
                addDataComponent(entity, entityName, groupNumber);
                addEnemyComponent(entity, entityName, type);
                addSoundComponent(entity);
                break;
            case "StaticNPC":
            case "RovingNPC":
                addAnimationComponent(entity, entityName);
                addStateComponent(entity, entityName);
                addCollisionComponent(entity);
                addRenderableComponent(entity, entityName);
                addTextureComponent(entity, entityName);
                addTypeComponent(entity, type);
                addPositionComponent(entity, x, y);
                addBodyComponent(entity, entityName, type, x, y,0,0);
                addDataComponent(entity, entityName, groupNumber);
                addNPCComponent(entity, entityName, type);
                addSoundComponent(entity);
                break;
            case "Dragoon10":
            case "Dragoon25":
            case "Potion":
            case "Heart":
            case "Plant1":
            case "Plant2":
            case "LSword":
            case "HSword":
            case "Bow":
            case "Bricks":
                addInteractiveComponent(entity, type, entityName);
                addRenderableComponent(entity, entityName);
                addTextureComponent(entity, entityName);
                addTypeComponent(entity, type);
                addPositionComponent(entity, x, y);
                addBodyComponent(entity, entityName, type, x, y,0,0);
                addDataComponent(entity, entityName, groupNumber);
                break;
            case "Arrow":
            case "Orb":
                addAnimationComponent(entity, entityName);
                addStateComponent(entity,entityName);
                addInteractiveComponent(entity, type, entityName);
                addRenderableComponent(entity, entityName);
                addTextureComponent(entity, entityName);
                addTypeComponent(entity, type);
                addPositionComponent(entity, x, y);
                addBodyComponent(entity, entityName, type, x, y,0,0);
                addDataComponent(entity, entityName, groupNumber);
                break;


            case "LiteSword":
            case "HeavySword":
            case "HeavySword360":
                addTypeComponent(entity,type);
                addPositionComponent(entity,x,y);
                addBodyComponent(entity,entityName,type,x,y,0,0);
                addRenderableComponent(entity,entityName);
                addTextureComponent(entity,entityName);
                addDataComponent(entity, entityName,groupNumber);
                addCollisionComponent(entity);
                addSoundComponent(entity);

                addInteractiveComponent(entity,type, entityName);
                break;
            case "Touch":
                addTypeComponent(entity, type);
                addPositionComponent(entity, x, y);
                addBodyComponent(entity, entityName, type, x, y,0,0);
                addDataComponent(entity, entityName, groupNumber);
                addTouchComponent(entity);
                addInteractiveComponent(entity, type, entityName);
                break;
            case "Player":
                addAnimationComponent(entity, entityName);
                addStateComponent(entity, entityName);
                addCollisionComponent(entity);
                addRenderableComponent(entity, entityName);
                addTextureComponent(entity, entityName);
                addTypeComponent(entity, type);
                addPositionComponent(entity, x, y);
                addBodyComponent(entity, entityName, type, x, y,0,0);
                addDataComponent(entity, entityName, groupNumber);
                addPlayerComponent(entity);
                addPlayerControlComponent(entity);
                addSoundComponent(entity);
                break;


        }



        engine.addEntity(entity);
        return entity;

    }



    private Entity addAnimationComponent(Entity entity, String entityName){
        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);

        switch (entityName){
            case "RovingNPC":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.DOWN, rovingNPCDown);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.UP, rovingNPCUp);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEFT, rovingNPCLeft);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT, rovingNPCRight);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.TALKING, rovingNPCTalking);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEAVING, rovingNPCLeaving);
                break;
            case "StaticNPC":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.TALKING, staticNPCTalking);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEAVING, staticNPCLeaving);
                //animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.DOWN,staticNPCIdle);
                break;
            case "Skeleton":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.DOWN, skeletonDown);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.UP, skeletonUp);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEFT, skeletonLeft);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT, skeletonRight);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEAVING, skeletonLeaving);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.HIT,skeletonHit);
                break;
            case "Snake":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.DOWN, snakeDown);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.UP, snakeUp);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEFT, snakeLeft);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT, snakeRight);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEAVING, snakeLeaving);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.HIT,snakeHit);
                break;
            case "Dragon":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.ENTERING, dargonEntering);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.PREPARING_TO_ATTACKING, dargonPrepareToAttack);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.ATTACKING, dargonAttack);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.HIT, dargonHit);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.FORCE_PUSH, dargonPush);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEAVING, dargonLeaving);

                break;
            case "Player":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.DOWN, redBeardDown);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.UP, redBeardUp);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEFT, redBeardLeft);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT, redBeardRight);
                break;
            case "Arrow":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.DOWN, arrowDown);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.UP, arrowUp);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.LEFT, arrowLeft);
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT, arrowRight);
            case "Orb":
                animationComponent.addAnimation(AnimationComponent.ANIMATIONSTATE.IMMOBILE,orb);
                break;
        }


        //  Gdx.app.log(TAG,"AnimationComponent "+ !(animationComponent==null));
        entity.add(animationComponent);
        return entity;

    }

    private Entity addStateComponent(Entity entity,String entityName ){
        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        StateComponent state;
        switch(entityName){
            case "StaticNPC":
                stateComponent.setDirection(StateComponent.DIRECTION.NONE);
                stateComponent.setState(StateComponent.STATE.MOVING);
                break;
            case "Orb":
                stateComponent.setDirection(StateComponent.DIRECTION.NONE);
                stateComponent.setState(StateComponent.STATE.IMMOBILE);
                break;
            case "Dragon":
                stateComponent.setDirection(StateComponent.DIRECTION.NONE);
                stateComponent.setState(StateComponent.STATE.IDLE);
                break;
            case "Arrow":
                stateComponent.setDirection(StateComponent.DIRECTION.NONE);
                state = Mappers.state.get(player);
                switch(state.getDirection()) {
                    case LEFT:
                        stateComponent.setDirection(StateComponent.DIRECTION.LEFT);
                        stateComponent.setState(StateComponent.STATE.MOVING);
                        break;
                    case DOWN:
                        stateComponent.setDirection(StateComponent.DIRECTION.DOWN);
                        stateComponent.setState(StateComponent.STATE.MOVING);
                        break;
                    case RIGHT:
                        stateComponent.setDirection(StateComponent.DIRECTION.RIGHT);
                        stateComponent.setState(StateComponent.STATE.MOVING);
                        break;
                    case UP:
                        stateComponent.setDirection(StateComponent.DIRECTION.UP);
                        stateComponent.setState(StateComponent.STATE.MOVING);
                        break;
                }
                break;




            default:
                stateComponent.setDirection(StateComponent.DIRECTION.DOWN);
                stateComponent.setState(StateComponent.STATE.IDLE);
                break;

        }


        // Gdx.app.log(TAG,"StateComponent "+ !(stateComponent==null));
        entity.add(stateComponent);
        return entity;
    }

    private Entity addRenderableComponent(Entity entity, String entityName){
        RenderableComponent renderableComponent = engine.createComponent(RenderableComponent.class);
        entity.add(renderableComponent);
        return entity;
    }

    private Entity addTextureComponent(Entity entity, String entityName){
        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        // Gdx.app.log(TAG, "Texture: "+ entityName + " "+ (entityName.equalsIgnoreCase("Dragoon10")));
        float random = MathUtils.random(100);
        StateComponent state;
        switch(entityName){
            case "StaticNPC":
                textureComponent.setRegion(((TextureRegion) staticNPCIdle.getKeyFrames()[0]));
                break;
            case "RovingNPC":
                textureComponent.setRegion((TextureRegion)rovingNPCDown.getKeyFrames()[0]);
                break;
            case "Skeleton":
                textureComponent.setRegion((TextureRegion)skeletonDown.getKeyFrames()[0]);
                break;
            case "Snake":
                textureComponent.setRegion((TextureRegion)snakeDown.getKeyFrames()[0]);
                break;
            case "Dragon":
                textureComponent.setRegion((TextureRegion)dargonEntering.getKeyFrames()[0]);
                break;
            case "Plant1":
                //float random = MathUtils.random(100);
                if(random<50) {
                    textureComponent.setRegion(plant1);
                }
                else{
                    plant1.flip(true,false);
                    textureComponent.setRegion(plant1);
                }
                break;
            case "Plant2":
                //float random = MathUtils.random(100);
                if(random<50) {
                    textureComponent.setRegion(plant2);
                }
                else {
                    plant2.flip(true, false);
                    textureComponent.setRegion(plant2);
                }
                break;
            case "Bricks":
                textureComponent.setRegion(bricks);
                break;
            case "Dragoon10":
                textureComponent.setRegion(dragoon10);
                break;
            case "Dragoon25":
                textureComponent.setRegion(dragoon25);
                break;
            case "Potion":
                textureComponent.setRegion(potion);
                break;
            case "Heart":
                textureComponent.setRegion(heart);
                break;
            case "LSword":
                textureComponent.setRegion(liteSwordDown);
                break;
            case "HSword":

                textureComponent.setRegion(heavySwordDown);
                break;
            case "HeavySword":
            case "HeavySword360":
                state = Mappers.state.get(player);
                switch(state.getDirection()){
                    case LEFT:
                        textureComponent.setRegion(heavySwordLeft);
                        break;
                    case DOWN:
                        textureComponent.setRegion(heavySwordDown);
                        break;
                    case RIGHT:
                        textureComponent.setRegion(heavySwordRight);
                        break;
                    case UP:
                        textureComponent.setRegion(heavySwordUp);
                        break;
                    case NONE:
                        break;
                }
                break;

            case "LiteSword":
                state = Mappers.state.get(player);
                switch(state.getDirection()){
                    case LEFT:
                        textureComponent.setRegion(liteSwordLeft);
                        break;
                    case DOWN:
                        textureComponent.setRegion(liteSwordDown);
                        break;
                    case RIGHT:
                        textureComponent.setRegion(liteSwordRight);
                        break;
                    case UP:
                        textureComponent.setRegion(liteSwordUp);
                        break;
                    case NONE:
                        break;
                }
                break;

            case "Orb":
                textureComponent.setRegion(((TextureRegion) orb.getKeyFrames()[0]));
                break;
            case "Player":
                textureComponent.setRegion((TextureRegion)redBeardDown.getKeyFrames()[0]);

                break;
            case "Bow":
                textureComponent.setRegion(bow);
                break;
            case "Arrow":
                state = Mappers.state.get(player);
                switch(state.getDirection()){
                    case LEFT:
                        textureComponent.setRegion((TextureRegion) arrowLeft.getKeyFrames()[0]);
                        break;
                    case DOWN:
                        textureComponent.setRegion((TextureRegion) arrowDown.getKeyFrames()[0]);
                        break;
                    case RIGHT:
                        textureComponent.setRegion((TextureRegion) arrowRight.getKeyFrames()[0]);
                        break;
                    case UP:
                        textureComponent.setRegion((TextureRegion) arrowUp.getKeyFrames()[0]);
                        break;
                    case NONE:
                        break;
                }
                break;
            case "Fireball":
                textureComponent.setRegion(fireball);
                break;

        }

        // Gdx.app.log(TAG,"TextureComponent "+ !(textureComponent==null));
        entity.add(textureComponent);
        return entity;
    }

    private Entity addDataComponent(Entity entity, String entityName, float groupNumber){
        DataComponent dataComponent = engine.createComponent(DataComponent.class);
        dataComponent.setData("group", groupNumber);
        entity.add(dataComponent);

        return entity;
    }

    private Entity addPositionComponent(Entity entity, int x, int y){
        PositionComponent positionComponent = engine.createComponent(PositionComponent.class);
        positionComponent.setX(x);
        positionComponent.setY(y);
        entity.add(positionComponent);
        // Gdx.app.log(TAG,"PositionComponent "+ !(positionComponent==null)+"X: "+ x+ "Y: "+ y);
        return entity;
    }

    private Entity addTypeComponent(Entity entity, short type){
        TypeComponent typeComponent = engine.createComponent(TypeComponent.class);
        typeComponent.setType(type);
        entity.add(typeComponent);
        return entity;
    }

    private Entity addCollisionComponent(Entity entity){
        CollisionComponent collisionComponent = engine.createComponent(CollisionComponent.class);
        entity.add(collisionComponent);
        return entity;
    }

    private Entity addEnemyComponent(Entity entity, String entityName, short type){
        EnemyComponent enemyComponent = engine.createComponent(EnemyComponent.class);
        switch (entityName){
            case "Snake":
                enemyComponent.setType(EnemyComponent.TYPE.SNAKE);
                enemyComponent.setHealth(30);
                enemyComponent.setMaxHealth(30);
                enemyComponent.setAggressive(0);
                enemyComponent.setMaxAggressive(15);
                enemyComponent.setBored(0);
                break;
            case  "Skeleton":
                enemyComponent.setType(EnemyComponent.TYPE.SKELETON);
                enemyComponent.setHealth(60);
                enemyComponent.setMaxHealth(60);
                enemyComponent.setAggressive(0);
                enemyComponent.setMaxAggressive(20);
                enemyComponent.setBored(0);
                break;
            case "Dragon":
                dragonEnemyComponent = enemyComponent;
                enemyComponent.setType(EnemyComponent.TYPE.DRAGON);
                enemyComponent.setHealth(120);
                enemyComponent.setMaxHealth(120);
                enemyComponent.setAggressive(5);
                enemyComponent.setMaxAggressive(30);
                enemyComponent.setBored(0);
               // enemyComponent.setAttachedEntities(attachedEntities);
                break;


        }



        //Gdx.app.log(TAG,"EnemyComponent "+ !(enemyComponent==null));
        entity.add(enemyComponent);
        return entity;

    }

    private Entity addNPCComponent(Entity entity, String entityName, short type){
        NPCComponent npcComponent = engine.createComponent(NPCComponent.class);
        if(entityName.equalsIgnoreCase("RovingNPC")){
            npcComponent.setType(NPCComponent.TYPE.ROVING);
            npcComponent.setHealth(20);
            npcComponent.setMaxHealth(20);
        }
        else{
            npcComponent.setType(NPCComponent.TYPE.STATIC);
            npcComponent.setHealth(20);
            npcComponent.setMaxHealth(20);
        }
        // Gdx.app.log(TAG,"NPCComponent "+ !(npcComponent==null));
        entity.add(npcComponent);
        return entity;
    }

    private Entity addPlayerControlComponent(Entity entity){
        PlayerControlComponent touchControl = engine.createComponent(PlayerControlComponent.class);
        touchControl.setControl(PlayerControlComponent.CONTROL.NONE);
        // Gdx.app.log(TAG,"PlayerControlComponent "+ !(touchControl==null));
        entity.add(touchControl);
        return entity;


    }

    private Entity addPlayerComponent(Entity entity){
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        entity.add(playerComponent);
        //Gdx.app.log(TAG,"PlayerComponent "+ !(playerComponent==null));
        if(!game.newGame) {
            playerComponent.setHealth(game.loadData.getFloat("health", 120));
            playerComponent.setPastGroup(game.loadData.getFloat("pastGroup", 0f));
            playerComponent.setCurrentGroup(game.loadData.getFloat("currentGroup", 14f));
            playerComponent.setCameraX(game.loadData.getFloat("cameraX", 128f));
            playerComponent.setCameraY(game.loadData.getFloat("cameraY", 264f));
            playerComponent.setX(game.loadData.getFloat("X", 128f));
            playerComponent.setY(game.loadData.getFloat("Y", 264f));
            playerComponent.setDragoons(game.loadData.getFloat("dragoons", 0f));

            if (game.loadData.getBoolean("heavySword", false)) {
                playerComponent.addWeapon(InteractiveComponent.TYPE.HSWORD);
            }
            else if (game.loadData.getBoolean("liteSword", false)) {
                playerComponent.addWeapon(InteractiveComponent.TYPE.LSWORD);
            }

            if (game.loadData.getBoolean("bow", false)) {
                playerComponent.addWeapon(InteractiveComponent.TYPE.BOW);
            }

            playerComponent.setPotions(game.loadData.getFloat("potions", 0f));
            playerComponent.setFirstTime(game.loadData.getBoolean("firstTime",true));
        }
        setPlayer(entity);
        return entity;
    }

    private Entity addSoundComponent(Entity entity){
        SoundComponent soundComponent = engine.createComponent(SoundComponent.class);
        entity.add(soundComponent);
        return entity;
    }



    private Entity addProjectileComponent(Entity entity, BodyComponent body, float originX, float originY, float xVel, float yVel){
        ProjectileComponent projectile = engine.createComponent(ProjectileComponent.class);
        projectile.setxVel(xVel);
        projectile.setyVel(yVel);
        projectile.setOriginX(originX);
        projectile.setOriginY(originY);

        projectile.setParticleEffect(makeParticleEffect(entity,ParticleEffectsManager.FIRE,body));
        entity.add(projectile);
        return entity;
    }

    private Entity addTouchComponent(Entity entity){
        TouchComponent touch = engine.createComponent(TouchComponent.class);
        entity.add(touch);
        return entity;
    }

    private Entity addProjectileBodyComponent(Entity entity, String entityName, short type, int x, int y, float xVel, float yVel) {
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        FixtureDef fdef = new FixtureDef();

        fdef.filter.categoryBits = TypeComponent.COL_WEAPON;
        fdef.filter.maskBits = TypeComponent.COL_PLAYER;
        fdef.isSensor = true;

        bodyComponent.setBody(generator.generateBody(entity, x, y, 8, fdef));
        bodyComponent.getBody().setBullet(true);
        bodyComponent.setActive(true);
        bodyComponent.getBody().setUserData(entity);
        entity.add(bodyComponent);
        addProjectileComponent(entity, bodyComponent, x, y, xVel, yVel);

        return entity;
    }


    private Entity addBodyComponent(Entity entity, String entityName, short type, int x, int y, float xVel, float yVel) {
        BodyComponent bodyComponent = engine.createComponent(BodyComponent.class);
        FixtureDef fdef = new FixtureDef();
        PrismaticJointDef jointDef;
        Joint joint;
        StateComponent state;
        Body playerBody;
        MassData mass;

        switch(entityName){
            case "StaticNPC":
                fdef.filter.categoryBits = TypeComponent.COL_FRIENDLY;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER | TypeComponent.COL_WEAPON | TypeComponent.COL_ENEMY;

                bodyComponent.setBody(generator.generateBody(entity, x, y,
                        16, Gdx.files.internal("scripts/PlayerBody.json"), fdef));

                bodyComponent.setActive(true);

                bodyComponent.getBody().setLinearDamping(3f);
                bodyComponent.getBody().setUserData(entity);
                //Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);



                break;
            case "RovingNPC":
                fdef.filter.categoryBits = TypeComponent.COL_FRIENDLY;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER | TypeComponent.COL_WEAPON | TypeComponent.COL_ENEMY;

                bodyComponent.setBody(generator.generateBody(entity, x, y,
                        16, Gdx.files.internal("scripts/PlayerBody.json"), fdef));

                bodyComponent.setActive(true);

                bodyComponent.getBody().setLinearDamping(3f);
                bodyComponent.getBody().setUserData(entity);
                //Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);


                addSteeringComponent(entity, bodyComponent);


                break;
            case "Skeleton":
            case "Snake":
                fdef.filter.categoryBits = TypeComponent.COL_ENEMY;
                fdef.filter.maskBits =
                        TypeComponent.COL_ENEMY |
                                TypeComponent.COL_FRIENDLY |
                                TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER |TypeComponent.COL_WEAPON;

                bodyComponent.setBody(generator.generateBody(entity, x, y,
                        16, Gdx.files.internal("scripts/PlayerBody.json"), fdef));

                bodyComponent.setActive(true);

                bodyComponent.getBody().setLinearDamping(3f);
                bodyComponent.getBody().setUserData(entity);
                // Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);

                addSteeringComponent(entity, bodyComponent);

                break;
            case "Dragon":
                fdef.density = 10f;
                fdef.filter.categoryBits = TypeComponent.COL_ENEMY;
                fdef.filter.maskBits =
                        TypeComponent.COL_ENEMY |
                                TypeComponent.COL_FRIENDLY |
                                TypeComponent.COL_PLAYER |TypeComponent.COL_WEAPON;

                fdef.restitution = 0.4f;
                bodyComponent.setBody(generator.generateBody(entity, x, y,
                        16, Gdx.files.internal("scripts/DragonBody.json"), fdef));

                bodyComponent.setActive(true);

                bodyComponent.getBody().setLinearDamping(10f);

                bodyComponent.getBody().setUserData(entity);
                // Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);
                break;
            case "Plant1":
            case "Plant2":
                fdef.isSensor = true;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_WEAPON;
                bodyComponent.setBody(generator.generateBody(entity,
                        x, y, 8, fdef));

                bodyComponent.setActive(true);


                bodyComponent.getBody().setUserData(entity);
                // Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);

                break;
            case "Bricks":
                fdef.isSensor = false;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_PLAYER;
                bodyComponent.setBody(generator.generateBrick(entity,x,y,16,16,0,0,fdef));
                bodyComponent.setActive(true);
                bodyComponent.getBody().setUserData(entity);
                entity.add(bodyComponent);
                break;
            case "Dragoon10":
            case "Dragoon25":
                //fdef.friction = 8f;
                fdef.restitution = .3f;
                fdef.isSensor = true;
                fdef.density = .20f;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER ;
                bodyComponent.setBody(generator.generateBody(entity,
                        x, y,
                        8, fdef));

                bodyComponent.setActive(true);
                bodyComponent.setDead(false);




                bodyComponent.getBody().setUserData(entity);
                //  Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);

                break;
            case "Potion":
                //fdef.friction = 8f;
                fdef.restitution = .3f;
                fdef.isSensor = true;
                fdef.density = .20f;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER | TypeComponent.COL_WEAPON ;
                bodyComponent.setBody(generator.generateBody(entity,
                        x, y,
                        8, fdef));

                bodyComponent.setActive(true);
                bodyComponent.setDead(false);


                bodyComponent.getBody().setUserData(entity);
                // Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);

                break;
            case "Heart":
                //fdef.friction = 8f;
                fdef.restitution = .3f;
                fdef.isSensor = true;
                fdef.density = .20f;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER ;
                bodyComponent.setBody(generator.generateBody(entity,
                        x, y,
                        8, fdef));

                bodyComponent.setActive(true);
                bodyComponent.setDead(false);




                bodyComponent.getBody().setUserData(entity);
                //  Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);

                break;
            case "LSword":
            case "HSword":
            case "Bow":

                //fdef.friction = 8f;
                fdef.restitution = .3f;
                fdef.isSensor = true;
                fdef.density = .20f;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_PLAYER;
                bodyComponent.setBody(generator.generateBody(entity,
                        x, y,
                        8, fdef));

                bodyComponent.setActive(true);
                bodyComponent.setDead(false);


                bodyComponent.getBody().setUserData(entity);
                //  Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);


                break;

            case "LiteSword":
            case "HeavySword":
                fdef.isSensor = true;
                fdef.density = 0f;
                fdef.friction = 0f;
                fdef.filter.categoryBits = TypeComponent.COL_WEAPON;
                fdef.filter.maskBits = TypeComponent.COL_ENEMY |
                        TypeComponent.COL_INTERACTIVE;

                // fdef.restitution = 0.3f;

                jointDef = new PrismaticJointDef();
                jointDef.collideConnected = false;
                jointDef.enableLimit = true;
                jointDef.lowerTranslation = 0;
                jointDef.upperTranslation = 8/ Figures.PPM;
                jointDef.enableMotor = true;
                jointDef.maxMotorForce = 10;
                jointDef.motorSpeed = 10;
                jointDef.localAnchorA.set(0, 0);

                jointDef.localAxisA.set(0, 1);

                mass = new MassData();
                mass.mass = 0f;


                state = Mappers.state.get(player);
                playerBody = Mappers.body.get(player).getBody();
                switch (state.getDirection()) {
                    case NONE:

                        break;
                    case UP:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 8, 16, 0, 8, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAnchorB.set(0, -8/Figures.PPM);


                        break;
                    case RIGHT:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 16, 8, 8, 0, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAnchorB.set(-8/Figures.PPM, 0);
                    /*jointDef.localAnchorA.set(0, 0);

                    jointDef.localAxisA.set(0, 1);*/
                        // jointDef.localAxisA.nor();
                        break;
                    case DOWN:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 8, 16, 0, -8, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAnchorB.set(0,8/Figures.PPM);
                        break;
                    case LEFT:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 16, 8, -8, 0, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAnchorB.set(8/Figures.PPM,0);
                        break;
                }

                jointDef.bodyA = playerBody;
                jointDef.bodyB = bodyComponent.getBody();


                joint = (PrismaticJoint) world.createJoint(jointDef);


                bodyComponent.getBody().setUserData(entity);
                entity.add(bodyComponent);

                break;

            case "HeavySword360":
                //fdef.restitution = .2f;
                fdef.isSensor = true;
                fdef.density = 0f;
                fdef.friction = 0f;
                fdef.filter.categoryBits = TypeComponent.COL_WEAPON;
                fdef.filter.maskBits = TypeComponent.COL_ENEMY |
                        TypeComponent.COL_INTERACTIVE;


                jointDef = new PrismaticJointDef();
                jointDef.collideConnected = false;
                jointDef.enableLimit = true;
                jointDef.lowerTranslation = 0;
                jointDef.upperTranslation = 8/ Figures.PPM;
                jointDef.enableMotor = true;
                jointDef.maxMotorForce = 5;
                jointDef.motorSpeed = 10;

                jointDef.localAnchorA.set(0, 0);

                mass = new MassData();
                mass.mass = 0;



                state = Mappers.state.get(player);
                playerBody = Mappers.body.get(player).getBody();
                switch (state.getDirection()) {
                    case NONE:

                        break;
                    case UP:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 4, 16, 0, 8, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAxisA.set(0, 1);

                        jointDef.localAnchorB.set(0, -8/Figures.PPM);


                        break;
                    case RIGHT:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 16, 4, 8, 0, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAxisA.set(1, 0);
                        jointDef.localAnchorB.set(-8/Figures.PPM, 0);
                    /*jointDef.localAnchorA.set(0, 0);

                    jointDef.localAxisA.set(0, 1);*/
                        // jointDef.localAxisA.nor();
                        break;
                    case DOWN:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 4, 16, 0, -8, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAxisA.set(0, -1);
                        jointDef.localAnchorB.set(0,8/Figures.PPM);
                        break;
                    case LEFT:
                        bodyComponent.setBody(generator.generateSword(entity, x, y, 16, 4, -8, 0, fdef));
                        bodyComponent.getBody().setMassData(mass);
                        jointDef.localAxisA.set(-1, 0);
                        jointDef.localAnchorB.set(8/Figures.PPM,0);
                        break;
                }

                jointDef.bodyA = playerBody;
                jointDef.bodyB = bodyComponent.getBody();


                joint = (PrismaticJoint) world.createJoint(jointDef);


                bodyComponent.getBody().setUserData(entity);
                entity.add(bodyComponent);

                break;

            case "Orb":
                fdef.isSensor = true;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_PLAYER;
                bodyComponent.setBody(generator.generateBody(entity,x,y,16,fdef));
                bodyComponent.setActive(true);
                bodyComponent.getBody().setUserData(entity);
                entity.add(bodyComponent);

                break;
            case "Player":
                int playerX = x;
                int playerY = y;

                if(!game.newGame){

                    playerX=(int) game.loadData.getFloat("X", 128f);
                    playerY=(int) game.loadData.getFloat("Y", 264f);

                    // playerX/=Figures.PPM;
                    //playerY/=Figures.PPM;

                }
                //fdef.density = 3f;
                //fdef.friction = 5f;
                fdef.filter.categoryBits = TypeComponent.COL_PLAYER;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL | TypeComponent.COL_INTERACTIVE
                        | TypeComponent.COL_FRIENDLY |
                        TypeComponent.COL_ENEMY | TypeComponent.COL_WEAPON ;

                bodyComponent.setBody(generator.generateBody(entity, playerX, playerY,
                        16, Gdx.files.internal("scripts/PlayerBody.json"), fdef));

                bodyComponent.setActive(true);

                bodyComponent.getBody().setLinearDamping(3f);
                bodyComponent.getBody().setUserData(entity);
                // Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);
                addSteeringComponent(entity, bodyComponent);

                break;



            case "Arrow":
                fdef.filter.categoryBits = TypeComponent.COL_WEAPON;
                fdef.filter.maskBits = TypeComponent.COL_LEVEL |
                        TypeComponent.COL_ENEMY  ;
                fdef.isSensor = true;

                float impulse = 5;

                state = Mappers.state.get(player);

                switch (state.getDirection()) {
                    case NONE:

                        break;
                    case UP:
                        bodyComponent.setBody(generator.generateArrow(entity, x, y, 2, 16, 0, 0, fdef));
                        bodyComponent.getBody().applyLinearImpulse(0,impulse,bodyComponent.getBody().getLocalCenter().x,bodyComponent.getBody().getLocalCenter().y,true);



                        break;
                    case RIGHT:
                        bodyComponent.setBody(generator.generateArrow(entity, x, y, 16, 2, 0, 0, fdef));
                        bodyComponent.getBody().applyLinearImpulse(impulse,0,bodyComponent.getBody().getLocalCenter().x,bodyComponent.getBody().getLocalCenter().y,true);


                        break;
                    case DOWN:
                        bodyComponent.setBody(generator.generateArrow(entity, x, y, 2, 16, 0, 0, fdef));
                        bodyComponent.getBody().applyLinearImpulse(0,-impulse,bodyComponent.getBody().getLocalCenter().x,bodyComponent.getBody().getLocalCenter().y,true);


                        break;
                    case LEFT:
                        bodyComponent.setBody(generator.generateArrow(entity, x, y, 16, 2, 0, 0, fdef));
                        bodyComponent.getBody().applyLinearImpulse(-impulse,0,bodyComponent.getBody().getLocalCenter().x,bodyComponent.getBody().getLocalCenter().y,true);


                        break;
                }
                bodyComponent.getBody().setUserData(entity);
                entity.add(bodyComponent);

                break;
            case "Touch":
                fdef.isSensor = true;
                fdef.filter.categoryBits = TypeComponent.COL_INTERACTIVE;
                fdef.filter.maskBits = TypeComponent.COL_PLAYER;

                bodyComponent.setBody(generator.generateBody(entity,
                        x, y, 0, fdef));

                bodyComponent.setActive(true);


                bodyComponent.getBody().setUserData(entity);
                // Gdx.app.log(TAG, "bodyComponent " + !(bodyComponent == null));
                entity.add(bodyComponent);

                addSteeringComponent(entity, bodyComponent);
                break;



            default:
                break;

        }







        return entity;


    }

    private Entity addSteeringComponent(Entity entity, BodyComponent bodyComponent){
        SteeringComponent steeringComponent = engine.createComponent(SteeringComponent.class);
        steeringComponent.setBody(bodyComponent.getBody());
        steeringComponent.steeringBehavior = null;
        steeringComponent.currentMode = SteeringComponent.SteeringState.NONE;
        steeringComponent.setMaxLinearSpeed(350);
        steeringComponent.setMaxLinearAcceleration(300);
        //Gdx.app.log(TAG, "Entity Manager: Max Linear Speed: "+ steeringComponent.getMaxLinearSpeed() +" max linear acceleration; "+ steeringComponent.getMaxLinearAcceleration());
        entity.add(steeringComponent);
        // Gdx.app.log(TAG,"SteeringComponent "+ !(steeringComponent==null));
        return entity;
    }

    private Entity addInteractiveComponent(Entity entity, short type, String entityName){
        InteractiveComponent interactiveComponent = engine.createComponent(InteractiveComponent.class);
        entity.add(interactiveComponent);

        switch(entityName){
            case "Potion":
                interactiveComponent.setType(InteractiveComponent.TYPE.POTION);
                break;
            case "Dragoon10":
                interactiveComponent.setType(InteractiveComponent.TYPE.DRAGOON10);
                break;
            case "Dragoon25":
                interactiveComponent.setType(InteractiveComponent.TYPE.DRAGOON25);
                break;
            case "LSword":
                interactiveComponent.setType(InteractiveComponent.TYPE.LSWORD);
                break;
            case "HSword":
                interactiveComponent.setType(InteractiveComponent.TYPE.HSWORD);
                break;
            case "LiteSword":
                interactiveComponent.setType(InteractiveComponent.TYPE.LITESWORD);
                break;
            case "HeavySword":
            case "HeavySword360":
                interactiveComponent.setType(InteractiveComponent.TYPE.HEAVYSWORD);
                break;
            case "Arrow":
                interactiveComponent.setType(InteractiveComponent.TYPE.ARROW);
                break;
            case "Bow":
                interactiveComponent.setType(InteractiveComponent.TYPE.BOW);
                break;
            case "Heart":
                interactiveComponent.setType(InteractiveComponent.TYPE.HEART);
                break;
            case "Touch":
                interactiveComponent.setType(InteractiveComponent.TYPE.TOUCH);
                break;
            case "Portal":
                interactiveComponent.setType(InteractiveComponent.TYPE.PORTAL);
            case "Orb":
                interactiveComponent.setType(InteractiveComponent.TYPE.ORB);
                break;
            case "Plant1":
            case "Plant2":
                interactiveComponent.setType(InteractiveComponent.TYPE.DESTROYABLE);
                break;
            case "Bricks":
                interactiveComponent.setType(InteractiveComponent.TYPE.BRICK);
                break;




        }

        return entity;
    }







    public Queue<Entity> getTouchPoints() {
        return touchPoints;
    }

    public void setTouchPoints(Queue<Entity> touchPoints) {
        this.touchPoints = touchPoints;
    }

    public static Entity getTarget(){
        return target;

    }

    public static Entity getPlayer(){
        if(player == null){
            //  Gdx.app.log(TAG, "PLAYER IS NULL in get");
        }
        return player;

    }

    public static void setTarget(Entity target){
        EntityManager.target = target;
    }

    public void setPlayer(Entity player){
        if(player == null){
            // Gdx.app.log(TAG, "PLAYER IS NULL in set");
        }
        EntityManager.player = player;

    }

    public Array<Entity> getAttachedEntities() {
        return attachedEntities;
    }

    public void dispose(){
        particleEffectsManager.dispose();
        attachedEntities.clear();
    }
}
