package me.xjcyan1de.cyanbot.gui.commands;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CommandChat extends Command {
    JTextField chatField;
    JButton chatButton;

    public CommandChat() {
        super("Сообщение в чат");
        chatField = new JTextField();
        chatField.setToolTipText("Сообщение");
        chatField.setText("Привет, друзья");
        chatButton = new JButton();
        chatButton.setText("Сообщение");
    }

    @Override
    public void initPanel(JPanel commandPanel) {
        commandPanel.getRootPane().setDefaultButton(chatButton);
        commandPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        commandPanel.add(chatField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        commandPanel.add(chatButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        for (ActionListener actionListener : chatButton.getActionListeners()) {
            chatButton.removeActionListener(actionListener);
        }
        chatButton.addActionListener(e -> {
            Bot[] selectedBots = Main.getMainFrame().getSelectedBots();
            for (Bot bot : selectedBots) {
                bot.sendMessage(chatField.getText());
            }
        });
    }
}
