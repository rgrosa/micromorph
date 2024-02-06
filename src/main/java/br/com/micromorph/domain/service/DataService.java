package br.com.micromorph.domain.service;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.MicromorphMetaDataDTO;
import br.com.micromorph.domain.dto.MicromorphReturnDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface DataService {

    Data createAndPersistDataObject(MicromorphDataDTO micromorphData) throws IOException, PersistenceDeserializationException;

    Page<MicromorphReturnDataDTO> findAllData(Integer page) throws IOException;

    List<MicromorphReturnDataDTO> findAllByMetadata(RequestByMetadataDTO requestByMetadata) throws IOException;

    MicromorphReturnDataDTO findById(String id) throws JSONException, JsonProcessingException;

    void deleteById(String id) throws IOException ,PersistenceDeserializationException;

}
