package br.com.micromorph.domain.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "#{@environment.getProperty('micromorph.elastic.index.prefix')}")
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

    @Field(type = FieldType.Long, name = "metaCreatedAtEpoch")
    private Long metaCreatedAtEpoch;

    @Field(type = FieldType.Keyword, name = "metaFileSizeKilobytes")
    private Long metaFileSizeKilobytes;

    @Field(type = FieldType.Keyword, name = "metaContentHash")
    private String metaContentHash;

    @Field(type = FieldType.Text, name = "fileContent")
    private String fileContent;

    @Field(type = FieldType.Keyword, name = "metaLabelList")
    private List<String> labels;

}