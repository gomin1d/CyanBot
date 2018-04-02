package me.xjcyan1de.cyanbot.gui.commands;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.Main;

import javax.swing.*;
import java.awt.*;

public class CommandChat extends Command {

    public CommandChat() {
        super("Сообщение в чат");
    }

    @Override
    public void initPanel(JPanel commandPanel) {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        chatPanel.setBorder(BorderFactory.createTitledBorder("Чат"));

        JTextField chatText = new JTextField();
        JButton chatButton = new JButton();
        chatButton.setText("Отправить");
        chatButton.addActionListener(e -> {
            Bot[] selectedBots = Main.getMainFrame().getSelectedBots();
            for (Bot bot : selectedBots) {
                bot.sendMessage(chatText.getText());
            }
        });
        commandPanel.getRootPane().setDefaultButton(chatButton);

        chatPanel.add(chatButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chatPanel.add(chatText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        commandPanel.add(chatPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }
}
