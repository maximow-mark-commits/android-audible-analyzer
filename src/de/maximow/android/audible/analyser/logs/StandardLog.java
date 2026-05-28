package de.maximow.android.audible.analyser.logs;

public class StandardLog {

	private String date;
	private String process;
	private String type;
	private String worker;
	private String description;

	public String getDate() {
		return date;
	}

	public String getProcess() {
		return process;
	}

	public String getType() {
		return type;
	}

	public String getWorker() {
		return worker;
	}

	public String getDescription() {
		return description;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "StandardLog [date=" + date + ", process=" + process + ", type=" + type + ", worker=" + worker + ", description=" + description + "]";
	}

}
