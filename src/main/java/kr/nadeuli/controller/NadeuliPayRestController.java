package kr.nadeuli.controller;

import kr.nadeuli.category.TradeType;
import kr.nadeuli.dto.MemberDTO;
import kr.nadeuli.dto.NadeuliPayHistoryDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.service.member.MemberService;
import kr.nadeuli.service.nadeuli_pay.NadeuliPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nadeuliPay")
@RequiredArgsConstructor
public class NadeuliPayRestController {
    private final NadeuliPayService nadeuliPayService;
    private final MemberService memberService;

    @Value("${pageSize}")
    private int pageSize;

    @GetMapping("/getNadeuliPayList/{currentPage}/{tradeType}/{tag}")
    public List<NadeuliPayHistoryDTO> getNadeuliPayList(@PathVariable String tag, @PathVariable TradeType tradeType, @PathVariable int currentPage){
        SearchDTO searchDTO = SearchDTO.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                                       .build();
        return nadeuliPayService.getNadeuliPayList(tag, tradeType, searchDTO);
    }
    
    // api 사용 로직 필요
    @PostMapping("/nadeuliPayCharge")
    public ResponseEntity<String> nadeuliPayCharge(@RequestBody NadeuliPayHistoryDTO nadeuliPayHistoryDTO) throws Exception {
        if(memberService.handleNadeuliPayBalance(nadeuliPayHistoryDTO.getMember().getTag(), nadeuliPayHistoryDTO, null, null)){
            nadeuliPayService.nadeuliPayCharge(nadeuliPayHistoryDTO.getMember().getTag(), nadeuliPayHistoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": false}");
    }

    @PostMapping("/nadeuliPayWithdraw")
    public ResponseEntity<String> nadeuliPayWithdraw(@RequestBody NadeuliPayHistoryDTO nadeuliPayHistoryDTO) throws Exception {
        if(memberService.handleNadeuliPayBalance(nadeuliPayHistoryDTO.getMember().getTag(), nadeuliPayHistoryDTO, null, null)){
            nadeuliPayService.nadeuliPayWithdraw(nadeuliPayHistoryDTO.getMember().getTag(), nadeuliPayHistoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": false}");
    }

    @PostMapping("/nadeuliPayPay")
    public ResponseEntity<String> nadeuliPayPay(@RequestBody NadeuliPayHistoryDTO nadeuliPayHistoryDTO) throws Exception {
        if(memberService.handleNadeuliPayBalance(nadeuliPayHistoryDTO.getMember().getTag(), nadeuliPayHistoryDTO, null, null)){
            nadeuliPayService.nadeuliPayPay(nadeuliPayHistoryDTO.getMember().getTag(), nadeuliPayHistoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body("{\"success\": true}");
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"success\": false}");
    }

}
