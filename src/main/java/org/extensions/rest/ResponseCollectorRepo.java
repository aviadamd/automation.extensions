package org.extensions.rest;

import org.utils.rest.assured.RestAssuredValidateResponse;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseCollectorRepo {

    private static ConcurrentHashMap<Integer, RestAssuredValidateResponse> responseCollectorMap = new ConcurrentHashMap<>();

    public void addResponse(Integer key , RestAssuredValidateResponse responseData) {
        responseCollectorMap.put(key, responseData);
    }

    protected void deleteOne(int key) {
        responseCollectorMap.remove(key);
    }

    protected void deleteAll() {
        responseCollectorMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, RestAssuredValidateResponse> findByAll() {
        return responseCollectorMap;
    }

    public RestAssuredValidateResponse findById(Integer findBy) {
        for (Map.Entry<Integer, RestAssuredValidateResponse> byId: responseCollectorMap.entrySet()) {
            if (byId.getKey().equals(findBy)) {
               return byId.getValue();
            }
        }
        throw new RuntimeException("unable to find response with id number: " + findBy);
    }
}
