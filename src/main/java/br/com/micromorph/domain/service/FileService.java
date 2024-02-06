package br.com.micromorph.domain.service;

import br.com.micromorph.domain.entity.Data;
import br.com.micromorph.infrasctructure.exception.NotSupportedException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Data postFile(MultipartFile studentDTO) throws Exception, NotSupportedException;

}
