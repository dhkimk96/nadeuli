import { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { css } from 'styled-components';
import HeaderBack from '../../components/HeaderBack';
import { login, getAuthNumCellphone, checkAuthNum, handleMemberActivate } from '../../shared/axios';
import { getToken, removeToken, saveToken } from '../../shared/localStorage';
import { getMember, setMember } from '../../redux/modules/member';

function Login() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [btnState, setBtnState] = useState(false);
    const [isToEditable, setIsToEditable] = useState(true);
    const [isAuthNumBtnDisabled, setIsAuthNumBtnDisabled] = useState(false);
    const [isCheckAuthNumBtnDisabled, setIsCheckAuthNumBtnDisabled] = useState(false);
    const [isCheckAuthNumInputDisabled, setIsCheckAuthNumInputDisabled] = useState(false); // 새로 추가한 상태

    const [to, setTo] = useState('');
    const [authNum, setAuthNum] = useState('');

    //로그인 체크
    // useEffect를 활용하여 컴포넌트가 마운트될 때 실행될 코드를 작성합니다.
    useEffect(() => {
        // getToken 함수를 통해 토큰을 가져옵니다.
        const token = getToken();

        // 토큰이 있으면 '/main'으로 navigate합니다.
        if (token) {
            navigate('/main');
        }
    }, []); // 빈 배열을 전달하여 컴포넌트가 처음 렌더링될 때만 실행되도록 설정합니다.

    const handleGetAuthNumBtnClick = (e) => {
        e.preventDefault(); // 폼의 기본 동작을 막음
        if (/[^0-9]/g.test(to) || to.length < 8) {
            // 안에 숫자가 아닌 값이 있을 경우
            alert('번호는 숫자만, 길이는 8자 이상 입력해주세요');
            return;
        }
        // 휴대폰 번호가 유효하다면, 인증번호를 받기 위한 요청을 보냄
        getAuthNumCellphone(to)
            .then((response) => {
                alert('인증번호가 발송되었습니다.');
                setIsAuthNumBtnDisabled(true); // 버튼 비활성화
            })
            .catch((err) => {
                alert('이미 존재하거나 올바르지 않은 이메일입니다.');
            });
    };

    const handleCheckAuthNumBtnClick = (e) => {
        e.preventDefault(); // 폼의 기본 동작을 막음
        if (!/^[a-zA-Z0-9!@#$%^*+=-]{5}$/.test(authNum)) {
            // 안에 소문자,대문자,숫자, 특수문자 '!' ~ '+' (괄호 제외)를 제외한 값이 있을 경우
            alert('인증번호는 숫자 5자리로 입력해야 합니다.');
            return;
        }

        const data = {
            authNumber: authNum,
            to: to,
        };
        checkAuthNum(data)
            .then((response) => {
                alert('인증번호가 일치합니다.');
                setBtnState(true);
                setIsToEditable(false);
                setIsCheckAuthNumBtnDisabled(true);
                setIsCheckAuthNumInputDisabled(true);
            })
            .catch((err) => {
                alert('인증번호가 일치하지 않습니다.');
                setIsToEditable(true);
            });
    };

    const handleLoginBtnClick = async (e) => {
        removeToken();
        e.preventDefault();
        const memberDTO = {
            cellphone: to,
        };

        try {
            const response = await login(memberDTO);

            // 확인 알림 창 띄우고, 비활성화 여부 확인
            const shouldDeactivate = response.data.activate;

            if (shouldDeactivate) {
                const confirmResult = window.confirm('계정이 비활성화 상태입니다. 활성화하시겠습니까?');

                if (confirmResult) {
                    // 비활성화가 해제되었다는 메시지를 띄우고 API 호출
                    await handleMemberActivate(response.data.tag);
                    alert('비활성화가 해제되었습니다.');
                } else {
                    // 사용자가 취소한 경우 로그인 불가능
                    alert('비활성 회원은 로그인할 수 없습니다.');
                    return;
                }
            }

            if (response.data) {
                const receivedToken = response.headers.get('Authorization');
                if (receivedToken) {
                    const token = receivedToken.replace('Bearer ', '');
                    saveToken(token);
                }
                dispatch(setMember(response.data));
                navigate('/main');
            }
        } catch (err) {
            alert('로그인에 실패하였습니다. 다시 시도 해주세요.');
        }
    };

    const onChange = (e) => {
        // 버튼 활성화
        const updatedTo = e.target.name === 'to' ? e.target.value : to;
        const updatedAuthNum = e.target.name === 'authNum' ? e.target.value : authNum;

        setTo(updatedTo);
        setAuthNum(updatedAuthNum);
    };

    return (
        <Box>
            <HeaderBack />
            <Content>
                <em>
                    안녕하세요!
                    <br />
                    휴대폰 번호로 로그인 해주세요.
                </em>
                <p>휴대폰 번호는 안전하게 보관되며 이웃들에게 공개되지 않아요</p>
                <Form>
                    <div>
                        <input
                            className="to"
                            type="text"
                            placeholder="휴대폰 번호 (- 없이 숫자만 입력)"
                            required
                            maxLength={11}
                            disabled={!isToEditable}
                            onChange={onChange}
                            name="to"
                        />
                        <Button
                            className="authNumberBtn"
                            onClick={handleGetAuthNumBtnClick}
                            disabled={isAuthNumBtnDisabled}
                        >
                            인증번호 받기
                        </Button>
                    </div>
                    <div>
                        <input
                            className="authNum"
                            type="text"
                            placeholder="인증번호"
                            maxLength={5}
                            required
                            onChange={onChange}
                            name="authNum"
                            disabled={isCheckAuthNumInputDisabled} // 인증번호 확인 입력 창의 활성화 여부 설정
                        />
                        <Button
                            className="authNumberBtn"
                            onClick={handleCheckAuthNumBtnClick}
                            disabled={isCheckAuthNumBtnDisabled}
                        >
                            인증번호 확인
                        </Button>
                    </div>
                    <Button
                        onClick={handleLoginBtnClick}
                        isActive={!btnState}
                    >
                        로그인
                    </Button>
                    <p>
                        <span>
                            <Link to="/findAccount">이메일로 계정 찾기</Link>
                        </span>
                    </p>
                </Form>
            </Content>
        </Box>
    );
}

const Box = styled.div``;

const Content = styled.div`
    padding: 40px 20px 0;

    em {
        font-size: 20px;
        font-weight: bold;
        line-height: 1.4;
    }

    p {
        font-size: 12px;
        margin-top: 20px;
    }
`;

const Form = styled.form`
    display: flex;
    flex-direction: column;
    margin-top: 30px;

    input {
        border: 1px solid #bbb;
        height: 50px;
        border-radius: 5px;
        padding: 0 10px;

        &::placeholder {
            color: #ccc;
        }
    }

    .authNum,
    .to {
        width: 50%;
    }
    .authNumberBtn {
        width: 40%;
        margin-left: 10%; /* 오른쪽에 여백 추가 */
        border: 1px solid #bbb;
        height: 50px;
        border-radius: 5px;
        padding: 0 10px;
    }

    input + input {
        margin-top: 10px;
    }

    p {
        margin-top: 30px;
        text-align: center;
        font-size: 18px;
    }
`;

const Button = styled.button`
    margin-top: 20px;
    height: 80px;
    border-radius: 5px;
    border: none;
    background-color: #ddd;
    font-size: 20px;
    color: #fff;
    transition: background 0.3s;
    cursor: pointer;
    ${(props) =>
        props.isActive
            ? css`
                  background-color: ${(props) => props.theme.color.orange};
                  &:hover {
                      background-color: ${(props) => props.theme.hoverColor.orange};
                  }
              `
            : css`
                  background-color: #ddd;
              `}
`;

export default Login;
