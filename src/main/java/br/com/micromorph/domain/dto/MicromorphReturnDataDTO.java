package br.com.micromorph.domain.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicromorphReturnDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2651167754147117350L;

    private String id;
    private String metaName;
    private String metaSource;
    private String metaDocumentFormat;
    private Long metaCreatedAtEpoch;
    private Long metaFileSizeKilobytes;
    private String metaContentHash;
    private JsonNode fileContent;
    private List<String> labels;
}
