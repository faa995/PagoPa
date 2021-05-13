package Contracts;

import Util.GenericException;

public class SendedMessage extends Message{

	public SendedMessage(String subject, String markdown) throws GenericException {
		super(subject, markdown);
	}
	
	public SendedMessage(String subject, String markdown, String id, String fiscalCode, String senderServiceId, String status) throws GenericException {
		super(subject, markdown);
		this.id = id;
		this.fiscalCode = fiscalCode;
		this.senderServiceId = senderServiceId;
		this.status = status;
	}
	
	public void print() {
		System.out.println("ID: " + this.id);
		super.print();
		System.out.println("FiscalCode: " + this.fiscalCode);
		System.out.println("SenderServiceId : " + this.senderServiceId);
		System.out.println("Status : " + this.status);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderServiceId() {
		return senderServiceId;
	}

	public void setSenderServiceId(String senderSservice_id) {
		this.senderServiceId = senderSservice_id;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	private String id, senderServiceId, fiscalCode, status;

}
