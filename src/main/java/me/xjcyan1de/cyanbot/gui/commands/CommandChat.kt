package me.xjcyan1de.cyanbot.gui.commands

import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import me.xjcyan1de.cyanbot.Main

import javax.swing.*
import java.awt.*

class CommandChat : Command("Сообщение в чат") {

    override fun initPanel(commandPanel: JPanel) {
        val chatPanel = JPanel()
        chatPanel.layout = GridLayoutManager(2, 1, Insets(0, 0, 0, 0), -1, -1)
        chatPanel.border = BorderFactory.createTitledBorder("Чат")

        val chatText = JTextField()
        val chatButton = JButton()
        chatButton.text = "Отправить"
        chatButton.addActionListener { _ ->
            val selectedBots = Main.getMainFrame().selectedBots
            for (bot in selectedBots) {
                bot.sendMessage(chatText.text)
            }
        }
        commandPanel.rootPane.defaultButton = chatButton

        chatPanel.add(chatButton, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        chatPanel.add(chatText, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))

        commandPanel.add(chatPanel, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
    }
}
