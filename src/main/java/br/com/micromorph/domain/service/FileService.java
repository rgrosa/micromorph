package br.com.micromorph.domain.service;

import br.com.micromorph.domain.entity.Data;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Data postFile(MultipartFile studentDTO) throws Exception;

}
