package kr.nadeuli.service.orikkiri.impl;

import kr.nadeuli.dto.OriScheMemChatFavDTO;
import kr.nadeuli.dto.OrikkiriScheduleDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.entity.*;
import kr.nadeuli.mapper.OriScheMemChatFavMapper;
import kr.nadeuli.mapper.OrikkiriScheduleMapper;
import kr.nadeuli.service.orikkiri.OriScheMenChatFavRepository;
import kr.nadeuli.service.orikkiri.OrikkiriScheduleRepository;
import kr.nadeuli.service.orikkiri.OrikkiriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Transactional
@Service("orikkiriServiceImpl")
public class OrikkiriServiceImpl implements OrikkiriService {

    private final OriScheMenChatFavRepository oriScheMenChatFavRepository;
    private final OriScheMemChatFavMapper oriScheMemChatFavMapper;

    private final OrikkiriScheduleRepository orikkiriScheduleRepository;
    private final OrikkiriScheduleMapper orikkiriScheduleMapper;

    @Override
    public void addOrikkrirSignUp(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        OriScheMemChatFav oriScheMemChatFav = oriScheMemChatFavMapper.oriScheMemChatFavDTOToOriScheMemChatFav(oriScheMemChatFavDTO);
        log.info(oriScheMemChatFav);
        oriScheMenChatFavRepository.save(oriScheMemChatFav);
    }

    @Override
    public List<OriScheMemChatFavDTO> getOrikkiriSignUpList(long ansQuestionId, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
        Page<OriScheMemChatFav> oriScheMemChatFavPage;
        oriScheMemChatFavPage = oriScheMenChatFavRepository.findByAnsQuestions(AnsQuestion.builder().ansQuestionId(ansQuestionId).build(), pageable);
        log.info(oriScheMemChatFavPage);
        return oriScheMemChatFavPage.map(oriScheMemChatFavMapper::oriScheMemChatFavToOriScheMemChatFavDTO).toList();
    }

    @Override
    public List<OriScheMemChatFavDTO> getMyOrikkiriList(String tag, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
        Page<OriScheMemChatFav> oriScheMemChatFavPage;
        oriScheMemChatFavPage = oriScheMenChatFavRepository
                .findByMemberAndOrikkiriNotNull(Member.builder().tag(tag).build(), pageable);
        log.info(oriScheMemChatFavPage);
        return oriScheMemChatFavPage.map(oriScheMemChatFavMapper::oriScheMemChatFavToOriScheMemChatFavDTO).toList();

    }

    @Override
    public void deleteOrikkiriMember(String tag, long orikkiriId) throws Exception {
        log.info(tag);
        log.info(orikkiriId);

        oriScheMenChatFavRepository.deleteByMemberAndOrikkiri(
                Member.builder().tag(tag).build(),
                Orikkiri.builder().orikkiriId(orikkiriId).build()
        );
    }

    @Override
    public List<OriScheMemChatFavDTO> getOrikkiriMemberList(long orikkiriId, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
        Page<OriScheMemChatFav> oriScheMemChatFavPage;
        oriScheMemChatFavPage = oriScheMenChatFavRepository.findByOrikkiri(Orikkiri.builder().orikkiriId(orikkiriId).build(), pageable);
        log.info(oriScheMemChatFavPage);
        return oriScheMemChatFavPage.map(oriScheMemChatFavMapper::oriScheMemChatFavToOriScheMemChatFavDTO).toList();
    }


    @Override
    public void addOrikkiriScheduleMember(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        OriScheMemChatFav oriScheMemChatFav = oriScheMemChatFavMapper.oriScheMemChatFavDTOToOriScheMemChatFav(oriScheMemChatFavDTO);
        log.info(oriScheMemChatFav);
        oriScheMenChatFavRepository.save(oriScheMemChatFav);
    }

    @Override
    public void addOrikkiriMember(OriScheMemChatFavDTO oriScheMemChatFavDTO) throws Exception {
        OriScheMemChatFav oriScheMemChatFav = oriScheMemChatFavMapper.oriScheMemChatFavDTOToOriScheMemChatFav(oriScheMemChatFavDTO);
        log.info(oriScheMemChatFav);
        oriScheMenChatFavRepository.save(oriScheMemChatFav);
    }

    @Override
    public List<OriScheMemChatFavDTO> getOrikkiriScheduleMemberList(long orikkiriScheduleId, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
        Page<OriScheMemChatFav> oriScheMemChatFavPage;
        oriScheMemChatFavPage = oriScheMenChatFavRepository.findByOrikkiriSchedule(OrikkiriSchedule.builder().orikkiriScheduleId(orikkiriScheduleId).build(), pageable);
        log.info(oriScheMemChatFavPage);
        return oriScheMemChatFavPage.map(oriScheMemChatFavMapper::oriScheMemChatFavToOriScheMemChatFavDTO).toList();
    }

    @Override
    public void addOrikkiriSchedule(OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception {
        OrikkiriSchedule orikkiriSchedule = orikkiriScheduleMapper.orikkiriScheduleDTOToOrikkiriSchedule(orikkiriScheduleDTO);
        log.info(orikkiriSchedule);
        orikkiriScheduleRepository.save(orikkiriSchedule);
    }

    //스케쥴 구분 하기위한 수정 필요
    @Override
    public List<OrikkiriScheduleDTO> getOrikkiriScheduleList(long orikkiriId, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
        Page<OrikkiriSchedule> orikkiriSchedulePage;
        orikkiriSchedulePage = orikkiriScheduleRepository.findByOrikkiri(Orikkiri.builder().orikkiriId(orikkiriId).build(), pageable);

        log.info(orikkiriSchedulePage);
        return orikkiriSchedulePage.map(orikkiriScheduleMapper::orikkiriScheduleToOrikkiriScheduleDTO).toList();
    }

    @Override
    public OrikkiriScheduleDTO getOrikkiriSchedule(long orikkiriScheduleId) throws Exception {
        return orikkiriScheduleRepository.findById(orikkiriScheduleId).map(orikkiriScheduleMapper::orikkiriScheduleToOrikkiriScheduleDTO).orElse(null);
    }


    @Override
    public void updateOrikkiriSchedule(OrikkiriScheduleDTO orikkiriScheduleDTO) throws Exception {
        OrikkiriSchedule orikkiriSchedule = orikkiriScheduleMapper.orikkiriScheduleDTOToOrikkiriSchedule(orikkiriScheduleDTO);
        log.info(orikkiriSchedule);
        orikkiriScheduleRepository.save(orikkiriSchedule);
    }

    @Override
    public void deleteOrikkiriSchedule(long orikkiriScheduleId) throws Exception {
        log.info(orikkiriScheduleId);
        orikkiriScheduleRepository.deleteById(orikkiriScheduleId);
    }



}
