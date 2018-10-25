package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

import Components.BodyComponent;

public class B2bodyRenderSystem extends IteratingSystem {

    private Box2DDebugRenderer debugRenderer;
    private World world;
    private Viewport viewport;

    public B2bodyRenderSystem(World world, Viewport viewport) {
        super(Family.all(BodyComponent.class).get());
        this.world = world;
        this.viewport = viewport;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        debugRenderer.render(world, viewport.getCamera().combined);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
