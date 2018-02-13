package xyz.besentv.nothing.networking;

public class PingPacket extends Packet {

    public PingPacket() {
        super(PacketType.PING);
    }

}
