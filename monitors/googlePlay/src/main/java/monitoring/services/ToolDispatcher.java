/*******************************************************************************
 * Copyright (c) 2016 Universitat Politécnica de Catalunya (UPC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Quim Motger (UPC) - main development
 * 	
 * Initially developed in the context of SUPERSEDE EU project
 * www.supersede.eu
 *******************************************************************************/
package monitoring.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringParams;
import monitoring.model.Utils;
import monitoring.tools.AppTweak;

@Path("configuration")
@Singleton
public class ToolDispatcher {

	final static Logger logger = Logger.getLogger(AppTweak.class);
	
	private int confId = 0;
	private final String toolPackageRoute = "monitoring.tools.";
	private Map<Integer, ToolInterface> monitoringInstances = new HashMap<>();
	
	@POST
	public String addConfiguration(String jsonConf) throws Exception {
		logger.debug("Adding new configuration...");
		try {
			++confId;		
			MonitoringParams params = Utils.parseJsonConfiguration(jsonConf);
			if (params.getToolName() == null) 
				return throwError("Missing tool name");
			Class monitor = Class.forName(toolPackageRoute + params.getToolName());
			ToolInterface toolInstance = (ToolInterface) monitor.newInstance();
			logger.debug("Tool instantiation completed");
			toolInstance.addConfiguration(params, confId);
			monitoringInstances.put(confId, toolInstance);
			return getResponse(confId);
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (InstantiationException e) {
			return throwError("Monitor class must be concrete");
		} catch (IllegalAccessException e) {
			return throwError("Monitor class must have a constructor with no args");
		} catch (ClassNotFoundException e) {
			return throwError("Not existing tool");
		} catch (Exception e) {
			return throwError(e.getMessage());
		}
		
	}
	
	@PUT
	@Path("{id}")
	public String updateConfiguration(@PathParam("id") Integer id, String jsonConf) {
		try {
			MonitoringParams params = Utils.parseJsonConfiguration(jsonConf);
			if(!monitoringInstances.containsKey(id))
				return throwError("Not existing configuration with ID " + String.valueOf(id));
			ToolInterface toolInstance = monitoringInstances.get(id);
			toolInstance.updateConfiguration(params);
		} catch (JSONException e) {
			return throwError("Not a valid JSON configuration object");
		} catch (Exception e) {
			return throwError("There was an unexpected error");
		}
		return getResponse(id);
	}
	
	/**
	 * Deletes the implicit monitoring and stops the monitoring
	 * @param id		the configuration id
	 * @return			response
	 */
	@DELETE
	@Path("{id}")
	public String deleteConfiguration(@PathParam("id") Integer id) {
		try {
			if (!monitoringInstances.containsKey(id))
				return throwError("Not existing configuration with the specified ID");
			monitoringInstances.get(id).deleteConfiguration();
			monitoringInstances.remove(id);
		} catch (Exception e) {
			return throwError("There was an unexpected error");
		}
		return getResponse(id);
	}

	public String throwError(String error) {
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		try {
			resInfo.put("message", error);
			resInfo.put("status", "error");
			response.put("GooglePlayConfProfResult", resInfo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}
	
	public String getResponse(int id) {
		JSONObject response = new JSONObject();
		JSONObject resInfo = new JSONObject();
		try {
			resInfo.put("idConf", id);
			resInfo.put("status", "success");
			response.put("GooglePlayConfProfResult", resInfo);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response.toString();
	}
	
	
}
