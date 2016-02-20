package com.climate.farmr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by aliHitawala on 2/20/16.
 */
public class CropInformation {
    public static List<String> crops = new ArrayList<>();
    public static Map<String, Set<String>> cropToSoilType = new HashMap<>();
    public static Map<String, Integer> cropToYieldPerAcre = new HashMap<>();
    public static final String POTATO = "Potato";
    public static final String RICE = "Rice";
    public static final String WHEAT = "Wheat";
    public static final String SOYBEAN = "Soybean";
    public static final String CORN = "Corn";
    public static final String LOAM_SOIL_TYPE = "Loam";
    public static final String SILT_LOAM_SOIL_TYPE = "Silt Loam";
    public static final String CLAY_LOAM_SOIL_TYPE = "Clay Loam";
    public static final String SILTY_CLAY_LOAM = "Silty Clay Loam";

    static {
        crops.add(POTATO);
        crops.add(WHEAT);
        crops.add(CORN);
        crops.add(SOYBEAN);
        cropToSoilType.put(POTATO, new HashSet<String>());
        cropToSoilType.get(POTATO).add(LOAM_SOIL_TYPE);
        cropToSoilType.get(POTATO).add(SILT_LOAM_SOIL_TYPE);

        cropToSoilType.put(RICE, new HashSet<String>());
        cropToSoilType.get(RICE).add(SILTY_CLAY_LOAM);
        cropToSoilType.get(RICE).add(CLAY_LOAM_SOIL_TYPE);

        cropToSoilType.put(SOYBEAN, new HashSet<String>());
        cropToSoilType.get(SOYBEAN).add(LOAM_SOIL_TYPE);

        cropToSoilType.put(CORN, new HashSet<String>());
        cropToSoilType.get(CORN).add(LOAM_SOIL_TYPE);

        cropToSoilType.put(WHEAT, new HashSet<String>());
        cropToSoilType.get(WHEAT).add(LOAM_SOIL_TYPE);
        cropToSoilType.get(WHEAT).add(SILT_LOAM_SOIL_TYPE);
        cropToSoilType.get(WHEAT).add(SILTY_CLAY_LOAM);
        cropToSoilType.get(WHEAT).add(CLAY_LOAM_SOIL_TYPE);

        cropToYieldPerAcre.put(POTATO, 325);
        cropToYieldPerAcre.put(CORN, 129);
        cropToYieldPerAcre.put(SOYBEAN, 43);
        cropToYieldPerAcre.put(WHEAT, 69);
    }
}

/*
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Soil Type :::: Loam
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Soil Type :::: Clay Loam
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Soil Type :::: Silt Loam
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Soil Type :::: Silty Clay Loam

02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Hydrogroup :::: B/D
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Hydrogroup :::: C/D
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Hydrogroup :::: B
02-20 13:39:22.133 25495-25495/com.climate.farmr D/Farms_Activity: Hydrogroup :::: C
 */
