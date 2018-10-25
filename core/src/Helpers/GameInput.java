package Helpers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

import Components.BodyComponent;
import Components.PlayerComponent;
import Components.PlayerControlComponent;

public class GameInput implements GestureDetector.GestureListener{
    public static final String TAG = GameInput.class.getSimpleName();

    private OrthographicCamera camera;
    private Viewport viewport;
    private Vector3 touch;
    private Vector3 touch2;
    private float deltaX;
    private float deltaY;
    private Entity player;
    private PlayerControlComponent control;
    private PlayerComponent playerComponent;

  


    public GameInput(Viewport viewport) {
        this.viewport = viewport;
        this.camera = (OrthographicCamera) viewport.getCamera();
        touch = new Vector3(Vector3.Zero);
        touch2 = new Vector3(Vector3.Zero);




    }

    public void setPlayer(Entity player){
        this.player = player;
        if(player == null){
            Gdx.app.log("PLAYER IS NULL","");
        }
        control =Mappers.control.get(this.player);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(count==1){
            touch.x = x;
            touch.y = y;

            viewport.unproject(touch);
            touch.set(MathUtils.floor(touch.x),MathUtils.floor(touch.y),0);
            //Gdx.app.log(TAG,touch.toString() + " current control before setting "+ control.getControl());
            control.setControl(PlayerControlComponent.CONTROL.TAP);
            return true;

        }
       else if(count == 2){
            touch.x = x;
            touch.y = y;
            viewport.unproject(touch);
            control.setControl(PlayerControlComponent.CONTROL.DOUBLETAP);
            return true;

        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        touch.x = x;
        touch.y = y;
        viewport.unproject(touch);


        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        touch.x = x;
        touch.y = y;
        viewport.unproject(touch);
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        touch2.x = x;
        touch2.y = y;
        viewport.unproject(touch2);
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public Vector3 getTouch(){
        return touch;
    }

    public void resetTouch(){
        touch.set(Vector3.Zero);
    }

    public Vector3 getTouch2(){
        return touch2;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }

    public void resetTouch2(){
        touch2.set(Vector3.Zero);
    }

}
