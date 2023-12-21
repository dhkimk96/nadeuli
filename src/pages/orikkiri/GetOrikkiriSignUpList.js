import React, { useEffect, useState, useRef, useCallback } from 'react';
import {
    MDBBadge,
    MDBListGroup,
    MDBListGroupItem,
    MDBContainer,
    MDBNavbar,
    MDBBtn,
    MDBInputGroup,
    MDBModal,
    MDBModalDialog,
    MDBModalContent,
    MDBModalHeader,
    MDBModalTitle,
    MDBModalBody,
    MDBModalFooter,
    MDBTextArea,
    MDBRadio,
} from 'mdb-react-ui-kit';
import TopArrowLeft from '../../components/TopArrowLeft';
import HeaderBack from '../../components/HeaderBack';
import { addBlockMember, deleteBlockMember, getMemberList } from '../../util/memberAxios';
import { useInView } from 'react-intersection-observer';
import styled from 'styled-components';
import axios from 'axios';
import { get } from '../../util/axios';

export default function GetOrikkiriSignUpList() {
    const [items, setItems] = useState([]);
    const [page, setPage] = useState(0);
    const [searchKeyword, setSearchKeyword] = useState('');
    const [basicModal, setBasicModal] = useState(false);
    const [blockReason, setBlockReason] = useState('');
    const [blockDay, setBlockDay] = useState('');
    const [selectedMember, setSelectedMember] = useState(null);
    const BASE_URL = process.env.REACT_APP_BASE_URL;
    // 서버에서 아이템을 가지고 오는 함수
    const getItems = async () => {
        try {
            const response = await axios.get(`${BASE_URL}/orikkiri/getOrikkrirSignupList/2`);
            console.log(response.data);
            // 서버 응답 결과를 전부 setItems 호출
            setItems(response.data);
        } catch (error) {
            console.error('데이터를 불러오는 데 실패했습니다.', error);
        }
    };

    useEffect(() => {
        getItems(); // 초기 렌더링 시에 데이터 불러오기
    }, []); // 검색어가 변경될 때마다 실행

    // 회원 정지
    const toggleOpen = () => {
        setBasicModal(!basicModal);
    };

    useEffect(() => {
        // 모달이 열릴 때 값 초기화
        if (basicModal) {
            setBlockReason('');
            setBlockDay('');
        }
    }, [basicModal]);
    // 정지 모달에서 정지 기간 선택 시
    const handleBlockDayChange = (value) => {
        setBlockDay(value);
    };

    // 정지 모달에서 정지 사유 입력 시
    const handleBlockReasonChange = (e) => {
        setBlockReason(e.target.value);
    };

    const handleBlockMember = async () => {
        if (blockReason.length >= 10) {
            const memberDTO = { tag: selectedMember.tag };
            const blockDTO = { blockReason, blockDay };

            try {
                await addBlockMember(memberDTO, blockDTO);

                // 정지 성공 시 추가적인 처리 (예: 화면 갱신 등)
                // 서버에서 새로운 상태를 가져오거나 직접 상태를 업데이트
                // setItems, setPage, 또는 다른 상태 업데이트 로직을 추가할 수 있음
                const updatedItems = await getMemberList({ searchKeyword, currentPage: page });
                setItems(updatedItems.data);
            } catch (error) {
                console.error('정지에 실패했습니다.', error);
            }

            // 모달 닫기
            toggleOpen();
        } else {
            console.warn('정지사유를 10자 이상으로 작성해주세요.');
        }
    };

    const handleMemberClick = (selectedTag) => {
        // 선택한 회원 정보를 state에 저장
        setSelectedMember({ tag: selectedTag });
        toggleOpen();
    };
    // 회원 정지 해제 함수
    const handleUnblockMember = async (selectedTag) => {
        try {
            await deleteBlockMember(selectedTag);
            // 정지 해제 성공 시 추가적인 처리 (예: 화면 갱신 등)

            // 서버에서 새로운 상태를 가져오거나 직접 상태를 업데이트
            // setItems, setPage, 또는 다른 상태 업데이트 로직을 추가할 수 있음
            const updatedItems = await getMemberList({ searchKeyword, currentPage: page });
            setItems(updatedItems.data);
            setBasicModal(false); // 모달을 강제로 닫기
        } catch (error) {
            // 에러 처리
            console.error('회원 정지 해제에 실패했습니다.', error);
        }
    };

    //정지기간 파싱
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');

        return `${year}년 ${month}월 ${day}일 까지`;
    };

    return (
        <div>
            <MDBNavbar
                light
                bgColor="light"
            >
                <TMenuBar>
                    <HeaderBack />
                    <p>우리끼리 가입신청 목록</p>
                </TMenuBar>
            </MDBNavbar>
            <MDBListGroup
                style={{ minWidth: '22rem' }}
                light
            >
                {items.length > 0 &&
                    items.map((item, index) => (
                        <div
                            key={index}
                            className={`list-item ${items.length - 1 === index ? 'last-item' : ''}`}
                        >
                            <MDBListGroupItem className="relative d-flex justify-content-between align-items-center">
                                <div className="d-flex align-items-center">
                                    <img
                                        src={item.picture}
                                        alt={`Profile ${index}`}
                                        style={{ width: '45px', height: '45px' }}
                                        className="rounded-circle ms-3"
                                    />
                                    <div className="ms-3">
                                        <p className="fw-bold mb-0">
                                            {item.nickname}
                                            <i>#{item.tag}</i>
                                        </p>
                                        <p className="text-muted mb-0">{item.email}</p>
                                    </div>
                                    <MDBBtn>{/* 버튼 내용 */}</MDBBtn>
                                </div>
                            </MDBListGroupItem>
                        </div>
                    ))}
            </MDBListGroup>
            {/* 정지모달 */}
            <MDBModal
                open={basicModal}
                setopen={setBasicModal}
                onClick={() => setBasicModal(false)}
                tabIndex="-999"
            >
                <MDBModalDialog onClick={(e) => e.stopPropagation()}>
                    <MDBModalContent>
                        <MDBModalHeader>
                            <MDBModalTitle>회원 정지</MDBModalTitle>
                            <MDBBtn
                                className="btn-close"
                                color="none"
                                onClick={toggleOpen}
                            ></MDBBtn>
                        </MDBModalHeader>
                        <MDBModalBody>
                            <MDBTextArea
                                label="정지사유"
                                id="textAreaExample"
                                rows={4}
                                value={blockReason} // 여기에 value 추가
                                onChange={handleBlockReasonChange}
                            />
                        </MDBModalBody>
                        <div style={{ display: 'flex', justifyContent: 'center', margin: '20px 0' }}>
                            <MDBRadio
                                name="inlineRadio"
                                id="inlineRadio1"
                                label="7일"
                                inline
                                checked={blockDay === '7'}
                                onChange={() => handleBlockDayChange('7')}
                            />
                            <MDBRadio
                                name="inlineRadio"
                                id="inlineRadio2"
                                label="15일"
                                inline
                                checked={blockDay === '15'}
                                onChange={() => handleBlockDayChange('15')}
                            />
                            <MDBRadio
                                name="inlineRadio"
                                id="inlineRadio3"
                                label="30일"
                                inline
                                checked={blockDay === '30'}
                                onChange={() => handleBlockDayChange('30')}
                            />
                            <MDBRadio
                                name="inlineRadio"
                                id="inlineRadio4"
                                label="무기한"
                                inline
                                checked={blockDay === '3000'}
                                onChange={() => handleBlockDayChange('3000')}
                            />
                        </div>
                        <MDBModalFooter>
                            <MDBBtn
                                color="secondary"
                                onClick={toggleOpen}
                            >
                                취소
                            </MDBBtn>
                            <MDBBtn onClick={handleBlockMember}>정지</MDBBtn>
                        </MDBModalFooter>
                    </MDBModalContent>
                </MDBModalDialog>
            </MDBModal>
        </div>
    );
}
const TMenuBar = styled.div`
    position: relative;
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-align: center;

    p {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);
        margin: 0;
    }
`;
