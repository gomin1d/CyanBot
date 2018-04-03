package me.xjcyan1de.cyanbot.gui.commands

import com.github.steveice10.mc.protocol.data.MagicValues
import com.github.steveice10.mc.protocol.data.game.window.ClickItemParam
import com.github.steveice10.mc.protocol.data.game.window.WindowAction
import com.github.steveice10.mc.protocol.data.game.window.WindowActionParam
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import me.xjcyan1de.cyanbot.Main
import java.awt.Dimension
import java.awt.Insets
import javax.swing.*

class CommandInventoryClick : Command("Клик в инвентаре") {
    override fun initPanel(commandPanel: JPanel) {
        val clickPanel = JPanel()
        clickPanel.layout = GridLayoutManager(3, 1, Insets(0, 0, 0, 0), -1, -1)
        clickPanel.border = BorderFactory.createTitledBorder("Клик в инвентаре")

        val clickField = JTextField()
        clickField.text = "0"

        val model = DefaultComboBoxModel<String>()
        model.addElement("Левый клик")
        model.addElement("Правый клик")
        model.addElement("Shift + Левый клик")
        model.addElement("Shift + Правый клик")
        model.addElement("Клавиша Q (выкинуть предмет)")
        model.addElement("Cntr + Q (выкинуть стак)")
        model.addElement("Левый клик за инвентарём")
        model.addElement("Правый клик за инвентарём")
        model.addElement("Двойной клик")
        val clickComboBox = JComboBox(model)
        clickComboBox.model = model

        val clickButton = JButton()
        clickButton.text = "КЛИК!"
        clickButton.addActionListener({ _ ->
            run {
                val text = clickField.text
                var slot = 0
                try {
                    slot = Integer.parseInt(text)
                } catch (ignored: NumberFormatException) {
                    clickField.text = "0"
                }
                val buttonId = getButtonId(clickComboBox.selectedIndex)
                val windowAction = getAction(clickComboBox.selectedIndex)
                val packet = ClientWindowActionPacket(0, 0, slot, null, windowAction, MagicValues.key(ClickItemParam::class.java, buttonId) as WindowActionParam)
                for (bot in Main.getMainFrame().selectedBots) {
                    bot.sendPacket(packet)
                }
            }
        })

        clickPanel.add(clickField, GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, Dimension(150, -1), null, 0, false))
        clickPanel.add(clickButton, GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        clickPanel.add(clickComboBox, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
        commandPanel.add(clickPanel, GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK or GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false))
        clickPanel.rootPane.defaultButton = clickButton
    }

    private fun getButtonId(index: Int): Int {
        return when (index) {
            0 -> 0 //Левый клик
            1 -> 1 //правый клик
            2 -> 0 //Shift + левый
            3 -> 1 //Shift + правый
            4 -> 0 //Клавиша Q
            5 -> 1 //Выкинуть стак
            6 -> 0 //Выкинуть за инвентарь
            7 -> 1 //Выкинуть за инвентарь
            8 -> 0 //двойной
            else -> 0
        }
    }

    private fun getAction(index: Int): WindowAction {
        return when (index) {
            0, 1 -> MagicValues.key(WindowAction::class.java, 0) as WindowAction
            2, 3 -> MagicValues.key(WindowAction::class.java, 1) as WindowAction
            4, 5, 6, 7 -> MagicValues.key(WindowAction::class.java, 4) as WindowAction
            8 -> MagicValues.key(WindowAction::class.java, 6) as WindowAction
            else -> MagicValues.key(WindowAction::class.java, 0) as WindowAction
        }
    }
}
