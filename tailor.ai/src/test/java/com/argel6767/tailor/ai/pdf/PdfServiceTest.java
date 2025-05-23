package com.argel6767.tailor.ai.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceTest {

    @Mock
    private File file = mock(File.class);
    @Mock
    private PDDocument document = mock(PDDocument.class);
    @Mock
    PDFTextStripper pdfStripper = mock(PDFTextStripper.class);
    private final String PDFCONTENT = "Blah Blah";
    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfService = spy(new PdfService());
    }

    @Test
    void testReadFile() throws IOException {
        doReturn(PDFCONTENT).when(pdfService).readFile(file);
        when(pdfStripper.getText(document)).thenReturn(PDFCONTENT);;
        String text = pdfService.readFile(file);
        assertEquals(PDFCONTENT, text);
    }
}