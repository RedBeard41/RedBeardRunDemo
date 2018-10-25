package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component, Poolable{
    private TextureRegion region = null;



    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
    }

    @Override
    public void reset() {
        region = null;
    }
}
