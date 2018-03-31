package me.xjcyan1de.cyanbot.gui;

import me.xjcyan1de.cyanbot.logger.FormatterTime;

import javax.swing.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class FormLoggerHandler extends Handler {
    private JTextArea logs;

    public FormLoggerHandler(JTextArea logs) {
        this.logs = logs;
        this.setFormatter(new FormatterTime());
        this.setLevel(Level.ALL);
    }

    @Override
    public void publish(LogRecord record) {
        logs.append(this.getFormatter().format(record));
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
