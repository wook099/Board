package pr1.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pr1.board.dto.PostRequestDto;
import pr1.board.dto.PostResponseDto;
import pr1.board.entity.User;
import pr1.board.repository.UserRepository;
import pr1.board.service.PostService;
import pr1.board.service.UserDetailsImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

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
//
////    @GetMapping("/page")
////    public ResponseEntity<Page<PostResponseDto>> getPostsByPage(
////            @RequestParam(defaultValue = "0") int page,
////            @RequestParam(defaultValue = "10") int size) {
////        Page<PostResponseDto> pagePosts = postService.getPostsByPage(page, size);
////        return ResponseEntity.ok(pagePosts);
////    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String keyword) {
//        List<PostResponseDto> results = postService.searchPosts(keyword);
//        return ResponseEntity.ok(results);
//    }
//
//    @PostMapping("/{id}/like")
//    public ResponseEntity<Void> likePost(@PathVariable Long id) {
//        postService.likePost(id);
//        return ResponseEntity.ok().build();
//    }
}
