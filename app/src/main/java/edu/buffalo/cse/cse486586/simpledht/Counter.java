package edu.buffalo.cse.cse486586.simpledht;

import android.app.Application;

public class Counter extends Application {
     int noofneeded;


    public  int getval() {
        return noofneeded;
    }

    public  void setval(int someVariable) {
        this.noofneeded =someVariable ;
    }
}
