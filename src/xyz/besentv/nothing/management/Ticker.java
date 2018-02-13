package xyz.besentv.nothing.management;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Ticker {

    private int tickrate;
    private long tickDelta;
    private Timer ticker;

    private ArrayList<TickEvent> tickSubscriber = new ArrayList<TickEvent>();

    public Ticker(int tickrate) {
        this.tickrate = tickrate;
        this.tickDelta = 1000L / tickrate;
        this.ticker = new Timer();
    }


    public void addTickSubscriber(TickEvent subscriber) {
        this.tickSubscriber.add(subscriber);
    }

    public void start() {
        ticker.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (TickEvent subscriber : tickSubscriber) {
                    subscriber.onTickEvent(System.currentTimeMillis());
                }
            }
        }, 0, tickDelta);
    }


}
