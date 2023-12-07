package kr.nadeuli.controller;

import kr.nadeuli.dto.*;
import kr.nadeuli.service.comment.CommentService;
import kr.nadeuli.service.image.ImageService;
import kr.nadeuli.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dongNe")
@RequiredArgsConstructor
@Log4j2
public class DongNeRestController {

    private final PostService postService;
    private final ImageService imageService;
    private final CommentService commentService;



    @Value("${pageSize}")
    int pageSize;

    @PostMapping("/addPost")
    public ResponseEntity<String> addPost(@RequestBody PostDTO postDTO) throws Exception {
        Long postId = postService.addPost(postDTO);
        for(String image : postDTO.getImages()){
            imageService.addImage(ImageDTO.builder()
                    .imageName(image)
                    .post(PostDTO.builder()
                            .postId(postId)
                            .build())
                    .build());
        }
      return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getPost/{postId}")
    public PostDTO getPost(@PathVariable long postId) throws Exception {
        return postService.getPost(postId);
    }

    @GetMapping("/getPostList")
    public List<PostDTO> getPostList(@RequestParam String gu, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
        System.out.println(gu);
        System.out.println(searchDTO);
        return postService.getPostList(gu, searchDTO);
    }

    @PostMapping("/updatePost")
    public ResponseEntity<String> updatePost(@RequestBody PostDTO postDTO) throws Exception {
        Long postId = postService.updatePost(postDTO);

        imageService.deletePostImage(postId);

        for(String image : postDTO.getImages()){
            imageService.addImage(ImageDTO.builder()
                    .imageName(image)
                    .post(PostDTO.builder()
                            .postId(postDTO.getPostId())
                            .build())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/deletePost/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable long postId) throws Exception {
        postService.deletePost(postId);
        imageService.deletePostImage(postId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @PostMapping("/addComment")
    public ResponseEntity<String> addComment(@RequestBody CommentDTO commentDTO) throws Exception {
        commentService.addComment(commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getComment/{commentId}")
    public CommentDTO getComment(@PathVariable long commentId) throws Exception {
        return commentService.getComment(commentId);
    }

    @GetMapping("/getCommentList")
    public List<CommentDTO> getCommentList(long postId, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
        return commentService.getCommentList(postId, searchDTO);
    }

    @PostMapping("/updateComment")
    public ResponseEntity<String> updateComment(@RequestBody CommentDTO commentDTO) throws Exception {
        commentService.updateComment(commentDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/deleteComment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId) throws Exception {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

}
