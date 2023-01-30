package com.lhs.service;




import java.util.HashMap;
import java.util.Map;

public interface PoolDataService {

    HashMap<Object, Object> savePoolData(String token);
    HashMap<Object, Object> findPoolDataByUid(String id);
 
}
