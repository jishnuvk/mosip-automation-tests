package io.mosip.ivv.registration.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.ivv.core.base.BaseStep;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.dtos.Person;
import io.mosip.ivv.core.dtos.RequestDataDTO;
import io.mosip.ivv.core.dtos.ResponseDataDTO;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.registration.config.Setup;
import io.mosip.registration.context.SessionContext;
import io.mosip.registration.dto.ErrorResponseDTO;
import io.mosip.registration.dto.RegistrationDTO;
import io.mosip.registration.dto.ResponseDTO;
import io.mosip.registration.dto.SuccessResponseDTO;
import io.mosip.registration.service.sync.PreRegistrationDataSyncService;

public class GetPreRegistration extends BaseStep implements StepInterface {

    private Person person;



    @Override
    public void run() {
        this.index = Utils.getPersonIndex(step);
        this.person = this.store.getPersona().getPersons().get(index);
        SessionContext.map().put("registrationDTOContent", Setup.getRegistrationDTO());

        PreRegistrationDataSyncService serv = store.getRegApplicationContext().getBean(PreRegistrationDataSyncService.class);
        ResponseDTO responseDTO = serv.getPreRegistration("38142157162067");
        if(responseDTO.getErrorResponseDTOs() != null && responseDTO.getErrorResponseDTOs().size() > 0){
            for(ErrorResponseDTO es: responseDTO.getErrorResponseDTOs()){
                logSevere("Message: "+es.getMessage()+", code: "+es.getCode()+", info: "+es.getInfoType());
            }
            this.hasError = true;
            return;
        }else{
            SuccessResponseDTO es = responseDTO.getSuccessResponseDTO();
            RegistrationDTO registrationDTO = (RegistrationDTO) SessionContext.map().get("registrationDTOContent");
            this.store.setRegistrationDto(registrationDTO);
            logInfo("Message: "+es.getMessage()+", code: "+es.getCode()+", infoType: "+es.getInfoType());
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writeValueAsString(SessionContext.map().get("registrationDTOContent"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logSevere("Jackson ObjectMapper: "+e.getMessage());
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
}