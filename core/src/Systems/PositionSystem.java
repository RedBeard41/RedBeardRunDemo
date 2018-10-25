package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Components.BodyComponent;
import Components.InteractiveComponent;
import Components.ParticleEffectComponent;
import Components.PlayerComponent;
import Components.PositionComponent;
import Components.TextureComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;

public class PositionSystem extends IteratingSystem {
    public static final String TAG = PositionSystem.class.getSimpleName();
    private World world;
    private Array<Entity> bodiesQueue;
    private PooledEngine engine;
    private Entity player;
    private PlayerComponent playerComponent;
    private float timer;


    public PositionSystem(World world, PooledEngine engine, Entity player) {

        super(Family.all(PositionComponent.class, BodyComponent.class).get());
        this.world = world;
        this.bodiesQueue = new Array<Entity>();
        this.engine = engine;
        this.player = player;
        this.playerComponent = Mappers.player.get(player);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //timer = playerComponent.isPause()? 0:deltaTime;
        world.step(1/60f,6,2);


            //Gdx.app.log(TAG,"Before: Player: "+playerbody.getBody().getPosition().x + ", "+playerbody.getBody().getPosition().y+" Target: "+ x+", "+y);

           // playerComponent.setPause(true);
            // playerbody.getBody().setTransform(x,y,playerbody.getBody().getAngle());



        for (Entity entity : bodiesQueue) {
            PositionComponent position = Mappers.position.get(entity);
            BodyComponent body = Mappers.body.get(entity);
            TextureComponent texture = Mappers.texture.get(entity);
            TypeComponent type = Mappers.type.get(entity);
            InteractiveComponent interactive;

            if(body.isDead()&&type.getType()!=TypeComponent.COL_PLAYER){
               // Gdx.app.log(TAG,"Removing entity from world");
                world.destroyBody(body.getBody());
                engine.removeEntity(entity);
                continue;
            }


            if(!body.isDead()&&body.isActive()) {
                if (body != null && texture != null) {
                    position.setX(body.getBody().getPosition().x * Figures.PPM - (texture.getRegion().getRegionWidth() / 2));
                    position.setY(body.getBody().getPosition().y * Figures.PPM - (texture.getRegion().getRegionHeight() / 2));
                } else if (texture == null) {
                    position.setX(body.getBody().getPosition().x);
                    position.setY(body.getBody().getPosition().y);
                }
                //disable Portals
                if(type.getType()==TypeComponent.COL_INTERACTIVE){
                    interactive = Mappers.interactive.get(entity);
                   // Gdx.app.log(TAG,"Portal is active: "+ body.getBody().isActive());
                    if(playerComponent.isPause()&&interactive.getType()==InteractiveComponent.TYPE.PORTAL){
                        body.setActive(false);
                      //  Gdx.app.log(TAG,"Portal is active: "+ body.getBody().isActive());
                    }

                }
            }






            //Enable portals
            if(type.getType()==TypeComponent.COL_INTERACTIVE&&!playerComponent.isPause()){
                interactive = Mappers.interactive.get(entity);
                if(interactive.getType()==InteractiveComponent.TYPE.PORTAL) {
                    //Gdx.app.log(TAG,"Portal is active: "+ body.getBody().isActive());
                    body.setActive(true);
                }
                }

        }
        if(playerComponent.isPause()) {
           lerpToTarget(player, playerComponent.getX(), playerComponent.getY(), .2f);
            // playerComponent.setPause(false);

            // Gdx.app.log(TAG,"AFter: Player: "+" Target: "+ x+", "+y);
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        bodiesQueue.add(entity);

    }

    public static Body lerpToTarget(Entity entity, float x, float y, float beginFactor){
        //a+(b-a)*lerp factor
        float targetX = x;
        float targetY = y;
        float begin = beginFactor;
        Body body = Mappers.body.get(entity).getBody();
        PlayerComponent playerComponent = Mappers.player.get(entity);


        Vector2 position = body.getPosition();
        position.x*=Figures.PPM;
        position.y*=Figures.PPM;

        //Gdx.app.log(TAG, position.toString() +" target: ("+targetX+", "+targetY);

        if(Math.abs(position.x-targetX)<2&&Math.abs(position.y-targetY)<2){
            //body.setTransform(x/Figures.PPM,y/Figures.PPM,body.getAngle());
            playerComponent.setPause(false);
        }
        else {
            position.x = MathUtils.lerp(position.x, targetX, begin);
            position.y = MathUtils.lerp(position.y, targetY, begin);
            body.setTransform(position.x/Figures.PPM,position.y/Figures.PPM,body.getAngle());
        }
        return body;

    }
}
