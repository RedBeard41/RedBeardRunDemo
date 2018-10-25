package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

import Components.BodyComponent;
import Components.DataComponent;
import Components.EnemyComponent;
import Components.InteractiveComponent;
import Components.NPCComponent;
import Components.ParticleEffectComponent;
import Components.PlayerComponent;
import Components.ProjectileComponent;
import Components.RenderableComponent;
import Components.SoundComponent;
import Components.TypeComponent;
import Helpers.Figures;
import Helpers.Mappers;
import Managers.EntityManager;

public class RespawnSystem extends IteratingSystem {
    public static final String TAG = RespawnSystem.class.getSimpleName();
    private EntityManager entityManager;
    private Entity player;
    private float pastGroup;
    private TiledMap map;
    private PlayerComponent playerComponent;
    private  float currentGroup;
    private boolean spawnGroup;
    private boolean spawnedBossRoom = false;

    public RespawnSystem(Entity player, EntityManager entityManager, TiledMap map) {
        super(Family.all(RenderableComponent.class).exclude(PlayerComponent.class, ProjectileComponent.class).get());

        this.entityManager = entityManager;
        this.player = player;
        this.map = map;


    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        playerComponent = Mappers.player.get(player);
        currentGroup = playerComponent.getCurrentGroup();
        pastGroup = playerComponent.getPastGroup();
        spawnGroup = playerComponent.isSpawnPastGroupEntities();
       // Gdx.app.log(TAG,playerComponent.isSpawnPastGroupEntities()+" to get into if statement "+ (!spawnGroup));

        if( !spawnGroup) {
            if (currentGroup == 27) {
                BodyComponent body = Mappers.body.get(player);
                  Gdx.app.log("Respawn System","player position: "+body.getBody().getPosition().x+", "+body.getBody().getPosition().y);
                if (body.getBody().getPosition().x > 1660/Figures.PPM && body.getBody().getPosition().y < 130/Figures.PPM) {

                    if (!spawnedBossRoom) {
                        entityManager.spawnGroup(map, currentGroup);
                        playMusic((int) currentGroup);
                        playerComponent.setSpawnPastGroupEntities(true);
                        spawnedBossRoom = true;
                    }

                }
            } else {
                entityManager.spawnGroup(map, currentGroup);
                // body.setActive(true);
                playerComponent.setSpawnPastGroupEntities(true);
                playMusic((int) currentGroup);
            }
        }




    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        DataComponent data = Mappers.data.get(entity);
        BodyComponent body = Mappers.body.get(entity);
        TypeComponent type = Mappers.type.get(entity);
        float group = data.getData("group");

        // Gdx.app.log(TAG, "Current Group to spawn: "+ currentGroup + "SpawnedGroup: "+ spawnGroup);



        if(group == pastGroup&&group!=currentGroup){
            body.setActive(false);

            if (type.getType() == TypeComponent.COL_FRIENDLY) {
                NPCComponent npc = Mappers.npc.get(entity);
                if(!npc.isDead()) {
                    npc.setDead(true);
                   // Gdx.app.log(TAG, "Destroyed a friendly");
                    //  Gdx.app.log("BOUNDARY SYSTEM","Entity: Enemy "+(type.getType() == 2) + "Entity: Friendly "+(type.getType() == 128) +" Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive());
                }
            } else if (type.getType() == TypeComponent.COL_ENEMY) {
                EnemyComponent enemy = Mappers.enemy.get(entity);
                if(!enemy.isDead()) {
                    enemy.setDead(true);
                   // Gdx.app.log(TAG, "Destroyed an enemy");
                    // Gdx.app.log("BOUNDARY SYSTEM","Entity: Enemy "+(type.getType() == 2) + "Entity: Friendly "+(type.getType() == 128) +" Current Group "+ currentGroup + "Body is active "+ body.getBody().isActive());

                }
            } else if( type.getType() == TypeComponent.COL_INTERACTIVE){
                InteractiveComponent interactive = Mappers.interactive.get(entity);
                if(!interactive.isDead()){
                    interactive.setDead(true);
                   // Gdx.app.log(TAG, "Destroyed an interactive");
                }
            }
        }


    }

    private void playMusic(int group){

        SoundComponent sound = Mappers.sound.get(player);

        switch(group){
            case 4:
            case 5:
            case 6:
            case 11:
            case 12:
            case 13:
            case 18:
            case 19:
            case 20:
            case 25:
            case 26:
            case 27:
                sound.addSound(SoundComponent.EVENT.DUNGEON_SONG);
                break;

            default:
                sound.addSound(SoundComponent.EVENT.GAME_SONG);
                break;

        }
    }
}
