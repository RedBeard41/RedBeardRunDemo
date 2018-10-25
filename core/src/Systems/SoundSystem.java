package Systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Queue;

import Components.SoundComponent;
import Components.SoundComponent.EVENT;
import Components.TypeComponent;
import Helpers.Mappers;
import Managers.MyAssetManager;

public class SoundSystem extends IteratingSystem {
    private Entity player;
    private SoundComponent sound;
    private Queue<SoundComponent.EVENT> soundList;
    private Sound playerHit;
    private Sound enemyHit;
    private Sound playerAttackSword;
    private Sound playerAttackRange;
    private Sound destroyedPlant;
    private Sound destroyedBlock;
    private Sound secretDiscovery;
    private Sound rovingTalking;
    private Sound staticTalking;
    private Sound staticLeaving;
    private Sound rovingLeaving;
    private Sound enemyDeath;
    private Sound playerDeath;
    private Sound dragonRoar;
    private Sound dragonAttack;
    private Sound dragonHit;
    private Sound dragonDying;
    private Sound coinDrop;
    private Sound coinPickup;
    private Sound itemPickup;
    private Music gameSong;
    private Music dungeonSong;

    /*PLAYER_HIT,ENEMY_HIT,PLAYER_ATTACK_SWORD,PLAYER_ATTACK_RANGE,
        DESTROYED_PLANT,DESTROYED_BLOCK,SECRET_AREA_DISCOVERY,
        ROVING_NPC_TALKING,STATIC_NPC_TALKING,STATIC_NPC_LEAVING,ROVING_NPC_LEAVING,ENEMY_DYING,
        PLAYER_DYING,DRAGON_ROAR,DRAGON_ATTACK,DRAGON_HIT,DRAGON_DYING,COIN_DROP,ITEM_PICKUP*/

    public SoundSystem(MyAssetManager myAssetManager) {
        super(Family.all(SoundComponent.class).get());

        playerHit = myAssetManager.getSoundAsset("sounds/weird_sound.wav");
        enemyHit = myAssetManager.getSoundAsset("sounds/enemy_Hit.wav");
        playerAttackSword = myAssetManager.getSoundAsset("sounds/Sword_attack.wav");
        playerAttackRange =myAssetManager.getSoundAsset("sounds/rangeAttack.wav");
        destroyedPlant = myAssetManager.getSoundAsset("sounds/plant_destroyed.wav");
        destroyedBlock = myAssetManager.getSoundAsset("sounds/destroyedBlock.wav");
        secretDiscovery = myAssetManager.getSoundAsset("sounds/discovery.wav");
        rovingTalking=myAssetManager.getSoundAsset("sounds/rovingTalking.wav");
        staticTalking=myAssetManager.getSoundAsset("sounds/staticTalking.wav");
        staticLeaving=myAssetManager.getSoundAsset("sounds/staticLeaving.wav");
        rovingLeaving =myAssetManager.getSoundAsset("sounds/rovingLeaving.wav");
        enemyDeath =myAssetManager.getSoundAsset("sounds/enemyDeath.wav");
        playerDeath  =myAssetManager.getSoundAsset("sounds/playerDeath.wav");
        dragonRoar = myAssetManager.getSoundAsset("sounds/dragonRoar.wav");
        dragonAttack = myAssetManager.getSoundAsset("sounds/dragonAttack.wav");
        dragonHit = myAssetManager.getSoundAsset("sounds/dragonHit.wav");;
        dragonDying= myAssetManager.getSoundAsset("sounds/dragonDeath.wav");
        coinDrop = myAssetManager.getSoundAsset("sounds/Coin_Drop.wav");
        coinPickup = myAssetManager.getSoundAsset("sounds/Coin_Pickup.wav");
        itemPickup = myAssetManager.getSoundAsset("sounds/Health_Collect.wav");
        gameSong = myAssetManager.getMusicAsset("sounds/Game Song.wav");
        dungeonSong = myAssetManager.getMusicAsset("sounds/Dungeon Song.wav");




    }

    /*public SoundSystem(Entity player, MyAssetManager myAssetManager) {

        this.player = player;
        sound = Mappers.sound.get(player);
        if(sound.getSoundList()==null){
            Gdx.app.log("SOUND SYSTEM", "sound list is null");
            return;
        }
        else {
            soundList = sound.getSoundList();
        }

        playerHit = myAssetManager.getSoundAsset("sounds/weird_sound.wav");
        enemyHit = myAssetManager.getSoundAsset("sounds/enemy_Hit.wav");
        playerAttackSword = myAssetManager.getSoundAsset("sounds/Sword_attack.wav");
        playerAttackRange =myAssetManager.getSoundAsset("sounds/rangeAttack.wav");
        destroyedPlant = myAssetManager.getSoundAsset("sounds/plant_destroyed.wav");
        destroyedBlock = myAssetManager.getSoundAsset("sounds/destroyedBlock.wav");
        secretDiscovery = myAssetManager.getSoundAsset("sounds/discovery.wav");
        rovingTalking=myAssetManager.getSoundAsset("sounds/rovingTalking.wav");
        staticTalking=myAssetManager.getSoundAsset("sounds/staticTalking.wav");
        staticLeaving=myAssetManager.getSoundAsset("sounds/staticLeaving.wav");
        rovingLeaving =myAssetManager.getSoundAsset("sounds/rovingLeaving.wav");
        enemyDeath =myAssetManager.getSoundAsset("sounds/enemyDeath.wav");
        playerDeath  =myAssetManager.getSoundAsset("sounds/playerDeath.wav");
        dragonRoar = myAssetManager.getSoundAsset("sounds/dragonRoar.wav");
        dragonAttack = myAssetManager.getSoundAsset("sounds/dragonAttack.wav");
        dragonHit = myAssetManager.getSoundAsset("sounds/dragonHit.wav");;
        dragonDying= myAssetManager.getSoundAsset("sounds/dragonDeath.wav");
        coinDrop = myAssetManager.getSoundAsset("sounds/Coin_Drop.wav");
        coinPickup = myAssetManager.getSoundAsset("sounds/Coin_Pickup.wav");
        itemPickup = myAssetManager.getSoundAsset("sounds/Health_Collect.wav");
        gameSong = myAssetManager.getMusicAsset("sounds/Game Song.wav");
        dungeonSong = myAssetManager.getMusicAsset("sounds/Dungeon Song.wav");


    }*/

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        SoundComponent sound = Mappers.sound.get(entity);


        if(sound.getSoundList()==null){
            TypeComponent type = Mappers.type.get(entity);
           // Gdx.app.log("SOUND SYSTEM", "sound list is null " + type.getType());
            return;
        }
        else {
            soundList = sound.getSoundList();


            if (soundList.size < 1) {
                return;
            } else {
               // Gdx.app.log("Sound System", soundList.toString());
                EVENT event = soundList.removeLast();

                switch (event) {
                    case PLAYER_HIT:
                        playerHit.play(.7f);
                        break;
                    case PLAYER_DYING:
                        playerDeath.play(.8f);
                        break;
                    case PLAYER_ATTACK_RANGE:
                        playerAttackRange.play(.6f);
                        break;
                    case PLAYER_ATTACK_SWORD:
                        playerAttackSword.play(.6f);
                        break;

                    case COIN_DROP:
                        coinDrop.play(.6f);
                        break;
                    case COIN_PICKUP:
                        coinPickup.play(.3f);
                        break;
                    case ITEM_PICKUP:
                        itemPickup.play(.6f);
                        break;

                    case ENEMY_HIT:
                        enemyHit.play(.7f);
                        break;
                    case ENEMY_DYING:
                        enemyDeath.play(.7f);
                        break;

                    case DRAGON_HIT:
                        dragonHit.play(.8f);
                        break;
                    case DRAGON_ROAR:
                        dragonRoar.play(.2f);
                        break;
                    case DRAGON_DYING:
                        dragonDying.play(.8f);
                        break;
                    case DRAGON_ATTACK:


                        dragonAttack.play(.5f);
                        break;


                    case DESTROYED_BLOCK:
                        destroyedBlock.play(.7f);
                        break;
                    case DESTROYED_PLANT:
                        destroyedPlant.play(1.0f);
                        break;

                    case ROVING_NPC_LEAVING:
                        //rovingTalking.stop();
                        rovingLeaving.play(.7f);
                        break;
                    case ROVING_NPC_TALKING:
                        rovingTalking.play(.6f);
                        break;

                    case STATIC_NPC_LEAVING:
                        //staticTalking.stop();
                        staticLeaving.play(.9f);
                        break;
                    case STATIC_NPC_TALKING:
                        staticTalking.play(.7f);

                        break;


                    case SECRET_AREA_DISCOVERY:
                        secretDiscovery.play(.8f);
                        break;
                    case DUNGEON_SONG:
                        if (dungeonSong.isPlaying()) {
                            return;
                        }
                        gameSong.stop();
                        dungeonSong.setVolume(.7f);
                        dungeonSong.setLooping(true);
                        dungeonSong.play();
                        break;

                    case GAME_SONG:
                        if (gameSong.isPlaying()) {
                            return;
                        }
                        dungeonSong.stop();
                        gameSong.setVolume(.6f);
                        gameSong.setLooping(true);
                        gameSong.play();
                        break;
                }

            }
        }






    }

    public Music getGameSong() {
        return gameSong;
    }

    public Music getDungeonSong() {
        return dungeonSong;
    }
}
