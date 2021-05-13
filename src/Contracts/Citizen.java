package Contracts;

import Util.GenericException;

public class Citizen {

	public Citizen(String citizenCf) throws GenericException {
		//Verifica se il CF rispetta il formato
		if(citizenCf.matches("[A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST][0-9LMNPQRSTUV]{2}[A-Z][0-9LMNPQRSTUV]{3}[A-Z]"))
			this.citizenCf = citizenCf;
		else
			throw new GenericException("Codice fiscale non corretto");
		this.citizenCf = citizenCf;
	}

	public String getCitizenCf() {
		return citizenCf;
	}

	public void setCitizenCf(String citizenCf) {
		citizenCf = citizenCf;
	}
	
	private String citizenCf;
}
