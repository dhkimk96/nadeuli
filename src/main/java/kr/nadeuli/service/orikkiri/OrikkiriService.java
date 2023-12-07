package kr.nadeuli.service.orikkiri;

import kr.nadeuli.dto.*;

import java.util.List;

public interface OrikkiriService {

    void addOrikkrirSignUp(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception; // 우리끼리에 가

    List<OriScheMemChatFavDTO> getOrikkiriSignUpList(long ansQuestionId, SearchDTO searchDTO) throws Exception;

    List<OriScheMemChatFavDTO> getMyOrikkiriList(String tag, SearchDTO searchDTO) throws Exception;

    List<OriScheMemChatFavDTO> getOrikkiriMemberList(long orikkiriId, SearchDTO searchDTO) throws Exception;

    void deleteOrikkiriMember(String tag, long oriScheMemChatFavId) throws Exception; // 우리끼리에서 나가기

    void addOrikkiriScheduleMember(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception; // 우리끼리 스케줄 참가

    void addOrikkiriMember(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception;

    List<OriScheMemChatFavDTO> getOrikkiriScheduleMemberList(long orikkiriScheduleId, SearchDTO searchDTO) throws Exception; // 우리끼리 스케줄 참가 멤버 확인

    void addOrikkiriSchedule(OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception;

    List<OrikkiriScheduleDTO> getOrikkiriScheduleList(long orikkiriId, SearchDTO searchDTO) throws Exception; // 우리끼리 스케줄 리스트

    OrikkiriScheduleDTO getOrikkiriSchedule(long orikkiriScheduleId) throws Exception; // 우리끼리 스케쥴 상세 정보

    void updateOrikkiriSchedule(OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception;

    void deleteOrikkiriSchedule(long orikkiriScheduleId) throws Exception;
}
