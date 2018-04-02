package me.xjcyan1de.cyanbot;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import me.xjcyan1de.cyanbot.events.GenerateAccessKeyEvent;
import me.xjcyan1de.cyanbot.events.BotChatEvent;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.handlers.*;
import me.xjcyan1de.cyanbot.listeners.*;
import me.xjcyan1de.cyanbot.listeners.event.EventSystem;
import me.xjcyan1de.cyanbot.utils.Schedule;
import me.xjcyan1de.cyanbot.world.*;
import me.xjcyan1de.cyanbot.world.Vector;

import javax.annotation.Nullable;
import java.net.Proxy;
import java.util.*;
import java.util.logging.Logger;

public class Bot {

    private int entityId;

    private String username;
    private MinecraftProtocol protocol;

    private Client client;

    private BoundBox boundBox;
    private boolean onGround = true;

    private Server server;
    private Location loc = new Location(0, 0, 0);
    private Vector speed = new Vector();

    private List<Handler> handlers = new ArrayList<>();
    private List<SessionListener> listeners = new ArrayList<>();
    private EventSystem eventSystem;
    private TimerTask timerTask;

    private boolean debug = false;
    private BotManager botManager;
    private Logger logger;

    private String accessKey;
    private List<String> joinCommands;

    public Bot(BotManager botManager, MainFrame mainFrame, Logger logger, String username, String host, int port) {
        this.botManager = botManager;
        this.logger = logger;
        this.protocol = new MinecraftProtocol(username);
        this.username = username;
        this.client = new Client(host, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        this.eventSystem = new EventSystem(logger);

        this.registerEvents();
        this.registerHandlers();
        this.registerListeners(mainFrame, botManager);
    }

    public BotManager getBotManager() {
        return botManager;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    private void registerEvents() {
        this.eventSystem.registerLisneter(new InitEvents(this));
        this.listeners.add(new ChatListener(this));
    }

    private void registerListeners(MainFrame mainFrame, BotManager manager) {
        this.listeners.add(new PacketWorldListener(this));
        this.listeners.add(new ChatToGuiListener(mainFrame));
        this.listeners.add(new CloseConnectionListener(this, manager, logger));
        this.listeners.add(new BotListener(this, eventSystem)); // для системы иветов
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

    public EventSystem getEventSystem() {
        return eventSystem;
    }

    public void startBot() {
        logger.info("Пробуем подключится к " + client.getHost() + ":" + client.getPort() + "...");

        this.boundBox = new BoundBox(0.6, 1.8);

        listeners.forEach(sessionAdapter -> {
            this.getClient().getSession().addListener(sessionAdapter);
        });

        this.client.getSession().connect();

        this.timerTask = Schedule.timer(this::onUpdate, 50, 50);
    }

    public void stopBot() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        // this.getWorld().clearChunks(); // зачем?
    }

    public void sendPacket(Packet packet) {
       /* if (!(packet instanceof ClientPlayerPositionRotationPacket || packet instanceof ClientChatPacket)) {
            System.out.println(username + " > " + packet.toString());
        }*/
        getClient().getSession().send(packet);
    }

    public void sendMessage(String message) {
        final BotChatEvent event = eventSystem.callEvent(new BotChatEvent(this, message));
        if (!event.isCancelled()) {
            sendPacket(new ClientChatPacket(event.getMessage()));
        }
    }

    public void onJoin(ServerJoinGamePacket packet) {

    }

    public void onUpdate() {
        for (Handler handler : handlers) {
            handler.onUpdate();
        }
    }

    public Server getServer() {
        return server;
    }

    public Block getSolidBlock() {
        for (double y = loc.getY(); y > 0; y -= 0.1) {
            Block block = server.getWorld().getBlockAt(loc.getBlockX(), (int) Math.floor(y), loc.getBlockZ());
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

    public String getAccessKey() {
        return accessKey;
    }

    public boolean isClose() {
        return !client.getSession().isConnected();
    }

    @Nullable
    public String generateAccessKey() {
        Random random = new Random();
        this.accessKey = String.valueOf(1000 + random.nextInt(8999));
        if (!eventSystem.callEvent(new GenerateAccessKeyEvent(this, "XjCyan1de", accessKey)).isCancelled()) {
            logger.info("Сгенерирован новый ключ: " + accessKey);
            eventSystem.callEvent(accessKey);
            this.sendMessage("/tell XjCyan1de Ключ: " + accessKey);
            return accessKey;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bot bot = (Bot) o;
        return Objects.equals(username, bot.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Bot{" +
                "entityId=" + entityId +
                ", username='" + username + '\'' +
                '}';
    }

    public void setJoinCommands(List<String> joinCommands) {
        this.joinCommands = joinCommands;
    }

    public List<String> getJoinCommands() {
        return joinCommands;
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
        return server.getWorld();
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
}
