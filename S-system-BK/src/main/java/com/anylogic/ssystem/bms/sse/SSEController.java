package com.anylogic.ssystem.bms.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/sse")
public class SSEController {

    private final List<SseEmitter> sseEmitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() throws IOException {
        SseEmitter emitter = new SseEmitter();
        sseEmitters.add(emitter);

        emitter.onCompletion(() -> sseEmitters.remove(emitter));
        emitter.onTimeout(() -> {
            sseEmitters.remove(emitter);
            emitter.complete();
        });

        emitter.send(SseEmitter.event()
                .name("connect")
                .data("dummyDataFor503Error"));

        return ResponseEntity.ok(emitter);
    }

    public void progressCheck(String progress) {
        List<SseEmitter> emittersToRemove = new ArrayList<>();
        for (SseEmitter emitter : sseEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("progress")
                        .data(progress));
            } catch (IOException e) {
                emittersToRemove.add(emitter);
            }
        }
        sseEmitters.removeAll(emittersToRemove);
    }

    @GetMapping("/initializeSSE")
    private void initializeSSE() throws IOException{
        try {
            for (SseEmitter emitter : sseEmitters) {
                sseEmitters.remove(emitter);
            }
        } catch (ConcurrentModificationException e) {}
    }
}
