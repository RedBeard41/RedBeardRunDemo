package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Queue;

public class SoundComponent implements Poolable, Component {

    public enum EVENT {PLAYER_HIT,ENEMY_HIT,PLAYER_ATTACK_SWORD,PLAYER_ATTACK_RANGE,
        DESTROYED_PLANT,DESTROYED_BLOCK,SECRET_AREA_DISCOVERY,
        ROVING_NPC_TALKING,STATIC_NPC_TALKING,STATIC_NPC_LEAVING,ROVING_NPC_LEAVING,ENEMY_DYING,
        PLAYER_DYING,DRAGON_ROAR,DRAGON_ATTACK,DRAGON_HIT,DRAGON_DYING,COIN_DROP,COIN_PICKUP,ITEM_PICKUP,DUNGEON_SONG,GAME_SONG}

    private Queue<EVENT> soundList = new Queue(8);

    public void addSound(EVENT event){
        soundList.addFirst(event);
    }



    public Queue<EVENT> getSoundList() {
        return this.soundList;
    }

    @Override
    public void reset() {
        soundList.clear();

    }
}
