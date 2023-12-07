package kr.nadeuli.controller;

import kr.nadeuli.dto.*;
import kr.nadeuli.service.member.MemberService;
import kr.nadeuli.service.nadeulidelivery.NadeuliDeliveryService;
import kr.nadeuli.service.product.ProductService;
import kr.nadeuli.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/nadeulidelivery")
public class NadeuliDeliveryRestController {

    @Value("${pageSize}")
    private int pageSize;

    private final MemberService memberService;
    private final TradeService tradeService;
    private final ProductService productService;
    private final NadeuliDeliveryService nadeuliDeliveryService;

    // 미해결
    @Transactional
    @GetMapping("/getAddOrUpdateUsedDeliveryOrder")
    public List<TradeScheduleDTO> getAddOrUpdateUsedDeliveryOrder(@ModelAttribute String tag, @ModelAttribute SearchDTO searchDTO) throws Exception {
        // 중고상품 선택 시, 등록 된 거래 일정 리스트를 호출한다.
        log.info("/nadeulidelivery/getAddUpdateUsedDeliveryOrder : GET");
        log.info(tag);

        searchDTO.setPageSize(pageSize);
        log.info(searchDTO);

        List<TradeScheduleDTO> tradeScheduleDTOList = tradeService.getTradeScheduleList(tag, searchDTO);

        // Base class 가 public 이 아닌 문제로 regDate 를 불러올 수 없다고 하는데,
        // ProductMapper 내의 regDate ignore = true 로 되어있어서 인 것 같다.

        for (TradeScheduleDTO tradeScheduleDTO : tradeScheduleDTOList) {
            if (tradeScheduleDTO.getProduct().getProductId() != null) {
                ProductDTO productDTO = productService.getProduct(tradeScheduleDTO.getProduct().getProductId());
                tradeScheduleDTO.setProduct(productDTO);
            }
        }

        log.info(tradeScheduleDTOList);

        return tradeScheduleDTOList;
    }

    @Transactional
    @PostMapping("/addOrUpdateDeliveryOrder")
    public ResponseEntity<?> addOrUpdateDeliveryOrder(@RequestBody NadeuliDeliveryDTO nadeuliDeliveryDTO) throws Exception{
        // 중고상품 배달 주문을 등록한다.
        log.info("/nadeulidelivery/addOrUpdateDeliveryOrder : POST");
        log.info(nadeuliDeliveryDTO);
        Long beforeDeposit;

        // nadeuliDeliveryId 값이 있다면, 해당 deposit 을 balance 에 더하고 새로 받은 deposit 값 만큼 뺀다.
        if (nadeuliDeliveryDTO.getNadeuliDeliveryId() != null) {
            beforeDeposit = nadeuliDeliveryService.getDeliveryOrder(nadeuliDeliveryDTO.getNadeuliDeliveryId()).getDeposit();

            memberService.handleNadeuliPayBalance(
                    nadeuliDeliveryDTO.getBuyer().getTag(),
                    null ,
                    nadeuliDeliveryDTO, beforeDeposit
            );
        } else {
            // nadeuliDeliveryId 값이 null 이면, MemberService 로 나드리페이 잔액을 상품 금액 만큼 먼저 뺀다.
            memberService.handleNadeuliPayBalance(
                    nadeuliDeliveryDTO.getBuyer().getTag(),
                    null ,
                    nadeuliDeliveryDTO, null
            );
        }

        nadeuliDeliveryService.addOrUpdateDeliveryOrder(nadeuliDeliveryDTO);
        return ResponseEntity.ok("Delivery Order 등록/수정 완료");
    }

    @GetMapping("/getDeliveryOrder/{nadeuliDeliveryId}")
    public NadeuliDeliveryDTO getDeliveryOrder(@PathVariable long nadeuliDeliveryId) throws Exception {
        // 배달 주문을 조회한다.
        // GetMyOrderHistory, getMyDeliveryHistory 모두 getDeliveryOrder 하나로 가능하며
        // 추후 frontend 에서 본인 tag 가 buyer.tag / deliveryPerson.tag 에 따라 UI(버튼 종류 등)를 바꾸도록 한다.
        log.info("/nadeulidelivery/getDeliveryOrder : GET");
        log.info(nadeuliDeliveryId);

        return nadeuliDeliveryService.getDeliveryOrder(nadeuliDeliveryId);
    }

    @Transactional
    @PostMapping("/getDeliveryOrderList")
    public List<NadeuliDeliveryDTO> getDeliveryOrderList
            (@RequestBody MemberDTO memberDTO, @ModelAttribute SearchDTO searchDTO) throws Exception {
        // 배달 주문을 회원이 소속된 구 로 조회한다.
        log.info("/nadeulidelivery/getDeliveryOrderList : POST");
        log.info(memberDTO);

        searchDTO.setPageSize(pageSize);
        log.info(searchDTO);

        return nadeuliDeliveryService.getDeliveryOrderList(memberDTO, searchDTO);
    }

    @Transactional
    @PostMapping("/getMyOrderHistoryList")
    public List<NadeuliDeliveryDTO> getMyOrderHistoryList
            (@RequestBody NadeuliDeliveryDTO nadeuliDeliveryDTO, @ModelAttribute SearchDTO searchDTO) throws Exception {
        // 나의 주문 내역 목록을 조회한다.
        log.info("/nadeulidelivery/getMyOrderHistoryList : POST");
        log.info(nadeuliDeliveryDTO);

        searchDTO.setPageSize(pageSize);
        log.info(searchDTO);

        return nadeuliDeliveryService.getMyOrderHistoryList(nadeuliDeliveryDTO, searchDTO);
    }

//    @GetMapping("/getMyOrderHistory/{nadeuliDeliveryId}")
//    public NadeuliDeliveryDTO getMyOrderHistory
//            (@PathVariable long nadeuliDeliveryId) throws Exception {
//        // 나의 주문 내역을 상세 조회한다.
//        log.info("/nadeulidelivery/getMyOrderHistory : GET");
//        log.info(nadeuliDeliveryId);
//
//        return nadeuliDeliveryService.getMyOrderHistory(nadeuliDeliveryId);
//    }

    @Transactional
    @PostMapping("/getMyDeliveryHistoryList")
    public List<NadeuliDeliveryDTO> getMyDeliveryHistoryList
            (@RequestBody NadeuliDeliveryDTO nadeuliDeliveryDTO, @ModelAttribute SearchDTO searchDTO) throws Exception {
        // 나의 배달 내역 목록을 조회한다.
        log.info("/nadeulidelivery/getMyDeliveryHistoryList : POST");
        log.info(nadeuliDeliveryDTO);

        searchDTO.setPageSize(pageSize);
        log.info(searchDTO);

        return nadeuliDeliveryService.getMyDeliveryHistoryList(nadeuliDeliveryDTO, searchDTO);
    }

//    @GetMapping("/getMyDeliveryHistory/{nadeuliDeliveryId}")
//    public NadeuliDeliveryDTO getMyDeliveryHistory
//            (@RequestBody long nadeuliDeliveryId) throws Exception {
//        // 나의 배달 내역을 상세 조회한다.
//        log.info("/nadeulidelivery/getMyDeliveryHistory : GET");
//        log.info(nadeuliDeliveryId);
//
//        return nadeuliDeliveryService.getMyDeliveryHistory(nadeuliDeliveryId);
//    }

    @PostMapping("/getMyAcceptedDeliveryHistoryList")
    public List<NadeuliDeliveryDTO> getMyAcceptedDeliveryHistoryList
            (@RequestBody NadeuliDeliveryDTO nadeuliDeliveryDTO, SearchDTO searchDTO) throws Exception {
        // 나의 배달 시 주문 수락 내역을 목록 조회한다.
        log.info("/nadeulidelivery/getMyAcceptedDeliveryHistoryList : POST");
        log.info(nadeuliDeliveryDTO);

        searchDTO.setPageSize(pageSize);
        log.info(searchDTO);

        return nadeuliDeliveryService.getMyAcceptedDeliveryHistoryList(nadeuliDeliveryDTO, searchDTO);
    }

//    @PostMapping("/getAcceptedDeliveryLocationList")
//    public List<NadeuliDeliveryDTO> getAcceptedDeliveryLocationList
//            (@RequestBody NadeuliDeliveryDTO nadeuliDeliveryDTO) throws Exception {
//        // 최단 경로 계산 시 필요한 정보 전달
//        log.info("/nadeulidelivery/getAcceptedDeliveryLocationList : POST");
//        log.info(nadeuliDeliveryDTO);
//
//        return nadeuliDeliveryService.getAcceptedDeliveryLocationList(nadeuliDeliveryDTO);
//    }

    @Transactional
    @GetMapping("/cancelDeliveryOrder/{nadeuliDeliveryId}")
    public ResponseEntity<?> cancelDeliveryOrder(@PathVariable long nadeuliDeliveryId) throws Exception {
        // 주문을 취소한다.
        log.info("/nadeulidelivery/cancelDeliveryOrder : GET");
        log.info(nadeuliDeliveryId);

        nadeuliDeliveryService.cancelDeliveryOrder(nadeuliDeliveryId);

        // 다시 정보를 가져와서 MemberService 로 나드리페이 잔액을 상품 금액 만큼 다시 추가한다.
        NadeuliDeliveryDTO nadeuliDeliveryDTO = nadeuliDeliveryService.getDeliveryOrder(nadeuliDeliveryId);
        log.info(nadeuliDeliveryDTO);

        memberService.handleNadeuliPayBalance(
                nadeuliDeliveryDTO.getBuyer().getTag(),
                null,
                nadeuliDeliveryDTO, null
        );

        return ResponseEntity.ok("주문 취소 완료");
    }

    @Transactional
    @PostMapping("/acceptDeliveryOrder")
    public ResponseEntity<?> acceptDeliveryOrder(@RequestBody NadeuliDeliveryDTO nadeuliDeliveryDTO) throws Exception {
        // 주문을 수락한다.
        log.info("/nadeulidelivery/acceptDeliveryOrder : POST");
        log.info(nadeuliDeliveryDTO);

        nadeuliDeliveryService.acceptDeliveryOrder(nadeuliDeliveryDTO);

        return ResponseEntity.ok("주문 수락 완료");
    }

    @Transactional
    @GetMapping("/cancelDelivery/{nadeuliDeliveryId}")
    public ResponseEntity<?> cancelDelivery(@PathVariable long nadeuliDeliveryId) throws Exception {
        // 배달을 취소한다.
        log.info("/nadeulidelivery/cancelDelivery : GET");
        log.info(nadeuliDeliveryId);

        nadeuliDeliveryService.cancelDelivery(nadeuliDeliveryId);

        // 다시 정보를 가져와서 MemberService 로 나드리페이 잔액을 상품 금액 만큼 다시 추가한다.
        NadeuliDeliveryDTO nadeuliDeliveryDTO = nadeuliDeliveryService.getDeliveryOrder(nadeuliDeliveryId);
        log.info(nadeuliDeliveryDTO);

        memberService.handleNadeuliPayBalance(
                nadeuliDeliveryDTO.getBuyer().getTag(),
                null,
                nadeuliDeliveryDTO, null
        );

        return ResponseEntity.ok("배달 취소 완료");

    }

    @Transactional
    @GetMapping("/completeDelivery/{nadeuliDeliveryId}")
    public ResponseEntity<?> completeDelivery(@PathVariable long nadeuliDeliveryId) throws Exception {
        // 배달을 완료한다.
        log.info("/nadeulidelivery/completeDelivery : GET");
        log.info(nadeuliDeliveryId);

        nadeuliDeliveryService.completeDelivery(nadeuliDeliveryId);

        // 다시 정보를 가져와서 MemberService 로 나드리페이 잔액을 상품 금액 만큼 다시 추가한다.
        NadeuliDeliveryDTO nadeuliDeliveryDTO = nadeuliDeliveryService.getDeliveryOrder(nadeuliDeliveryId);
        log.info(nadeuliDeliveryDTO);

        memberService.handleNadeuliPayBalance(
                nadeuliDeliveryDTO.getBuyer().getTag(),
                null,
                nadeuliDeliveryDTO, null
        );

        return ResponseEntity.ok("배달 완료");

    }

    @Transactional
    @PostMapping("/getDeliveryNotificationList")
    public List<DeliveryNotificationDTO> getDeliveryNotificationList
            (@RequestBody DeliveryNotificationDTO deliveryNotificationDTO, @ModelAttribute SearchDTO searchDTO) throws Exception {
        // 부름 알림 목록을 조회한다.
        log.info("/nadeulidelivery/getDeliveryNotificationList : POST");
        log.info(deliveryNotificationDTO);

        searchDTO.setPageSize(pageSize);
        log.info(searchDTO);

        // Member 의 isNadeuliDelivery 가 true 여야 알림 받을 수 있음

        return nadeuliDeliveryService.getDeliveryNotificationList(deliveryNotificationDTO, searchDTO);
    }

    @GetMapping("/updateIsRead/{deliveryNotificationId}")
    public ResponseEntity<?> updateIsRead(@PathVariable long deliveryNotificationId) throws Exception {
        // 부름 알림을 읽음 처리한다.
        log.info("/nadeulidelivery/updateIsRead : GET");
        log.info(deliveryNotificationId);

        nadeuliDeliveryService.updateIsRead(deliveryNotificationId);

        return ResponseEntity.ok("부름 알림 읽음 처리 완료");
    }

    @GetMapping("/deleteDeliveryNotification/{deliveryNotificationId}")
    public ResponseEntity<?> deleteDeliveryNotification(@PathVariable long deliveryNotificationId) throws Exception {
        // 부름 알림을 삭제한다.
        log.info("/nadeulidelivery/deleteDeliveryNotification : GET");
        log.info(deliveryNotificationId);

        nadeuliDeliveryService.deleteDeliveryNotification(deliveryNotificationId);

        return ResponseEntity.ok("부름 알림 삭제 완료");
    }


    // 해당 부분은 react 에서 nadeuliDeliveryId, tag 넘겨서 report 로 라우팅 예정
//    @GetMapping("/reportNedauliDelivery")
//    public NadeuliDeliveryDTO reportNedauliDelivery() throws Exception {
//        // 나드리부름 게시물을 신고한다.
//        log.info("/nadeulidelivery/reportNedauliDelivery : GET");
//
//        return null;
//    }

}
