package kr.nadeuli.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import kr.nadeuli.dto.GpsDTO;
import kr.nadeuli.dto.MemberDTO;
import kr.nadeuli.dto.RefreshTokenDTO;
import kr.nadeuli.dto.TokenDTO;
import kr.nadeuli.service.jwt.AuthenticationService;
import kr.nadeuli.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nadeuli")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationRestController {
  //1. Jwt 토큰 발급을 위한 RestController
  private final AuthenticationService authenticationService;

  private final MemberService memberService;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/addMember")
  public ResponseEntity<TokenDTO> addMember(@RequestBody Map<String, Object> requestData) throws Exception {
    log.info("/member/login : POST");

    MemberDTO memberDTO = objectMapper.convertValue(requestData.get("memberDTO"), MemberDTO.class);
    GpsDTO gpsDTO = objectMapper.convertValue(requestData.get("gpsDTO"), GpsDTO.class);

    log.info("addMember에서 받은 memberDTO는 {}", memberDTO);
    log.info("addMember에서 받은 gpsDTO는 {}", gpsDTO);

    TokenDTO tokenDTO = authenticationService.addMember(memberDTO, gpsDTO);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());

    return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestBody Map<String, String> requestBody) throws Exception {
    log.info("/member/login : POST");
    log.info("login에서 받은 cellphone은 {}", requestBody.get("cellphone"));

    TokenDTO tokenDTO = authenticationService.login(requestBody.get("cellphone"));

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + tokenDTO.getAccessToken());

    return new ResponseEntity<>(tokenDTO, headers, HttpStatus.OK);
  }


  @PostMapping("/refresh")
  public TokenDTO refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) throws Exception {

    log.info("/api/v1/auth/json/refresh : POST");
    log.info("refresh에서 받은 tokenDTO는 {}",refreshTokenDTO);

    return authenticationService.refreshToken(refreshTokenDTO);
  }

}
