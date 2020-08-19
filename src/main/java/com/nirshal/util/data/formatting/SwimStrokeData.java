package com.nirshal.util.data.formatting;

import com.garmin.fit.SwimStroke;

import java.util.HashMap;
import java.util.Map;

public class SwimStrokeData {

    private static Map<String, String> lookupText= createMap();

    private static Map<String, String> createMap(){
        Map<String,String> lookup = new HashMap<String,String>();
        lookup.put(SwimStroke.BACKSTROKE.name(), "Dorso");
        lookup.put(SwimStroke.BREASTSTROKE.name(), "Rana");
        lookup.put(SwimStroke.BUTTERFLY.name(), "Delfino");
        lookup.put(SwimStroke.FREESTYLE.name(), "Stile");
        lookup.put(SwimStroke.DRILL.name(), "DRILL");
        lookup.put(SwimStroke.IM.name(), "IM");
        lookup.put(SwimStroke.INVALID.name(), "INVALID");
        lookup.put(SwimStroke.MIXED.name(), "Misti");
        return lookup;
    }
    public static String getText(String stroke){
        return lookupText.get(stroke);
    }
}
