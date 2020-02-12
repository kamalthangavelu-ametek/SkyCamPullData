package com.skybitz.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
public class VolumetricSensorData {

	static Logger logger = Logger.getLogger(VolumetricSensorData.class.getName());
	/***
	 * 
	 * @param updatedAfterStr
	 * @param updatedBeforeStr
	 * @return
	 */
	public Object getVolumedataByUpdatedDates(String updatedAfterStr, String updatedBeforeStr){		
		
    	logger.log(Level.INFO, "Entering getVolumedataByUpdatedDates method");
    	System.out.println("updatedAfterStr: "+updatedAfterStr+"    updatedBefore: "+updatedBeforeStr);

    	String token = 
        		"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJza3liaXR6IiwidHlwZSI6ImFwaSIsImlhdCI6MTU2OTYxODYyNiwiZXhwIjoxNjAxMTU0NjI2fQ.ZwKJ91MrKSMwTkefL9OyicaZ6gflKEltaPcCitIbQbI";

    	String BASE = "https://containers-api.compology.com/v2";
		String auth="Bearer "+token;
		
		//curl -H 'Accept: application/json' -H "$AUTH" ${BASE}/containers/dateUpdatedAfter/"+DateUpdatedAfter+"/dateUpdatedBefore/"+DateUpdatedBefore;
		String url = BASE+"/containers";
		//Get the data using above url
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		String jsonString = "";
		try {
			URL urlNew = new URL(url);
			HttpURLConnection connection = getConnectionToCompologyServer(urlNew, token);
			String type = connection.getContentType();
			if (type == null) {
			    return null;
			}
			InputStream inputStream = connection.getInputStream();
			
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str1="";
			StringBuilder stringBuilder = new StringBuilder();
			while((str1 = bufferedReader.readLine())!= null){
				stringBuilder.append(str1);
		    }
			String str = stringBuilder.toString();
			JSONParser parser = new JSONParser();
		    json = (JSONObject) parser.parse(str);
			
		    Iterator iter = (Iterator)json.keySet().iterator();
		    while (iter.hasNext()) {
		    	  for(int i=0;i<json.size();i++) {
		    	    String key = (String) iter.next();
		    	    jsonArray.add(json.get(key));
		    	  }
		    }
		    jsonString = jsonArray.get(0).toString();
		    
		}catch(ParseException pe) {
			pe.printStackTrace();
		}catch(MalformedURLException mue) {
			mue.printStackTrace();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		return jsonString;
	}
	/***
	 * 
	 * @param urlNew2
	 * @param token
	 * @return
	 */
	private static HttpURLConnection getConnectionToCompologyServer(URL urlNew2, String token) {		
    	logger.log(Level.INFO, "Entering getConnectionToCompologyServer method");

    	String auth = "Bearer " + token;
		String response="";
		ClientResponse response2=null;
		HttpURLConnection connection=null;
		try {			
			String urlFormatted = urlNew2.toString();			
			URL urlNew = new URL(urlFormatted);
			connection = (HttpURLConnection) urlNew.openConnection();
			connection.setRequestProperty("Authorization", auth);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setReadTimeout(10000); //10 Seconds
			connection.connect();
		}catch(MalformedURLException mue) {
			mue.printStackTrace();
		}catch(IOException ie) {
			ie.printStackTrace();
		}finally {
			
		}
		return connection;
	}
}
