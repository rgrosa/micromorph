package br.com.micromorph.domain.service;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.MicromorphMetaDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.io.IOException;

public interface DataService {

    Data createAndPersistDataObject(MicromorphDataDTO micromorphData) throws IOException, PersistenceDeserializationException;

    Page<Data> findAllData(Integer page) throws IOException;

    SearchHits<Data> findAllByMetadata(RequestByMetadataDTO requestByMetadata) throws IOException;

    Data findById(String id);

    void deleteById(String id) throws IOException ,PersistenceDeserializationException;

}
