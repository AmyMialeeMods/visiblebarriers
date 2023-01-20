package xyz.amymialee.visiblebarriers.util;

import net.minecraft.util.Pair;

import java.util.ArrayList;

public class ArrayPairList<K, V> extends ArrayList<Pair<K, V>> {
    public void put(K key, V value) {
        Pair<K, V> pair = this.getPair(key);
        if (pair != null) {
            pair.setRight(value);
        } else {
            this.add(new Pair<>(key, value));
        }
    }

    public V get(K key) {
        for (Pair<K, V> pair : this) {
            if (pair.getLeft().equals(key)) {
                return pair.getRight();
            }
        }
        return null;
    }

    public Pair<K, V> getPair(K key) {
        for (Pair<K, V> pair : this) {
            if (pair.getLeft().equals(key)) {
                return pair;
            }
        }
        return null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = this.get(key);
        return value == null ? defaultValue : value;
    }

    public V findOrDefault(int index, V defaultValue) {
        if (index < this.size() && index >= 0) {
            return this.get(index).getRight();
        }
        return defaultValue;
    }
}