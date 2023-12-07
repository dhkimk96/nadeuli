package kr.nadeuli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsDTO {
  //1. 휴대폰번호 인증을위한 DTO

  private String cellphone;
  private String authNumber;

}