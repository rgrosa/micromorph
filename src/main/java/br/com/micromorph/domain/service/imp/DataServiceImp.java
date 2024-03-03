package br.com.micromorph.domain.service.imp;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.MicromorphMetaDataDTO;
import br.com.micromorph.domain.dto.MicromorphReturnDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.domain.enums.SourceEnum;
import br.com.micromorph.domain.service.DataServicePersistence;
import br.com.micromorph.domain.service.DataService;
import br.com.micromorph.infrasctructure.exception.NotSupportedException;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DataServiceImp implements DataService {

    @Autowired
    DataServicePersistence dataServicePersistence;

    @Override
    public void createAndPersistDataObject(List<MicromorphDataDTO> micromorphDataList) throws IOException, PersistenceDeserializationException, NotSupportedException {
        List<Data> dataList = new ArrayList<>();
        for(MicromorphDataDTO micromorphData: micromorphDataList){
            dataList.add(MicromorphDataToData(micromorphData));
        }
        dataServicePersistence.insertBatchIntoDataIndex(dataList);
    }

    @Override
    public Data createAndPersistDataObject(MicromorphDataDTO micromorphData) throws IOException, PersistenceDeserializationException, NotSupportedException {
            return dataServicePersistence.insertIntoDataIndex(MicromorphDataToData(micromorphData));
    }

    @Override
    public Page<MicromorphReturnDataDTO> findAllData(Integer page) throws JsonProcessingException {
        List<MicromorphReturnDataDTO> micromorphReturnDataDTOList = new ArrayList<>();

        var dataList = dataServicePersistence.findAll(page);

        for (Data data : dataList) {
            micromorphReturnDataDTOList.add(toMicromorphReturnData(data));
        }

        return new PageImpl<>(micromorphReturnDataDTOList,
                dataList.getPageable(),
                micromorphReturnDataDTOList.size()
        );
    }

    @Override
    public List<MicromorphReturnDataDTO> findAllByMetadata(RequestByMetadataDTO requestByMetadata) throws IOException {
        List<MicromorphReturnDataDTO> micromorphReturnDataDTOList = new ArrayList<>();

        var dataList = dataServicePersistence.findAllByMetadata(requestByMetadata)
                .stream()
                .map(SearchHit::getContent)
                .toList();

        for (Data data : dataList) {
            micromorphReturnDataDTOList.add(toMicromorphReturnData(data));
        }
        return micromorphReturnDataDTOList;
    }


    @Override
    public MicromorphReturnDataDTO findById(String id) throws JSONException, JsonProcessingException {
        return toMicromorphReturnData(dataServicePersistence.findById(id));
    }

    
    //problema na conversao arrumar.
    
    private MicromorphReturnDataDTO toMicromorphReturnData(Data data) throws  JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(data.getFileContent());
        return MicromorphReturnDataDTO.builder()
                .id(data.getId())
                .metaContentHash(data.getMetaContentHash())
                .metaLabels(data.getMetaLabels())
                .metaCreatedAtEpoch(data.getMetaCreatedAtEpoch())
                .metaDocumentFormat(data.getMetaDocumentFormat())
                .metaSource(data.getMetaSource())
                .metaName(data.getMetaName())
                .metaFileSizeKilobytes(data.getMetaFileSizeKilobytes())
                .fileContent(json).build();
    }

    @Override
    public void deleteById(String id) throws PersistenceDeserializationException{
        dataServicePersistence.deleteDataById(id);
    }

    private Data MicromorphDataToData(MicromorphDataDTO micromorphData) throws NotSupportedException {
        if(micromorphData.getMicromorphMetaData() == null){
            micromorphData.setMicromorphMetaData(MicromorphMetaDataDTO.builder().name(UUID.randomUUID().toString()).build() );
        }
        var convertedFileContent = convertToFileDataToJson(micromorphData.getFileContent());

        return Data.builder()
                .metaName(micromorphData.getMicromorphMetaData().getName())
                .metaSource(
                        micromorphData.getMicromorphMetaData().getSource() != null
                                ? micromorphData.getMicromorphMetaData().getSource()
                                : SourceEnum.API.name()
                )
                .metaContentHash(DigestUtils.sha256Hex(convertedFileContent))
                .metaCreatedAtEpoch(LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset()))
                .metaDocumentFormat(
                        micromorphData.getMicromorphMetaData().getDocumentFormat() != null
                                ? micromorphData.getMicromorphMetaData().getDocumentFormat()
                                : "json"
                )
                .metaFileSizeKilobytes(
                        (long) (convertedFileContent.getBytes(StandardCharsets.UTF_8).length / 1024)
                )
                .metaLabels(micromorphData.getMicromorphMetaData().getLabels())
                .fileContent(convertedFileContent).build();
    }

    private String convertToFileDataToJson(String fileContent) throws NotSupportedException {
        if(StringUtils.isNotEmpty(fileContent)){
            try {
                Gson gson = new Gson();
                fileContent = fileContent.replace("\n", "");
                fileContent = gson.toJson(new ObjectMapper().readValue(fileContent, Object.class));
            } catch (JsonProcessingException e) {
                throw new NotSupportedException("The data sent it`s not supported, please send a json");
            }
        }
        return fileContent;
    }
}