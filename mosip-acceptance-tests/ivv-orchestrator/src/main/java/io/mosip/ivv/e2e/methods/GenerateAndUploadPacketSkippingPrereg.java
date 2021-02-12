package io.mosip.ivv.e2e.methods;

import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.exceptions.RigInternalError;
import io.mosip.ivv.orchestrator.BaseTestCaseUtil;

public class GenerateAndUploadPacketSkippingPrereg extends BaseTestCaseUtil implements StepInterface {

	@Override
	public void run() throws RigInternalError {

		for (String resDataPath : residentTemplatePaths.keySet()) {
			String rid = packetUtility.generateAndUploadPacketSkippingPrereg(residentTemplatePaths.get(resDataPath),
					resDataPath,contextKey);
			pridsAndRids.put("0", rid);
		}
		
	}

}
