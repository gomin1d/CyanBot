package me.xjcyan1de.cyanbot;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import me.xjcyan1de.cyanbot.handlers.CollisionHandler;
import me.xjcyan1de.cyanbot.handlers.DebugHandler;
import me.xjcyan1de.cyanbot.handlers.GravityHandler;
import me.xjcyan1de.cyanbot.handlers.GroundHandler;
import me.xjcyan1de.cyanbot.world.*;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Player {

    protected boolean chunksLoaded = false;
    protected int entityId;
    protected boolean running = false;
    protected TimerTask task;
    private String username;
    private MinecraftProtocol protocol;
    private Client client;
    private World world;
    private boolean onGround = true;
    private BoundBox boundBox;

    private Location loc = new Location(0, 0, 0);
    private Location prev = new Location(loc);
    private Vector speed = new Vector();

    private List<Handler> handlers = new ArrayList<>();

    public Player(String username) {
        this.protocol = new MinecraftProtocol(username);
        this.username = username;

        this.handlers.add(new GroundHandler(this));
        this.handlers.add(new GravityHandler(this));
        //....

        this.handlers.add(new CollisionHandler(this));
        this.handlers.add(new DebugHandler(this, false));
    }

    public void startBot(String host, int port) {
        chunksLoaded = false;
        running = true;
        try {
            connectServer(host, port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopBot() {
        running = false;
        chunksLoaded = false;
        if (task != null) {
            this.task.cancel();
        }
        this.getWorld().clearChunks();
    }

    public void connectServer(String host, int port) {
        System.out.println("Пробуем подключится к " + host + ":" + port + "...");
        this.client = new Client(host, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        this.client.getSession().connect();

        this.world = new World();
        this.boundBox = new BoundBox(0.6, 1.8);
        this.getClient().getSession().addListener(new PacketListener(this));

    }

    public void sendPacket(Packet packet) {
        if (!running) {
            return;
        }
        if (!(packet instanceof ClientPlayerPositionRotationPacket || packet instanceof ClientChatPacket)) {
            System.out.println(username + " > " + packet.toString());
        }
        getClient().getSession().send(packet);
    }

    public void sendMessage(String message) {
        sendPacket(new ClientChatPacket(message));
        System.out.println(username + " -> " + message);
    }

    public String getUsername() {
        return username;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc.set(loc);
    }

    public World getWorld() {
        return world;
    }

    public BoundBox getBoundBox() {
        return boundBox;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Client getClient() {
        return client;
    }

    public void onJoin(ServerJoinGamePacket packet) {

    }

    public void onUpdate() {
        for (Handler handler : handlers) {
            handler.onUpdate();
        }

        this.move();
    }


    public Block getSolidBlock() {
        for (double y = loc.getY(); y > 0; y -= 0.1) {
            Block block = world.getBlockAt(loc.getBlockX(), (int) Math.floor(y), loc.getBlockZ());
            if (block != null) {
                if (block.getBoundBox().isSolid()) {
                    return block;
                }
            }
        }
        return null;
    }

    private void move() {
        if (!speed.isZero()) {
            if (speed.size() > 8) {
                speed.setSize(8);
            }
        }

        loc.add(speed);

        ClientPlayerPositionRotationPacket packet = new ClientPlayerPositionRotationPacket(this.isOnGround(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.sendPacket(packet);
        prev.set(loc);
    }

    public void onReady() {
        task = Schedule.timer(this::onUpdate, 50, 50);
    }

    public boolean isClose() {
        if (client == null) {
            return true;
        }
        return !client.getSession().isConnected();
    }

    public Vector getSpeed() {
        return speed;
    }
}
