package kr.nadeuli.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.nadeuli.dto.AnsQuestionDTO;
import kr.nadeuli.dto.OrikkiriDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.dto.TokenDTO;
import kr.nadeuli.service.image.ImageService;
import kr.nadeuli.service.orikkirimanage.OrikkiriManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/nadeuli/orikkiriManage")
@RequiredArgsConstructor
@Log4j2
public class OrikkiriManageRestController {

  private final OrikkiriManageService orikkiriManageService;

  private final ImageService imageService;

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${pageSize}")
  private int pageSize;

  @PostMapping("/addOrikkiri")
  public OrikkiriDTO addOrikkiri(@ModelAttribute OrikkiriDTO orikkiriDTO,
      @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {

    log.info("addOrikkiri에서 받은 OrikkiriDTO : {}", orikkiriDTO);
    log.info("addOrikkiri에서 받은 image : {}", image);

    OrikkiriDTO existOrikkiriDTO = orikkiriManageService.addOrikkiri(orikkiriDTO);

    // image가 null이 아닌 경우에만 실행
    if (image != null) {
      imageService.addProfile(image, existOrikkiriDTO);
    }

    return existOrikkiriDTO;
  }

  @PostMapping("/updateOrikkiri")
  public ResponseEntity<String> updateOrikkiri(@RequestBody OrikkiriDTO orikkiriDTO,
      @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
    String fileName = orikkiriDTO.getOrikkiriPicture();
    imageService.deleteProfile(fileName);

    if (image != null && !image.isEmpty()) {
      imageService.addProfile(image, orikkiriDTO);
    } else {
      orikkiriManageService.updateOrikkiri(orikkiriDTO);
    }

    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }

  @GetMapping("/getOrikkiri/{orikkiriId}")
  public OrikkiriDTO getOrikkiri(@PathVariable long orikkiriId) throws Exception {
    return orikkiriManageService.getOrikkiri(orikkiriId);
  }

  @GetMapping("/deleteOrikkiri/{orikkiriId}")
  public ResponseEntity<String> deleteOrikkiri(@PathVariable long orikkiriId) throws Exception {
    orikkiriManageService.deleteOrikkiri(orikkiriId);
    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }

  @PostMapping("/addAnsQuestion")
  public ResponseEntity<String> addAnsQuestion(@RequestParam("orikkiriId") long orikkiriId,
      @RequestParam("content") String content) throws Exception {
    log.info("Received orikkiriId: {}, content: {}", orikkiriId, content);
    AnsQuestionDTO ansQuestionDTO = AnsQuestionDTO.builder()
        .content(content)
        .orikkiri(orikkiriManageService.getOrikkiri(orikkiriId))
        .build();
    orikkiriManageService.addAnsQuestion(ansQuestionDTO);
    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }

  @PostMapping("/addAns")
  public ResponseEntity<String> addAns(@RequestBody AnsQuestionDTO ansQuestionDTO) throws Exception {
    log.info("Received ansQuestionDTO: {}", ansQuestionDTO);

    // orikkiriId를 사용하여 orikkiriDTO를 가져옴
    Long orikkiriId = ansQuestionDTO.getOrikkiri().getOrikkiriId();
    OrikkiriDTO orikkiriDTO = orikkiriManageService.getOrikkiri(orikkiriId);

    // ansQuestionDTO에 orikkiriDTO 설정
    ansQuestionDTO.setOrikkiri(orikkiriDTO);

    // 서버에서 직접 oriScheMemChatFavId를 추출하여 사용
    Long oriScheMemChatFavId = ansQuestionDTO.getOriScheMemChatFav().getOriScheMemChatFavId();
    ansQuestionDTO.setOriScheMemChatFav(orikkiriManageService.getOriScheMemChatFavDTO(oriScheMemChatFavId));

    // 답변 추가 서비스 호출
    AnsQuestionDTO ansQuestionDTO1 = orikkiriManageService.addAnsQuestion(ansQuestionDTO);
    orikkiriManageService.addAns(ansQuestionDTO1);

    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }

  @PostMapping("/updateAnsQuestion")
  public ResponseEntity<String> updateAnsQuestion(@RequestBody AnsQuestionDTO ansQuestionDTO)
      throws Exception {
    orikkiriManageService.updateAnsQuestion(ansQuestionDTO);
    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }

  @GetMapping("/getAnsQuestion/{ansQuestionId}")
  public AnsQuestionDTO getAnsQuestion(@PathVariable long ansQuestionId) throws Exception {
    return orikkiriManageService.getAnsQuestion(ansQuestionId);
  }

  @GetMapping("/getAnsQuestionList/{orikkiriId}")
  public List<AnsQuestionDTO> getAnsQuestionList(@PathVariable long orikkiriId) throws Exception {
    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setCurrentPage(0);
    searchDTO.setPageSize(pageSize);
    return orikkiriManageService.getAnsQuestionList(orikkiriId, searchDTO);
  }

  @GetMapping("/deleteAnsQuestion/{ansQuestionId}")
  public ResponseEntity<String> deleteAnsQuestion(@PathVariable long ansQuestionId)
      throws Exception {
    orikkiriManageService.deleteAnsQuestion(ansQuestionId);
    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }


}
