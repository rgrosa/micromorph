package br.com.micromorph.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicromorphMetaDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2651167754147007350L;

    private String name;
    private String source;
    private String documentFormat;
    private LocalDateTime createdAt;
    private Long fileSize;
    private String contentHash;
}
