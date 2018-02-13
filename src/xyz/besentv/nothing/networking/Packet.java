package xyz.besentv.nothing.networking;

import java.net.DatagramPacket;

public abstract class Packet {

    public enum PacketType {
        INVALID(0x00),
        LOGIN(0x01),
        MESSAGE(0x02),
        LOGOUT(0x03),
        PING(0x04);

        private int magicNumber;

        PacketType(int magicNumber) {
            this.magicNumber = magicNumber;
        }

        public int getMagicNumber() {
            return magicNumber;
        }

        private static PacketType[] values = PacketType.values();

        public static PacketType getPacketType(int i) {
            for (PacketType type : PacketType.values()) {
                if (type.magicNumber == i) {
                    return type;
                }
            }
            return INVALID;
        }
    }

    private PacketHeader packetHeader;
    private String payload;

    public Packet(PacketType packetType) {
        this.packetHeader = new PacketHeader(packetType);
    }

    public Packet(PacketType packetType, String payload) {
        this.packetHeader = new PacketHeader(packetType);
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    protected void setPayload(String payload) {
        this.payload = payload;
    }

    public PacketType getPacketType() {
        return packetHeader.getPacketType();
    }

    public DatagramPacket toDatagramPacket() {
        byte[] rawPayload = (packetHeader.getPacketHeaderAsString() + payload).getBytes();
        return new DatagramPacket(rawPayload, rawPayload.length);
    }

    public static PacketType checkPacketType(DatagramPacket packet) {
        if (packet.getData().length >= 5) {
            String packetPayload = new String(packet.getData());
            PacketType packetType = PacketType.getPacketType(Integer.valueOf(packetPayload.substring(0)).intValue());
            return packetType;
        } else {
            return null;
        }
    }
}