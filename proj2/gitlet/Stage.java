package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Stage implements Serializable {
    Map<String, String> addtion;
    Map<String, String> removal;

    public Stage() {
        this.addtion = new HashMap<>();
        this.removal = new HashMap<>();
    }

    public void store_addtion(String key, String value) {
        this.addtion.put(key, value);
    }

    public void store_removal(String key, String value) {
        this.removal.put(key, value);
    }

    public void remove_addtion(String key) {
        this.addtion.remove(key);
    }

    public void remove_removal(String key) {
        this.removal.remove(key);
    }

    public Map<String, String> get_addtion() {
        return this.addtion;
    }

    public Map<String, String> get_removal() {
        return this.removal;
    }
}