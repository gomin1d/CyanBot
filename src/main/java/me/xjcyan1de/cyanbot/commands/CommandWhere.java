package me.xjcyan1de.cyanbot.commands;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.commands.command.Command;
import me.xjcyan1de.cyanbot.events.CommandEvent;
import me.xjcyan1de.cyanbot.utils.schedule.Schedule;
import me.xjcyan1de.cyanbot.utils.schedule.TimerCount;
import me.xjcyan1de.cyanbot.world.Entity;
import me.xjcyan1de.cyanbot.world.Location;

import java.util.TimerTask;

public class CommandWhere extends Command {

    private TimerTask timerTask;

    public CommandWhere() {
        super("где", false);
    }

    @Override
    public void execute(CommandEvent event) {
        final Bot bot = event.getBot();
        final Entity entity = event.has(0, "я")
                ? bot.getWorld().getPlayer(event.getSender())
                : bot.getWorld().getPlayer(event.arg(0));

        if (entity != null) {
            Schedule.cancel(timerTask);
            final Location loc = bot.getLoc();
            timerTask = Schedule.timer(new TimerCount(() -> {
                final float pitch = loc.getPitch();
                final float yaw = loc.getYaw();
                loc.setDir(
                        entity.getX() - loc.getX(),
                        entity.getY() - loc.getY(),
                        entity.getZ() - loc.getZ());

                loc.setYaw(smoothly(loc.getYaw(), yaw));
                loc.setPitch(smoothly(loc.getPitch(), pitch));

                System.out.println(loc.getYaw() + " " + loc.getPitch());

            }, 100) {
                @Override
                public boolean cancel() {
                    timerTask = null;
                    bot.sendMessage("Та вижу я!");
                    return super.cancel();
                }
            }, 50, 50);

        }
    }

    private float smoothly(float angle, float prev) {
        final float speedRotation = 10F;

        final float sub = Math.abs(prev - angle);
        float phi = sub % 360;       // This is either the distance or 360 - distance
        if ((phi > 180 ? 360 - phi : phi) <= speedRotation) {
            return angle;
        }

        if(prev < angle) {
            if(sub<180) {
                return prev + speedRotation;
            } else {
                return prev - speedRotation;
            }
        } else {
            if(sub<180) {
                return prev - speedRotation;
            } else {
                return prev + speedRotation;
            }
        }
    }
}
