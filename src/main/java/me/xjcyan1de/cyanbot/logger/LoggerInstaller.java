package me.xjcyan1de.cyanbot.logger;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerInstaller {
	public static Logger create(String name, String fileName) throws IOException {
		Logger logger = new Logger(name, null) {};
		logger.setLevel(Level.ALL);

		FileHandler fileHandler = new FileHandler(fileName, 16777216, 8, true);
		fileHandler.setFormatter(new FormatterTime());
		fileHandler.setLevel(Level.ALL);
		logger.addHandler(fileHandler);
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setFormatter(new FormatterTime());
		logger.addHandler(consoleHandler);

		System.setErr(new PrintStream(new LoggingOutputStream(logger, Level.SEVERE), true));
		System.setOut(new PrintStream(new LoggingOutputStream(logger, Level.INFO), true));
		return logger;
	}

	private static class LoggingOutputStream extends ByteArrayOutputStream {
		public LoggingOutputStream(Logger logger, Level level)
		{
			this.logger = logger;this.level = level;
		}

		private static final String separator = System.getProperty("line.separator");
		private final Logger logger;
		private final Level level;

		public void flush()
		throws IOException
		{
			String contents = toString("UTF-8");
			super.reset();
			if ((!contents.isEmpty()) && (!contents.equals(separator))) {
				this.logger.logp(this.level, "", "", contents);
			}
		}
	}
}
