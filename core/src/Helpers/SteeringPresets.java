package Helpers;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.ReachOrientation;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import Components.SteeringComponent;


public class SteeringPresets {
    public static Vector2 wanderTarget;

    public static Wander<Vector2> getWander(SteeringComponent sc){
        Wander<Vector2> wander = new Wander<Vector2>(sc)
                .setFaceEnabled(false)
                .setWanderOffset(50f)
                .setWanderOrientation(0f)

                .setWanderRadius(64f)
                .setWanderRate(MathUtils.PI2 *2);
            wanderTarget = wander.getInternalTargetPosition();

        return wander;
    }

    public static Wander<Vector2> getDragonWander(SteeringComponent sc){
        Wander<Vector2> wander = new Wander<Vector2>(sc)
                .setFaceEnabled(false)
                .setWanderOrientation(90)
                .setWanderOffset(10f)
                .setWanderRadius(16f)
                .setWanderRate(10);
        return wander;
    }

    public static Face<Vector2> getFace(SteeringComponent seeker, SteeringComponent target){
        Face<Vector2> face = new Face<Vector2>(seeker,target).
            setAlignTolerance(0.0001f)
                .setDecelerationRadius(MathUtils.degreesToRadians * 10)
                .setTimeToTarget(0.1f)

                .setEnabled(true);
        return face;

    }

    public static ReachOrientation<Vector2> getReachOrientation(SteeringComponent seeker, SteeringComponent target){
        ReachOrientation<Vector2> reach = new ReachOrientation<Vector2>(seeker, target)
                .setAlignTolerance(0.0001f)
                .setDecelerationRadius(MathUtils.degreesToRadians*10)
                .setTimeToTarget(0.1f)
                .setEnabled(true);
        return reach;
    }

    public static Vector2 getWanderTarget(){
        return wanderTarget;
    }

    public static Seek<Vector2> getSeek(SteeringComponent seeker, SteeringComponent target){
        Seek<Vector2> seek = new Seek<Vector2>(seeker, target);
        return seek;
    }

    public static Flee<Vector2> getFlee(SteeringComponent runner, SteeringComponent chaser){
        Flee<Vector2> flee = new Flee<Vector2>(runner, chaser);
        return flee;
    }
    public static Evade<Vector2> getEvade(SteeringComponent runner, SteeringComponent chaser){
        Evade<Vector2> evade = new Evade<Vector2>(runner, chaser);
        return evade;
    }

    public static Arrive<Vector2> getArrive(SteeringComponent seeker, SteeringComponent target){
        Arrive<Vector2> arrive = new Arrive<Vector2>(seeker, target)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(8f)
                .setDecelerationRadius(32f);
        return arrive;
    }

    public static CollisionAvoidance<Vector2> getCollisionAvoidance(SteeringComponent seeker, Proximity<Vector2> area){
        CollisionAvoidance<Vector2> colAvoid = new CollisionAvoidance<Vector2>(seeker, area);
        return colAvoid;
    }

    public static RaycastObstacleAvoidance<Vector2> getRaycastAvoidance(SteeringComponent seeker,
                                                                        World world, RaycastCollisionDetector raycastCollisionDetector){
        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<Vector2>(seeker,
                new CentralRayWithWhiskersConfiguration<Vector2>(seeker,24/Figures.PPM,
                        20/Figures.PPM,35*MathUtils.degreesToRadians),raycastCollisionDetector, 8f/Figures.PPM);


        return raycastObstacleAvoidance;
    }

    public static RaycastObstacleAvoidance<Vector2> getPlayerRaycastAvoidance(SteeringComponent seeker,
                                                                        World world, RaycastCollisionDetector raycastCollisionDetector){
        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<Vector2>(seeker,
                new CentralRayWithWhiskersConfiguration<Vector2>(seeker,16/Figures.PPM,
                        16/Figures.PPM,45*MathUtils.degreesToRadians),raycastCollisionDetector, 6f/Figures.PPM);


        return raycastObstacleAvoidance;
    }

    public static PrioritySteering<Vector2> getPrioritySteering (SteeringComponent seeker, SteeringComponent target,
                                                                 World world ){
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(seeker,0.0001f);
        prioritySteering.add(getRaycastAvoidance(seeker, world, new Box2dRaycastCollisionDetector(world)));
        prioritySteering.add(getSeek(seeker,target));
        prioritySteering.add(getArrive(seeker, target));



        return prioritySteering;
    }

    public static PrioritySteering<Vector2> getPlayerPrioritySteering (SteeringComponent seeker, SteeringComponent target,
                                                                 World world ){
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(seeker,0.0001f);
        prioritySteering.add(getPlayerRaycastAvoidance(seeker, world, new Box2dRaycastCollisionDetector(world)));
        prioritySteering.add(getSeek(seeker,target));
        prioritySteering.add(getArrive(seeker, target));



        return prioritySteering;
    }

    public static PrioritySteering<Vector2> getArriveSteering (SteeringComponent seeker, SteeringComponent target
                                                                  ){
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(seeker,0.0001f);
        prioritySteering.add(getSeek(seeker,target));
        prioritySteering.add(getArrive(seeker, target));



        return prioritySteering;
    }

    public static PrioritySteering<Vector2> getWanderPrioritySteering (SteeringComponent seeker, World world){
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(seeker, 0.0001f);
        prioritySteering.add(getRaycastAvoidance(seeker, world, new Box2dRaycastCollisionDetector(world)));
        prioritySteering.add(getWander(seeker));
        return prioritySteering;
    }

    public static PrioritySteering<Vector2>getDragonWanderSteering (SteeringComponent seeker, SteeringComponent target){
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(seeker, 0.0001f);


        prioritySteering.add(getDragonWander(seeker));
        prioritySteering.add(getFace(seeker,target));
        return prioritySteering;
    }

    public static BlendedSteering<Vector2> getBlendedSteering(SteeringComponent seeker, SteeringComponent target){
        BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(seeker);
       // blendedSteering.add(getRaycastAvoidance(seeker, world, raycastCollisionDetector),.65f);
        blendedSteering.add(getDragonWander(seeker),.4f);
        blendedSteering.add(getFace(seeker, target),.6f);

        return blendedSteering;
    }

    public static PrioritySteering<Vector2> getPrioritySteering (SteeringComponent seeker, SteeringComponent target){
        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<Vector2>(seeker,0.01f);

        prioritySteering.add(getSeek(seeker,target));
        prioritySteering.add(getArrive(seeker, target));

        return prioritySteering;

    }
}
