package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import Components.AnimationComponent;
import Components.BodyComponent;
import Components.EnemyComponent;
import Components.ParticleEffectComponent;
import Components.ProjectileComponent;
import Components.SoundComponent;
import Components.StateComponent;
import Components.SteeringComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;
import Helpers.SteeringPresets;
import Managers.EntityManager;
import Managers.ParticleEffectsManager;


public class EnemySystem extends IteratingSystem {
    private Entity player;
    private World world;
    private EntityManager entityManager;

    private Timer timer;
    private boolean dragonRoarPlayed = false;
    private boolean createdSmoke = false;

    public EnemySystem(Entity player, World world, Timer timer, EntityManager entityManager) {
        super(Family.all(EnemyComponent.class).get());
        this.player = player;
        this.world = world;
        this.entityManager = entityManager;
        this.timer = timer;

    }

    @Override
    protected void processEntity(final Entity entity, float deltaTime) {
        EnemyComponent enemy = Mappers.enemy.get(entity);
        BodyComponent body = Mappers.body.get(entity);
        BodyComponent playerbody = Mappers.body.get(player);
        SteeringComponent playersteering = Mappers.steering.get(player);
        StateComponent state = Mappers.state.get(entity);
        SteeringComponent steering = Mappers.steering.get(entity);
        SoundComponent sound = Mappers.sound.get(entity);
        AnimationComponent animation = Mappers.animation.get(entity);

        float distance = body.getBody().getPosition().dst(playerbody.getBody().getPosition());
        int health = enemy.getHealth();
        int bored = enemy.getBored();
        int aggressive = enemy.getAggressive();

        Entity particleEntity = null;
        Entity particleEntity2 = null;
        ParticleEffectComponent effectComponent = null;
        ParticleEffectComponent effectComponent2 = null;


        if (enemy.isDead()) {
           // Gdx.app.log("Enemy System setting body to dead", "" + enemy.getType());

            body.setDead(true);
        }

        if (state.getState() == StateComponent.STATE.LEAVING && !enemy.isPlayedDyingSound()) {

            if (enemy.getType() != EnemyComponent.TYPE.DRAGON) {
                sound.addSound(SoundComponent.EVENT.ENEMY_DYING);
            } else {
                sound.addSound(SoundComponent.EVENT.DRAGON_DYING);
                //Gdx.app.log("Enemy System", "Attached entity size "+entityManager.getAttachedEntities().size + " dragon size "+enemy.getAttachedEntities().size);
                for(Entity brick: enemy.getAttachedEntities()){
                    BodyComponent brickBody = Mappers.body.get(brick);
                    brickBody.setDead(true);

                }
                sound.addSound(SoundComponent.EVENT.DESTROYED_BLOCK);
            }
            enemy.setPlayedDyingSound(true);
        }

        if (health < 1) {

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

        if (enemy.getTimeSinceLastShot() > 0) {
            float currentTime = enemy.getTimeSinceLastShot();
            currentTime -= deltaTime;
            enemy.setTimeSinceLastShot(currentTime);

        }


        if (state.getState() != StateComponent.STATE.LEAVING) {
            Animation animations;
            switch (enemy.getType()) {
                case SNAKE:

                    // Gdx.app.log("Enemy System", "current enemy Snake "+ enemy.getType());
                    // Gdx.app.log("ENEMY SYSTEM", "Snake timer " + timer+ " Timer greater than " + (timer >.25f) + " Snake distance " + distance + " Bored "+ bored + "State " +steering.currentMode );
                    // Gdx.app.log("ENEMY SYSTEM", "Snake timer " + timer);

                    steering.setMaxLinearAcceleration(550);
                    steering.setMaxLinearSpeed(550);






                    switch (steering.currentMode) {
                        case HIT:
                            animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.HIT);

                            if (animations.isAnimationFinished(animation.getTime())) {
                                if (health > 15) {
                                    aggressive -= 2f;
                                    steering.currentMode = SteeringComponent.SteeringState.PATROL;

                                } else {
                                    aggressive -= 5f;
                                    steering.currentMode = SteeringComponent.SteeringState.WANDER;


                                }
                            }
                            break;

                        case NONE:
                            bored++;

                            if (distance > 0) {
                                steering.currentMode = SteeringComponent.SteeringState.WANDER;
                            } else {
                                //steering.currentMode = SteeringComponent.SteeringState.PATROL;
                            }
                            break;
                        case WANDER:
                            bored++;
                            if (bored > 10 && distance < 10) {
                                steering.currentMode = SteeringComponent.SteeringState.PATROL;
                            }
                            if (distance > 10) {
                                steering.currentMode = SteeringComponent.SteeringState.NONE;
                            }
                            break;
                        case PATROL:
                            if (distance < 3) {
                                steering.currentMode = SteeringComponent.SteeringState.ARRIVE;
                            } else if (health < 10 && distance < 2) {
                                steering.currentMode = SteeringComponent.SteeringState.EVADE;
                            }

                            aggressive++;
                            bored = 0;
                            break;
                        case ARRIVE:
                            if (distance < 1 && aggressive > 10) {
                                steering.currentMode = SteeringComponent.SteeringState.ATTACK;
                            }

                            if (distance > 5 || aggressive < 10) {
                                steering.currentMode = SteeringComponent.SteeringState.WANDER;
                            }

                            break;
                        case ATTACK:
                            if (aggressive < 10 || (health < 10)) {
                                steering.currentMode = SteeringComponent.SteeringState.EVADE;
                            } else if (distance > 3) {
                                steering.currentMode = SteeringComponent.SteeringState.PATROL;
                            }
                            break;
                        case EVADE:
                            if (distance > 15 && health < 25) {
                                steering.currentMode = SteeringComponent.SteeringState.NONE;
                            } else {
                                steering.currentMode = SteeringComponent.SteeringState.WANDER;

                            }
                            aggressive = 0;
                            break;

                    }

                    switch (steering.currentMode) {
                        case NONE:
                            return;
                        case WANDER:

                            if (steering.steeringBehavior == null) {
                                //  Gdx.app.log("This should only print once","");
                                steering.steeringBehavior = SteeringPresets.getWanderPrioritySteering(steering, world);
                                Vector2 wanderTarget = SteeringPresets.getWanderTarget();
                                setWanderFacing(entity, wanderTarget);
                                break;
                            }
                            //steering.steeringBehavior = SteeringPresets.getWander(steering);

                            break;


                        case ATTACK:
                            steering.steeringBehavior = null;
                            break;
                        case PATROL:
                            steering.steeringBehavior = SteeringPresets.getPrioritySteering(steering, playersteering, world);
                            setFacing(entity, player);
                            break;
                        case ARRIVE:
                            steering.steeringBehavior = SteeringPresets.getArriveSteering(steering,playersteering);
                            setFacing(entity, player);
                            break;
                        case EVADE:
                            steering.steeringBehavior = SteeringPresets.getFlee(steering, playersteering);
                            setFacing(entity, player);
                            break;
                    }


                    break;

                case SKELETON:

                    // Gdx.app.log("Enemy System", "current enemy Skeleton "+ enemy.getType());
                    // Gdx.app.log("ENEMY SYSTEM", "Skelton distance " + distance);
                    // Gdx.app.log("ENEMY SYSTEM", "Skelton timer " + timer);

                    steering.setMaxLinearAcceleration(500);
                    steering.setMaxLinearSpeed(500);




                    switch (steering.currentMode) {
                        case HIT:
                            animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.HIT);

                            if (animations.isAnimationFinished(animation.getTime())) {
                                if (health > 25) {
                                    aggressive -= 3f;
                                    steering.currentMode = SteeringComponent.SteeringState.PATROL;

                                } else {
                                    aggressive += 5f;
                                    steering.currentMode = SteeringComponent.SteeringState.WANDER;


                                }
                            }
                            break;
                        case NONE:
                            bored++;
                            if (distance > 2) {

                                steering.currentMode = SteeringComponent.SteeringState.WANDER;
                            }
                            break;
                        case WANDER:
                            bored++;
                            if (bored > 5 && distance < 7) {
                                steering.currentMode = SteeringComponent.SteeringState.PATROL;
                            }
                            break;
                        case PATROL:
                            if (distance < 5) {
                                steering.currentMode = SteeringComponent.SteeringState.ARRIVE;
                            } else if (health < 20) {
                                steering.currentMode = SteeringComponent.SteeringState.EVADE;
                            }

                            aggressive++;
                            bored = 0;
                            break;
                        case ARRIVE:
                            if (distance < 3 && aggressive > 8) {
                                steering.currentMode = SteeringComponent.SteeringState.ATTACK;
                            }
                            else{
                                steering.currentMode = SteeringComponent.SteeringState.PATROL;
                            }

                            break;
                        case ATTACK:
                            if (aggressive < 5 || (health < 10)) {
                                steering.currentMode = SteeringComponent.SteeringState.EVADE;
                            }
                            break;
                        case EVADE:
                            if (distance > 7 && health < 25) {
                                steering.currentMode = SteeringComponent.SteeringState.NONE;
                                health += 10;
                            } else {
                                steering.currentMode = SteeringComponent.SteeringState.WANDER;

                            }
                            aggressive = 0;
                            break;

                    }
                    switch (steering.currentMode) {
                        case NONE:
                            return;
                        case WANDER:
                            steering.steeringBehavior = SteeringPresets.getWander(steering);
                            Vector2 wanderTarget = SteeringPresets.getWanderTarget();
                            setWanderFacing(entity, wanderTarget);
                            break;
                        case PATROL:

                        case ARRIVE:

                            steering.steeringBehavior = SteeringPresets.getPrioritySteering(steering, playersteering, world);
                            setFacing(entity, player);
                            break;

                        case ATTACK:

                            steering.steeringBehavior = SteeringPresets.getArriveSteering(steering,playersteering);
                            setFacing(entity, player);
                            break;
                        case EVADE:
                            steering.steeringBehavior = SteeringPresets.getFlee(steering, playersteering);
                            setFacing(entity, player);
                            break;
                    }


                    break;
                case DRAGON:
                    float originX;
                    float originY;
                    //Gdx.app.log("Enemy System", "current enemy: "+ enemy.getType() + " state: "+ state.getState() + " direction: "+ state.getDirection());

                    if (enemy.getHealth() < 45) {
                        enemy.setShootDelay(0.5f);
                    }


                    Timer.Task task;
                    // Gdx.app.log("ENEMY SYSTEM", "Skelton distance " + distance);
                    // Gdx.app.log("ENEMY SYSTEM", "Skelton timer " + timer);

                    switch (state.getState()) {
                        case IDLE:

                            // Gdx.app.log("Enemy System", "In Idle state before timer");
                            if (distance < 10) {

                                if (state.getState() != StateComponent.STATE.ENTERING) {
                                    if (!dragonRoarPlayed) {
                                        sound.addSound(SoundComponent.EVENT.DRAGON_ROAR);
                                        dragonRoarPlayed = true;
                                    }

                                    task = timer.scheduleTask(new Timer.Task() {
                                        @Override
                                        public void run() {
                                            StateComponent state = Mappers.state.get(entity);
                                            BodyComponent body = Mappers.body.get(entity);
                                            AnimationComponent animation = Mappers.animation.get(entity);
                                            state.setState(StateComponent.STATE.ENTERING);
                                            body.getBody().setLinearVelocity(-2, 2);
                                            body.getBody().setLinearDamping(1f);
                                            animation.setTime(0f);

                                        }
                                    }, 0.5f);


                                }

                            }
                            break;
                        case ENTERING:

                            //Gdx.app.log("Enemy System", "position"+ body.getBody().getPosition().x*Figures.PPM+ ", "+body.getBody().getPosition().y*Figures.PPM);
                            animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.ENTERING);
                            // Gdx.app.log("Enemy System","animation time "+ (animation.getTime()==0));

                            if (animations.isAnimationFinished(animation.getTime()) &&
                                    body.getBody().getPosition().x * Figures.PPM < 1760 &&
                                    body.getBody().getPosition().y * Figures.PPM > 48 &&
                                    state.getState() != StateComponent.STATE.PAUSE) {
                                //todo must change position when setting up in the boss room
                                body.getBody().setLinearVelocity(0, 0);
                                body.getBody().setLinearDamping(3f);
                                state.setState(StateComponent.STATE.PAUSE);
                            /*task = timer.scheduleTask(new Timer.Task() {

                                @Override
                                public void run() {
                                    StateComponent state = Mappers.state.get(entity);
                                    state.setState(StateComponent.STATE.FORCE_PUSH);
                                    AnimationComponent animation = Mappers.animation.get(entity);
                                    animation.setTime(0f);


                                }
                            }, 2.0f);*/


                            }
                            break;
                        case PAUSE:
                            aggressive += 2f;

                            if (playerbody.getBody().getLinearDamping() != 3) {
                                playerbody.getBody().setLinearDamping(3f);
                            }

                            if (distance > 5) {
                                dragonRoarPlayed = false;

                            }

                            originX = body.getBody().getPosition().x * Figures.PPM - 32;
                            originY = body.getBody().getPosition().y * Figures.PPM + 28;

                            if(!createdSmoke) {

                                particleEntity = entityManager.makeParticleEffect(ParticleEffectsManager.SMOKE,
                                        body.getBody().getPosition().x * Figures.PPM - 31,
                                        body.getBody().getPosition().y * Figures.PPM + 28);

                                particleEntity2 = entityManager.makeParticleEffect(ParticleEffectsManager.SMOKE,
                                        body.getBody().getPosition().x * Figures.PPM - 28,
                                        body.getBody().getPosition().y * Figures.PPM + 31);
                                createdSmoke = true;


                            }

                            if(particleEntity!= null && particleEntity2!= null) {
                                effectComponent = Mappers.particle.get(particleEntity);
                                effectComponent2 = Mappers.particle.get(particleEntity2);
                            }


                            if (distance < 4 && enemy.getHealth() < 45) {

                                if (state.getState() != StateComponent.STATE.FORCE_PUSH) {
                                    state.setState(StateComponent.STATE.FORCE_PUSH);
                                    animation.setTime(0f);

                                    if(effectComponent!= null && effectComponent2!= null) {

                                        effectComponent.setDead(true);
                                        effectComponent2.setDead(true);
                                    }
                                }
                            }
                            if (distance < 3) {

                                if (state.getState() != StateComponent.STATE.FORCE_PUSH) {
                                    state.setState(StateComponent.STATE.FORCE_PUSH);
                                    animation.setTime(0f);
                                    if(effectComponent!= null && effectComponent2!= null) {

                                        effectComponent.setDead(true);
                                        effectComponent2.setDead(true);
                                    }
                                }
                            }

                            if (enemy.getTimeSinceLastShot() <= .2) {
                                if(effectComponent!= null && effectComponent2!= null) {

                                    effectComponent.setDead(true);
                                    effectComponent2.setDead(true);
                                }
                            }

                            if (enemy.getTimeSinceLastShot() <= 0) {

                                if (distance > 5 || aggressive > 10) {
                                    if (state.getState() != StateComponent.STATE.PREPARE_TO_ATTACK) {
                                        state.setState(StateComponent.STATE.PREPARE_TO_ATTACK);
                                        animation.setTime(0f);

                                    }
                                }

                                createdSmoke = false;
                            }
                            break;

                        case PREPARE_TO_ATTACK:
                            //Gdx.app.log("Enemy System", "In prepare state before timer");


                            if (distance < 3) {
                                /*if (state.getState() != StateComponent.STATE.FORCE_PUSH) {
                                    state.setState(StateComponent.STATE.FORCE_PUSH);
                                    animation.setTime(0f);
                                }*/
                            } else {
                                animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.PREPARING_TO_ATTACKING);

                                if (aggressive > 20) {
                                    animations.setFrameDuration(0.05f);
                                }
                                if (animations.isAnimationFinished(animation.getTime()) && state.getState() != StateComponent.STATE.ATTACK) {


                                    if (enemy.getTimeSinceLastShot() <= 0) {
                                        state.setState(StateComponent.STATE.ATTACK);
                                        animation.setTime(0f);

                                    } else {
                                        state.setState(StateComponent.STATE.PAUSE);
                                        animation.setTime(0f);
                                    }


                                }
                            }
                            break;
                        case ATTACK:
                            //Gdx.app.log("Enemy System", "In attack state before timer");


                            if (distance < 3) {
                                if (state.getState() != StateComponent.STATE.FORCE_PUSH) {
                                    state.setState(StateComponent.STATE.FORCE_PUSH);
                                    animation.setTime(0f);
                                }
                            } else {

                                animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.ATTACKING);
                                if (animations.isAnimationFinished(animation.getTime()) && state.getState() != StateComponent.STATE.HIT) {
                                    sound.addSound(SoundComponent.EVENT.DRAGON_ATTACK);
                                    float speed = 10f;
                                    float shooterX = playerbody.getBody().getPosition().x * Figures.PPM;
                                    float shooterY = playerbody.getBody().getPosition().y * Figures.PPM;


                                    originX = body.getBody().getPosition().x * Figures.PPM - 35;
                                    originY = body.getBody().getPosition().y * Figures.PPM + 30;


                                    float velX = shooterX - originX;
                                    float velY = shooterY - originY;
                                    // Gdx.app.log("Enemy System", velX +" "+ velY);

                                    float length = (float) Math.sqrt(Math.pow(velX, 2) + Math.pow(velY, 2));

                                    if (length != 0) {
                                        velX /= length;
                                        velY /= length;
                                    }
                                    // Gdx.app.log("Enemy System", velX +" "+ velY);
                                    entityManager.createProjectile("Fireball",
                                            TypeComponent.COL_WEAPON, (int) originX, (int) originY, velX, velY);

                                    enemy.setTimeSinceLastShot(enemy.getShootDelay());


                                    if (state.getState() != StateComponent.STATE.PAUSE) {
                                        state.setState(StateComponent.STATE.PAUSE);
                                        animation.setTime(0f);
                                    }
                                }
                            }


                       /* task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                StateComponent state = Mappers.state.get(entity);
                                state.setState(StateComponent.STATE.HIT);
                                AnimationComponent animation = Mappers.animation.get(entity);
                                animation.setTime(0f);


                            }
                        }, 2.0f);*/

                            break;
                        case HIT:
                            if (health > 15) {
                                aggressive -= 5f;
                            } else {
                                aggressive += 20f;
                            }
                            //Gdx.app.log("Enemy System", "In hit state before timer");
                            animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.HIT);
                            if (animations.isAnimationFinished(animation.getTime())) {
                                if (distance < 3) {
                                    if (state.getState() != StateComponent.STATE.FORCE_PUSH) {
                                        state.setState(StateComponent.STATE.FORCE_PUSH);
                                        animation.setTime(0f);
                                    }
                                } else if (aggressive < 2) {
                                    if (state.getState() != StateComponent.STATE.PREPARE_TO_ATTACK) {
                                        state.setState(StateComponent.STATE.PREPARE_TO_ATTACK);
                                        animation.setTime(0f);
                                    }
                                } else {
                                    if (state.getState() != StateComponent.STATE.PAUSE) {
                                        state.setState(StateComponent.STATE.PAUSE);
                                        animation.setTime(0f);
                                    }
                                }


                            }

                            break;
                        case FORCE_PUSH:
                            //Gdx.app.log("Enemy System","In push state before timer");

                            playersteering.steeringBehavior = null;
                            animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.FORCE_PUSH);

                            if (!dragonRoarPlayed) {
                                sound.addSound(SoundComponent.EVENT.DRAGON_ROAR);
                                dragonRoarPlayed = true;
                            }
                            Vector2 direction = body.getBody().getLocalCenter().sub(playerbody.getBody().getLocalCenter()).nor();
                            playerbody.getBody().setLinearDamping(10f);
                            playerbody.getBody().applyLinearImpulse(direction, playerbody.getBody().getLocalCenter(), true);


                            if (animations.isAnimationFinished(animation.getTime())) {

                                state.setState(StateComponent.STATE.PAUSE);
                                animation.setTime(0f);
                       /* task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                StateComponent state = Mappers.state.get(entity);
                                state.setState(StateComponent.STATE.PREPARE_TO_ATTACK);
                                AnimationComponent animation = Mappers.animation.get(entity);
                                animation.setTime(0f);


                            }
                        }, 2.0f);*/
                            }


                            break;
                    }
                    break;
            }

        /*if (body.getBody().isActive()) {


            if (enemy.getType() == EnemyComponent.TYPE.SNAKE) {
                Gdx.app.log("Enemy System", "current enemy Snake "+ enemy.getType());
                // Gdx.app.log("ENEMY SYSTEM", "Snake timer " + timer+ " Timer greater than " + (timer >.25f) + " Snake distance " + distance + " Bored "+ bored + "State " +steering.currentMode );
                // Gdx.app.log("ENEMY SYSTEM", "Snake timer " + timer);

                steering.setMaxLinearAcceleration(3);
                steering.setMaxLinearSpeed(7);


                switch (steering.currentMode) {
                    case NONE:
                        bored++;

                        if (distance > 5) {
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;
                        }
                        break;
                    case WANDER:
                        bored++;
                        if (bored > 10 && distance < 10) {
                            steering.currentMode = SteeringComponent.SteeringState.PATROL;
                        }
                        if(distance>10){
                            steering.currentMode = SteeringComponent.SteeringState.NONE;
                        }
                        break;
                    case PATROL:
                        if (distance < 3) {
                            steering.currentMode = SteeringComponent.SteeringState.ARRIVE;
                        } else if(health < 10 && distance< 2){
                            steering.currentMode = SteeringComponent.SteeringState.EVADE;
                        }

                        aggressive++;
                        bored = 0;
                        break;
                    case ARRIVE:
                        if (distance < 1 && aggressive > 10) {
                            steering.currentMode = SteeringComponent.SteeringState.ATTACK;
                        }

                        if (distance > 5 || aggressive < 10){
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;
                        }

                        break;
                    case ATTACK:
                        if (aggressive < 10|| (health<10)){
                            steering.currentMode = SteeringComponent.SteeringState.EVADE;
                        }
                        else if(distance>3){
                            steering.currentMode = SteeringComponent.SteeringState.PATROL;
                        }
                        break;
                    case EVADE:
                        if(distance >15 && health < 25){
                            steering.currentMode = SteeringComponent.SteeringState.NONE;
                        }
                        else {
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;

                        }
                        aggressive = 0;
                        break;

                }

                switch(steering.currentMode){
                    case NONE:
                        return;
                    case WANDER:

                        if(steering.steeringBehavior == null){
                            Gdx.app.log("This should only print once","");
                            steering.steeringBehavior = SteeringPresets.getWanderPrioritySteering(steering,world);
                            Vector2 wanderTarget = SteeringPresets.getWanderTarget();
                            setWanderFacing(entity,wanderTarget);
                            break;
                        }
                        //steering.steeringBehavior = SteeringPresets.getWander(steering);

                        break;


                    case ATTACK:
                        steering.steeringBehavior = null;
                        break;
                    case PATROL:
                    case ARRIVE:
                        steering.steeringBehavior = SteeringPresets.getPrioritySteering(steering,playersteering,world);
                        setFacing(entity,player);
                        break;
                    case EVADE:
                        steering.steeringBehavior = SteeringPresets.getFlee(steering,playersteering);
                        setFacing(entity,player);
                        break;
                }





            }

            if (enemy.getType() == EnemyComponent.TYPE.SKELETON) {
                Gdx.app.log("Enemy System", "current enemy Skeleton "+ enemy.getType());
                // Gdx.app.log("ENEMY SYSTEM", "Skelton distance " + distance);
                // Gdx.app.log("ENEMY SYSTEM", "Skelton timer " + timer);

                steering.setMaxLinearAcceleration(15);
                steering.setMaxLinearSpeed(25);

                // if (timer > 1f) {

                switch (steering.currentMode) {
                    case NONE:
                        bored++;
                        if (distance > 8) {

                            steering.currentMode = SteeringComponent.SteeringState.WANDER;
                        }
                        break;
                    case WANDER:
                        bored++;
                        if (bored > 5 && distance < 8) {
                            steering.currentMode = SteeringComponent.SteeringState.PATROL;
                        }
                        break;
                    case PATROL:
                        if (distance < 5) {
                            steering.currentMode = SteeringComponent.SteeringState.ARRIVE;
                        } else if(health<20){
                            steering.currentMode = SteeringComponent.SteeringState.EVADE;
                        }

                        aggressive++;
                        bored = 0;
                        break;
                    case ARRIVE:
                        if (distance < 3 && aggressive > 8) {
                            steering.currentMode = SteeringComponent.SteeringState.ATTACK;
                        }

                        break;
                    case ATTACK:
                        if (aggressive < 5|| (health<10)){
                            steering.currentMode = SteeringComponent.SteeringState.EVADE;
                        }
                        break;
                    case EVADE:
                        if(distance >10 && health < 25){
                            steering.currentMode = SteeringComponent.SteeringState.NONE;
                        }
                        else {
                            steering.currentMode = SteeringComponent.SteeringState.WANDER;

                        }
                        aggressive = 0;
                        break;

                }
                switch(steering.currentMode){
                    case NONE:
                        return;
                    case WANDER:
                        steering.steeringBehavior = SteeringPresets.getWander(steering);
                        Vector2 wanderTarget = SteeringPresets.getWanderTarget();
                        setWanderFacing(entity,wanderTarget);
                        break;
                    case PATROL:

                    case ATTACK:
                    case ARRIVE:
                        steering.steeringBehavior = SteeringPresets.getPrioritySteering(steering,playersteering,world);
                        setFacing(entity,player);
                        break;
                    case EVADE:
                        steering.steeringBehavior = SteeringPresets.getFlee(steering,playersteering);
                        setFacing(entity,player);
                        break;
                }







            }
            if (enemy.getType() == EnemyComponent.TYPE.DRAGON) {
                Gdx.app.log("Enemy System", "current enemy Dragon"+ enemy.getType());

                Animation animations;

                Timer.Task task;
                // Gdx.app.log("ENEMY SYSTEM", "Skelton distance " + distance);
                // Gdx.app.log("ENEMY SYSTEM", "Skelton timer " + timer);

                switch(state.getState()) {
                    case IDLE:

                        Gdx.app.log("Enemy System", "In Idle state before timer");
                        if (distance < 5) {
                            if (state.getState() != StateComponent.STATE.ENTERING) {
                                state.setState(StateComponent.STATE.ENTERING);
                                body.getBody().setLinearVelocity(-2, 2);
                                animation.setTime(0f);

                            }

                        }
                        break;
                    case ENTERING:
                        Gdx.app.log("Enemy System", "In enter state before timer");
                        animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.ENTERING);
                        if (animations.isAnimationFinished(animation.getTime()) && state.getState() != StateComponent.STATE.FORCE_PUSH) {
                            state.setState(StateComponent.STATE.FORCE_PUSH);
                            *//*task = timer.scheduleTask(new Timer.Task() {

                                @Override
                                public void run() {
                                    StateComponent state = Mappers.state.get(entity);
                                    state.setState(StateComponent.STATE.FORCE_PUSH);
                                    AnimationComponent animation = Mappers.animation.get(entity);
                                    animation.setTime(0f);


                                }
                            }, 2.0f);*//*
                        }
                        break;

                    case PREPARE_TO_ATTACK:
                        Gdx.app.log("Enemy System", "In prepare state before timer");
                        animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.PREPARING_TO_ATTACKING);
                        if (animations.isAnimationFinished(animation.getTime()) && state.getState() != StateComponent.STATE.ATTACK) {
                            state.setState(StateComponent.STATE.ATTACK);
                            animation.setTime(0f);
                        *//*task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                StateComponent state = Mappers.state.get(entity);
                                state.setState(StateComponent.STATE.ATTACK);
                                AnimationComponent animation = Mappers.animation.get(entity);
                                animation.setTime(0f);


                            }
                        }, 2.0f);*//*
                        }
                        break;
                    case ATTACK:
                        Gdx.app.log("Enemy System", "In attack state before timer");
                        animations = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.ATTACKING);
                        if (animations.isAnimationFinished(animation.getTime()) && state.getState() != StateComponent.STATE.HIT) {
                            state.setState(StateComponent.STATE.HIT);
                            animation.setTime(0f);
                       *//* task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                StateComponent state = Mappers.state.get(entity);
                                state.setState(StateComponent.STATE.HIT);
                                AnimationComponent animation = Mappers.animation.get(entity);
                                animation.setTime(0f);


                            }
                        }, 2.0f);*//*
                        }
                        break;
                    case HIT:
                        Gdx.app.log("Enemy System", "In hit state before timer");
                        animations  = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.HIT);
                        *//*if(animations.isAnimationFinished(animation.getTime())&&state.getState()!= StateComponent.STATE.LEAVING) {
                            state.setState(StateComponent.STATE.LEAVING);
                            animation.setTime(0f);
                       *//**//* task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                StateComponent state = Mappers.state.get(entity);
                                state.setState(StateComponent.STATE.LEAVING);
                                AnimationComponent animation = Mappers.animation.get(entity);
                                animation.setTime(0f);


                            }
                        }, 2.0f);*//**//*
                }*//*
                        break;
                    case FORCE_PUSH:
                        Gdx.app.log("Enemy System","In push state before timer");
                        animations  = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.FORCE_PUSH);
                        if(animations.isAnimationFinished(animation.getTime())&&state.getState()!= StateComponent.STATE.PREPARE_TO_ATTACK) {
                            state.setState(StateComponent.STATE.PREPARE_TO_ATTACK);
                            animation.setTime(0f);
                       *//* task = timer.scheduleTask(new Timer.Task() {

                            @Override
                            public void run() {
                                StateComponent state = Mappers.state.get(entity);
                                state.setState(StateComponent.STATE.PREPARE_TO_ATTACK);
                                AnimationComponent animation = Mappers.animation.get(entity);
                                animation.setTime(0f);


                            }
                        }, 2.0f);*//*
                        }
                        break;
                }
















            }*/
            enemy.setAggressive(aggressive);
            enemy.setBored(bored);
            enemy.setHealth(health );


            //}


        }
    }

    private void setWanderFacing(Entity seeker, Vector2 target) {
        StateComponent state = Mappers.state.get(seeker);
        SteeringComponent steering = Mappers.steering.get(seeker);
        AnimationComponent animation = Mappers.animation.get(seeker);
        Vector2 position = steering.getPosition();


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


    private void setFacing(Entity seeker, Entity target){
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
        }
        else {
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
}
