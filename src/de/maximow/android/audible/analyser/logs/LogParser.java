package de.maximow.android.audible.analyser.logs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

	public static StandardLog parseStandardLine(String string) {
		StandardLog log = new StandardLog();

		// looks for 4 bracketed groups at the start
		// group 5 is description / rest
		Pattern pattern = Pattern.compile("^\\[(.*?)\\]\\s*\\[(.*?)\\]\\s*\\[(.*?)\\]\\s*\\[(.*?)\\](.*)$");
		Matcher matcher = pattern.matcher(string);

		if (matcher.find()) {
			String date = matcher.group(1);
			String process = matcher.group(2);
			String type = matcher.group(3);
			String workerRaw = matcher.group(4);
			String workerID = workerRaw.substring(workerRaw.lastIndexOf('/') + 1);
			String description = matcher.group(5).trim();

			log.setDate(date);
			log.setProcess(process);
			log.setType(type);
			log.setWorker(workerID);
			log.setDescription(description);
		} else {
			log.setDescription(string);
		}

		return log;
	}

	public static List<StandardLog> parseStandardLogs(File file) {
		List<StandardLog> logs = new ArrayList<>();
		Pattern headerPattern = Pattern.compile("^\\[.*?\\]");

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			StringBuilder currentFullEntry = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				// is this a new log entry?
				if (headerPattern.matcher(line).find()) {
					// parse the buffered log
					if (currentFullEntry.length() > 0) {
						logs.add(parseStandardLine(currentFullEntry.toString()));
					}
					currentFullEntry = new StringBuilder(line);
				} else {
					// continuation of log
					if (currentFullEntry.length() > 0) {
						currentFullEntry.append(" ").append(line.trim());
					}
				}
			}

			// adds the last log
			if (currentFullEntry.length() > 0) {
				logs.add(parseStandardLine(currentFullEntry.toString()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return logs;
	}

}
