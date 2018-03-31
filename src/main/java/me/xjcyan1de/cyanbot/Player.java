package me.xjcyan1de.cyanbot;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.handlers.*;
import me.xjcyan1de.cyanbot.listeners.ChatListener;
import me.xjcyan1de.cyanbot.listeners.ChatToGuiListener;
import me.xjcyan1de.cyanbot.listeners.CloseConnectionListener;
import me.xjcyan1de.cyanbot.listeners.PacketListener;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.world.*;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Player {

    private int entityId;

    private String username;
    private Logger logger;
    private MinecraftProtocol protocol;

    private Client client;

    private BoundBox boundBox;
    private boolean onGround = true;

    private World world;
    private Location loc = new Location(0, 0, 0);
    private Vector speed = new Vector();

    private List<Handler> handlers = new ArrayList<>();
    private List<SessionAdapter> listeners = new ArrayList<>();

    private TimerTask timerTask;
    private boolean debug = false;

    public Player(PlayerManager manager, MainFrame mainFrame, Logger logger, String username) {
        this.logger = logger;
        this.protocol = new MinecraftProtocol(username);
        this.username = username;

        this.registerHandlers();
        this.registerListeners(mainFrame, manager);
    }

    private void registerListeners(MainFrame mainFrame, PlayerManager manager) {
        this.listeners.add(new PacketListener(this));
        this.listeners.add(new ChatListener(this));
        this.listeners.add(new ChatToGuiListener(mainFrame));
        this.listeners.add(new CloseConnectionListener(this, manager, logger));
    }

    private void registerHandlers() {
        this.handlers.add(new GroundHandler(this));
        this.handlers.add(new GravityHandler(this));
        //....

        this.handlers.add(new CollisionHandler(this));

        this.handlers.add(new ValidateHandler(this));
        this.handlers.add(new UpdatePositionHandler(this));
        this.handlers.add(new DebugHandler(this));
    }

    public void startBot(String host, int port) {
        try {
            connectServer(host, port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.timerTask = Schedule.timer(this::onUpdate, 50, 50);
    }

    public void stopBot() {
        if (timerTask != null) {
            timerTask.cancel();
        }
       // this.getWorld().clearChunks(); // зачем?
    }

    public void connectServer(String host, int port) {
        logger.info("Пробуем подключится к " + host + ":" + port + "...");
        this.client = new Client(host, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        this.client.getSession().connect();

        this.world = new World();
        this.boundBox = new BoundBox(0.6, 1.8);

        listeners.forEach(sessionAdapter -> {
            this.getClient().getSession().addListener(sessionAdapter);
        });
    }

    public void sendPacket(Packet packet) {
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

    public Vector getSpeed() {
        return speed;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
