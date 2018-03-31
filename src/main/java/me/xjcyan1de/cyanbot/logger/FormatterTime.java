package me.xjcyan1de.cyanbot.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FormatterTime extends Formatter {
    private final DateFormat date = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder formatted = new StringBuilder();

        formatted.append("[");
        formatted.append(this.date.format(record.getMillis()));
        formatted.append(" ");
        formatted.append(record.getLevel().getLocalizedName());
        formatted.append("]");
        //formatted.append(record.getSourceClassName()).append(".").append(record.getSourceMethodName())
        formatted.append(": ");
        formatted.append(formatMessage(record));
        formatted.append('\n');
        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            formatted.append(writer);
        }
        return formatted.toString();
    }
}
