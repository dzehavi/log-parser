package com.dz.logparser;

import com.dz.logparser.model.LogEntry;
import com.dz.logparser.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LogParser implements CommandLineRunner {

    @Autowired
    LogRepository logRepository;

    private final String logEntryPattern = "(DEBUG|INFO|WARN|ERROR) : (\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3}) (.*)";
    private final Pattern pattern = Pattern.compile(logEntryPattern);
    // I used the time format that is given inside the log
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void run(String... args) throws Exception {
        Scanner inputScanner = new Scanner(Paths.get("src/main/resources/static/standard.log"));
        // this is for appending unmatched lines, like stack trace lines, to the previously saved message.
        final LogEntry[] lastSavedLogEntry = new LogEntry[1];
        // read 1 line at a time for low memory footprint
        while (inputScanner.hasNext()) {
            String line = inputScanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    LogEntry logEntry = new LogEntry();
                    try {
                        String level = matcher.group(1);
                        logEntry.setLevel(level);
                        String timestamp = matcher.group(2);
                        logEntry.setTimestamp(new Timestamp(DATE_TIME_FORMAT.parse(timestamp).getTime()));
                        String message = matcher.group(3);
                        logEntry.setMessage(message);
                        logRepository.save(logEntry);
                        lastSavedLogEntry[0] = logEntry;
                    } catch (ParseException e) {
                        logger.log(Level.WARNING, logEntry.getMessage());
                    }
                } else {
                    // append unmatched content (like exception stack trace) to the last matched log message
                    // my assumption here is that unmatched lines are the suffix of a previously matched entry.
                    lastSavedLogEntry[0].setMessage(lastSavedLogEntry[0].getMessage() + "\n" + line);
                    logRepository.save(lastSavedLogEntry[0]);
                }
            }
    }
}
