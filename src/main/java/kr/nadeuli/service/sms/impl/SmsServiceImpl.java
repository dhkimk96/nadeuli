package kr.nadeuli.service.sms.impl;

import jakarta.annotation.PostConstruct;
import java.util.Random;
import kr.nadeuli.dto.SmsDTO;
import kr.nadeuli.service.sms.SmsRepository;
import kr.nadeuli.service.sms.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Log4j2
@Service
public class SmsServiceImpl implements SmsService {

  @Value("${coolsms.api.key}")
  private String apiKey;
  @Value("${coolsms.api.secret}")
  private String apiSecretKey;
  @Value("${coolsms.sender.number}")
  private String senderNumber;

  private final SmsRepository smsCertification;
  private DefaultMessageService messageService;

  private final SmsRepository smsCertificationRepository;

  @PostConstruct
  private void init(){
    this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
  }

  // 단일 메시지 발송 예제
  @Override
  public SingleMessageSentResponse sendOne(String cellphone) {
    Message message = new Message();
    String authNum = generateRandomCode(5);
    // 발신번호 및 수신번호는 반드시 01012345678 형태.
    message.setFrom(senderNumber);
    message.setTo(cellphone);
    message.setText("김성윤 샤워실에서 알몸으로 뽀삐뽀삐추는 영상[보러가기]" + authNum);

    SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

    // DB에 발송한 인증번호 저장
    smsCertification.addAuthNum(cellphone,authNum);

    return response;
  }

  //테스트용
//  @Override
//  public void sendOne(String cellphone) {
//    Message message = new Message();
//    String authNum = generateRandomCode(5);
//
//    // DB에 발송한 인증번호 저장
//    System.out.println(authNum);
//    System.out.println(cellphone);
//    smsCertification.addAuthNum(cellphone,authNum);
//
//  }
  private String generateRandomCode(int length) {
    StringBuilder code = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      code.append(random.nextInt(10)); // 0부터 9까지의 랜덤한 숫자
    }
    return code.toString();
  }

  //사용자가 입력한 인증번호가 Redis에 저장된 인증번호와 동일한지 확인
  @Override
  public boolean verifySms(SmsDTO requestDto) throws Exception {
    if (!(smsCertificationRepository.isAuthNum(requestDto.getCellphone()) &&
        smsCertificationRepository.getAuthNum(requestDto.getCellphone())
            .equals(requestDto.getAuthNumber()))) {
      throw new Exception("인증번호가 일치하지 않습니다.");
    }

    smsCertificationRepository.deleteAuthNum(requestDto.getCellphone());

    return true;
  }
}