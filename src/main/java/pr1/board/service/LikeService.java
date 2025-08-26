package pr1.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pr1.board.entity.Post;
import pr1.board.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 알림
    private final PostRepository postRepository;


    private String getLikeKey(Long postId) {
        return "post:like:" + postId;
    }

    public boolean toggleLike(Long postId, Long userId) {

        // 게시글 존재 확인 + Post 객체 캐싱
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음 id=" + postId));

        String key = getLikeKey(postId); // redis set key
        String member = String.valueOf(userId);

        // 이미 좋아요 눌렀는지 확인
        Boolean isMember = redisTemplate.opsForSet().isMember(key, member);//opsForSet-> 레디스의 set자료구조임
        //-> opsForSet().isMember 집합에 해당 맴버 있는지 확인

        if (Boolean.TRUE.equals(isMember)) {
            // 좋아요 취소
            redisTemplate.opsForSet().remove(key, member);
            Long likeCount = redisTemplate.opsForSet().size(key);

            System.out.println("[좋아요 취소] postId=" + postId + " userId=" + userId + " / 총 좋아요: " + likeCount);
            return false; // 현재 상태 = 좋아요 안 누름
        } else {
            // 좋아요 등록
            redisTemplate.opsForSet().add(key, member);
            Long likeCount = redisTemplate.opsForSet().size(key);

            // 알림 전송
            String notificationMsg = "게시글에 새로운 좋아요! 총 좋아요: " + likeCount;
            messagingTemplate.convertAndSend("/topic/notifications/" + post.getAuthor().getId(), notificationMsg);

            System.out.println("[알림 전송] " + notificationMsg + " / 수신자: " + post.getAuthor().getId());
            return true; // 현재 상태 = 좋아요 누름
        }
    }


    public Long getLikeCount(Long postId) {
        String key = getLikeKey(postId);
        Long size = redisTemplate.opsForSet().size(key); // Set에 몇 명이 좋아요 눌렀는지
        return size != null ? size : 0;
    }

        /*public Long likePost(Long postId, Long userId) {

        String key = getLikeKey(postId);

        // Redis INCR
        Long likeCount = redisTemplate.opsForValue().increment(key, 1);
        // 원자적 값 증가, 레디스 자체가 단일 스레드 동시요청 안전 , 단점: 누를때마다 무한히 증가

        // DB 저장 선택적: 필요하면 JPA 엔티티에 기록
        Post post = postRepository.findById(postId).orElseThrow();
        // post.setLikeCount(likeCount); // 필요하면 DB에 반영
        postRepository.save(post);

//        // 웹소켓 알림: 작성자에게 전송
//        messagingTemplate.convertAndSend("/topic/notifications/" + post.getAuthor().getId(),
//                "게시글에 새로운 좋아요! 총 좋아요: " + likeCount);

        // 웹소켓 알림 전송
        String notificationMsg = "게시글에 새로운 좋아요! 총 좋아요: " + likeCount;
        messagingTemplate.convertAndSend("/topic/notifications/" + post.getAuthor().getId(), notificationMsg);

        System.out.println("[알림 전송] " + notificationMsg + " / 수신자: " + post.getAuthor().getId());

        return likeCount;
    }*/
}
