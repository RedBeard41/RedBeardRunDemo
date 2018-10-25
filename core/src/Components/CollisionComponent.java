package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool.Poolable;

public class CollisionComponent implements Component, Poolable{

    private Entity collisionEntity;

    public Entity getCollisionEntity() {
        return collisionEntity;
    }

    public void setCollisionEntity(Entity collisionEntity) {
        this.collisionEntity = collisionEntity;
    }

    @Override
    public void reset() {
        collisionEntity = null;

    }
}
