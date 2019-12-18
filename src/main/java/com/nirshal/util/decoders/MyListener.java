package com.nirshal.util.decoders;

import com.garmin.fit.SetMesg;
import com.garmin.fit.SetMesgListener;

public class MyListener implements SetMesgListener {
    @Override
    public void onMesg(SetMesg setMesg) {
        System.out.println("Ciao ho trovato un set! ");
    }

}
