package kr.nadeuli.controller;

import kr.nadeuli.dto.*;
import kr.nadeuli.service.comment.CommentService;
import kr.nadeuli.service.orikkiri.OrikkiriService;
import kr.nadeuli.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orikkiri")
@RequiredArgsConstructor
@Log4j2
public class OrikkiriRestController {

    private final OrikkiriService orikkiriService;
    private final PostService postService;
    private final CommentService commentService;

    @Value("${pageSize}")
    private int pageSize;

    @PostMapping("/addOrikkrirSignUp")
    public ResponseEntity<String> addOrikkrirSignUp(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        orikkiriService.addOrikkrirSignUp(oriScheMemChatFavDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkrirSignupList")
    public List<OriScheMemChatFavDTO> getOrikkiriSignUpList(long ansQuestionId, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
        return orikkiriService.getOrikkiriSignUpList(ansQuestionId, searchDTO);
    }

    @GetMapping("/getMyOrikkiriList")
    public List<OriScheMemChatFavDTO> getMyOrikkiriList(String tag, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
        return orikkiriService.getMyOrikkiriList(tag, searchDTO);
    }

    @GetMapping("/deleteOrikkiriMember/{tag}/{orikkiriId}")
    public ResponseEntity<String> deleteOrikkiriMember(@PathVariable String tag, @PathVariable long orikkiriId) throws Exception {
        orikkiriService.deleteOrikkiriMember(tag, orikkiriId);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkiriMemberList")
    public List<OriScheMemChatFavDTO> getOrikkiriMemberList(long orikkiriId, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
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

    @GetMapping("/getOrikkiriScheduleMemberList")
    public List<OriScheMemChatFavDTO> getOrikkiriScheduleMemberList(long orikkiriScheduleId, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
        return orikkiriService.getOrikkiriScheduleMemberList(orikkiriScheduleId, searchDTO);
    }

    @PostMapping("/addOrikkiriSchedule")
    public ResponseEntity<String> addOrikkiriSchedule(@RequestBody OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception {
        orikkiriService.addOrikkiriSchedule(orikkiriScheduleDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/getOrikkiriScheduleList")
    public List<OrikkiriScheduleDTO> getOrikkiriScheduleList(long orikkiriId, SearchDTO searchDTO) throws Exception {
        searchDTO.setPageSize(pageSize);
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
        postService.addPost(postDTO);
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
        postService.updatePost(postDTO);
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
    }

    @GetMapping("/deletePost/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable long postId) throws Exception {
        postService.deletePost(postId);
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

