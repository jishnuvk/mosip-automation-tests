package io.mosip.ivv.core.dtos;

import jdk.nashorn.internal.objects.Global;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Person extends PersonaDef {
    /* required */
    private HashMap<String, IDObjectField> idObject = new HashMap<String, IDObjectField>();
    private String id;
    private String userid;
    private String password;
    private String email;
    private String phone;
    private String otp;
    private String registrationCenterId;
    private String primaryLang;
    private String secondaryLang;

    //adding field as an variants
    private String date;
    private String fullName;


    /* pre-reg store */
    private String preRegistrationId = "";
    private String preRegistrationCenterId = "";
    private String preRegistrationStatusCode = "";
    private BookingSlot slot = new BookingSlot();
    private BookingSlot prevSlot = new BookingSlot();

    /* reg store */
    private String registrationId = "";

    /* system info */
    private String macAddress = "";

    /* IDA fields */
    private String staticToken = "";
    private String authenticationOTP = "";
    private JSONObject authenticationJSON = new JSONObject();
    private ArrayList<String> authParams = new ArrayList<String>();

    // required in create pre-registration api
    private String dateOfBirth = "";
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String addressLine3 = "";
    private String region = "";
    private String province = "";
    private String city = "";
    private String zone = "";
    private String idSchemaVersion = "";
    private String referenceIdentityNumber = "";
    private String postalCode = "";
    private String langCode = "fra";
    private String gender = "";
    private String residenceStatus = "";


    private ArrayList<String> docTypeList;
    private ProofDocument proofOfAddress = null;
    private ProofDocument proofOfBirth = null;
    private ProofDocument proofOfIdentity = null;
    private ProofDocument proofOfRelationship = null;
    private ProofDocument proofOfException = null;
    private ProofDocument proofOfExemption = null;

    private BiometricsDTO face;
    private BiometricsDTO leftEye;
    private BiometricsDTO rightEye;
    private BiometricsDTO leftThumb;
    private BiometricsDTO leftIndexFinger;
    private BiometricsDTO leftMiddleFinger;
    private BiometricsDTO leftRingFinger;
    private BiometricsDTO leftLittleFinger;
    private BiometricsDTO rightThumb;
    private BiometricsDTO rightIndexFinger;
    private BiometricsDTO rightMiddleFinger;
    private BiometricsDTO rightRingFinger;
    private BiometricsDTO rightLittleFinger;


    private String uin = "";
    private List<String> vids = new ArrayList<String>();
}
