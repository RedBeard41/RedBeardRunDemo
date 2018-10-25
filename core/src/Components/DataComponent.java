package Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

import java.util.HashMap;

public class DataComponent implements Component, Poolable {

    private HashMap <String, Float> data =
            new HashMap<String, Float>();

    public float getData(String key) {
        return data.get(key);
    }



    public void setData(String key, float entry) {
        this.data.put(key, entry);
    }

    @Override
    public void reset() {
        data.clear();



    }
}

