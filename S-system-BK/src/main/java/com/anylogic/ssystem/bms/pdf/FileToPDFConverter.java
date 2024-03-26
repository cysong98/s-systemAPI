package com.anylogic.ssystem.bms.pdf;
import java.io.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.xslf.usermodel.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/pdfconverter")
public class FileToPDFConverter {

    @PostMapping("/pptToPDFConverter")
    public void pptToPDFConverter(@RequestParam("filepath") String filePath) {
        try {
            PDDocument pdfDocument = convertPPTToPDF(filePath);
            pdfDocument.save(filePath.replace(".pptx", ".pdf"));
            pdfDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ppt -> pdf
    private PDDocument convertPPTToPDF(String filePath) throws IOException {
        try (InputStream pptInputStream = new FileInputStream(filePath)) {
            XMLSlideShow ppt = new XMLSlideShow(pptInputStream);
            PDDocument pdfDocument = new PDDocument();

            for (XSLFSlide slide : ppt.getSlides()) {
                PDPage page = new PDPage();
                pdfDocument.addPage(page);
            }

            return pdfDocument;
        }
    }

    // docx -> pdf
    public static void convertWordToPDF(String inputFilePath, String outputFilePath) throws IOException {
        try (InputStream docInputStream = new FileInputStream(inputFilePath);
             OutputStream pdfOutputStream = new FileOutputStream(outputFilePath)) {
            XWPFDocument document = new XWPFDocument(docInputStream);
            document.write(pdfOutputStream);
        }
    }


    // xlsx -> pdf
    public static void convertExcelToPDF(String inputFilePath, String outputFilePath) throws IOException {
        try (InputStream excelInputStream = new FileInputStream(inputFilePath);
             OutputStream pdfOutputStream = new FileOutputStream(outputFilePath)) {
            XSSFWorkbook workbook = new XSSFWorkbook(excelInputStream);
            PDDocument pdfDocument = new PDDocument();
            PDPage page = new PDPage();
            pdfDocument.addPage(page);
            pdfDocument.save(pdfOutputStream);
            pdfDocument.close();
        }
    }

}
