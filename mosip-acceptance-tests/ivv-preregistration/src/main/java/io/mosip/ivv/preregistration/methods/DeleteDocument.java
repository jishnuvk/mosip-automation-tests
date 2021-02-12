package io.mosip.ivv.preregistration.methods;

import io.mosip.ivv.core.base.BaseStep;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.dtos.*;
import io.mosip.ivv.preregistration.utils.Helpers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteDocument extends BaseStep implements StepInterface {

    @Override
    public void run() {
        RequestDataDTO requestData = prepare();
        ResponseDataDTO responseData = call(requestData);
        process(responseData);
    }

    public RequestDataDTO prepare(){
        String docType = step.getParameters().get(0);
        String docId = "";
        switch(ProofDocument.DOCUMENT_CATEGORY.valueOf(docType)){
            case POA:
                docId = store.getCurrentPerson().getProofOfAddress().getDocId();
                break;

            case POB:
                docId = store.getCurrentPerson().getProofOfBirth().getDocId();
                break;

            case POR:
                docId = store.getCurrentPerson().getProofOfRelationship().getDocId();
                break;

            case POI:
                docId = store.getCurrentPerson().getProofOfIdentity().getDocId();
                break;
        }
        String url = "/preregistration/" + System.getProperty("ivv.prereg.version") + "/documents/" + docId + "?preRegistrationId=" + store.getCurrentPerson().getPreRegistrationId();
        return new RequestDataDTO(url, null);
    }

    public ResponseDataDTO call(RequestDataDTO data){
        RestAssured.baseURI = System.getProperty("ivv.mosip.host");
        RestAssured.useRelaxedHTTPSValidation();
        Response responseData =
                (Response) given()
                        .contentType(ContentType.JSON)
                        .cookie("Authorization", this.store.getHttpData().getCookie())
                        .delete(data.getUrl());
        this.callRecord = new CallRecord(RestAssured.baseURI+data.getUrl(), "POST", data.getRequest(), responseData);
        Helpers.logCallRecord(this.callRecord);
        return new ResponseDataDTO(responseData.getStatusCode(), responseData.getBody().asString(), responseData.getCookies());
    }

    public void process(ResponseDataDTO res){

    }

}
