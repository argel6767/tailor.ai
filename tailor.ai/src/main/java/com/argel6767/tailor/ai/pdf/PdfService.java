package com.argel6767.tailor.ai.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * holds the service logic for reading pdf files to allow for chat completions to read the file
 */
@Service
public class PdfService {

    public String readFile(File file) throws IOException {
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        String text = stripper.getText(document);
        document.close();
        return text;
    }

    public ResponseEntity<?> sendPDFToBucket(File file) {

    }
}
