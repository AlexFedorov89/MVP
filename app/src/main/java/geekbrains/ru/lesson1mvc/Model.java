package geekbrains.ru.lesson1mvc;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private Map<Integer, Integer> storage = new HashMap<>();

    public int getElementValueAtIndex(@NonNull int index) {
        if (!storage.containsKey(index)) {
            storage.put(index, 0);
        }
 
        return storage.get(index);
    }

    public void setElementValueAtIndex(int index, int value) {
        storage.put(index, value);
    }
}

