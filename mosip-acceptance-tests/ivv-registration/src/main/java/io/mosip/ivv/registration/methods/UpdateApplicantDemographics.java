package io.mosip.ivv.registration.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.ivv.core.base.BaseStep;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.dtos.RequestDataDTO;
import io.mosip.ivv.core.dtos.ResponseDataDTO;
import io.mosip.ivv.core.exceptions.RigInternalError;
import io.mosip.ivv.core.dtos.PersonaDef;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.registration.context.ApplicationContext;
import io.mosip.registration.dto.RegistrationDTO;
import io.mosip.registration.dto.demographic.CBEFFFilePropertiesDTO;
import io.mosip.registration.dto.demographic.IndividualIdentity;
import io.mosip.registration.dto.demographic.ValuesDTO;

import java.math.BigInteger;
import java.util.ArrayList;

public class UpdateApplicantDemographics extends BaseStep implements StepInterface {

    private enum fields {
        name, email, dob, gender
    }

    @Override
    public void validateStep() throws RigInternalError{
        if(step.getParameters().size() == 0){
            throw new RigInternalError("DSL error: Expect key and its value");
        }

        if(step.getParameters().get(0).isEmpty()){
            throw new RigInternalError("DSL error: key should not be empty");
        }
        try {
            fields.valueOf(step.getParameters().get(0));
        } catch (IllegalArgumentException ex) {
            throw new RigInternalError("DSL error: Key does not match a valid field");
        }
        if(store.getCurrentPerson().getAgeGroup().equals(PersonaDef.AGE_GROUP.CHILD) && store.getCurrentIntroducer() == null){
            throw new RigInternalError("Introducer is required to process this step");
        }
    }



    @Override
    public void run() {
        RegistrationDTO registrationDTO = (RegistrationDTO) this.store.getRegistrationDto();
        IndividualIdentity individualIdentity = new IndividualIdentity();
        ApplicationContext applicationContext = (ApplicationContext) store.getRegLocalContext();

        String platformLanguageCode = applicationContext.applicationLanguage();
        String localLanguageCode = applicationContext.localLanguage();

        String key = step.getParameters().get(0);
        switch(fields.valueOf(key)){
            case name:
//                individualIdentity.setFullName(createValueDTO(platformLanguageCode, store.getCurrentPerson().getName()));
                break;

            case email:
                individualIdentity.setEmail(store.getCurrentPerson().getUserid());
                break;

            case dob:
                individualIdentity.setDateOfBirth(store.getCurrentPerson().getDateOfBirth());
                break;

            case gender:
                individualIdentity.setGender(createValueDTO(platformLanguageCode, store.getCurrentPerson().getGender()));
                break;

            default:
                logWarning("Skipping step " + step.getName() + " as key: " + key + " not found");
                return;
        }
        individualIdentity.setIdSchemaVersion(1.0);
        individualIdentity.setUin(new BigInteger(store.getCurrentPerson().getUin()));

        /* Only for Child */
        if(store.getCurrentPerson().getAgeGroup().equals(PersonaDef.AGE_GROUP.CHILD)){
//            individualIdentity.setParentOrGuardianName(createValueDTO(platformLanguageCode, store.getCurrentIntroducer().getIdObject().get("fullName")));
            if(store.getCurrentIntroducer().getRegistrationId() != null && store.getCurrentIntroducer().getRegistrationId().length()>0){
                individualIdentity.setParentOrGuardianRID(new BigInteger(store.getCurrentIntroducer().getRegistrationId()));
            }
            if(store.getCurrentIntroducer().getUin() != null && store.getCurrentIntroducer().getUin().length()>0){
                individualIdentity.setParentOrGuardianUIN(new BigInteger(store.getCurrentIntroducer().getUin()));
            }
            CBEFFFilePropertiesDTO cbeffDTO = new CBEFFFilePropertiesDTO();
            cbeffDTO.setFormat("cbeff");
            cbeffDTO.setVersion(1.0);
            cbeffDTO.setValue("introducer_bio_CBEFF");
            individualIdentity.setParentOrGuardianBiometrics(cbeffDTO);

            registrationDTO.getDemographicDTO().setIntroducerRID(store.getCurrentIntroducer().getRegistrationId());
            registrationDTO.getDemographicDTO().setIntroducerUIN(store.getCurrentIntroducer().getUin());
        }

        CBEFFFilePropertiesDTO cbeffDTO = new CBEFFFilePropertiesDTO();
        cbeffDTO.setFormat("cbeff");
        cbeffDTO.setVersion(1.0);
        cbeffDTO.setValue("applicant_bio_CBEFF");
        individualIdentity.setIndividualBiometrics(cbeffDTO);

        registrationDTO.getDemographicDTO().getDemographicInfoDTO().setIdentity(individualIdentity);
        this.store.setRegistrationDto(registrationDTO);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Utils.auditLog.info(mapper.writeValueAsString(registrationDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RequestDataDTO prepare() {
        return null;
    }

    @Override
    public ResponseDataDTO call(RequestDataDTO requestData) {
        return null;
    }

    @Override
    public void process(ResponseDataDTO res) {

    }

    private ArrayList<ValuesDTO> createValueDTO(String platformLanguageCode, String value){
        ArrayList<ValuesDTO> values = new ArrayList<ValuesDTO>();
        ValuesDTO valuesDTO = new ValuesDTO();
        valuesDTO.setLanguage(platformLanguageCode);
        valuesDTO.setValue(value);
        values.add(valuesDTO);
        return values;
    }
}