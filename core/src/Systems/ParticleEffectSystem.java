package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Map;

import Components.BodyComponent;
import Components.InteractiveComponent;
import Components.ParticleEffectComponent;
import Components.ProjectileComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;

public class ParticleEffectSystem extends IteratingSystem {
    private SpriteBatch batch;
    private Array<Entity> particleQueue;
    private OrthographicCamera camera;
    private PooledEngine engine;

    public ParticleEffectSystem(PooledEngine engine,SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(ParticleEffectComponent.class).get());
        particleQueue = new Array<>();
        this.batch = batch;
        this.camera = camera;
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();


        batch.begin();
        //Gdx.app.log("Particle Effect System", "Number in queue: "+ particleQueue.size);
        for(Entity entity: particleQueue) {
            ParticleEffectComponent particle = Mappers.particle.get(entity);
            if (particle.isStarted()) {
                //particle.getParticleEffect().update(deltaTime);
                particle.getParticleEffect().draw(batch, deltaTime);
            }
            else{
                particle.setStarted(true);
            }
        }
        batch.end();
        particleQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParticleEffectComponent particle = Mappers.particle.get(entity);
        if(particle.isDead()){
            particle.setTimeTillDeath(particle.getTimeTillDeath()-deltaTime);
        }

        if(particle.getParticleEffect().isComplete() || particle.getTimeTillDeath() <= 0){
            BodyComponent body = Mappers.body.get(entity);
            if(body != null) {
                body.setDead(true);
            }
            else {
                engine.removeEntity(entity);
            }
            //entity.remove(ParticleEffectComponent.class);

            /*if(particle.getTimeTillDeath() <= 0){
                particle.getParticleEffect().free();
            }*/
        }

        if(particle.isAttached()){
            TypeComponent type = Mappers.type.get(entity);
            if(type != null){
                if(type.getType() == TypeComponent.COL_INTERACTIVE){
                    //Gdx.app.log("Particle System"," trying to attach particle to the wrong thing");
                    return;
                }
                else if(type.getType()==TypeComponent.COL_WEAPON){
                    particle.getParticleEffect().setPosition(
                            particle.getAttachedBody().getPosition().x * Figures.PPM + particle.getxOffset(),
                            particle.getAttachedBody().getPosition().y *Figures.PPM + particle.getyOffset());
                    particleQueue.add(entity);
                }
            }


            //Gdx.app.log("Particle Effect System", "("+particle.getAttachedBody().getPosition().x * Figures.PPM + particle.getxOffset()+", "+
             //       particle.getAttachedBody().getPosition().y *Figures.PPM + particle.getyOffset()+")");
        }



        else {

            particleQueue.add(entity);
        }
    }
}
