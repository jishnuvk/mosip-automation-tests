package io.mosip.ivv.e2e.methods;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Reporter;

import io.mosip.authentication.fw.util.ReportUtil;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.exceptions.RigInternalError;
import io.mosip.ivv.orchestrator.BaseTestCaseUtil;
import io.mosip.kernel.util.KernelAuthentication;
import io.restassured.response.Response;

public class GetUINByRid extends BaseTestCaseUtil implements StepInterface {

	private String getIdentityUrl = "/resident/uin/";
	//private String identitypath = "preReg/identity/";
	Logger logger = Logger.getLogger(GetIdentityByRid.class);
	KernelAuthentication kauth = new KernelAuthentication();
	Boolean isForChildPacket = false;
    @SuppressWarnings("static-access")
	@Override
    public void run() throws RigInternalError {
    	//must call e2e_wait() before generating uin
		if (!step.getParameters().isEmpty() && step.getParameters().size() == 1) { // used for child packet processing
			isForChildPacket = Boolean.parseBoolean(step.getParameters().get(0));
			if (isForChildPacket && !StringUtils.isEmpty(rid_updateResident)) {
				HashMap<String, String> rid = new HashMap<>();
				rid.put("rid", rid_updateResident);
				getIdentity(rid);
			}
		} else
			getIdentity(this.pridsAndRids);
	}
    public void getIdentity(HashMap<String, String> rids) throws RigInternalError
    {
    	uinReqIds.clear();
    	uinPersonaProp.clear();
    	for(String rid: rids.values())
    	{
    		if(rid!=null) {
    			Reporter.log("<b><u>"+"GetIdentity By Rid"+ "</u></b>");
        		Reporter.log("<pre>" + ReportUtil.getTextAreaJsonMsgHtml("{Rid: "+rid +"}") + "</pre>");
        		long startTime = System.currentTimeMillis();
				logger.info(this.getClass().getSimpleName()+" starts at..."+startTime +" MilliSec");
        		Response response = getRequest(baseUrl+getIdentityUrl+rid, "Get uin by rid: " + rid);
        		long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;
				logger.info("Time taken to execute "+ this.getClass().getSimpleName()+": " +elapsedTime +" MilliSec");
				Reporter.log("<b><u>"+"Time taken to execute "+ this.getClass().getSimpleName()+": " +elapsedTime +" MilliSec"+ "</u></b>");
				logger.info("Response from get Identity for RID: "+rid+" "+response.asString());
    		
				String uin = response.asString();
				if (isForChildPacket && !StringUtils.isEmpty(uin) && !(uin.trim().contains("errorCode")))
					uin_updateResident = uin; // used for child packet processing
				else if (!StringUtils.isEmpty(uin)  && !(uin.trim().contains("errorCode"))) {
					uinReqIds.put(uin, null);
					if (!uinPersonaProp.containsKey(uin))
						uinPersonaProp.put(uin, ridPersonaPath.get(rid));
				} else {
					logger.error("Issue while fetching identity for RID: " + rid + " Response: " + response.toString());
					throw new RigInternalError("Not able to Fetch identity for RID: " + rid);
				}
			}
		}
	}

}
