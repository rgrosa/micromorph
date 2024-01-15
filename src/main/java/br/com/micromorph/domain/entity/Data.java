package br.com.micromorph.domain.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.json.Json;

@Document(indexName = "data_index")
@Builder
@lombok.Data
public class Data {

    @Id
    private String id;

    @Field(type = FieldType.Keyword, name = "metaName")
    private String metaName;

    @Field(type = FieldType.Keyword, name = "metaSource")
    private String metaSource;

    @Field(type = FieldType.Keyword, name = "metaDocumentFormat")
    private String metaDocumentFormat;

    @Field(type = FieldType.Keyword, name = "metaCreatedAtEpoch")
    private Long metaCreatedAtEpoch;

    @Field(type = FieldType.Keyword, name = "metaFileSizeKilobytes")
    private Long metaFileSizeKilobytes;

    @Field(type = FieldType.Keyword, name = "metaContentHash")
    private String metaContentHash;

    @Field(type = FieldType.Object, name = "fileContentJson")
    private Object fileContentJson;
}