package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.GdxAI;

import Components.SteeringComponent;
import Helpers.Mappers;

public class SteeringSystem extends IteratingSystem {

    public SteeringSystem() {
        super(Family.all(SteeringComponent.class).get());
    }

    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        GdxAI.getTimepiece().update(deltaTime);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SteeringComponent steer = Mappers.steering.get(entity);
        steer.update(deltaTime);

    }
}