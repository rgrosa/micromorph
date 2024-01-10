package br.com.micromorph.domain.service.imp;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.MicromorphMetaDataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.domain.enums.SourceEnum;
import br.com.micromorph.domain.service.DataService;
import br.com.micromorph.domain.service.FileService;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FileServiceImp implements FileService {

    @Autowired
    DataService dataService;

    @Override
    public Data postFile(MultipartFile file) throws Exception {
        MicromorphDataDTO micromorphData = makeMicromorphData(file);
        micromorphData.setFileJsonFileContent(processFileData(file));
        return dataService.createAndPersistDataObject(micromorphData);
    }

    private MicromorphDataDTO makeMicromorphData(MultipartFile file) throws IOException {
        return  MicromorphDataDTO.builder()
                .micromorphMetaData(
                        MicromorphMetaDataDTO.builder()
                                .name(file.getName())
                                .source(SourceEnum.FILE_UPLOAD.toString())
                                .fileSize(file.getSize())
                                .documentFormat(file.getContentType())
                                .contentHash(DigestUtils.sha256Hex(file.getBytes()))
                                .createdAt(LocalDateTime.now()).build()
                )
                .fileJsonFileContent(
                        processFileData(file)
                ).build();
    }

    private String getFileData(InputStream fileStream) throws IOException {
        return new String(fileStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private String processFileData(MultipartFile multipartFile) throws IOException {
       String fileData = getFileData(multipartFile.getInputStream());
        return switch (Objects.requireNonNull(multipartFile.getContentType())) {
            case "csv", "xls", "xlsx" -> csvToJson(fileData);
            default -> fileData;
        };
    }

    private String csvToJson(String fileData) {
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        String returnValue = null;
        try {
            List<Map<?, ?>> list;
            try (MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader()
                    .forType(Map.class)
                    .with(csvSchema)
                    .readValues(fileData)) {
                list = mappingIterator.readAll();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            returnValue = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(list);

        } catch (IOException e) {
            e.printStackTrace();

        }
        if (returnValue != null){
            returnValue = returnValue.replace("\n", "");
        }

        return returnValue;
    }


}
