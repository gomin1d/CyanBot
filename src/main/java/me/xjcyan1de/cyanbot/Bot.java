package me.xjcyan1de.cyanbot;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import me.xjcyan1de.cyanbot.commands.CommandTest;
import me.xjcyan1de.cyanbot.commands.CommandWhere;
import me.xjcyan1de.cyanbot.commands.CommandYou;
import me.xjcyan1de.cyanbot.commands.command.CommandSystem;
import me.xjcyan1de.cyanbot.events.GenerateAccessKeyEvent;
import me.xjcyan1de.cyanbot.events.BotChatEvent;
import me.xjcyan1de.cyanbot.gui.MainFrame;
import me.xjcyan1de.cyanbot.handlers.*;
import me.xjcyan1de.cyanbot.listeners.*;
import me.xjcyan1de.cyanbot.listeners.event.EventSystem;
import me.xjcyan1de.cyanbot.utils.schedule.Schedule;
import me.xjcyan1de.cyanbot.world.*;
import me.xjcyan1de.cyanbot.world.Vector;

import javax.annotation.Nullable;
import java.net.Proxy;
import java.util.*;
import java.util.logging.Logger;

/**
 * Бот ебаный в рот
 */
public class Bot {

    private int entityId;

    private String username;
    private MinecraftProtocol protocol;

    private Client client;

    private BoundBox boundBox;
    private boolean onGround = true;

    private Server server;
    private final Location loc = new Location(0, 0, 0);
    private final Vector speed = new Vector();

    private List<Handler> handlers = new ArrayList<>();
    private EventSystem eventSystem;
    private CommandSystem commandSystem;
    private TimerTask timerTask;

    private boolean debug = false;
    private BotManager botManager;
    private Logger logger;

    private List<String> joinCommands;

    private String accessKey;
    private List<String> accessPlayers = new ArrayList<>(1);
    private boolean close = false;

    public Bot(BotManager botManager, MainFrame mainFrame, Logger logger, String username, String host, int port, Server server) {
        this.botManager = botManager;
        this.logger = logger;
        this.protocol = new MinecraftProtocol(username);
        this.username = username;
        this.server = server;
        this.client = new Client(host, port, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        this.eventSystem = new EventSystem(logger);
        this.commandSystem = new CommandSystem(this, logger);

        this.registerEvents(); // регистрация ивентов
        this.registerHandlers(); // регистрация handler'ов
        this.registerListeners(mainFrame); // регистрация ивентов (ванильная)
        this.registerCommands(); //регистрация команд
    }

    private void registerCommands() {
        this.commandSystem.registerCommand(new CommandYou());
        this.commandSystem.registerCommand(new CommandWhere());
        this.commandSystem.registerCommand(new CommandTest());
    }

    private void registerEvents() {
        this.eventSystem.registerLisneter(new InitEvents(this));
        this.eventSystem.registerLisneter(new AccessEvents(this));
        this.eventSystem.registerLisneter(new CommandEvents(this, logger));
        this.eventSystem.registerLisneter(new CommandSystemEvents(this, commandSystem));
        this.eventSystem.registerLisneter(new PacketWorldEntityEvents(this));
    }

    private void registerListeners(MainFrame mainFrame) {
        this.client.getSession().addListener(new PacketWorldListener(this));
        this.client.getSession().addListener(new ChatToGuiListener(mainFrame));
        this.client.getSession().addListener(new CloseConnectionListener(this, botManager, logger));
        this.client.getSession().addListener(new BotListener(this, eventSystem)); // для системы иветов
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
        try {
            logger.info("Пробуем подключится к " + client.getHost() + ":" + client.getPort() + "...");

            this.boundBox = new BoundBox(0.6, 1.8);

            this.client.getSession().connect();

            this.timerTask = Schedule.timer(this::onUpdate, 50, 50);
        } catch (Exception e) {
            close = true;
            throw e;
        }
    }

    public void stopBot() {
        close = true;
        if (this.client.getSession().isConnected()) {
            Schedule.later(()->this.client.getSession().disconnect("Final Kick"), 50);
        }

        Schedule.cancel(timerTask);
    }


    public BotManager getBotManager() {
        return botManager;
    }

    public void sendPacket(Packet packet) {
        getClient().getSession().send(packet);
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void sendMessage(String message) {
        final BotChatEvent event = eventSystem.callEvent(new BotChatEvent(this, message));
        if (!event.isCancelled()) {
            sendPacket(new ClientChatPacket(event.getMessage()));
        }
    }

    public List<String> getAccessPlayers() {
        return accessPlayers;
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
        return close && !client.getSession().isConnected();
    }

    @Nullable
    public String generateAccessKey() {
        this.accessKey = null;

        Random random = new Random();
        String newKey = String.valueOf(1000 + random.nextInt(8999));
        final GenerateAccessKeyEvent event = eventSystem.callEvent(
                new GenerateAccessKeyEvent(this,  newKey));
        if (!event.isCancelled()) {
            this.accessKey = newKey = event.getKey();
            logger.info("Сгенерирован новый ключ: " + newKey);
            return newKey;
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

    public boolean hasAccess(String player) {
        return accessPlayers.stream()
                .anyMatch(s -> s.equalsIgnoreCase(player));
    }

    public void disconnect(String reason) {
        client.getSession().disconnect(reason);
    }
}
