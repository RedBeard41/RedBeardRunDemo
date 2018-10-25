package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Components.AnimationComponent;
import Components.BodyComponent;
import Components.EnemyComponent;
import Components.NPCComponent;
import Components.RenderableComponent;
import Components.StateComponent;
import Components.TextureComponent;
import Components.TypeComponent;
import Helpers.Mappers;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(RenderableComponent.class, TextureComponent.class,
                AnimationComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimationComponent animation = Mappers.animation.get(entity);
        TextureComponent texture = Mappers.texture.get(entity);
        StateComponent state = Mappers.state.get(entity);
        TypeComponent type = Mappers.type.get(entity);

        animation.setTime((deltaTime+animation.getTime()));
        float stateTimer = animation.getTime();

        Animation currentAnimation;
       // Gdx.app.log("Animation system", "type: "+ type.getType()+" state: "+state.getState()+" direction: "+state.getDirection());
        switch(state.getDirection()){
            case DOWN:
                if(state.getState() == StateComponent.STATE.MOVING){
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.DOWN);
                    animation.setLooping(true);

                    if(currentAnimation == null){
                       // Gdx.app.log("Animation: ","Animation was null");
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));
                    //  Gdx.app.log("Number of frames in animation", "KeyFrame Index: "+animation.getKeyFrameIndex(stateTimer)+
                    //  " Animation Duration: "+ animation.getAnimationDuration()+ " Animation Play Mode: "+ animation.getPlayMode()+ " Animation is Finished: "+animation.isAnimationFinished(stateTimer));


                }
                else if(state.getState() ==StateComponent.STATE.IDLE){
                    animation.setLooping(false);
                    // Gdx.app.log("Able to get to animation", ""+(stCom.getState() ==StateComponent.STATE.IDLE));
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.DOWN);
                    if(currentAnimation == null){
                        // Gdx.app.log("Animation: ","Animation was null");
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrames()[0]);
                    // Gdx.app.log("Animation Key frame is not null: ",""+(animation.getKeyFrames()[0]!=null));
                    // Gdx.app.log("Sprite has a region: ",""+(sCom.region.getTexture()!=null));

                }
                break;
            case UP:
                if(state.getState() == StateComponent.STATE.MOVING){
                    animation.setLooping(true);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.UP);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));

                }
                else if(state.getState() ==StateComponent.STATE.IDLE){
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.UP);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrames()[0]);

                }
                break;
            case RIGHT:
                if(state.getState() == StateComponent.STATE.MOVING){
                    animation.setLooping(true);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));

                }
                else if(state.getState() ==StateComponent.STATE.IDLE){
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.RIGHT);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrames()[0]);

                }
                break;
            case LEFT:
                if(state.getState() == StateComponent.STATE.MOVING){
                    animation.setLooping(true);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.LEFT);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));

                }
                else if(state.getState() ==StateComponent.STATE.IDLE){
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.LEFT);
                    if(animation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrames()[0]);

                }
                break;
            case NONE:
                if(state.getState() == StateComponent.STATE.TALKING){
                    animation.setLooping(true);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.TALKING);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));

                }

                else if(state.getState() == StateComponent.STATE.LEAVING){
                    BodyComponent body = Mappers.body.get(entity);
                    body.getBody().setActive(false);
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.LEAVING);
                    if(currentAnimation == null){
                       // Gdx.app.log("Animation System","Missing Leaving animation");
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));
                   // Gdx.app.log("9999999999999999999999", "I should be here");

                    //Gdx.app.log("Number of frames in animation", "KeyFrame Index: "+currentAnimation.getKeyFrameIndex(stateTimer)+
                             // " Animation Duration: "+ currentAnimation.getAnimationDuration()+ " Animation Play Mode: "+ currentAnimation.getPlayMode()+ " Animation is Finished: "+currentAnimation.isAnimationFinished(stateTimer));

                    if(currentAnimation.isAnimationFinished(stateTimer)) {
                        //Gdx.app.log("Dragon Animation", "leaving animation finished");
                       // type.setType(TypeComponent.COL_OTHER);
                        if (type.getType() == TypeComponent.COL_FRIENDLY) {
                            NPCComponent npc = Mappers.npc.get(entity);
                            if(!npc.isDead()) {
                                npc.setDead(true);
                            }
                           // Gdx.app.log("Animation System", npc.getType()+" status "+npc.isDead());
                        }

                        if (type.getType() == TypeComponent.COL_ENEMY) {
                          //  type.setType(TypeComponent.COL_OTHER);
                            EnemyComponent enemy = Mappers.enemy.get(entity);

                            if(!enemy.isDead()) {
                               // Gdx.app.log("Animation System", enemy.getType()+" status "+enemy.isDead());
                               // Gdx.app.log("Animation System", ""+enemy.getType());
                                enemy.setDead(true);
                            }
                           // Gdx.app.log("Animation System", enemy.getType()+" status "+enemy.isDead());
                        }

                    }


                }
                else if (state.getState() == StateComponent.STATE.ENTERING){
                    //Gdx.app.log("9999999999999999999999", "I should be here");
                    // animation.setTime((deltaTime+animation.getTime())%13);
                    animation.setLooping(true);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.ENTERING);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));


                }
                else if (state.getState() == StateComponent.STATE.FORCE_PUSH){
                    //Gdx.app.log("9999999999999999999999", "I should be here");
                    // animation.setTime((deltaTime+animation.getTime())%13);
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.FORCE_PUSH);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));


                }
                else if (state.getState() == StateComponent.STATE.PREPARE_TO_ATTACK){
                    //Gdx.app.log("9999999999999999999999", "I should be here");
                    // animation.setTime((deltaTime+animation.getTime())%13);
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.PREPARING_TO_ATTACKING);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));


                }
                else if (state.getState() == StateComponent.STATE.ATTACK){
                    //Gdx.app.log("9999999999999999999999", "I should be here");
                    // animation.setTime((deltaTime+animation.getTime())%13);
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.ATTACKING);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));


                }
                else if (state.getState() == StateComponent.STATE.HIT){
                    //Gdx.app.log("9999999999999999999999", "I should be here");
                    // animation.setTime((deltaTime+animation.getTime())%13);
                    animation.setLooping(false);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.HIT);
                    if(currentAnimation == null){
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));

                }
                else if (state.getState() == StateComponent.STATE.IMMOBILE){
                     //Gdx.app.log("9999999999999999999999", "I should be here");
                   // animation.setTime((deltaTime+animation.getTime())%13);
                    animation.setLooping(true);
                    currentAnimation = animation.getAnimation(AnimationComponent.ANIMATIONSTATE.IMMOBILE);
                    if(currentAnimation == null){
                  //      Gdx.app.log("9999999999999999999999", "I shouldn't be here");
                        return;
                    }
                    texture.setRegion((TextureRegion) currentAnimation.getKeyFrame(stateTimer,animation.isLooping()));

                }

                break;
            default:
                break;

        }






    }
}
