package me.xjcyan1de.cyanbot.gui.commands

import com.github.steveice10.mc.protocol.data.game.entity.player.Hand
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import me.xjcyan1de.cyanbot.Main
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel

class CommandUseItem : Command("Использовать предмет") {
    override fun initPanel(commandPanel: JPanel) {
        val useItemPanel = JPanel()
        useItemPanel.layout = GridLayoutManager(2, 1, Insets(0, 0, 0, 0), -1, -1)
        useItemPanel.border = BorderFactory.createTitledBorder("Использовать предмет")

        val useItemButtonMain = JButton()
        useItemButtonMain.text = "Главной рукой"
        useItemButtonMain.addActionListener { _ ->
            run {
                for (bot in Main.getMainFrame().selectedBots) {
                    bot.sendPacket(ClientPlayerUseItemPacket(Hand.MAIN_HAND))
                }
            }
        }

        val useItemButtonOff = JButton()
        useItemButtonOff.text = "Второй рукой"
        useItemButtonOff.addActionListener({ _ ->
            run {
                for (bot in Main.getMainFrame().selectedBots) {
                    bot.sendPacket(ClientPlayerUseItemPacket(Hand.OFF_HAND))
                }
            }
        })

        useItemPanel.add(useItemButtonMain, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        useItemPanel.add(useItemButtonOff, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        commandPanel.add(useItemPanel, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
        useItemPanel.rootPane.defaultButton = useItemButtonMain
    }
}
