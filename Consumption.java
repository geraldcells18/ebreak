package com.example.admin.ebreak;

public class Consumption {

    private int rep_ic;
    private String rep_kwh;
    private String rep_date;
    private String rep_feed;

    public Consumption(int rep_ic, String rep_kwh, String rep_feed, String rep_date) {
        this.rep_ic = rep_ic;
        this.rep_kwh = rep_kwh;
        this.rep_date = rep_date;
        this.rep_feed = rep_feed;
    }

    public int getRep_ic() {
        return rep_ic;
    }

    public void setRep_ic(int rep_ic) {
        this.rep_ic = rep_ic;
    }

    public String getRep_kwh() {
        return rep_kwh;
    }

    public void setRep_kwh(String rep_kwh) {
        this.rep_kwh = rep_kwh;
    }

    public String getRep_date() {
        return rep_date;
    }

    public void setRep_date(String rep_date) {
        this.rep_date = rep_date;
    }

    public String getRep_feed() {
        return rep_feed;
    }

    public void setRep_feed(String rep_feed) {
        this.rep_feed = rep_feed;
    }
}
