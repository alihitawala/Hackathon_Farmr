package com.climate.farmr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class HydrocarbonInformation {
    public static Map<String, Integer> valueInfo = new HashMap<>();
    static {
        valueInfo.put("B/D", 50);
        valueInfo.put("C/D", 40);
        valueInfo.put("B", 60);
        valueInfo.put("C", 45);
    }
}
