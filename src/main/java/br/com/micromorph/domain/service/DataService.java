package br.com.micromorph.domain.service;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.MicromorphReturnDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.infrasctructure.exception.NotSupportedException;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import br.com.micromorph.infrasctructure.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface DataService {

    Data createAndPersistDataObject(MicromorphDataDTO micromorphData) throws IOException, PersistenceDeserializationException, NotSupportedException;

    void createAndPersistDataObject(List<MicromorphDataDTO> micromorphData) throws IOException, PersistenceDeserializationException, NotSupportedException;

    Page<MicromorphReturnDataDTO> findAllData(Integer page) throws IOException, ResourceNotFoundException;

    List<MicromorphReturnDataDTO> findAllByMetadata(RequestByMetadataDTO requestByMetadata) throws IOException, ResourceNotFoundException;

    MicromorphReturnDataDTO findById(String id) throws JSONException, JsonProcessingException, ResourceNotFoundException;

    void deleteById(String id) throws IOException ,PersistenceDeserializationException;

}
