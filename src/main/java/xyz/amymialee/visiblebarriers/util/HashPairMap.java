package xyz.amymialee.visiblebarriers.util;

import net.minecraft.util.Pair;

import java.util.HashMap;

public class HashPairMap<K, V, C> extends HashMap<K, Pair<V, C>> {
    public void put(K key, V left, C right) {
        this.put(key, new Pair<>(left, right));
    }

    public V getLeft(K key) {
        return this.get(key).getLeft();
    }

    public C getRight(K key) {
        return this.get(key).getRight();
    }

    public V getLeftOrDefault(K key, V defaultValue) {
        Pair<V, C> pair = this.get(key);
        if (pair != null) {
            V left = pair.getLeft();
            if (left != null) {
                return left;
            }
        }
        return defaultValue;
    }

    public C getRightOrDefault(K key, C defaultValue) {
        Pair<V, C> pair = this.get(key);
        if (pair != null) {
            C right = pair.getRight();
            if (right != null) {
                return right;
            }
        }
        return defaultValue;
    }
}