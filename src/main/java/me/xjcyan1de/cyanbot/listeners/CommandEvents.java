package me.xjcyan1de.cyanbot.listeners;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.events.CommandEvent;
import me.xjcyan1de.cyanbot.events.PlayerChatEvent;
import me.xjcyan1de.cyanbot.listeners.event.EventHandler;
import me.xjcyan1de.cyanbot.listeners.event.Listener;
import me.xjcyan1de.cyanbot.utils.schedule.Schedule;
import org.apache.commons.lang3.StringUtils;

import java.util.logging.Logger;
import java.util.regex.Pattern;


public class CommandEvents implements Listener {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", Pattern.LITERAL);

    private Bot bot;
    private Logger logger;

    public CommandEvents(Bot bot, Logger logger) {
        this.bot = bot;
        this.logger = logger;
    }

    @EventHandler
    public void on(CommandEvent event) {
        // если игрок не авторизировался
        if (!event.hasAccessSender()) {
            return;
        }

        final String[] args = event.getArgs();
        switch (event.getCommand().toLowerCase()) {
            case "say":
            case "напиши":
            case "скажи": {
                final String arguments = event.getArguments(0);
                if (!arguments.isEmpty()) {
                    bot.sendMessage(arguments);
                }
                break;
            }
            case "flood":
            case "флуди": {
                final String arguments = event.getArguments(0);
                if (!arguments.isEmpty()) {
                    Schedule.timer(() -> {
                        bot.sendMessage(arguments);
                    }, 1000, 1000);
                }
                break;
            }
            case "walk":
            case "иди": {
                if (args.length != 3) return;
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);

                bot.getLoc().add(x, y, z);
                break;
            }
            case "spin":
            case "кружись": {
                final int[] yaw = {0};
                Schedule.timer(() -> {
                    bot.getLoc().setYaw(yaw[0]);
                    yaw[0] += 30;
                }, 50, 50);
                break;
            }
            case "drop":
            case "выкинь": {
                bot.sendPacket(new ClientPlayerActionPacket(
                        PlayerAction.DROP_ITEM,
                        new Position(bot.getLoc().getBlockX(), bot.getLoc().getBlockY(), bot.getLoc().getBlockZ()),
                        BlockFace.UP));
                break;
            }
            case "swap":
            case "переложи": {
                bot.sendPacket(new ClientPlayerActionPacket(
                        PlayerAction.SWAP_HANDS,
                        new Position(bot.getLoc().getBlockX(), bot.getLoc().getBlockY(), bot.getLoc().getBlockZ()),
                        BlockFace.UP));
                break;
            }
            case "swapping":
            case "перехватывай": {
                if (args.length != 1) return;
                int delay = Integer.parseInt(args[0]);
                Schedule.timer(() -> {
                    bot.sendPacket(new ClientPlayerActionPacket(
                            PlayerAction.SWAP_HANDS,
                            new Position(bot.getLoc().getBlockX(), bot.getLoc().getBlockY(), bot.getLoc().getBlockZ()),
                            BlockFace.UP));
                }, delay, delay);
                break;
            }
            case "rightclick":
            case "пкм": {
                bot.sendPacket(new ClientPlayerUseItemPacket(Hand.MAIN_HAND));
                break;
            }
            case "deus": {
                if (event.has(0, "vult")) {
                    bot.sendMessage("Ave Maria!");
                }
                break;
            }
            case "слава": {
                if (event.has(0, "украине")) {
                    bot.sendMessage("ГЕРОЯМ СЛАВА!");
                }
                break;
            }
            default:
                return;
        }

        event.setCancelled(true); //если обработали команду, тогда дальше не пускаем
    }

    @EventHandler
    public void on(PlayerChatEvent event) {
        final String message = event.getMessage();
        if (StringUtils.startsWithIgnoreCase(message, bot.getUsername())  //для "НИК," тоже сработает
                || StringUtils.startsWithIgnoreCase(message, "боты")) {
            final String[] commandArgs = PATTERN_ON_SPACE.split(message);
            if (commandArgs.length == 1) {
                bot.sendMessage(event.getSender() + ", шо ты хочешь?");
            } else if (commandArgs.length >= 2) {
                String command = commandArgs[1];
                String args[] = new String[commandArgs.length - 2];
                System.arraycopy(commandArgs, 2, args, 0, commandArgs.length - 2);
                logger.info("Игрок " + event.getSender() + "  написал команду: " + message);
                bot.getEventSystem().callEvent(new CommandEvent(
                        bot, event.getSender(), command, args));
            }
        }
    }
}
