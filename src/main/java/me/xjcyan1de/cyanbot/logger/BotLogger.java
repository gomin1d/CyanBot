package me.xjcyan1de.cyanbot.logger;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class BotLogger extends Logger {

	private String pluginName;

	public BotLogger(String name, Logger logger) {
		super("CeanBot", null);
		this.pluginName = "[" + name + "] ";
		this.setParent(logger);
		this.setLevel(Level.ALL);
	}

	@Override
	public void log(LogRecord logRecord) {
		logRecord.setMessage(this.pluginName + logRecord.getMessage());
		super.log(logRecord);
	}
}
