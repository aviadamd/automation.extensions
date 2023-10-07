package org.extensions.rest;

import org.utils.rest.assured.ValidateResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ResponseCollectorRepo {

    private static ConcurrentHashMap<Integer, ValidateResponse> responseCollectorMap = new ConcurrentHashMap<>();

    public void addResponse(Integer key , ValidateResponse responseData) {
        responseCollectorMap.put(key, responseData);
    }

    protected void deleteOne(int key) {
        responseCollectorMap.remove(key);
    }

    protected void deleteAll() {
        responseCollectorMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, ValidateResponse> findByAll() {
        return responseCollectorMap;
    }

    public ValidateResponse findByStepId(Integer findBy) {
        for (Map.Entry<Integer, ValidateResponse> entry : responseCollectorMap.entrySet()) {
            if (entry.getKey().equals(findBy)) {
                return entry.getValue();
            }
        }
        throw new RuntimeException("unable to find response with id number: " + findBy);
    }

}
