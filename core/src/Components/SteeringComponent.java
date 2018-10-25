package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool.Poolable;

import Helpers.Box2dLocation;

public class SteeringComponent implements Steerable<Vector2>, Component,Poolable{

    public void setBoundingRadius(float boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    public static enum SteeringState {
        WANDER, PATROL, EVADE, ARRIVE, ATTACK, NONE, HIT}


    public SteeringState currentMode = SteeringState.NONE;

    private Body body;

    //Steering Data
    private float maxLinearSpeed =150f;
    private float maxLinearAcceleration= 100f;
    private float maxAngularSpeed =10f;
    private float maxAngularAcceleration =5f;
    private float zeroThreshold = 0.1f;

    public SteeringBehavior<Vector2> steeringBehavior;
    private Vector2 steeringVector = new Vector2();
    public SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(steeringVector);
    private float boundingRadius = 8f;
    private boolean tagged = true;
    private boolean independentFacing = false;


    public void update (float delta){
        if(steeringBehavior != null){
           // Gdx.app.log("STEERING BEHAVIOR",""+steeringBehavior.calculateSteering(steeringOutput).isZero());
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
        }
    }

    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime){
        boolean anyAccelerations = false;

        if(!steeringOutput.linear.isZero()){
            Vector2 force = steeringOutput.linear.scl(deltaTime);
            body.applyForceToCenter(force, true);
            anyAccelerations = true;
        }

        if(isIndependentFacing()){
            if(steeringOutput.angular != 0){
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        }
        else{
            Vector2 linVel = getLinearVelocity();
            if(!linVel.isZero(getZeroLinearSpeedThreshold())){
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity())*deltaTime);
                body.setTransform(body.getPosition(), newOrientation);
            }


        }

        if(anyAccelerations){
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if(currentSpeedSquare > (maxLinearSpeed*maxLinearSpeed)){
                body.setLinearVelocity(velocity.scl(maxLinearSpeed/ (float) Math.sqrt(currentSpeedSquare)));
            }

            float maxAngleVelocity = getMaxAngularSpeed();
            if(body.getAngularVelocity() > maxAngleVelocity){
                body.setAngularVelocity(maxAngleVelocity);
            }
        }


    }

    public boolean isIndependentFacing (){
        return independentFacing;
    }

    public void setIndependentFacing (boolean independentFacing){
        this.independentFacing = independentFacing;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroThreshold = value;

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(),orientation);
    }

    @Override
    public float vectorToAngle(Vector2 vector) {

        return (float)Math.atan2(-vector.x,vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);

        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }



    @Override
    public void reset() {
        currentMode = SteeringState.NONE;
        body = null;

        maxLinearSpeed =35f;
        maxLinearAcceleration= 35f;
        maxAngularSpeed =50f;
        maxAngularAcceleration =5f;
        zeroThreshold = 0.1f;
        steeringBehavior = null;
        steeringVector = Vector2.Zero;
        steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
        boundingRadius = 8f;
        tagged = true;
        independentFacing = true;

    }
}
