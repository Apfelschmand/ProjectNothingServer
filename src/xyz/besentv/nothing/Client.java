package xyz.besentv.nothing;

import java.net.InetAddress;

public class Client {

    public static final long MILLISECONDS_UNTIL_TIMEOUT = 15000;

    private InetAddress clientAddress;
    private int clientPort;
    private String name;
    private long lastTimeConnectedAt = 0;

    public Client(InetAddress clientAddress, int clientPort, String name) {
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.name = name;
        this.lastTimeConnectedAt = System.currentTimeMillis();
    }

    public boolean isThisClient(InetAddress clientAddress, int clientPort) {
        if (this.clientAddress.equals(clientAddress) && (this.clientPort == clientPort)) {
            return true;
        } else {
            return false;
        }
    }

    public void setLastTimeConnectedAt(long time){
        if(time < 0){
            throw new IllegalArgumentException("Time negative.");
        }
        if(time > lastTimeConnectedAt){
            lastTimeConnectedAt = time;
        }
    }

    public boolean timedOut(){
        return timedOut(System.currentTimeMillis());
    }

    public boolean timedOut(long timeNow){
        long dif = timeNow - lastTimeConnectedAt;
        if(dif >= MILLISECONDS_UNTIL_TIMEOUT){
            return true;
        }
        return false;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getName() {
        return name;
    }
}
