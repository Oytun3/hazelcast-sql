package org.example;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.HashMap;
import java.util.Map;

public class Hazelcast {
    public static void main(String[] args) {
        HazelcastInstance hzInstance = com.hazelcast.core.Hazelcast.newHazelcastInstance();
        IMap<Integer, Integer> numbersMap = hzInstance.getMap("numbers");

        measureTime(numbersMap, 20000);
        measureTime(numbersMap, 100000);

        hzInstance.shutdown();
    }

    private static void measureTime(IMap<Integer, Integer> map, int count) {
        try {
            long startTime = System.nanoTime();
            Map<Integer, Integer> batch = new HashMap<>();
            for (int i = 0; i < count; i++) {
                int randomNum = (int) (Math.random() * 100000);
                batch.put(i, randomNum);
            }
            map.putAll(batch);
            long endTime = System.nanoTime();
            System.out.println("HAZELCAST: Time taken to insert " + count + " numbers: " + (endTime - startTime) + " ns");

            startTime = System.nanoTime();
            for (int i = 0; i < count; i++) {
                int num = map.get(i);
            }
            endTime = System.nanoTime();
            System.out.println("HAZELCAST: Time taken to select " + count + " numbers: " + (endTime - startTime) + " ns");

            map.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}