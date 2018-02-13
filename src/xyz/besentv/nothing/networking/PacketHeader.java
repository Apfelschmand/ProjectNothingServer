package xyz.besentv.nothing.networking;

public class PacketHeader {

    public static final String MAGIC_STRING = "Ning";
    public static final int HEADER_LENGTH = 5;

    private Packet.PacketType packetType;

    public PacketHeader(Packet.PacketType packetType){
        this.packetType = packetType;
    }

    public String getPacketHeaderAsString(){
        StringBuilder header = new StringBuilder(MAGIC_STRING);
        header.append(packetType.getMagicNumber());
        return header.toString();
    }

    public Packet.PacketType getPacketType() {
        return packetType;
    }

    public static PacketHeader parsePacketHeader(String packet){
        if(packet.length() < 5){
            return null;
        }else if(packet.length() >= 5){
            if(packet.startsWith(MAGIC_STRING)){
               return new PacketHeader(Packet.PacketType.getPacketType(Integer.valueOf(packet.substring(4,5))));
            }
            else{
                return null;
            }
        }
        return null;
    }
}
