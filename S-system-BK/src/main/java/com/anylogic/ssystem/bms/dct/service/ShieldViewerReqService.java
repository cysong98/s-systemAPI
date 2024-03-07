package com.anylogic.ssystem.bms.dct.service;

import com.anylogic.ssystem.bms.dct.model.CheckDocReqVO;
import com.anylogic.ssystem.bms.dct.model.CheckDocResVO;
import com.anylogic.ssystem.bms.dct.model.RegisterDocReqVO;
import com.anylogic.ssystem.bms.dct.model.RegisterDocResVO;
import com.anylogic.ssystem.common.file.model.AttachFileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.File;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShieldViewerReqService {
    private final WebClient poolWebClientForViewer;
    private static final String URI_CREATE_REQ = "/dms/api/create";
    private static final String URI_CREATE_FILEPATH_REQ = "/dms/api/createFilePath";
    private static final String URI_CHECK_REQ = "/dms/api/check/comp?fid=";
    private static final String BODY_KEY_USER_ID = "user_id";


    // 원본 문서 파라미터 방식
    public RegisterDocResVO registerDocByFile(MultipartFile mf, RegisterDocReqVO vo) {
        RegisterDocResVO result;
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("files", mf.getResource());
        result = poolWebClientForViewer.post()
                                    .uri(URI_CREATE_REQ)
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                                    .body(BodyInserters.fromMultipartData(builder.build())
                                                        .with(BODY_KEY_USER_ID, vo.getUserid()))
                                    .retrieve()
                                    .bodyToMono(RegisterDocResVO.class)
                                    .onErrorResume(e -> {
                                        log.error(e.getMessage());
                                        return Mono.just(null);
                                    })
                                    .block();
        return result;
    }


    // 원본 문서경로 파라미터 방식
    public RegisterDocResVO registerDocByFilePath(AttachFileVO vo) {
        RegisterDocResVO result;
        File file = new File(vo.getFlepath());
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file_path", file.toPath());
        result = poolWebClientForViewer.post()
                                    .uri(URI_CREATE_FILEPATH_REQ)
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                                    .body(BodyInserters.fromMultipartData(builder.build())
                                                        .with(BODY_KEY_USER_ID, vo.getInid()))
                                    .retrieve()
                                    .bodyToMono(RegisterDocResVO.class)
                                    .onErrorResume(e -> {
                                        log.error(e.getMessage());
                                        return Mono.just(null);
                                    })
                                    .block();
        return result;
    }


    // 문서 변환 확인
    public CheckDocResVO checkDocConvert(CheckDocReqVO param) {
        String uri = URI_CHECK_REQ + param.getFid();
        CheckDocResVO result;
        result = poolWebClientForViewer.get()
                                    .uri(uri)
                                    .retrieve()
                                    .bodyToMono(CheckDocResVO.class)
                                    .retryWhen(Retry.backoff(2, Duration.ofSeconds(2)).jitter(0.75))
                                    .onErrorResume(e -> {
                                        log.error(e.getMessage());
                                        return Mono.just(null);
                                    })
                                    .block();
        return result;
    }
}