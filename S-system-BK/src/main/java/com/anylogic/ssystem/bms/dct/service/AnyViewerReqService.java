package com.anylogic.ssystem.bms.dct.service;

//import com.anylogic.ssystem.bms.dct.model.HwpFile;
import com.anylogic.ssystem.common.file.model.AttachFileVO;

import java.util.List;

public interface AnyViewerReqService {
    String convertHwpToPdf(AttachFileVO hwpFile) throws Exception;
    String convertHwpToHtml(AttachFileVO hwpFile) throws Exception;
    String convertHtmlToPdf(AttachFileVO hwpFile) throws Exception;

    String convertTextToPdf(AttachFileVO hwpFile) throws Exception;

    String convertPptxToImages(String pptxFilePath) throws Exception;
    String convertPptToImages(String pptFilePath) throws Exception;

//    String convertImagesToPdf(List<String> imagePaths) throws Exception;
    String convertImagesToPdf(List<AttachFileVO> attachFiles) throws Exception;
    //테스트용 하드코딩 임시 메서드
    String convertImagesToPdf2() throws Exception;

    String convertPPTXToPDF(String pptxPath, String pdfPath, double scale, int dpi) throws Exception;

    String convertPPTToPDF(String pptPath, String pdfPath, double scale, int dpi) throws Exception;
    String splitPDF(String pdfPath, String outputFolder) throws Exception;
}
