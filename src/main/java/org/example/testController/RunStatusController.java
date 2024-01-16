package org.example.testController;

import lombok.RequiredArgsConstructor;
import org.example.openAI.AssistantsClient;
import org.example.model.dto.openai.RunsResponseDto;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

@RestController
@RequiredArgsConstructor
public class RunStatusController {

    private final AssistantsClient assistantsClient;

//
//    @GetMapping("/create/chat")
//    public String createChat(@RequestParam String Id) {
//        try {
//            String assistantId = Id;
//            var threads = assistantsClient.createThreads();
//            assistantsClient.createMessages(threads.getId(), new MessagesRequestDto("user", "This is question"));
//            var run = assistantsClient.createRuns(threads.getId(), new RunsRequestDto(assistantId));
//            assistantsClient.getRun(threads.getId(), run.getId());
//
//            CompletableFuture<RunsResponseDto> runsResponseDtoFuture = asyncRunRequest(threads.getId(), run.getId());
//            MessagesListResponseDto d = runsResponseDtoFuture.get() != null ? assistantsClient.getMessagesList(threads.getId()) : null;
//
//            //String response = d != null && d.getData().size() > 0 && d.getData().get(0).getContent().size() > 0 ? d.getData().get(0).getContent().get(0).getText().getValue() : "";
//
//            //return response;
//            return "";
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return "error";
//        }
//    }

    public CompletableFuture<RunsResponseDto> asyncRunRequest(String threadId, String runId) {
        Instant startTime = Instant.now();
        CompletableFuture<RunsResponseDto> future = new CompletableFuture<>();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
            try {
                RunsResponseDto response = assistantsClient.getRun(threadId, runId);
                System.out.println("retrying............");
                if ("completed".equals(response.getStatus())) {
                    future.complete(response);
                }
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, 0, 1, TimeUnit.SECONDS);

        future.whenComplete((response, throwable) -> {
            scheduledFuture.cancel(true);
            Instant endTime = Instant.now();
            Duration duration = Duration.between(startTime, endTime);
            System.out.println("asyncRunRequest executed in " + duration.toMillis() + " milliseconds");
        });
        return future;
    }

    public void loopRunRequest(String threadId, String runId) {
        Instant startTime = Instant.now();
        while (true) {
            try {
                Thread.sleep(2000);
                RunsResponseDto response = assistantsClient.getRun(threadId, runId);
                System.out.println("retrying............");
                if ("completed".equals(response.getStatus())) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("error");
                return;
            }
        }
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("asyncRunRequest executed in " + duration.toMillis() + " milliseconds");
    }
}