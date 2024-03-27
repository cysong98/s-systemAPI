/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



package com.anylogic.ssystem.bms.dct.service;

import com.anylogic.ssystem.common.exception.AnyXException;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.awt.geom.Dimension2D;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


import java.io.File;


import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.io.StreamDocumentSource;



import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;

import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/*======================================== ppt to png ====================================================*/
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
/*========================================================================================================*/

import org.apache.pdfbox.pdmodel.PDDocument; // PDFBox 라이브러리의 PDDocument 클래스를 임포트합니다. PDDocument 객체는 PDF 문서 전체를 대표합니다.
import org.apache.pdfbox.pdmodel.PDPage; // PDPage 클래스는 PDF 문서 내의 개별 페이지를 대표합니다.
import org.apache.pdfbox.pdmodel.PDPageContentStream; // PDPageContentStream 클래스는 페이지에 컨텐츠(이미지, 텍스트 등)를 추가하는 데 사용됩니다.
import org.apache.pdfbox.pdmodel.common.PDRectangle; // PDRectangle 클래스는 페이지의 크기와 형태(너비와 높이)를 정의합니다.
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject; // PDImageXObject 클래스는 PDF 문서에 이미지를 삽입할 때 사용됩니다.

import java.io.File; // File 클래스는 파일과 디렉토리 경로명의 추상 표현입니다.
import java.io.IOException; // IOException은 입출력 작업 실패 또는 인터럽트 시 발생하는 예외를 처리합니다.
import java.util.List; // List 인터페이스는 순서가 있는 컬렉션을 나타냅니다. 이미지 파일 경로를 저장하는 데 사용됩니다.

import java.io.*;

@Slf4j
@RequiredArgsConstructor
@Service("AnyViewerReqService")
@Transactional
public class AnyViewerReqServiceImpl implements AnyViewerReqService {

//    @Autowired
//    private HwpToPdfMapper hwpToPdfMapper;

    @Override
    public String convertHwpToPdf(AttachFileVO hwpFile) throws Exception {
        String hwpFilePath = hwpFile.getFlepath();
        String pdfFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/path_to_output_pdf_file.pdf";

//        // HWP 파일을 로드
//        File fileObj = new File(hwpFilePath);
////        File hwpFile = hwpFilePath;
//
//        // PDF 문서를 생성
//        PDDocument document = PDDocument.load(fileObj);
//
//        // PDF 문서를 출력
////        PrinterJob job = PrinterJob.getPrinterJob();
////        job.setPageable(new PDFPageable(document));
//
//        // PDF 파일로 저장
////        job.print();
//        document.save(pdfFilePath);

        return pdfFilePath;
    }

    public void convert(String hwpFilePath, String htmlFilePath, String pdfFilePath) throws Exception {
        // 1. Convert HWP to HTML
//        convertHwpToHtml(hwpFilePath, htmlFilePath);

        // 2. Convert HTML to PDF
        // TODO: Implement HTML to PDF conversion
        // This may involve using a library like Apache PDFBox
    }

//    private void convertHwpToHtml(String hwpFilePath, String htmlFilePath) throws Exception {

    // hwp to html
    public String convertHwpToHtml(AttachFileVO param) throws Exception {
        String hwpFilePath = param.getFlepath();
        String htmlFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/path_to_output_pdf_file.html";
        HWPFile hwpFile = HWPReader.fromFile(hwpFilePath);
        if (hwpFile != null) {
            String extractedText = TextExtractor.extract(hwpFile,TextExtractMethod.InsertControlTextBetweenParagraphText);
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(htmlFilePath)))) {
                out.println(extractedText);
            }
        }
        return htmlFilePath;
    }

    // html -> pdf
    public String convertHtmlToPdf(AttachFileVO param) throws Exception {
        String hwpFilePath = param.getFlepath();
        String pdfFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/pdf_path_to_output_pdf_file.pdf";
        InputStream ishwpFilePath = new FileInputStream(hwpFilePath);
        try (PDDocument pdfDoc = new PDDocument()) {
            DocumentSource docSource = new StreamDocumentSource(ishwpFilePath, new URL("file:."), "utf-8");
            org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(docSource.getInputStream());
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new Dimension2D.Float(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new Dimension2D.Float(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
            // A4 크기를 포인트 단위로 계산
            float a4WidthInPoints = (210 / 25.4f) * 72;
            float a4HeightInPoints = (297 / 25.4f) * 72;
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new FloatDimension(PDDocument.PAGE_SIZE_A4.getWidth(), PDDocument.PAGE_SIZE_A4.getHeight()));
//            PDFRenderer pdfRenderer = new PDFRenderer(doc, docSource.getURL(), pdfDoc, new FloatDimension(a4WidthInPoints, a4HeightInPoints));


//            // Render and save
//            pdfRenderer.renderDocument();
//            pdfDoc.save(pdfFilePath);
            // Render each page and save
            for (int pageIndex = 0; pageIndex < pdfDoc.getNumberOfPages(); pageIndex++) {
//                BufferedImage image = pdfRenderer.renderImage(pageIndex);
                // Save the image or perform other operations
            }
            pdfDoc.save(pdfFilePath);
        }
        return pdfFilePath;
    }

    // txt -> pdf
    public String convertTextToPdf (AttachFileVO param) throws Exception {
        String textFilePath = param.getFlepath(); // 텍스트 파일 경로
        String pdfFilePath = "//172.18.18.29/share/fileUpload/2024/3_sabon/txt_path_to_output_pdf_file.pdf"; // 출력 PDF 파일 경로


        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDType0Font font = PDType0Font.load(document, new File("//172.18.18.29/share/anyFont/NanumSquareNeo-Variable.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page);
                 BufferedReader br = new BufferedReader(new FileReader(textFilePath))) {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(25, 725);

                String line;
                while ((line = br.readLine()) != null) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -15); // 다음 줄로 이동
                }
                contentStream.endText();
            }

            document.save(pdfFilePath);
            System.out.println("PDF created successfully at " + pdfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFilePath;
    }



    // pptx -> png
    public String convertPptxToImages(String pptxFilePath) throws IOException {
//        pptFilePath = "//172.18.18.29/share/forConvertTest/01_input/pptx/testpptx.pptx";
        String outputDirectory = "//172.18.18.29/share/forConvertTest/02_output/images";

        try (FileInputStream inputStream = new FileInputStream(pptxFilePath);
             XMLSlideShow pptx = new XMLSlideShow(inputStream)) {

            Dimension pgsize = pptx.getPageSize();
            List<XSLFSlide> slides = pptx.getSlides();

            for (int i = 0; i < slides.size(); i++) {
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                slides.get(i).draw(graphics);

                String outputFileName = outputDirectory + "/slide-" + (i + 1) + ".png";
                try (FileOutputStream out = new FileOutputStream(outputFileName)) {
                    ImageIO.write(img, "png", out);
                }
            }
            return "PPTX Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to convert PPT to images due to an IO exception.";
        }
    }

    @Override
    public String convertPptToImages(String pptFilePath) throws Exception {
        String outputDirectory = "//172.18.18.29/share/forConvertTest/02_output/images";

        try (FileInputStream inputStream = new FileInputStream(pptFilePath);
             HSLFSlideShow ppt = new HSLFSlideShow(inputStream)) {

            Dimension pgsize = ppt.getPageSize();
            List<HSLFSlide> slides = ppt.getSlides();

            for (int i = 0; i < slides.size(); i++) {
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

                slides.get(i).draw(graphics);

                String outputFileName = outputDirectory + "/pptslide-" + (i + 1) + ".png";
                try (FileOutputStream out = new FileOutputStream(outputFileName)) {
                    ImageIO.write(img, "png", out);
                }
            }
            return "PPT Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to convert PPT to images due to an IO exception.";
        }
    }

    /**
     * //테스트용 하드코딩 임시 메서드
     *
     * @param imagePaths 이미지 파일 경로 리스트
     * @param outputPath PDF 파일이 저장될 경로
     */
    public String convertImagesToPdf(List<String> imagePaths) {
        List<String> hardcodedImagePaths = Arrays.asList(
                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-1.png",
                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-2.png",
                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-3.png"
        );
        imagePaths = new ArrayList<>(Arrays.asList(
                "path/to/your/image1.png",
                "path/to/your/image2.png"
        ));
        imagePaths = hardcodedImagePaths;

        String outputPath = "//172.18.18.29/share/forConvertTest/02_output/images/convertedImages.pdf";
        try (PDDocument document = new PDDocument()) {
            for (String imagePath : imagePaths) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    float scale = Math.min(page.getMediaBox().getWidth() / image.getWidth(), page.getMediaBox().getHeight() / image.getHeight());
                    float imageWidth = image.getWidth() * scale;
                    float imageHeight = image.getHeight() * scale;
                    contentStream.drawImage(image, (page.getMediaBox().getWidth() - imageWidth) / 2, (page.getMediaBox().getHeight() - imageHeight) / 2, imageWidth, imageHeight);
                }
            }
            document.save(outputPath);
            System.out.println("PDF 파일 생성 완료: " + outputPath);
            return "PDF Conversion completed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("PDF 변환 중 오류가 발생하였습니다.");
            return "PDF로 변환하는 과정에서 오류가 발생했습니다.";
        }
    }

    /**
     * 이미지 파일들을 PDF로 변환하고 합치는 메소드입니다.
     *
     * @param imagePaths 이미지 파일 경로 리스트
     * @param outputPath PDF 파일이 저장될 경로
     */
    public String convertImagesToPdf2() {
        // 하드코딩된 이미지 경로 리스트
        List<String> imagePaths = Arrays.asList(
                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-1.png",
                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-2.png",
                "//172.18.18.29/share/forConvertTest/02_output/images/pptslide-3.png"
        );

        // 이후 PDF 변환 로직...
        try {
            String outputPath = "//172.18.18.29/share/forConvertTest/02_output/images/convertedImages.pdf";
            try (PDDocument document = new PDDocument()) {
                for (String imagePath : imagePaths) {
                    PDPage page = new PDPage(PDRectangle.A4);
                    document.addPage(page);

                    PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);
                    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                        float scale = Math.min(page.getMediaBox().getWidth() / image.getWidth(), page.getMediaBox().getHeight() / image.getHeight());
                        float imageWidth = image.getWidth() * scale;
                        float imageHeight = image.getHeight() * scale;
                        contentStream.drawImage(image, (page.getMediaBox().getWidth() - imageWidth) / 2, (page.getMediaBox().getHeight() - imageHeight) / 2, imageWidth, imageHeight);
                    }
                }
                document.save(outputPath);
                System.out.println("PDF 파일 생성 완료: " + outputPath);
                return "PDF Conversion completed successfully.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("PDF 변환 중 오류가 발생하였습니다.");
            return "PDF로 변환하는 과정에서 오류가 발생했습니다.";
        }
    }

}
