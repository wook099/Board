package pr1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPurgeScheduler {

    private final PostService postService;

    // 매일 자정(00:00)에 실행됨 단 배포시 작동가능함
    @Scheduled(cron = "0 0 0 * * ?")
    public void purgeOldDeletedPosts() {
        postService.purgeOldDeletedPosts();
    }
}
