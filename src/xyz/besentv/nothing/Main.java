package xyz.besentv.nothing;

import java.net.SocketException;

public class Main {
    public static void main(String[] args) {
        try {
            Server s = new Server(15084);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
