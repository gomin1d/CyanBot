package me.xjcyan1de.cyanbot.gui;

import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.utils.JFrameUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigSavedJFrameData {
    private MainFrame mainFrame;
    private Config config;
    private String configJoinCommands;
    private String configUsername;

    public ConfigSavedJFrameData(MainFrame mainFrame, Config config) {
        this.mainFrame = mainFrame;
        this.config = config;
    }

    private boolean fixHistory = true;

    @SuppressWarnings("unchecked")
    public void loadConfig() {
        final JTextArea joinCommands = mainFrame.getJoinCommands();

        final JComboBox historyIp = mainFrame.getHistoryIp();
        final JTextField ip = mainFrame.getIp();

        joinCommands.setText(configJoinCommands = String.join("\n", config.getOrSet("join-commands", Arrays.asList("/reg test123 test123",
                "/login test123"))));

        mainFrame.getUsername().setText(configUsername = config.getOrSet("username", "CyanBot"));

        historyIp.removeAllItems();
        final List<String> history = config.getOrSet("history-ip", Arrays.asList("localhost", "mc.JustVillage.ru:25565"));
        history.forEach(historyIp::addItem);
        if (!history.isEmpty()) {
            historyIp.setSelectedIndex(0);
        }
        ip.setText(history.get(0));

        mainFrame.getAutoJoin().setSelected(config.getOrSet("auto-join", false));
        mainFrame.getDeplayRelogin().setText(config.getOrSet("delay-relogin", "2000"));
    }

    @SuppressWarnings("unchecked")
    public void register() {
        final JComboBox historyIp = mainFrame.getHistoryIp();
        final JTextField ip = mainFrame.getIp();
        final JTextArea joinCommands = mainFrame.getJoinCommands();

        mainFrame.getJoin().addActionListener(e -> {
            final String ipText = ip.getText();
            if (!historyIp.getItemAt(0).equals(ipText)) {
                fixHistory = false;
                historyIp.removeItem(ipText);
                historyIp.insertItemAt(ipText, 0);
                historyIp.setSelectedIndex(0);
                fixHistory = true;

                config.setAndSave("history-ip", JFrameUtils.getAllItems(historyIp).stream()
                        .limit(10)
                        .map(String.class::cast)
                        .collect(Collectors.toList()));
            }

            if (!configJoinCommands.equals(joinCommands.getText())) {
                config.setAndSave("join-commands", Arrays.asList((configJoinCommands = joinCommands.getText()).split("\n")));
            }

            if (!configUsername.equals(mainFrame.getUsername().getText())) {
                config.setAndSave("username", configUsername = mainFrame.getUsername().getText());
            }
        });

        historyIp.addItemListener(e -> {
            if (fixHistory && e.getStateChange() == ItemEvent.SELECTED) {
                ip.setText(((String) e.getItem()));
            }
        });

        mainFrame.getAutoJoin().addItemListener(e->{
            config.setAndSave("auto-join", e.getStateChange() == ItemEvent.SELECTED);
        });

        mainFrame.getDeplayRelogin().addActionListener(e -> {
            config.setAndSave("delay-relogin", mainFrame.getDeplayRelogin().getText());
        });
    }
}
