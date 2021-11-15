package com.example.libor.midterm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Isiah Libor on 2/8/2018.
 */

public class GameInfo implements Serializable {
    private ArrayList<String> gesture;
    public ArrayList<String> getGesture()
    {
        return gesture;
    }
    public void setGesture (ArrayList<String> gesture)
    {
        this.gesture = gesture;
    }

    private ArrayList<String> top;
    public ArrayList<String> getTop()
    {
        return top;
    }
    public void setTop (ArrayList<String> top)
    {
        this.top = top;
    }
}
