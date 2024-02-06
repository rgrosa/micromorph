package br.com.micromorph.domain.service;

import br.com.micromorph.domain.dto.MicromorphReturnDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.io.IOException;

public interface DataServicePersistence {

    Data insertIntoDataIndex(Data data) throws IOException, PersistenceDeserializationException;

    Page<Data> findAll(Integer page);

    SearchHits<Data> findAllByMetadata(RequestByMetadataDTO requestByMetadata);

    void deleteDataById(String id) throws PersistenceDeserializationException;

    Data findById(String id);

}
