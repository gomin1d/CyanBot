package me.xjcyan1de.cyanbot.gui.commands;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.Main;

import javax.swing.*;
import java.awt.*;

public class CommandSelectSlot extends Command {

    public CommandSelectSlot() {
        super("Выбрать слот");
    }

    @Override
    public void initPanel(JPanel commandPanel) {
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayoutManager(9, 1, new Insets(0, 0, 0, 0), -1, -1));
        commandPanel.add(slotPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        slotPanel.setBorder(BorderFactory.createTitledBorder("Слот"));
        for (int i = 0; i < 9; i++) {
            createButton(slotPanel, i);
        }
    }

    public void createButton(JPanel jPanel, int slot) {
        JButton slotButton = new JButton();
        slotButton.setText("Слот " + slot);
        slotButton.addActionListener(e -> {
            final Bot[] selectedBots = Main.getMainFrame().getSelectedBots();
            for (Bot selectedBot : selectedBots) {
                selectedBot.sendPacket(new ClientPlayerChangeHeldItemPacket(slot));
            }
        });
        jPanel.add(slotButton, new GridConstraints(slot, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }
}
