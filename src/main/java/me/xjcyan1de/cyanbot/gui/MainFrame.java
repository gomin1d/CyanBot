package me.xjcyan1de.cyanbot.gui;

import com.github.steveice10.packetlib.packet.Packet;
import me.xjcyan1de.cyanbot.Player;
import me.xjcyan1de.cyanbot.PlayerManager;
import me.xjcyan1de.cyanbot.logger.PlayerLogger;
import me.xjcyan1de.cyanbot.utils.Schedule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class MainFrame extends JDialog {
    private JPanel contentPane;
    private JTextArea joinCommands;
    private JTextField ip;
    private JTextField port;
    private JButton join;
    private JTextField name;
    private JTextArea chat;
    private JTextArea logs;
    private JTextField messageText;
    private JButton sendMessage;
    private PlayerManager manager;
    private Logger logger;

    public MainFrame(PlayerManager manager, Logger logger) {
        this.manager = manager;
        this.logger = logger;

        this.setTitle("CyanBot");

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(join);

        logger.addHandler(new FormLoggerHandler(logs));

        join.addActionListener(e -> {
            final Player player = new Player(new PlayerLogger(name.getText(), logger), name.getText());
            manager.connectPlayer(player, ip.getText(), Integer.parseInt(port.getText()));
            Schedule.later(() -> {
                for (String cmd : joinCommands.getText().split("\n")) {
                    player.sendMessage(cmd);
                }
            }, 1000);
        });

        sendMessage.addActionListener(e -> {
            final Player player = manager.getPlayer(name.getText());
            if (player != null) {
                player.sendMessage(messageText.getText());
            }
        });
    }
}
