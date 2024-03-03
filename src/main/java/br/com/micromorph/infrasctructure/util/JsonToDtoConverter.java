package br.com.micromorph.infrasctructure.util;

import br.com.micromorph.domain.dto.MicromorphDataDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonToDtoConverter {

    public static MicromorphDataDTO convertJsonToDto(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        MicromorphDataDTO dto = null;
        try {
            dto = objectMapper.readValue(json, MicromorphDataDTO.class);
        } catch (Exception e) {
            dto = MicromorphDataDTO.builder().fileContent(json).build();
        }

        return dto;

    }

    public static List<MicromorphDataDTO> convertJsonToDtoList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<MicromorphDataDTO> dto = null;
        try {
            dto = objectMapper.readValue(json, new TypeReference<List<MicromorphDataDTO>>(){});
        } catch (Exception e) {
              dto = new ArrayList<>();
                dto.add(MicromorphDataDTO.builder().fileContent(json).build());
        }
        return dto;
    }


}
