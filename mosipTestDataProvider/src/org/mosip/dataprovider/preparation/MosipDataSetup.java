package org.mosip.dataprovider.preparation;


import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mosip.dataprovider.models.setup.MosipDeviceModel;
import org.mosip.dataprovider.models.setup.MosipMachineModel;
import org.mosip.dataprovider.models.setup.MosipMachineSpecModel;
import org.mosip.dataprovider.models.setup.MosipMachineTypeModel;
import org.mosip.dataprovider.models.setup.MosipRegistrationCenterTypeModel;
import org.mosip.dataprovider.util.CommonUtil;
import org.mosip.dataprovider.util.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import variables.VariableManager;

public class MosipDataSetup {
	private static final Logger logger = LoggerFactory.getLogger(MosipDataSetup.class);
	public static void geConfig() {
		//https://sandbox.mosip.net/config/*/mz/1.1.4/print-mz.properties
		//https://dev.mosip.net/config/*/mz/develop/registration-processor-mz.properties
	}
	public static Object getCache(String key) {
	
		try {
			return VariableManager.getVariableValue(key);
		}catch(Exception e) {
			
		}
		return null;
	}
	public static void setCache(String key, Object value) {
		
		VariableManager.setVariableValue(key,  value);
	}
	public static void createRegCenterType(MosipRegistrationCenterTypeModel type) {
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"regcentertype").toString();
		
		JSONObject jsonType = new JSONObject();
		jsonType.put("code", type.getCode());
		jsonType.put("descr", type.getDescription());
		jsonType.put("isActive", type.isActive());
		jsonType.put("langCode", type.getLangCode());
		jsonType.put("name", type.getName());

		JSONObject jsonReqWrapper = new JSONObject();
		jsonReqWrapper.put("request", jsonType);
		jsonReqWrapper.put("requesttime", CommonUtil.getUTCDateTime(null));
		jsonReqWrapper.put("version", "1.0");
		jsonReqWrapper.put("id", "id.machine");
		jsonReqWrapper.put("metadata", new JSONObject());

		try {
			JSONObject resp = RestClient.post(url,jsonReqWrapper);
			if(resp != null) {
				String r = resp.toString();
				System.out.println(r);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void createMachineType(MosipMachineTypeModel type) {
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"machinetype").toString();
		
		JSONObject jsonType = new JSONObject();
		jsonType.put("code", type.getCode());
		jsonType.put("description", type.getDescription());
		jsonType.put("isActive", type.isActive());
		jsonType.put("langCode", type.getLangCode());
		jsonType.put("name", type.getName());

		JSONObject jsonReqWrapper = new JSONObject();
		jsonReqWrapper.put("request", jsonType);
		jsonReqWrapper.put("requesttime", CommonUtil.getUTCDateTime(null));
		jsonReqWrapper.put("version", "1.0");
		jsonReqWrapper.put("id", "id.machine");
		jsonReqWrapper.put("metadata", new JSONObject());

		try {
			JSONObject resp = RestClient.post(url,jsonReqWrapper);
			if(resp != null) {
				String r = resp.toString();
				System.out.println(r);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	public static List<MosipMachineTypeModel> getMachineTypes() {
		List<MosipMachineTypeModel> machineTypes = null;
		
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"machinetype").toString();
		url = url + "all";
		
		Object o =getCache(url);
		if(o != null)
			return( (List<MosipMachineTypeModel>) o);
		
		try {
			JSONObject resp = RestClient.get(url,new JSONObject() , new JSONObject());
			if(resp != null) {
				JSONArray typeArray = resp.getJSONArray("data");
				ObjectMapper objectMapper = new ObjectMapper();
				
				machineTypes = objectMapper.readValue(typeArray.toString(), 
						objectMapper.getTypeFactory().constructCollectionType(List.class, MosipMachineTypeModel.class));
				
				setCache(url, machineTypes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return machineTypes;
	}
	public static List<MosipRegistrationCenterTypeModel> getRegCenterTypes() {
		List<MosipRegistrationCenterTypeModel> machineTypes = null;
		
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"regcentertype").toString();
		url = url + "all";
		
		Object o =getCache(url);
		if(o != null)
			return( (List<MosipRegistrationCenterTypeModel>) o);
		
		try {
			JSONObject resp = RestClient.get(url,new JSONObject() , new JSONObject());
			if(resp != null) {
				JSONArray typeArray = resp.getJSONArray("data");
				ObjectMapper objectMapper = new ObjectMapper();
				
				machineTypes = objectMapper.readValue(typeArray.toString(), 
						objectMapper.getTypeFactory().constructCollectionType(List.class, MosipRegistrationCenterTypeModel.class));
				
				setCache(url, machineTypes);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return machineTypes;
	}
		
		
	public static void createMachineSpec(MosipMachineSpecModel spec) {
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"machinespec").toString();
		JSONObject jsonSpec = new JSONObject();
		jsonSpec.put("id", spec.getId());
		jsonSpec.put("name", spec.getName());
		jsonSpec.put("brand", spec.getBrand());
		jsonSpec.put("description", spec.getDescription());
		jsonSpec.put("isActive", spec.isActive());
		jsonSpec.put("langCode", spec.getLangCode());
		jsonSpec.put("machineTypeCode", spec.getMachineTypeCode());
		jsonSpec.put("minDriverVersion", spec.getMinDriverversion());
		jsonSpec.put("model", spec.getModel());

		JSONObject jsonReqWrapper = new JSONObject();
		jsonReqWrapper.put("request", jsonSpec);
		jsonReqWrapper.put("requesttime", CommonUtil.getUTCDateTime(null));
		jsonReqWrapper.put("version", "1.0");
		jsonReqWrapper.put("id", "id.machine");
		jsonReqWrapper.put("metadata", new JSONObject());

		try {
			JSONObject resp = RestClient.post(url,jsonReqWrapper);
			if(resp != null) {
				String r = resp.toString();
				System.out.println(r);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public static void createMachine(MosipMachineModel machine) {
		
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"createmachine").toString();
		
		JSONObject jsonMachine = new JSONObject();
		jsonMachine.put("id", machine.getId());
		jsonMachine.put("ipAddress", machine.getIpAddress());
		jsonMachine.put("isActive", machine.isActive());
		jsonMachine.put("langCode", machine.getLangCode());
		jsonMachine.put("macAddress", machine.getMacAddress());
		jsonMachine.put("machineSpecId", machine.getMachineSpecId());
		jsonMachine.put("name", machine.getName());
		jsonMachine.put("publicKey", machine.getPublicKey());
		jsonMachine.put("regCenterId", machine.getRegCenterId());
		jsonMachine.put("serialNum", machine.getSerialNum());
		jsonMachine.put("signPublicKey", machine.getSignPublicKey());
		jsonMachine.put("validityDateTime", machine.getValidityDateTime());
		jsonMachine.put("zoneCode", machine.getZoneCode());
					
		JSONObject jsonReqWrapper = new JSONObject();
		jsonReqWrapper.put("request", jsonMachine);
		jsonReqWrapper.put("requesttime", CommonUtil.getUTCDateTime(null));
		jsonReqWrapper.put("version", "1.0");
		jsonReqWrapper.put("id", "id.machine");
		jsonReqWrapper.put("metadata", new JSONObject());

	
		
		try {
			JSONObject resp = RestClient.post(url,jsonReqWrapper);
			if(resp != null) {
				String r = resp.toString();
				System.out.println(r);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static List<MosipDeviceModel> getDevices(String centerId) {
		//GET /v1/masterdata/devices/mappeddevices/1001?direction=DESC&orderBy=createdDateTime&pageNumber=0&pageSize=100
	
		List<MosipDeviceModel> devices = null;
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue(VariableManager.NS_MASTERDATA,"mappeddevices").toString();
	
		url = url +centerId;
		
		Object o =getCache(url);
		if(o != null)
			return( (List<MosipDeviceModel>) o);
		
		try {
			JSONObject resp = RestClient.get(url,new JSONObject() , new JSONObject());
			if(resp != null) {
				JSONArray typeArray = resp.getJSONArray("data");
				ObjectMapper objectMapper = new ObjectMapper();
				
				devices = objectMapper.readValue(typeArray.toString(), 
						objectMapper.getTypeFactory().constructCollectionType(List.class, MosipDeviceModel.class));
				
				setCache(url, devices);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return devices;
	}
	public static String configureMockABISBiometric(String bdbString, boolean bDuplicate, String[] duplicateBdbs, int delay, String operation) 
			throws JSONException, NoSuchAlgorithmException {
		//System.out.println("configureMockABISBiometric initiated....");
		logger.info("configureMockABISBiometric initiated....");
		if(operation == null || operation.equals(""))
			operation = "Indentify";
		
		String responseStr = "";
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue("mockABISsetExpectaion").toString();

		JSONObject req = new JSONObject();
		req.put("id", CommonUtil.getSHA(bdbString));
		req.put("version","1.0");
		req.put("requesttime",CommonUtil.getUTCDateTime(null) );
		req.put("actionToInterfere",operation );
		req.put("forcedResponse","Duplicate" );
		
		req.put("delayInExecution",Integer.toString(delay) );
		
		if(!bDuplicate)
			req.put("gallery",JSONObject.NULL);
		else
		{
			JSONObject duprefs = new JSONObject();
			JSONArray arr = new JSONArray();
			for(String s: duplicateBdbs) {
				JSONObject ref = new JSONObject();
				ref.put("referenceId", CommonUtil.getSHA(bdbString));
				arr.put(ref);
			}
			duprefs.put("referenceIds", arr);
			req.put("gallery", duprefs);
		}
				
		try {
			JSONObject resp = RestClient.post(url, req);
			if(resp != null) {
				responseStr = resp.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseStr = e.getMessage();
			logger.error("configureMockABISBiometric end...."+e.getMessage());
			System.out.println("configureMockABISBiometric end...."+e.getMessage());
		}

		return responseStr;
	}
	public static String uploadPackets( List<String> packetPaths) {

		String responseStr = "";
		String url = VariableManager.getVariableValue("urlBase").toString() +
				VariableManager.getVariableValue("bulkupload").toString();

		JSONObject req = new JSONObject();
		req.put("category","packet");
		req.put("tableName","packet");
		req.put("operation","");
		
		
		try {
			JSONObject resp = RestClient.uploadFiles(url, packetPaths, req);
			if(resp != null) {
				responseStr = resp.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseStr = e.getMessage();
		}
		return responseStr;
	}


}
