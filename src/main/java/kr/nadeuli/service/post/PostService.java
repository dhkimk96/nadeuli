package kr.nadeuli.service.post;

import kr.nadeuli.dto.PostDTO;
import kr.nadeuli.dto.SearchDTO;

import java.util.List;

public interface PostService {
    Long addPost(PostDTO postDTO) throws Exception;

    PostDTO getPost(long postId) throws Exception;

    List<PostDTO> getPostList(String gu, SearchDTO searchDTO) throws Exception;

    Long updatePost(PostDTO postDTO) throws Exception;

    void deletePost(long postId) throws Exception;

//    PostDTO addStreamingChannel(PostDTO postDTO) throws Exception;
}
