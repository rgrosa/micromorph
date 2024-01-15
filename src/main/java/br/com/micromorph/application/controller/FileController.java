package br.com.micromorph.application.controller;

import br.com.micromorph.application.resource.Response;
import br.com.micromorph.domain.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Response> postFile(
            @RequestParam("file") MultipartFile file) throws Exception {
        return ResponseEntity.ok().
                body(new Response(
                        200,
                        "Success",
                        fileService.postFile(file)
                ));
    }
}