package com.anylogic.ssystem.bms.dct.service;

//import com.anylogic.ssystem.bms.dct.model.HwpFile;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
public interface AnyViewerReqService {
    String convertHwpToPdf(AttachFileVO hwpFile) throws Exception;
}
