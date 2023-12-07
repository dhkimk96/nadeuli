package kr.nadeuli.controller;

import kr.nadeuli.dto.*;
import kr.nadeuli.service.comment.CommentService;
import kr.nadeuli.service.image.ImageService;
import kr.nadeuli.service.orikkiri.OrikkiriService;
import kr.nadeuli.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orikkiri")
@RequiredArgsConstructor
@Log4j2
public class OrikkiriRestController {

    private final OrikkiriService orikkiriService;
    private final PostService postService;
    private final CommentService commentService;
    private final ImageService imageService;

    @Value("${pageSize}")
    private int pageSize;

    @PostMapping("/addOrikkrirSignUp")
    public ResponseEntity<String> addOrikkrirSignUp(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        orikkiriService.addOrikkrirSignUp(oriScheMemChatFavDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkrirSignupList/{orikkiriId}/{currentPage}")
    public List<OriScheMemChatFavDTO> getOrikkiriSignUpList(@PathVariable long orikkiriId, @PathVariable int currentPage) throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
        return orikkiriService.getOrikkiriSignUpList(orikkiriId, searchDTO);
    }

    @GetMapping("/getMyOrikkiriList/{tag}/{currentPage}")
    public List<OriScheMemChatFavDTO> getMyOrikkiriList(@PathVariable String tag, @PathVariable int currentPage) throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
        return orikkiriService.getMyOrikkiriList(tag, searchDTO);
    }

    @GetMapping("/deleteOrikkiriMember/{tag}/{orikkiriId}")
    public ResponseEntity<String> deleteOrikkiriMember(@PathVariable String tag, @PathVariable long orikkiriId) throws Exception {
        orikkiriService.deleteOrikkiriMember(tag, orikkiriId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkiriMemberList/{orikkiriId}/{currentPage}")
    public List<OriScheMemChatFavDTO> getOrikkiriMemberList(@PathVariable long orikkiriId, @PathVariable int currentPage) throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
        return orikkiriService.getOrikkiriMemberList(orikkiriId, searchDTO);
    }

    @PostMapping("/addOrikkiriScheduleMember")
    public ResponseEntity<String> addOrikkiriScheduleMember(@RequestBody OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        orikkiriService.addOrikkiriScheduleMember(oriScheMemChatFavDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @PostMapping("/addOrikkiriMember")
    public ResponseEntity<String> addOrikkiriMember(@RequestBody OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        orikkiriService.addOrikkiriMember(oriScheMemChatFavDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkiriScheduleMemberList/{orikkiriScheduleId}/{currentPage}")
    public List<OriScheMemChatFavDTO> getOrikkiriScheduleMemberList(@PathVariable long orikkiriScheduleId, @PathVariable int currentPage) throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
        return orikkiriService.getOrikkiriScheduleMemberList(orikkiriScheduleId, searchDTO);
    }

    @PostMapping("/addOrikkiriSchedule")
    public ResponseEntity<String> addOrikkiriSchedule(@RequestBody OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception {
        orikkiriService.addOrikkiriSchedule(orikkiriScheduleDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkiriScheduleList/{orikkiriId}/{currentPage}")
    public List<OrikkiriScheduleDTO> getOrikkiriScheduleList(long orikkiriId, @PathVariable int currentPage) throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
        return orikkiriService.getOrikkiriScheduleList(orikkiriId, searchDTO);
    }

    @GetMapping("/getOrikkiriSchedule/{orikkiriScheduleId}")
    public OrikkiriScheduleDTO getOrikkiriSchedule(@PathVariable long orikkiriScheduleId) throws Exception {
        return orikkiriService.getOrikkiriSchedule(orikkiriScheduleId);
    }

    @PostMapping("/updateOrikkiriSchedule")
    public ResponseEntity<String> updateOrikkiriSchedule(@RequestBody OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception {
        orikkiriService.updateOrikkiriSchedule(orikkiriScheduleDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");

    }

    @GetMapping("/deleteOrikkiriSchedule/{orikkiriScheduleId}")
    public ResponseEntity<String> deleteOrikkiriSchedule(@PathVariable long orikkiriScheduleId) throws Exception {
        orikkiriService.deleteOrikkiriSchedule(orikkiriScheduleId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");

    }

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
        List<ImageDTO> imageDTOList =imageService.getImageList(postId, SearchDTO.builder()
                .isPost(true)
                .build());
        PostDTO postDTO = postService.getPost(postId);
        List<String> imageNames = imageDTOList.stream()
                .map(ImageDTO::getImageName)
                .collect(Collectors.toList());
        postDTO.setImages(imageNames);
        return postDTO;
    }

    @GetMapping("/getPostList/{gu}/{currentPage}")
    public List<PostDTO> getPostList(@PathVariable String gu, @PathVariable int currentPage) throws Exception {
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
        return postService.getPostList(gu, searchDTO);
    }

    @PostMapping("/updatePost")
    public ResponseEntity<String> updatePost(@RequestBody PostDTO postDTO) throws Exception {
        Long postId = postService.updatePost(postDTO);
        imageService.deletePostImage(postId);

        for(String image : postDTO.getImages()){
            imageService.addImage(ImageDTO.builder()
                    .imageName(image)
                    .post(PostDTO.builder().postId(postDTO.getPostId()).build())
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
    public List<CommentDTO> getCommentList(long postId) throws Exception {
        return commentService.getCommentList(postId);
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

