package xyz.besentv.nothing;

import xyz.besentv.nothing.management.TickEvent;
import xyz.besentv.nothing.management.Ticker;
import xyz.besentv.nothing.networking.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

public class Server implements PacketReceivedEvent, TickEvent {

    private ServerSocket socket;
    private Ticker ticker;
    private ArrayList<Client> clients;

    public Server(int port) throws SocketException {
        this.socket = new ServerSocket(port);
        this.socket.startReceive(this);
        this.clients = new ArrayList<Client>();
        this.ticker = new Ticker(5);
        this.ticker.addTickSubscriber(this);
        ticker.start();
    }

    private boolean serverHasClient(InetAddress address, int port) {
        boolean hasClient = false;
        if (getClientByAddress(address, port) != null) {
            hasClient = true;
        }
        return hasClient;
    }

    private void validateClients() throws IOException {
        synchronized (clients){
            Iterator<Client> clientsIter = clients.iterator();
            while(clientsIter.hasNext()){
                Client c = clientsIter.next();
                if(c.timedOut()){
                    clientsIter.remove();
                    sendToAllClients(new MessagePacket(c.getName() + " timed out!").getPayload());
                    System.out.println(c.getName() + " was removed from client list! (TIMED OUT)");
                }
            }
        }
    }

    private void sendToAllClients(String payload) throws IOException {
        synchronized (clients) {
            for (Client c : clients) {
                socket.send(payload.getBytes(), c.getClientAddress(), c.getClientPort());
            }
        }
    }

    private Client getClientByAddress(InetAddress address, int port) {
        synchronized (clients) {
            for (Client c : clients) {
                if (c.isThisClient(address, port)) {
                    return c;
                }
            }
            return null;
        }
    }

    private synchronized void handlePacket(DatagramPacket packet) throws IOException {
        String data = new String(packet.getData());
        if (data.startsWith(PacketHeader.MAGIC_STRING)) {
            Packet.PacketType packetType = PacketHeader.parsePacketHeader(data).getPacketType();
            Client sender = null;
            switch (packetType) {
                case LOGIN: {
                    LoginPacket p = new LoginPacket(packet);
                    if (!serverHasClient(packet.getAddress(), packet.getPort())) {
                        sender = new Client(packet.getAddress(), packet.getPort(), p.getLoginName());
                        clients.add(sender);
                        sendToAllClients(new String(p.getLoginName() + " joined the chat!"));
                        System.out.println("added new client!");
                    }
                    break;
                }
                case MESSAGE: {
                    MessagePacket messagePacket = new MessagePacket(packet);
                    sender = getClientByAddress(packet.getAddress(), packet.getPort());
                    if (sender != null) {
                        sendToAllClients(new String("Message from " + sender.getName() + ": " + messagePacket.getMessage()));
                        break;
                    }
                    System.out.println("Dismissed message from " + packet.getAddress() + ":" + packet.getPort());
                    break;
                }
                case LOGOUT: {
                    sender = getClientByAddress(packet.getAddress(), packet.getPort());
                    if (clients.contains(sender)) {
                        clients.remove(sender);
                        System.out.println("Removed Client " + sender.getName());
                        sendToAllClients(new String(sender.getName() + " left the chat!"));
                    }
                    break;
                }
                case PING: {
                    sender = getClientByAddress(packet.getAddress(), packet.getPort());
                    sender.setLastTimeConnectedAt(System.currentTimeMillis());
                    //socket.send(new PingPacket().toDatagramPacket().getData(),sender.getClientAddress(),sender.getClientPort());
                    break;
                }
                case INVALID:
                    System.out.println("Packet Dismissed!");
                    break;
            }
        }
        else{
            System.out.println("Packet Dismissed!");
        }
    }

    @Override
    public void onPacketReceived(DatagramPacket packet) {
        try {
            if (packet.getData().length >= 5) {
                this.handlePacket(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTickEvent(long tickTime) {
        //System.out.println("Tick at " + Long.toString(tickTime));
        try {
            validateClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
