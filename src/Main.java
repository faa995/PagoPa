import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import Biz.Service;
import Contracts.Citizen;
import Contracts.Message;
import Contracts.SendedMessage;

public class Main {

	public static void main(String[] args) {
		
		try {
			String key = "1d36deca4a984067822118aeec8e2f23";
			Options options = new Options();
			options.addOption("test","Test richiesto dal task");
			options.addOption("sendM", "Invio messaggio. Richiede CF, ObjM e BodyM");
			options.addOption("CF",true, "CF a cui inviare il messaggio. Richiede opzione sendM");
			options.addOption("ObjM",true, "Oggetto messaggio da inviare. Richiede opzione sendM");
			options.addOption("BodyM",true, "Corpo messaggio da inviare. Richiede opzione sendM");
			options.addOption("RecoverM", "Recupera messaggio da Id. Richiede CF e IdMes");
			options.addOption("IdMes",true, "ID messaggio inviato");
			options.addOption("h", "help", false, "Help.");
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
	
			HelpFormatter formatter = new HelpFormatter();
			
			if(cmd.hasOption("help")) {
				formatter.printHelp("Elaboration", options);
			}
			else if(cmd.hasOption("sendM")) { //Invio messaggio
				String CF, ObjM, BodyM;
				if(cmd.hasOption("CF") && cmd.hasOption("ObjM") && cmd.hasOption("BodyM")) {
					CF = cmd.getOptionValue("CF");
					ObjM = cmd.getOptionValue("ObjM");
					BodyM = cmd.getOptionValue("BodyM");
					
					//Crea il cittadino
					Citizen c = new Citizen(CF);
					//Crea il servizio per il cittadino
					Service s = new Service(key, c);
					
					//Verifica se l'utente ha il servizio abilitato, invia il messaggio e stampa l'ID del messaggio
					Message mes = new Message(ObjM, BodyM);
					String idMes = s.sendMessage(mes);
					System.out.println("Id Messaggio: " + idMes);
					mes.print();
				}
			}
			else if(cmd.hasOption("RecoverM")) { //Recupero messaggio
				if(cmd.hasOption("CF") && cmd.hasOption("IdMes")) {
					String CF = cmd.getOptionValue("CF");
					String ID = cmd.getOptionValue("IdMes");
					
					//Crea il cittadino
					Citizen c = new Citizen(CF);
					
					//Crea il servizio per il cittadino
					Service s = new Service(key, c);
					
					//Recupera il messaggio
					SendedMessage smess = s.getSendedMessage(ID);
					smess.print();
				}
			}
			else if(cmd.hasOption("test")) { //Esempio
				
				//Crea il cittadino
				Citizen c = new Citizen("AAAAAA00A00A000A");
				//Crea il servizio per il cittadino
				Service s = new Service(key, c);
				
				//Verifica se l'utente ha il servizio abilitato, invia il messaggio e stampa l'ID del messaggio
				Message mes = new Message("Messaggio prova", "Messaggio di prova per il task di PagoPA per il nostro cittadino di prova. Distinti Saluti");
				String idMes = s.sendMessage(mes);
				System.out.println("Id Messaggio: " + idMes);
				mes.print();
				
				Thread.sleep(7000); //Tempo di processamento di PagoPa
				//Recupera il messaggio
				SendedMessage smess = s.getSendedMessage(idMes);
				smess.print();
				
			}
			else {
					formatter.printHelp("Elaboration", options);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}

	}

}
