package com.example.lenovo.todoupdated;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lenovo on 09-10-2017.
 */

public class Tasks implements Serializable {

    private String event;
    private String venue;
    private int id;

    public Tasks(String event, String venue , int id) {
        this.event = event;
        this.venue = venue;
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id; }




}

