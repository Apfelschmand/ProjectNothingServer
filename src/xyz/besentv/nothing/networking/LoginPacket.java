package xyz.besentv.nothing.networking;

import java.net.DatagramPacket;

public class LoginPacket extends Packet {

    private String loginName;

    public LoginPacket(String loginName) {
        super(PacketType.LOGIN, loginName);
        this.loginName = loginName;
    }

    public LoginPacket(DatagramPacket packet) {
        super(PacketType.LOGIN);
        String data = new String(packet.getData());
        super.setPayload(data.substring(PacketHeader.HEADER_LENGTH, (data.length())));
        this.loginName = getPayload();
    }

    public String getLoginName() {
        return loginName;
    }
}
