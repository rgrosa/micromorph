package br.com.micromorph.domain.service.imp;

import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.domain.repository.DataRepository;
import br.com.micromorph.domain.service.DataServicePersistence;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


@Service
public class DataServicePersistenceImp implements DataServicePersistence {

    @Autowired
    DataRepository dataRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private static final String DEFAULT_INDEX_NAME = "data_index";
    private static final int FIVE_MINUTES_IN_SECONDS =  300;
    private final static Integer C_MAX_PAGE_SIZE = 1000;

    @Override
    public Data insertIntoDataIndex(Data data) throws PersistenceDeserializationException {
        try{
            return dataRepository.save(data);
        }catch (DataAccessResourceFailureException ex){
            if (ex.getMessage().contains("201")){
                throw new PersistenceDeserializationException("Data Created");
            }
            throw new DataAccessResourceFailureException(ex.getMessage());
        }
    }

    @Override
    public void insertBatchIntoDataIndex(List<Data> dataList) throws PersistenceDeserializationException {
        try{
            dataRepository.saveAll(dataList);
        }catch (DataAccessResourceFailureException ex){
            if (ex.getMessage().contains("201") || ex.getMessage().contains("200")){
                throw new PersistenceDeserializationException("Data Created");
            }
            throw new DataAccessResourceFailureException(ex.getMessage());
        }
    }

    @Override
    public Page<Data> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, C_MAX_PAGE_SIZE);
        return dataRepository.findAll(pageable);
    }

    @Override
    public SearchHits<Data> findAllByMetadata(RequestByMetadataDTO requestByMetadata) {
        SearchHits<Data> productHits =
                elasticsearchOperations
                        .search(generateQuery(requestByMetadata),
                                Data.class,
                                IndexCoordinates.of(DEFAULT_INDEX_NAME));
        return  productHits;
    }

    @Override
    public void deleteDataById(String id) throws PersistenceDeserializationException {
        try {
            dataRepository.deleteById(id);
        } catch (DataAccessResourceFailureException ex) {
            if (ex.getMessage().contains("200")) {
                throw new PersistenceDeserializationException("Data Deleted");
            }
        }
    }

    @Override
    public Data findById(String id) {
        return dataRepository.findById(id).orElse(null);
    }

    private Query generateQuery(RequestByMetadataDTO requestByMetadata) {
        Criteria criteria = new Criteria();
        if(requestByMetadata.getMicromorphMetaData().getName() != null){
            criteria.and(new Criteria("metaName").is((requestByMetadata.getMicromorphMetaData().getName())));
        }
        if(requestByMetadata.getMicromorphMetaData().getSource() != null){
            criteria.and(new Criteria("metaSource").is((requestByMetadata.getMicromorphMetaData().getSource())));
        }
        if(requestByMetadata.getMicromorphMetaData().getDocumentFormat() != null){
            criteria.and(new Criteria("metaDocumentFormat").is((requestByMetadata.getMicromorphMetaData().getDocumentFormat())));
        }
        if(requestByMetadata.getFromDate() != null && requestByMetadata.getToDate() != null ){
            criteria.and(new Criteria("metaCreatedAtEpoch").between(requestByMetadata.getFromDate(), requestByMetadata.getToDate()));
        }
        if(requestByMetadata.getFromDate() != null && requestByMetadata.getToDate() == null ){
            criteria.and(new Criteria("metaCreatedAtEpoch").greaterThanEqual(requestByMetadata.getFromDate()));
        }
        if(requestByMetadata.getFromDate() == null && requestByMetadata.getToDate() != null ){
            criteria.and(new Criteria("metaCreatedAtEpoch").lessThanEqual(requestByMetadata.getToDate()));
        }
        if(requestByMetadata.getMicromorphMetaData().getLabels() != null){
            criteria.and(new Criteria("metaLabelList").in(requestByMetadata.getMicromorphMetaData().getLabels()));
        }
        if(requestByMetadata.getFromDate() == null && requestByMetadata.getToDate() == null && criteria.getCriteriaChain().isEmpty() ){
            criteria.and(new Criteria("metaCreatedAtEpoch").greaterThanEqual(LocalDateTime.now().toEpochSecond(OffsetDateTime.now().getOffset()) - FIVE_MINUTES_IN_SECONDS));
        }
        return new CriteriaQuery(criteria);
    }
}