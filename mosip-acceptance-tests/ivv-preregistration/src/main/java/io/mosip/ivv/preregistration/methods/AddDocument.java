package io.mosip.ivv.preregistration.methods;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.mosip.ivv.core.base.BaseStep;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.dtos.*;
import io.mosip.ivv.core.exceptions.RigInternalError;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.preregistration.utils.Helpers;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.io.File;

import static io.restassured.RestAssured.given;

public class AddDocument extends BaseStep implements StepInterface {

    @Override
    public void run() {
        RequestDataDTO requestData = prepare();
        ResponseDataDTO responseData = call(requestData);
        process(responseData);
    }

    public RequestDataDTO prepare(){
        String filePath = "";
        JSONObject request_json = new JSONObject();
        JSONObject requestData = new JSONObject();
        if(step.getParameters()==null ||step.getParameters().isEmpty()){
            throw new RuntimeException("Parameter is Missing for AddDocument");
        }

        switch(step.getParameters().get(0)){
            case "POA":
                request_json.put("docCatCode", store.getCurrentPerson().getProofOfAddress().getDocCatCode().name().toUpperCase());
                request_json.put("docTypCode", store.getCurrentPerson().getProofOfAddress().getDocTypeCode().toUpperCase());
                filePath = store.getCurrentPerson().getProofOfAddress().getPath();

                break;
            case "POR":
                request_json.put("docCatCode", store.getCurrentPerson().getProofOfRelationship().getDocCatCode().name().toUpperCase());
                request_json.put("docTypCode", store.getCurrentPerson().getProofOfRelationship().getDocTypeCode().toUpperCase());
                filePath = store.getCurrentPerson().getProofOfRelationship().getPath();

                break;
            case "POI":
                request_json.put("docCatCode", store.getCurrentPerson().getProofOfIdentity().getDocCatCode().name().toUpperCase());
                request_json.put("docTypCode", store.getCurrentPerson().getProofOfIdentity().getDocTypeCode().toUpperCase());
                filePath = store.getCurrentPerson().getProofOfIdentity().getPath();

                break;
            case "POB":
                request_json.put("docCatCode", store.getCurrentPerson().getProofOfBirth().getDocCatCode().name().toUpperCase());
                request_json.put("docTypCode", store.getCurrentPerson().getProofOfBirth().getDocTypeCode().toUpperCase());
                filePath = store.getCurrentPerson().getProofOfBirth().getPath();

                break;
            default:
                throw new RuntimeException("Invalid DocCategory :"+step.getParameters().get(0));
           }

        request_json.put("langCode", store.getCurrentPerson().getPrimaryLang());


        requestData.put("id", "mosip.pre-registration.document.upload");
        requestData.put("version", System.getProperty("ivv.prereg.apiversion"));
        requestData.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        requestData.put("request", request_json);

        String url = "/preregistration/" + System.getProperty("ivv.prereg.version") + "/documents/" + store.getCurrentPerson().getPreRegistrationId();
        return new RequestDataDTO(url, requestData.toJSONString(), filePath);
    }

    public ResponseDataDTO call(RequestDataDTO data){
        RestAssured.baseURI = System.getProperty("ivv.mosip.host");
        RestAssured.useRelaxedHTTPSValidation();
        Response responseData =
                (Response) given()
                        .cookie("Authorization", this.store.getHttpData().getCookie())
                        .multiPart("file", new File(data.getFilePath()))
                        .multiPart("Document request", data.getRequest())
                        .contentType("multipart/form-data")
                        .post(data.getUrl());
        this.callRecord = new CallRecord(RestAssured.baseURI+data.getUrl(), "POST", data.getRequest(), responseData);
        Helpers.logCallRecord(this.callRecord);
        return new ResponseDataDTO(responseData.getStatusCode(), responseData.getBody().asString(), responseData.getCookies());
    }

    public void process(ResponseDataDTO res){
        ReadContext ctx = JsonPath.parse(res.getBody());
        String docCatCode="";
        String docId="";
        if(ctx.read("$['response']")!=null) {
            docCatCode=ctx.read("$['response']['docCatCode']");
            docId=ctx.read("$['response']['docId']");
            switch(docCatCode){
                case "POA":
                    this.store.getPersona().getPersons().get(index).getProofOfAddress().setDocId(docId);
                    break;

                case "POB":
                    this.store.getPersona().getPersons().get(index).getProofOfBirth().setDocId(docId);

                    break;

                case "POR":
                    this.store.getPersona().getPersons().get(index).getProofOfRelationship().setDocId(docId);
                    break;

                case "POI":
                   this.store.getPersona().getPersons().get(index).getProofOfIdentity().setDocId(docId);
                    break;
            }

        }
    }

}
