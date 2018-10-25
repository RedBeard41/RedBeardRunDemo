package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool.Poolable;

public class TypeComponent implements Component, Poolable {


   // public final static short OTHER = 1;
    public final static short COL_LEVEL =  0x0001;
    public final static short COL_PLAYER = 0x0002;
    public final static short COL_WEAPON = 0x0004;
    public final static short COL_INTERACTIVE = 0x0008;


    public final static short COL_OTHER = 0x0010;
   // public final static short COL_ENEMY_WEAPON = 0x0020;
   // public final static short COL_DRAGOONS = 0x0040;
    //public final static short COL_POTION = 0x0060;
    public final static short COL_FRIENDLY = 0x0080;
    public final static short COL_ENEMY =  0x0200;
   // public final static short COL_PLAYER_WEAPON = 0x0400;
   // public final static short COL_TOUCH = 0x0800;
    //public final static short COL_DESTROYABLE = 0x1000;
    //public final static short COL_HEART = 0x2000;
    //public final static short COL_DRAGON= 0x4000;
    //public final static short COL_DRAGON_HEAD = 0xFFFF;


    private short type=COL_OTHER;

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    @Override
    public void reset() {
        type = COL_OTHER;

    }







}