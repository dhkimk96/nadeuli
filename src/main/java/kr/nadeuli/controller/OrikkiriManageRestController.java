package kr.nadeuli.controller;

import kr.nadeuli.dto.AnsQuestionDTO;
import kr.nadeuli.dto.OrikkiriDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.service.image.ImageService;
import kr.nadeuli.service.orikkirimanage.OrikkiriManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/orikkiriManage")
@RequiredArgsConstructor
@Log4j2
public class OrikkiriManageRestController {

  private final OrikkiriManageService orikkiriManageService;

  private final ImageService imageService;

  @Value("${pageSize}")
  private int pageSize;

  @PostMapping("/addOrikkiri")
  public ResponseEntity<String> addOrikkiri(@ModelAttribute OrikkiriDTO orikkiriDTO,
      @RequestParam(value = "image", required = false) List<MultipartFile> image) throws Exception {

    orikkiriManageService.addOrikkiri(orikkiriDTO);
    imageService.addImage(image, orikkiriDTO);

    return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
  }

  @PostMapping("/updateOrikkiri")
  public ResponseEntity<String> updateOrikkiri(@RequestBody OrikkiriDTO orikkiriDTO,
      @RequestParam(value = "image", required = false) List<MultipartFile> image) throws Exception {
    String fileName = orikkiriDTO.getOrikkiriPicture();
    imageService.deleteProfile(fileName);

    if (image != null && !image.isEmpty()) {
      imageService.addImage(image, orikkiriDTO);
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
  public ResponseEntity<String> addAnsQuestion(@RequestBody AnsQuestionDTO ansQuestionDTO)
      throws Exception {
    orikkiriManageService.addAnsQuestion(ansQuestionDTO);
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

  @GetMapping("/getAnsQuestionList/{orikkiriId}/{currentPage}")
  public List<AnsQuestionDTO> getAnsQuestionList(@PathVariable long orikkiriId,
      @PathVariable int currentPage) throws Exception {
    SearchDTO searchDTO = new SearchDTO();
    searchDTO.setCurrentPage(currentPage);
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
