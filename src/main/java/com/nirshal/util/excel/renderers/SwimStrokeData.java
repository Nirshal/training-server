package com.nirshal.util.excel.renderers;

import com.garmin.fit.SwimStroke;

import java.util.HashMap;
import java.util.Map;

public class SwimStrokeData {

    private static Map<SwimStroke, String> lookupText= createMap();

    private static Map<SwimStroke, String> createMap(){
        Map<SwimStroke,String> lookup = new HashMap<SwimStroke,String>();
        lookup.put(SwimStroke.BACKSTROKE, "Dorso");
        lookup.put(SwimStroke.BREASTSTROKE, "Rana");
        lookup.put(SwimStroke.BUTTERFLY, "Delfino");
        lookup.put(SwimStroke.FREESTYLE, "Stile");
        lookup.put(SwimStroke.DRILL, "DRILL");
        lookup.put(SwimStroke.IM, "IM");
        lookup.put(SwimStroke.INVALID, "INVALID");
        lookup.put(SwimStroke.MIXED, "Misti");
        return lookup;
    }
    public static String getText(SwimStroke stroke){
        return lookupText.get(stroke);
    }
}
