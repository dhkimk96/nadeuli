package kr.nadeuli.controller;

import kr.nadeuli.dto.SmsDTO;
import kr.nadeuli.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Log4j2
public class SmsRestController {

  @Autowired
  private final SmsService smsService;
  @PostMapping("/sendOne")
  public String sendOne(@RequestBody SmsDTO smsDTO) throws Exception{
    log.info("/member/smsDTO : POST : {}", smsDTO);
    smsService.sendOne(smsDTO.getCellphone());

    return "{\"success\": true}";
  }

  @PostMapping("/verifySms")
  public String verifySms(@RequestBody SmsDTO smsDTO) throws Exception{
    log.info("/member/smsDTO : POST : {}", smsDTO);
    smsService.verifySms(smsDTO);

    return "{\"success\": true}";
  }

}
