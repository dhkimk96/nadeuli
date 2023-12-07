package kr.nadeuli.service.post.impl;

import kr.nadeuli.dto.PostDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.entity.Post;
import kr.nadeuli.mapper.PostMapper;
import kr.nadeuli.service.post.PostRepository;
import kr.nadeuli.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@RequiredArgsConstructor
@Log4j2
@Transactional
@Service("postServiceImpl")
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    @Override
    public Long addPost(PostDTO postDTO) throws Exception {
        Post post = postMapper.postDTOToPost(postDTO);
        log.info(post);
        Post savedPost = postRepository.save(post);
        return savedPost.getPostId();
    }

    @Override
    public PostDTO getPost(long postId) throws Exception {
        return postRepository.findById(postId).map(postMapper::postToPostDTO).orElse(null);
    }

    @Override
    public List<PostDTO> getPostList(String gu, SearchDTO searchDTO) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize(), sort);
        Page<Post> postPage;
        postPage = postRepository.findByGuAndTitleOrContentContaining(gu, searchDTO.getSearchKeyword(), pageable);
        log.info(postPage);
        System.out.println(postPage);
        return postPage.map(postMapper::postToPostDTO).toList();
    }

    @Override
    public Long updatePost(PostDTO postDTO) throws Exception {
        Post post = postMapper.postDTOToPost(postDTO);
        log.info(post);
        Post savedPost = postRepository.save(post);
        return savedPost.getPostId();
    }

    @Override
    public void deletePost(long postId) throws Exception {
        log.info(postId);
        postRepository.deleteById(postId);
    }

//    @Override
//    public PostDTO addStreamingChannel(PostDTO postDTO) throws Exception {
//        Long time = System.currentTimeMillis();
//        String accessKey = "A7YXXDFuySF3KfWwBjQg";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("x-ncp-apigw-timestamp", time.toString());
//        headers.set("x-ncp-iam-access-key", accessKey);
//        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
//        return postDTO;
//    }
//
//    public String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
//
//        String space = " ";					// 공백
//        String newLine = "\n";  				// 줄바꿈
////        String method = "GET";  				// HTTP 메서드
//        String method = "POST";  				// HTTP 메서드
//        String url = "/api/v2/channels";	// 도메인을 제외한 "/" 아래 전체 url (쿼리스트링 포함)
//        String accessKey = "A7YXXDFuySF3KfWwBjQg";		// access key id (from portal or Sub Account)
//        String secretKey = "BoekwrkmB32dJJCRSNSYrdkguTVLFZs50Vkh17Fx";		// secret key (from portal or Sub Account)
//        String timestamp = String.valueOf(System.currentTimeMillis());		// 현재 타임스탬프 (epoch, millisecond)
//
//        String message = new StringBuilder()
//                .append(method)
//                .append(space)
//                .append(url)
//                .append(newLine)
//                .append(timestamp)
//                .append(newLine)
//                .append(accessKey)
//                .toString();
//
//        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
//        Mac mac = Mac.getInstance("HmacSHA256");
//        mac.init(signingKey);
//
//        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
//        String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
//
//        return encodeBase64String;
//    }



}
