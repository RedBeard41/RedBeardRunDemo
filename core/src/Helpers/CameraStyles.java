package Helpers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class CameraStyles {
    public static void lockOnTarget(OrthographicCamera camera, Vector2 player){

        float playerX = player.x;
        float playerY = player.y;
        Vector3 position = camera.position;
        position.x =playerX;
        position.y = playerY;
        camera.position.set(position);

        camera.update();
    }

    public static OrthographicCamera lerpToTarget(OrthographicCamera camera, Vector2 target, float beginFactor){
        //a+(b-a)*lerp factor
        float targetX = target.x;
        float targetY = target.y;
        float begin = beginFactor;



        Vector3 position = camera.position;

        if(Math.abs(position.x-targetX)<.0001&&Math.abs(position.y-targetY)<.0001){
            lockOnTarget(camera,target);
        }
        else {
            // position.x =camera.position.x+(playerX-camera.position.x)*1f;
            //position.y = camera.position.y +(playerY-camera.position.y)*1f;
            position.x = MathUtils.lerp(position.x, targetX, begin);
            position.y = MathUtils.lerp(position.y, targetY, begin);
            //camera.position.set(position);

            camera.position.set(position);

            camera.update();
        }
        return camera;

    }

    public static OrthographicCamera lockAverageBetweenTargets(OrthographicCamera camera, Vector2 targetA, Vector2 targetB){

        float targetAX = targetA.x;
        float targetAY = targetA.y;

        float targetBX = targetB.x;
        float targetBY = targetB.y;

        Vector3 position = camera.position;
        position.x =(targetAX + targetBX)/2;
        position.y = (targetAY + targetBY)/2;
        camera.position.set(position);

        camera.update();
        return camera;

    }

    public static void lerpAverageBetweenTargets(OrthographicCamera camera, Vector2 targetA, Vector2 targetB){

        float targetAX = targetA.x;
        float targetAY = targetA.y;

        float targetBX = targetB.x;
        float targetBY = targetB.y;

        float avgX = (targetAX + targetBX)/2;
        float avgY = (targetAY + targetBY)/2;

        Vector3 position = camera.position;
        position.x =camera.position.x+(avgX-camera.position.x)*.2f;
        position.y = camera.position.y +(avgY-camera.position.y)*.2f;
        camera.position.set(position);

        camera.update();

    }

    /*public static boolean searchFocalPoints(OrthographicCamera camera, Array<Vector2> focalPoints, Vector2 target, float threshold){




        for(Vector2 point : focalPoints){
            if(target.dst(point)<threshold){
                CameraStyles.lerpToTarget(camera,point);
                return true;
            }
        }
        return false;
    }*/

    public static void boundary (Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;

        if(position.x < startX){
            position.x = startX;
        }

        if(position.y < startY){
            position.y = startY;
        }

        if(position.x > startX + width){
            position.x = startX + width;
        }

        if(position.y > startY + height){
            position.y = startY + height;
        }

        camera.position.set(position);
        camera.update();
    }
}

