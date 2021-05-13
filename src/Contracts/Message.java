package Contracts;

import Util.GenericException;

public class Message {

	public Message(String subject, String markdown, int timeToLive) throws GenericException {
		if(markdown.length() < 80 || markdown.length() > 1000)
			throw new GenericException("Lunghezza messaggio non corretta. >80 & < 1000");
		if(subject.length() < 10 || subject.length() > 121)
			throw new GenericException("Lunghezza oggetto non corretta. >10 & < 121");
		this.subject = subject;
		this.markdown = markdown;
		this.timeToLive = timeToLive;
	}
	
	public Message(String subject, String markdown) throws GenericException {
		if(markdown.length() < 80 || markdown.length() > 1000)
			throw new GenericException("Lunghezza messaggio non corretta. >80 & < 1000");
		if(subject.length() < 10 || subject.length() > 121)
			throw new GenericException("Lunghezza oggetto non corretta. >10 & < 121");
		this.subject = subject;
		this.markdown = markdown;
	}
	
	public void print() {
		System.out.println("Subject: " + this.getSubject());
		System.out.println("Markdown: " + this.getMarkdown());
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMarkdown() {
		return markdown;
	}

	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	private String subject, markdown;
	private int timeToLive = 3600;

}
