package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import Components.BodyComponent;
import Components.InteractiveComponent;
import Helpers.Mappers;

public class InteractiveSystem extends IteratingSystem {

    public InteractiveSystem() {
        super(Family.all(InteractiveComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        InteractiveComponent interactiveComponent = Mappers.interactive.get(entity);
        BodyComponent body = Mappers.body.get(entity);

        if(interactiveComponent.isDead()){
            body.setDead(true);
        }

    }
}
