package Biz;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import Contracts.Citizen;
import Contracts.Message;
import Contracts.SendedMessage;
import Util.GenericException;

public class Service {

	public Service(String key, Citizen c) throws GenericException{
		this.citizenCf = c.getCitizenCf();
		this.key = key;
	}
	//recupera il messaggio inviato
	public SendedMessage getSendedMessage(String messageId) throws Exception{
		
		//controlla l'ID
		if(messageId.isBlank())
			throw new GenericException("Id messaggio non valido");
		
		//Effettua la richiesta e fa il parse della risposta
		Response response = doGetRequest("messages/", citizenCf + "/" + messageId);
		JSONObject jsonS = new JSONObject(response.getRespondeBody());
			
		if(response.getRespondeCode() == 200) {
			JSONObject message = jsonS.getJSONObject("message");
			String id = message.getString("id");
			String fiscalCode = message.getString("fiscal_code");
			String senderServiceId = message.getString("sender_service_id");
			JSONObject content = message.getJSONObject("content");
			String subject = content.getString("subject");
			String markdown = content.getString("markdown");
			String status = jsonS.getString("status");
			return new SendedMessage(subject, markdown, id, fiscalCode, senderServiceId, status);
		}
		else if(response.getRespondeCode() > 400){
			throw new GenericException("Get message failed." + jsonS.getString("detail"));
		}
		return null;
	}
	
	//invia un messaggio per il cf specifico
	public String sendMessage(Message mes) throws Exception{
		//verifica se il cittadino è abilitato al servizio
		if(isServiceEnabledForCitizen()) {
			
			//controlla se il messaggio è correttamente formatto
			if(mes.getSubject().isBlank() || mes.getMarkdown().isBlank())
				throw new GenericException("Il messaggio non contiene l'oggetto o il corpo");
			
			//Compone il body della richiesta
			JSONObject json = new JSONObject();
			
			JSONObject content =  new JSONObject();
			content.put("subject", mes.getSubject());
			content.put("markdown", mes.getMarkdown());
			content.put("due_date", Util.Formatter.getUTCdate(new Date()));
			
			json.put("time_to_live", mes.getTimeToLive());
			json.put("content",content);
			json.put("fiscal_code", citizenCf);
			
			//Richiesta e parse della risposta
			Response response = doPostRequest("messages/", "", json.toString(), "application/json");
			JSONObject jsonS = new JSONObject(response.getRespondeBody());
				
			if(response.getRespondeCode() == 201) {
				return jsonS.getString("id");
			}
			else if(response.getRespondeCode() == 400  || response.getRespondeCode() == 500){
				throw new GenericException("Send message failed." + jsonS.getString("detail"));
			}
		}
		return "Send message failed.";
	}
	
	//Verifica se il cittadino è abilitato a ricevere messaggi
	public boolean isServiceEnabledForCitizen() throws Exception{
	
		Response response = doGetRequest("profiles/", citizenCf);
		JSONObject jsonS = new JSONObject(response.getRespondeBody());
			
		if(response.getRespondeCode() == 200) {
			return jsonS.getBoolean("sender_allowed");
		}
		else if(response.getRespondeCode() == 400){
			throw new GenericException("Check if service is enabled failed." + jsonS.getString("detail"));
		}
		
		return false;
	}
	
	//Metodo generico richieste POST
	public Response doPostRequest(String partialUrl, String urlParameters, String body, String contentType) throws Exception {

		String url = apiUrl + partialUrl + urlParameters;
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");	
		con.setRequestProperty("Ocp-Apim-Subscription-Key", key);
		con.setRequestProperty("Content-Type", contentType);

		// Send post request
		con.setDoOutput(true);
		con.setFixedLengthStreamingMode(body.length());
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(body);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		
		String resp = "";
		if(responseCode == 201) {
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
		
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			resp = response.toString();
		}
		
		return new Response(responseCode, resp);
	}
	
	//Metodo generico richieste GET
	private Response doGetRequest(String partialUrl, String urlParameters) throws Exception{
	
			String url = apiUrl + partialUrl + urlParameters;
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
			//add reuqest header
			con.setRequestMethod("GET");
			con.setRequestProperty("Ocp-Apim-Subscription-Key", key);
			
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			return new Response(responseCode, response.toString());
	}
	
	//Inner class che modella la risposta ad un GET
	private class Response{
		
		public Response(int respondeCode, String responseBody) {
			this.respondeCode = respondeCode;
			this.responseBody = responseBody;
		}
		
		public int getRespondeCode() {
			return respondeCode;
		}
		
		public String getRespondeBody() {
			return responseBody;
		}
		
		private int respondeCode;
		private String responseBody;
	}
	
	private String apiUrl = "https://api.io.italia.it/api/v1/";
	private String key;
	private String citizenCf;
	
	
}
