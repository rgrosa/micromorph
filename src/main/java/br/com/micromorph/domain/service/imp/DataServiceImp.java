package br.com.micromorph.domain.service.imp;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.domain.enums.SourceEnum;
import br.com.micromorph.domain.service.DataServicePersistence;
import br.com.micromorph.domain.service.DataService;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataServiceImp implements DataService {

    @Autowired
    DataServicePersistence dataServicePersistence;

    @Override
    public Data createAndPersistDataObject(MicromorphDataDTO micromorphData) throws IOException, PersistenceDeserializationException {
        return dataServicePersistence.insertIntoDataIndex(MicromorphDataToData(micromorphData));
    }

    @Override
    public Page<Data> findAllData(Integer page) {
        return dataServicePersistence.findAll(page);
    }

    @Override
    public List<Data> findAllByMetadata(RequestByMetadataDTO requestByMetadata) throws IOException {
        return dataServicePersistence.findAllByMetadata(requestByMetadata)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public Data findById(String id) {
        return  dataServicePersistence.findById(id);
    }

    @Override
    public void deleteById(String id) throws PersistenceDeserializationException{
        dataServicePersistence.deleteDataById(id);
    }

    private Data MicromorphDataToData(MicromorphDataDTO micromorphData) {
        if(micromorphData.getMicromorphMetaData().getName().isEmpty()){
            throw new ResourceNotFoundException("No metadata name found");
        }

        return Data.builder()
                .metaName(micromorphData.getMicromorphMetaData().getName())
                .metaSource(
                        micromorphData.getMicromorphMetaData().getSource() != null
                                ? micromorphData.getMicromorphMetaData().getSource()
                                : SourceEnum.API.name()
                )
                .metaContentHash(
                        micromorphData.getMicromorphMetaData().getContentHash() != null
                                ? micromorphData.getMicromorphMetaData().getContentHash()
                                : DigestUtils.sha256Hex(micromorphData.getFileJsonFileContent())
                )
                .metaCreatedAtEpoch(
                        micromorphData.getMicromorphMetaData().getCreatedAt() != null
                                ? micromorphData.getMicromorphMetaData().getCreatedAt().toEpochSecond(OffsetDateTime.now().getOffset())
                                : LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset())
                )
                .metaDocumentFormat(
                        micromorphData.getMicromorphMetaData().getDocumentFormat() != null
                                ? micromorphData.getMicromorphMetaData().getDocumentFormat()
                                : "json"
                )
                .metaFileSizeKilobytes(
                        micromorphData.getMicromorphMetaData().getFileSize() != null
                                ? micromorphData.getMicromorphMetaData().getFileSize()
                                : micromorphData.getFileJsonFileContent().getBytes(StandardCharsets.UTF_8).length / 1024
                )
                .fileContentJson(micromorphData.getFileJsonFileContent()).build();
    }
}
