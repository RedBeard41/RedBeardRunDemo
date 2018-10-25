package Helpers;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HealthBar extends Actor{

    private TextureRegion image;


    public HealthBar(TextureRegion image) {
        super();
        this.image = image;

        this.setWidth(32);
        this.setHeight(64);
        this.setVisible(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.draw(image, getX(), getY(), 32, 64);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void act(float delta) {
        super.act(delta);


    }
}
