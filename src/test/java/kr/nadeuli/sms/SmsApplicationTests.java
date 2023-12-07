package kr.nadeuli.sms;


import java.util.Optional;
import kr.nadeuli.dto.MemberDTO;
import kr.nadeuli.dto.SmsDTO;
import kr.nadeuli.mapper.MemberMapper;
import kr.nadeuli.service.member.MemberRepository;
import kr.nadeuli.service.sms.SmsService;
import kr.nadeuli.service.sms.SmsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SmsApplicationTests {


  @Autowired
  SmsService smsService;

  @Autowired
  SmsRepository smsRepository;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  MemberMapper memberMapper;


  @DisplayName("인증 번호 전송 테스트")
  //@Test
  public void testSendOne() throws Exception{
    String cellphone = "01089114554";

    // 1. cellphone을 통해 존재하는 회원인지 확인
    Optional<MemberDTO> existingMember = memberRepository.findByCellphone(cellphone)
        .map(memberMapper::memberToMemberDTO);

    // 2.회원이 존재할 경우
    if (existingMember.isPresent()) {
      smsService.sendOne(cellphone);
    } else {

      // 3.회원이 존재하지 않을 경우 예외 처리
      System.out.println("존재하지 않는 회원입니다.");
    }
  }


//  @DisplayName("인증 번호 확인 테스트")
//  @Test
  public void testVerifySms() throws Exception{
    SmsDTO smsDTO = SmsDTO.builder()
        .cellphone("010")
        .authNumber("91250")
        .build();

    System.out.println(smsService.verifySms(smsDTO));

  }

}
