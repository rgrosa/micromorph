package br.com.micromorph.domain.repository;


import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.entity.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends ElasticsearchRepository<Data, String>   {

    Page<Data> findById(String id, Pageable pageable);

    /*
    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
    Page<Data> findAllByMetadata(RequestByMetadataDTO requestByMetadata);
    */

}
