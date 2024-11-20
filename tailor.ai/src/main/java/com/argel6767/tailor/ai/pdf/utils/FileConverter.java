package com.argel6767.tailor.ai.pdf.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileConverter {

    /*
     * converts the file back into a regular File object
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile)  {
        // Create a temporary file and copy the contents of the multipart file
        File file = null;
        try {
            file = File.createTempFile("uploaded-", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
