package com.anylogic.ssystem.bms.dct.service;

//import com.anylogic.ssystem.bms.dct.model.HwpFile;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
public interface AnyViewerReqService {
    String convertHwpToPdf(AttachFileVO hwpFile) throws Exception;
    String convertHwpToHtml(AttachFileVO hwpFile) throws Exception;
    String convertHtmlToPdf(AttachFileVO hwpFile) throws Exception;

    String convertTextToPdf(AttachFileVO hwpFile) throws Exception;

    String convertPptxToImages(String pptFilePath) throws Exception;
    String convertPptToImages(String pptFilePath) throws Exception;
}
