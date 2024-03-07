package com.anylogic.ssystem.bms.dct.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConvertPDFService {
    private final WebClient poolWebClientForConvertPDF;
    private static final String URI_CREATE_REQ = "/api/convert";


    /* PDF변환 */
    public String convertPDF(String source, String target) {

        String strUri = URI_CREATE_REQ + "?source=/nas_data7" + source + "&" + "target=/nas_data7" + target;
        try {
            String responseStr = poolWebClientForConvertPDF.get()
                    .uri(strUri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return responseStr;
        } catch (Exception e) {
            log.error("Error occurred during PDF conversion", e);
            return "Error: PDF 변환 실패";
        }
    }

}