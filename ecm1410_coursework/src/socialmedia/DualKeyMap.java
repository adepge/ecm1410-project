package socialmedia;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * The DualKeyMap class is used to map two keys to one value
 * and is based off {@link HashMap} objects and methods.
 * There are several assumptions made for this generic class,
 * including that for every {@link Value} there is a given {@link FirstKey}
 * and {@link SecondKey}.
 *
 * @author Adam George
 * @version 17-03-2023
 */
public class DualKeyMap<FirstKey, SecondKey, Value> implements Serializable {

    /** Key-value pair of {@link FirstKey} to {@link Value}. */
    private final Map<FirstKey, Value> firstMap = new HashMap<>();

    /** Key-value pair of {@link SecondKey} to {@link Value}. */
    private final Map<SecondKey, Value> secondMap = new HashMap<>();

    /**
     * Associates the specified value with the specified keys in this map.
     * If the map previously contained a specific mapping for both of the keys,
     * the old value is replaced by the specified value.
     *
     * @param firstKey first key with which the specified value is to be associated.
     * @param secondKey second key with which the specified value is to be associated.
     * @param value value to be associated with both keys.
     */
    public void put(FirstKey firstKey, SecondKey secondKey, Value value) {
        firstMap.put(firstKey,value);
        secondMap.put(secondKey,value);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or null if this map contains no mapping for the key.
     *
     * @param key the key of either {@link FirstKey} or {@link SecondKey} type.
     * @return the value associated with the key.
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public Value get(Object key) {
        if (firstMap.containsKey(key)) {
            return firstMap.get(key);
        } else {
            return secondMap.getOrDefault(key, null);
        }
    }

    /**
     * Removes the mapping for a key from this map if it exists.
     * @param key the key of either {@link FirstKey} or {@link SecondKey} type.
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public void remove(Object key) {
        firstMap.remove(key);
        secondMap.remove(key);
    }

    /**
     * Returns true if a mapping exists for the key specified.
     * Otherwise, returns false.
     *
     * @param key the key of either {@link FirstKey} or {@link SecondKey} type.
     * @return true if a mapping exists for the specified key in this map.
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean containsKey(Object key) {
        return firstMap.containsKey(key) || secondMap.containsKey(key);
    }

    /**
     * Returns the number of key-value pairs in this map.
     * @return size of map.
     */
    public int size() {
        return firstMap.size();
    }

    /**
     * Returns the values in the map viewed as a {@link Collection}.
     * @return all values in map.
     */
    public Collection<Value> values() {
        return firstMap.values();
    }

    /**
     * Removes all key-value pairs in the map.
     */
    public void clear(){
        firstMap.clear();
        secondMap.clear();
    }
}


