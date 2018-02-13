package xyz.besentv.nothing.networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerSocket {

    private DatagramSocket socket;

    private Thread receiveThread = null;

    public ServerSocket(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void send(final byte[] data, InetAddress destAddress, int destPort) throws IOException {
        send(new DatagramPacket(data, data.length, destAddress, destPort));
    }

    public void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
        System.out.println("Sent " + Integer.toString(packet.getData().length) + " bytes to " + packet.getAddress().toString() + ":" + Integer.toString(packet.getPort()));
    }

    public void startReceive(PacketReceivedEvent eventHandler) {
        if (receiveThread == null) {
            this.receiveThread = new Thread("ProjectNothing-ServerReceiveThread") {
                @Override
                public void run() {
                    System.out.println("Start receive!");
                    while (true) {
                        try {
                            byte[] buffer = new byte[1024];
                            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                            socket.receive(receivePacket);
                            System.out.println("Received " + Integer.toString(receivePacket.getLength()) + " bytes from " + receivePacket.getAddress().toString() + ":" + Integer.toString(receivePacket.getPort()));
                            String s = new String(receivePacket.getData()).trim();
                            eventHandler.onPacketReceived(new DatagramPacket(s.getBytes(), s.getBytes().length, receivePacket.getAddress(), receivePacket.getPort()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            this.receiveThread.start();
        }
    }


}
