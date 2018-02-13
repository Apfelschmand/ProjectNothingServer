package xyz.besentv.nothing.networking;

import java.net.DatagramPacket;

public class MessagePacket extends Packet {

    private String message;

    public MessagePacket(String message) {
        super(PacketType.MESSAGE, message);
        this.message = message;
    }

    public MessagePacket(DatagramPacket packet) {
        super(PacketType.MESSAGE);
        String data = new String(packet.getData());
        super.setPayload(data.substring(PacketHeader.HEADER_LENGTH, (data.length())));
        this.message = getPayload();
    }

    public String getMessage() {
        return message;
    }
}
