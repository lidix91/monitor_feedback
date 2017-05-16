package monitoring.tools;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringData;
import monitoring.model.MonitoringParams;
import monitoring.services.ToolInterface;

public class SocialSearcherAPI implements ToolInterface {
	
	final static Logger logger = Logger.getLogger(TwitterAPI.class);
	
	Timer timer;
	
	//Data object instances
	MonitoringParams confParams;
	boolean firstConnection;
	int id = 1;
	int configurationId;
	
	KafkaCommunication kafka;
	
	@Override
	public void addConfiguration(MonitoringParams params, int configurationId) {
		logger.debug("Adding new configuration");
		this.confParams = params;
		this.configurationId = configurationId;
		this.kafka = new KafkaCommunication();
		resetStream();
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}
	
	@Override
	public void updateConfiguration(MonitoringParams params) throws Exception {
		deleteConfiguration();
		//generateData((new Timestamp((new Date()).getTime()).toString()));
		this.confParams = params;
		resetStream();
	}
	
	private void resetStream() {
		//logger.debug("Initialising kafka producer...");
		//kafka.initProducer(confParams.getKafkaEndpoint());
		logger.debug("Initialising proxy...");
		kafka.initProxy(confParams.getKafkaEndpoint());
		logger.debug("Initialising streaming...");
		firstConnection = true;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (firstConnection) {
		    		firstConnection = false;
		    	} else {
		    		generateData((new Timestamp((new Date()).getTime()).toString()));
		    	}
			}
		}, 0, Integer.parseInt(confParams.getTimeSlot())* 1000);
	}
	
	private void generateData(String searchTimeStamp) {
		
		try{
			
			String key = "63dc2a9e0fb6693c36ac5c1b0edadd87";
			String q = "\"" + this.confParams.getKeywordExpression().replaceAll(" AND ", "\"AND\"").replaceAll("[()]", "").replaceAll(" OR ", "\"OR\"") + "\"";
			String network = "twitter";
			String fields = "text,user,posted,url";
			String limit = "100";
			String request = "https://api.social-searcher.com/v2/search?q=" + URLEncoder.encode(q, "UTF-8") + "&key=" + key + "&network=" + network + "&fields=" + fields + "&limit=" + limit;
			
			URL url = new URL(request);
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();  
			encoding = encoding == null ? "UTF-8" : encoding;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[8192];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
			    baos.write(buf, 0, len);
			}
			String content = new String(baos.toByteArray(), encoding);
			baos.close();
			in.close();
			
			JSONArray json_array = new JSONObject(content).getJSONArray("posts"); 
			
			List<MonitoringData> data = new ArrayList<>();
			for(int i=0;i<json_array.length();i++){
				
				JSONObject json = json_array.getJSONObject(i);
				JSONObject user = json.getJSONObject("user");
				String author = "@" + user.getString("url").substring(user.getString("url").lastIndexOf("/") + 1, user.getString("url").length());				
				String id = json.getString("url").substring(json.getString("url").lastIndexOf("/") + 1, json.getString("url").length());				
				String timeStamp = json.getString("posted");				
				String message = json.getString("text");				
				String link = json.getString("url");				
				MonitoringData dataObj = new MonitoringData(id, timeStamp, message, author, link);
				data.add(dataObj);				
				
			}
			
			//kafka.generateResponseKafka(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic);
			kafka.generateResponseIF(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic());
			logger.debug("Data successfully sent to Kafka endpoint");
			++id;			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}

