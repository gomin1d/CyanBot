package me.xjcyan1de.cyanbot.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import me.xjcyan1de.cyanbot.Bot;
import me.xjcyan1de.cyanbot.BotManager;
import me.xjcyan1de.cyanbot.config.Config;
import me.xjcyan1de.cyanbot.utils.schedule.Schedule;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.logging.Logger;

public class MainFrame extends JFrame {
    private JPanel contentPane;
    private JTextArea joinCommands;
    private JTextField ip;
    private JButton join;
    private JTextField name;
    private JTextArea chat;
    private JTextArea logs;
    private JTextField messageText;
    private JButton sendMessage;
    private JScrollPane chatScroll;
    private JScrollPane logsScroll;
    private JButton leave;
    private JTextField status;
    private JComboBox historyIp;
    private JCheckBox autoJoin;
    private JTabbedPane tabbedPane1;
    private JList commandList;
    private JButton getKey;
    private JPanel commandPanel;
    private JList botList;
    private JPanel clickPanel;
    private JTextField deplayRelogin;
    private JScrollPane botListScorllPane;
    private JTextField clickField;
    private JComboBox clickComboBox;
    private JButton clickButton;
    private JScrollBar botListScrollPane;
    private Config config;
    private BotManager botManager;
    private Logger logger;

    private TimerTask taskRelogin;

    public MainFrame(Config config, BotManager botManager, Logger logger) {
        this.config = config;
        this.botManager = botManager;
        this.logger = logger;

        this.setTitle("CyanBot");
        setContentPane(contentPane);
        getRootPane().setDefaultButton(join);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final ConfigSavedJFrameData configSaved = new ConfigSavedJFrameData(this, config);
        configSaved.loadConfig();
        configSaved.register();

        logger.addHandler(new FormLoggerHandler(logs));

        this.registerListeners();
        this.scheduleUpdateStatus(this, botManager);

        this.updateRelogin();

        commandPanel.removeAll();
    }

    public void updateRelogin() {
        if (autoJoin.isSelected()) {
            taskRelogin = Schedule.timer(() -> {
                final Bot bot = botManager.getBot(name.getText());
                if (bot == null) {
                    this.onJoin();
                }
            }, 0, Integer.parseInt(deplayRelogin.getText()));
        } else if (taskRelogin != null) {
            taskRelogin.cancel();
        }
    }

    @SuppressWarnings("unchecked")
    private void scheduleUpdateStatus(MainFrame mainFrame, BotManager manager) {
        Schedule.timer(() -> {
            final DefaultListModel model = (DefaultListModel) botList.getModel();

            Set<String> namesInList = new LinkedHashSet<>();
            for (int i = 0; i < model.getSize(); i++) {
                final String name = String.valueOf(model.getElementAt(i));
                if (!manager.isConnected(name)) {
                    model.removeElement(name);
                } else {
                    namesInList.add(name);
                }
            }
            manager.getBots().forEach(player -> {
                final String name = player.getUsername();
                if (!namesInList.contains(name)) {
                    model.addElement(name);
                    if (model.size() == 1) {
                        botList.setSelectedIndex(0);
                    }
                }
            });

            boolean connected = manager.isConnected(name.getText());

            final JTextField status = mainFrame.getStatus();
            if (!connected) {
                BotStatus.CONNECTED.setStatus();
            } else {
                BotStatus.DISCONECTED.setStatus();
            }

            sendMessage.setEnabled(connected);
            leave.setEnabled(connected);
            join.setEnabled(!connected);
            getKey.setEnabled(connected);
        }, 0, 500);
    }

    public JCheckBox getAutoJoin() {
        return autoJoin;
    }

    public JTextField getStatus() {
        return status;
    }

    public JTextField getUsername() {
        return name;
    }

    public JTextField getDeplayRelogin() {
        return deplayRelogin;
    }


    public JButton getJoin() {
        return join;
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() {
        join.addActionListener(e -> this.onJoin());

        getKey.addActionListener(e -> {
            final Bot bot = botManager.getBot(name.getText());
            if (bot != null) {
                final String accessKey = bot.generateAccessKey();
                if (accessKey != null) {
                    Hover.hoverText("Введите этот код в чат", accessKey);
                }
            }
        });

        leave.addActionListener(e -> {
            final Bot bot = botManager.getBot(name.getText());
            if (bot != null) {
                botManager.disconnectBot(bot);
                /*final DefaultListModel model = (DefaultListModel) botList.getModel();
                model.removeElement(bot.getUsername());
                botList.setModel(model);*/
            }
        });

        sendMessage.addActionListener(e -> {
            final Bot bot = botManager.getBot(name.getText());
            if (bot != null) {
                bot.sendMessage(messageText.getText());
            }
        });

        autoJoin.addItemListener(e -> {
            this.updateRelogin();
        });

        deplayRelogin.addActionListener(e -> {
            this.updateRelogin();
        });

        final DefaultListModel listModel = new DefaultListModel();
        CommandListHandler.init(listModel);
        commandList.setModel(listModel);

        commandList.addListSelectionListener(e -> {
            final int index = commandList.getSelectedIndex();
            if (index != -1) {
                CommandListHandler.createPanel(commandPanel, index);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public Bot[] getSelectedBots() {
        return (Bot[]) botList.getSelectedValuesList().stream()
                .map(name1 -> botManager.getBot(String.valueOf(name1)))
                .filter(Objects::nonNull).toArray(Bot[]::new);
    }

    @SuppressWarnings("unchecked")
    private void onJoin() {
        String ipText = ip.getText();
        botManager.connectBot(ipText, name.getText(),
                Arrays.asList(joinCommands.getText().split("\n")),
                this);
    }

    public JTextField getIp() {
        return ip;
    }

    public JComboBox getHistoryIp() {
        return historyIp;
    }

    public JTextArea getJoinCommands() {
        return joinCommands;
    }

    public JTextArea getChat() {
        return chat;
    }

    public Logger getLogger() {
        return logger;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1 = new JTabbedPane();
        contentPane.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Основное", panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder("Данные"));
        name = new JTextField();
        name.setText("CyanBot");
        panel2.add(name, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        joinCommands = new JTextArea();
        joinCommands.setText("");
        panel2.add(joinCommands, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        leave = new JButton();
        leave.setEnabled(false);
        leave.setText("Отключиться");
        panel2.add(leave, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        status = new JTextField();
        status.setEditable(false);
        status.setText("Статус");
        panel2.add(status, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        join = new JButton();
        join.setText("Подключиться");
        panel2.add(join, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        autoJoin = new JCheckBox();
        autoJoin.setText("Заходить автоматически");
        panel3.add(autoJoin, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Задержка между заходами (ms): ");
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deplayRelogin = new JTextField();
        deplayRelogin.setText("2000");
        panel3.add(deplayRelogin, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder("IP"));
        ip = new JTextField();
        ip.setText("mc.JustVillage.ru");
        panel4.add(ip, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        historyIp = new JComboBox();
        panel4.add(historyIp, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder("Действия"));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder("Чат"));
        sendMessage = new JButton();
        sendMessage.setEnabled(false);
        sendMessage.setText("Сообщение");
        panel6.add(sendMessage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        messageText = new JTextField();
        messageText.setText("Привет, друзья");
        panel6.add(messageText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder("Авторизация"));
        getKey = new JButton();
        getKey.setText("Получить ключ");
        panel7.add(getKey, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Команды", panel8);
        commandList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        commandList.setModel(defaultListModel1);
        commandList.setSelectionMode(0);
        panel8.add(commandList, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        commandPanel = new JPanel();
        commandPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel8.add(commandPanel, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clickPanel = new JPanel();
        clickPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        commandPanel.add(clickPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clickPanel.setBorder(BorderFactory.createTitledBorder("Использзовать"));
        clickField = new JTextField();
        clickField.setText("0");
        clickPanel.add(clickField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        clickButton = new JButton();
        clickButton.setText("КЛИК!");
        clickPanel.add(clickButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clickComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Левый клик");
        defaultComboBoxModel1.addElement("Правый клик");
        defaultComboBoxModel1.addElement("Shift + Левый клик");
        defaultComboBoxModel1.addElement("Shift + Правый клик");
        defaultComboBoxModel1.addElement("Клавиша Q (выкинуть предмет)");
        defaultComboBoxModel1.addElement("Cntr + Q (выкинуть стак)");
        defaultComboBoxModel1.addElement("Левый клик за инвентарём");
        defaultComboBoxModel1.addElement("Правый клик за инвентарём");
        defaultComboBoxModel1.addElement("Двойной клик");
        clickComboBox.setModel(defaultComboBoxModel1);
        clickPanel.add(clickComboBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        botListScorllPane = new JScrollPane();
        panel8.add(botListScorllPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        botList = new JList();
        botList.setMaximumSize(new Dimension(0, 0));
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        botList.setModel(defaultListModel2);
        botList.setPreferredSize(new Dimension(100, 0));
        botListScorllPane.setViewportView(botList);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel9.setBorder(BorderFactory.createTitledBorder("Логи"));
        chatScroll = new JScrollPane();
        panel9.add(chatScroll, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 150), null, 0, false));
        chat = new JTextArea();
        chat.setText("чат");
        chatScroll.setViewportView(chat);
        logsScroll = new JScrollPane();
        panel9.add(logsScroll, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 150), null, 0, false));
        logs = new JTextArea();
        logs.setText("логи\n");
        logsScroll.setViewportView(logs);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
