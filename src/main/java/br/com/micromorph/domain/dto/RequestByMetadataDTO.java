package br.com.micromorph.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestByMetadataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5405557479372292999L;

        MicromorphMetaDataDTO micromorphMetaData;
        Long fromDate;
        Long toDate;
}
