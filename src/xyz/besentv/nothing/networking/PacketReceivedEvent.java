package xyz.besentv.nothing.networking;

import java.net.DatagramPacket;

public interface PacketReceivedEvent {

    void onPacketReceived(DatagramPacket packet);

}
