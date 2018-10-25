package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import Components.BodyComponent;
import Components.ParticleEffectComponent;
import Components.ProjectileComponent;
import Helpers.Figures;
import Helpers.Mappers;

public class ProjectileSystem extends IteratingSystem {

    public ProjectileSystem() {
        super(Family.all(ProjectileComponent.class).get());

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = Mappers.body.get(entity);
        ProjectileComponent projectile = Mappers.projectile.get(entity);
        //Gdx.app.log("Projectile",""+ projectile.getxVel()+" "+projectile.getyVel());

        body.getBody().setLinearVelocity(projectile.getxVel(), projectile.getyVel());





        if(projectile.isDead()){
            if(projectile.getParticleEffect() != null) {
                Entity particle = projectile.getParticleEffect();
                ParticleEffectComponent particleEffect = Mappers.particle.get(particle);
               // particleEffect.setAttached(false);
                particleEffect.setDead(true);
            }
            return;
        }
        else {
            float originX = projectile.getOriginX()/ Figures.PPM;
            float originY = projectile.getOriginY()/Figures.PPM;

            float projectileX = body.getBody().getPosition().x;
            float projectileY = body.getBody().getPosition().y;

            //Gdx.app.log("Projectile system","origin: ("+originX+", "+originY+") Projectile: ("+projectileX+", "+projectileY+")");

            if (Math.abs(projectileX - originX) > 5 || Math.abs(projectileY - originY) > 5) {
                projectile.setDead(true);
            }
        }








        /*if(projectile.isDead()){
            body.setDead(true);
        }*/
    }
}
