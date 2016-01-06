package com.example.dkotsopoulos.testdiploma;

import android.app.Application;


/**
 * Created by DKotsopoulos on 26/09/2015.
 */
public class TestDiplomaApp extends Application {
    private boolean locationServiceStart=false;


    public boolean islocationServiceStart() {
        return locationServiceStart;
    }

    public void setLocationServiceStart(boolean locationServiceStart) {
        this.locationServiceStart = locationServiceStart;
    }



    public TestDiplomaApp() {

    }
}
