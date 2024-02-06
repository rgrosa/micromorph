package br.com.micromorph.application.controller;


import br.com.micromorph.application.resource.Response;
import br.com.micromorph.domain.dto.DeleteRequestDTO;
import br.com.micromorph.domain.dto.MicromorphDataDTO;
import br.com.micromorph.domain.dto.MicromorphMetaDataDTO;
import br.com.micromorph.domain.dto.RequestByMetadataDTO;
import br.com.micromorph.domain.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
public class DataController {

    @Autowired
    DataService dataService;

    @PostMapping("/data")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> postData(
            @Valid @RequestBody MicromorphDataDTO micromorphData) throws Exception {
        return ResponseEntity.ok().
                body(new Response(
                        200,
                        "Success",
                        dataService.createAndPersistDataObject(micromorphData)
                ));
    }

    @GetMapping("/data/find-all-by-page")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> findAll(
            @RequestParam(required = false) Integer page) throws Exception {
        return ResponseEntity.ok().
                body(new Response(
                        200,
                        "Success",
                        dataService.findAllData(page)
                ));
    }

    @GetMapping("/data/find-all-by-metadata")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> findAllByMetadata(
            @RequestParam(required = false) String metaName,
            @RequestParam(required = false) String metaSource,
            @RequestParam(required = false) String documentFormat,
            @RequestParam(required = false) Long fromDate,
            @RequestParam(required = false) Long toDate,
            @RequestParam(required = false) List<String> labels
            ) throws Exception {
        return ResponseEntity.ok().
                body(new Response(
                        200,
                        "Success",
                        dataService.findAllByMetadata(
                                RequestByMetadataDTO.builder()
                                        .micromorphMetaData(
                                                MicromorphMetaDataDTO.builder()
                                                        .name(metaName)
                                                        .source(metaSource)
                                                        .documentFormat(documentFormat)
                                                        .labels(labels).build()
                                        ).fromDate(fromDate)
                                        .toDate(toDate)
                                        .build()
                        )
                ));
    }

    @GetMapping("/data/find-by-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> findById(
            @RequestParam String id
    ) throws Exception {
        return ResponseEntity.ok().
                body(new Response(
                                200,
                                "Success",
                                dataService.findById(id)
                ));
    }

    @DeleteMapping("/data/delete-by-id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> deleteById(
           @Valid @RequestBody(required = true) DeleteRequestDTO deleteRequest) throws Exception {
        dataService.deleteById(deleteRequest.getId());
        return ResponseEntity.ok().
                body(new Response(
                        200,
                        "Success, resource deleted",
                        null
                ));
    }

}
