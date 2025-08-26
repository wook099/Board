package pr1.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pr1.board.dto.PostRequestDto;
import pr1.board.dto.PostResponseDto;
import pr1.board.entity.User;
import pr1.board.repository.UserRepository;
import pr1.board.service.LikeService;
import pr1.board.service.PostService;
import pr1.board.service.UserDetailsImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PostRequestDto dto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        Long postId = postService.write(dto,user);
        System.out.println(user.getUsername());

        return ResponseEntity.ok(postId);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAll() {
        List<PostResponseDto> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto dto = postService.getPost(id);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {

        User loginuser = userDetails.getUser();

        postService.updatePost(id, dto,loginuser);
        return ResponseEntity.ok("수정 성공");
    }


    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws AccessDeniedException {

        User loginuser = userDetails.getUser();

        postService.deletePost(id,loginuser);
        return ResponseEntity.ok("삭제 성공");
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @RequestParam Long userId) {

        boolean likeCount = likeService.toggleLike(postId, userId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikeCount(postId));
    }
}
