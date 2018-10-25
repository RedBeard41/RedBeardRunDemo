package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.util.Map;

import Components.BodyComponent;
import Components.DataComponent;
import Components.EnemyComponent;
import Components.ParticleEffectComponent;
import Components.PositionComponent;
import Components.RenderableComponent;
import Components.TextureComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;

public class RenderSystem extends IteratingSystem {
    private SpriteBatch batch;
    private Array<Entity> bodiesQueue;
    private OrthographicCamera camera;

    public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(RenderableComponent.class).get());
        this.batch = batch;
        this.camera = camera;
        bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : bodiesQueue) {
           // DataComponent data = Mappers.data.get(entity);
            //Gdx.app.log("RENDERING GROUPS","Group " + data.getData("group"));
            TextureComponent texture = Mappers.texture.get(entity);
            PositionComponent position = Mappers.position.get(entity);
            /*EnemyComponent enemy = Mappers.enemy.get(entity);
            if(enemy!= null){
                if(enemy.getType()==EnemyComponent.TYPE.DRAGON){
                    BodyComponent body = Mappers.body.get(entity);


                    batch.draw(texture.getRegion(),body.getBody().getPosition().x*Figures.PPM-texture.getRegion().getRegionWidth()/2,
                            body.getBody().getPosition().y*Figures.PPM-texture.getRegion().getRegionHeight()/2,
                            texture.getRegion().getRegionWidth()/2,
                            texture.getRegion().getRegionHeight()/2,
                            texture.getRegion().getRegionWidth(),
                            texture.getRegion().getRegionHeight(),1,1,body.getBody().getAngle()* MathUtils.radiansToDegrees );
                    continue;
                }

            }*/


            batch.draw(texture.getRegion(),position.getX(),position.getY(),

                    texture.getRegion().getRegionWidth(),texture.getRegion().getRegionHeight());
        }
        batch.end();
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);





    }
}
