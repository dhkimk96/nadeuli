import React, { useEffect, useState } from "react";
import { post } from "../../util/axios";
import {
  Box,
  CardBox,
  DetailColumn,
  DetailLabel,
  DetailRow,
  DetailTimeAgoColumn,
  HeaderContainer,
  OrderImage,
  OrderInfo,
  OrderTitle,
} from "./NadeuliDeliveryStyledComponent";
import HeaderBack from "../../components/HeaderBack";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

const GetMyOrderHistoryList = () => {
  // 나의 주문 내역을 목록 조회한다.
  const [responseDTOList, setResponseDTOList] = useState([]);
  const navigate = useNavigate();
  const memberTag = useSelector((state) => state.member.tag);

  useEffect(() => {
    const requestData = {
      buyer: {
        tag: memberTag,
      },
    };

    post("/nadeulidelivery/getMyOrderHistoryList", requestData, {
      headers: {
        "Content-Type": "application/json",
      },
      params: {
        currentPage: 0,
      },
    })
      .then((response) => {
        console.log("getOrderHistoryList 호출 완료!", response);
        setResponseDTOList(response);
      })
      .catch((error) => {
        console.log("getOrderHistoryList 호출 에러!", error);
      });
  }, [memberTag]);

  const handleNavigateToOrder = (nadeuliDeliveryId) => {
    navigate(`/getDeliveryOrder/${nadeuliDeliveryId}`);
  };

  const maxLength = 9;

  const truncateTitle = (title) => {
    if (title.length > maxLength) {
      return `${title.substring(0, maxLength)}...`; // 길이가 maxLength보다 길면 잘라내고 "..." 추가
    }
    return title;
  };

  const formatCurrency = (value) => {
    return new Intl.NumberFormat("ko-KR", {
      minimumFractionDigits: 0, // 소수점 이하 자릿수 (0으로 설정하면 소수점 없음)
    }).format(value);
  };

  return (
    <>
      <HeaderContainer>
        <HeaderBack />
        <Box>
          <OrderTitle style={{ paddingLeft: "80px" }}>
            주문 내역 목록
          </OrderTitle>
        </Box>
      </HeaderContainer>
      {responseDTOList.map((responseDTO, index) => (
        <CardBox
          className="card"
          key={index}
          onClick={() => handleNavigateToOrder(responseDTO.nadeuliDeliveryId)}
        >
          <DetailRow>
            <DetailColumn>
              <OrderImage src={responseDTO.images[0]} alt="이미지" />
            </DetailColumn>
            <DetailColumn>
              <DetailLabel style={{ fontWeight: "bold" }}>
                {truncateTitle(responseDTO.title)}
              </DetailLabel>
              <OrderInfo>
                구매금액 {formatCurrency(responseDTO.productPrice)}원
              </OrderInfo>
              {responseDTO.productNum > 0 && (
                <OrderInfo>
                  수량 {formatCurrency(responseDTO.productNum)}개
                </OrderInfo>
              )}
              <OrderInfo>
                부름비 {formatCurrency(responseDTO.deliveryFee)}원
              </OrderInfo>
              <OrderInfo>
                보증금 {formatCurrency(responseDTO.deposit)}원
              </OrderInfo>
            </DetailColumn>
            <DetailTimeAgoColumn>
              {responseDTO.deliveryState === "DELIVERY_ORDER" && (
                <DetailLabel>주문 등록</DetailLabel>
              )}
              {responseDTO.deliveryState === "CANCEL_ORDER" && (
                <DetailLabel>주문 취소</DetailLabel>
              )}
              {responseDTO.deliveryState === "ACCEPT_ORDER" && (
                <DetailLabel>주문 수락</DetailLabel>
              )}
              {responseDTO.deliveryState === "CANCEL_DELIVERY" && (
                <DetailLabel>배달 취소</DetailLabel>
              )}
              {responseDTO.deliveryState === "COMPLETE_DELIVERY" && (
                <DetailLabel>배달 완료</DetailLabel>
              )}
              <DetailLabel>{responseDTO.timeAgo}</DetailLabel>
            </DetailTimeAgoColumn>
          </DetailRow>
        </CardBox>
      ))}
    </>
  );
};

export default GetMyOrderHistoryList;
