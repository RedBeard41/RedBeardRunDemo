package Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.IntMap;

public class ParticleEffectsManager {

    public static final int SMOKE = 0;
    public static final int FIRE = 1;

    private IntMap<ParticleEffectPool> partyEffectPool;
    private ParticleEffectPool poolSmoke;
    private ParticleEffectPool poolFire;
    private ParticleEffect smokeProto;
    private ParticleEffect fireProto;

    public ParticleEffectsManager() {
        //partyEffects = new IntMap<>();
        partyEffectPool =  new IntMap<>();
        //smokeProto = new ParticleEffect();
        //fireProto = new ParticleEffect();
    }

    public void createPrototype (ParticleEffect prototype, int type, float scale){
        if(!partyEffectPool.containsKey(type)) {
            switch (type) {
                case 0:
                    smokeProto = prototype;
                    smokeProto.scaleEffect(scale);
                    break;
                case 1:
                    fireProto = prototype;
                    fireProto.scaleEffect(scale);
                    break;
                default:
                    break;
            }
        }

    }



    public void addParticleEffect(int type ){
        addParticleEffect(type,  3, 10);
    }


    public void addParticleEffect(int type, int startCapacity, int maxCapacity){

        Gdx.app.log("Particle Effects Manager", "scale should only happen once ");
        if(!partyEffectPool.containsKey(type)) {
            switch (type) {
                case 0:
                    poolSmoke = new ParticleEffectPool(smokeProto, startCapacity, maxCapacity);
                    partyEffectPool.put(type, poolSmoke);
                    break;
                case 1:
                    poolFire = new ParticleEffectPool(fireProto, startCapacity, maxCapacity);
                    partyEffectPool.put(type, poolFire);
                    break;
            }
        }

        //partyEffects.put(type, party);



    }

    public PooledEffect getPooledParticleEffect (int type){
        return partyEffectPool.get(type).obtain();
    }

    public void dispose(){
        fireProto.dispose();
        smokeProto.dispose();
        partyEffectPool.clear();
        //partyEffects.clear();
    }


}
