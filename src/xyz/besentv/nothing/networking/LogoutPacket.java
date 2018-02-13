package xyz.besentv.nothing.networking;

public class LogoutPacket extends Packet {

    public LogoutPacket(){
        super(PacketType.LOGOUT,new String("Logout"));
    }

}
