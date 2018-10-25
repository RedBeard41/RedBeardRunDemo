package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;

import Components.BodyComponent;
import Components.DataComponent;
import Components.EnemyComponent;
import Components.NPCComponent;
import Components.PlayerComponent;
import Components.RenderableComponent;
import Components.TypeComponent;
import Helpers.Mappers;
import Managers.EntityManager;

public class BoundarySystem extends IteratingSystem {
    private Entity player;


    public BoundarySystem(Entity player) {
        super(Family.all(RenderableComponent.class, DataComponent.class).get());
        this.player = player;
        ;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DataComponent data = Mappers.data.get(entity);
        float group = data.getData("group");
        BodyComponent body = Mappers.body.get(entity);
        TypeComponent type = Mappers.type.get(entity);
        PlayerComponent playerComponent = Mappers.player.get(player);

        float currentGroup = playerComponent.getCurrentGroup();
        float pastGroup = playerComponent.getPastGroup();
        if (group == currentGroup ) {

            body.setActive(true);
           // Gdx.app.log("BOUNDARY SYSTEM","Entity group "+ group + " Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive()+"Entity: Enemy "+(type.getType() == 2) + "Entity: Friendly "+(type.getType() == 128) +" Current Group "+ currentGroup + " Body is active "+ body.getBody().isActive());

            // Gdx.app.log("BOUNDARY SYSTEM","Entity group "+ group + " Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive());
        } else {
            body.setActive(false);
           // Gdx.app.log("BOUNDARY SYSTEM","Entity group "+ group + " Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive()+"Entity: Enemy "+(type.getType() == 2) + "Entity: Friendly "+(type.getType() == 128) +" Current Group "+ currentGroup + " Body is active "+ body.getBody().isActive());

           // Gdx.app.log("BOUNDARY SYSTEM","Entity group "+ group + " Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive());
            if (pastGroup == group) {


                if (type.getType() == TypeComponent.COL_FRIENDLY) {
                    NPCComponent npc = Mappers.npc.get(entity);
                    npc.setDead(true);
                  //  Gdx.app.log("BOUNDARY SYSTEM","Entity: Enemy "+(type.getType() == 2) + "Entity: Friendly "+(type.getType() == 128) +" Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive());
                } else if (type.getType() == TypeComponent.COL_ENEMY) {
                    EnemyComponent enemy = Mappers.enemy.get(entity);
                    enemy.setDead(true);
                   // Gdx.app.log("BOUNDARY SYSTEM","Entity: Enemy "+(type.getType() == 2) + "Entity: Friendly "+(type.getType() == 128) +" Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive());

                }

            }
        }
    }
}
