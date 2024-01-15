package br.com.micromorph.domain.service.imp;

import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.domain.repository.DataRepository;
import br.com.micromorph.domain.service.DataServicePersistence;
import br.com.micromorph.infrasctructure.exception.PersistenceDeserializationException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


@Service
public class DataServicePersistenceImp implements DataServicePersistence {

    @Autowired
    DataRepository dataRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private static final String DEFAULT_INDEX_NAME = "data_index";

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
            criteria.and("metaName").is(requestByMetadata.getMicromorphMetaData().getName());
        }
        if(requestByMetadata.getMicromorphMetaData().getSource() != null){
            criteria.and("metaSource").is(requestByMetadata.getMicromorphMetaData().getSource());
        }
        if(requestByMetadata.getMicromorphMetaData().getDocumentFormat() != null){
            criteria.and("metaDocumentFormat").is(requestByMetadata.getMicromorphMetaData().getDocumentFormat());
        }
        if(requestByMetadata.getFromDate() != null && requestByMetadata.getToDate() != null ){
            criteria.and("metaCreatedAtEpoch").between(requestByMetadata.getFromDate(), requestByMetadata.getToDate());
        }
        if(requestByMetadata.getFromDate() != null && requestByMetadata.getToDate() == null ){
            criteria.and("metaCreatedAtEpoch").greaterThanEqual(requestByMetadata.getFromDate());
        }
        if(requestByMetadata.getFromDate() == null && requestByMetadata.getToDate() != null ){
            criteria.and("metaCreatedAtEpoch").lessThanEqual(requestByMetadata.getToDate());
        }
        return new CriteriaQuery(criteria);
    }
}