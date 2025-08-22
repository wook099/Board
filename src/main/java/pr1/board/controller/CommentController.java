package pr1.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pr1.board.dto.CommentRequestDto;
import pr1.board.dto.CommentResponseDto;
import pr1.board.service.CommentService;
import pr1.board.service.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto dto) {
        Long userId = userDetails.getUser().getId();
        CommentResponseDto response = commentService.writeComment(userId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }
}
