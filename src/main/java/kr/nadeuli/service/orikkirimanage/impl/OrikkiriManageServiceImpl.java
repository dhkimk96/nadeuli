package kr.nadeuli.service.orikkirimanage.impl;

import kr.nadeuli.dto.AnsQuestionDTO;
import kr.nadeuli.dto.OrikkiriDTO;
import kr.nadeuli.dto.SearchDTO;
import kr.nadeuli.entity.AnsQuestion;
import kr.nadeuli.entity.Orikkiri;
import kr.nadeuli.mapper.AnsQuestionMapper;
import kr.nadeuli.mapper.OrikkiriMapper;
import kr.nadeuli.service.orikkirimanage.AnsQuestionRepository;
import kr.nadeuli.service.orikkirimanage.OrikkiriManageRepository;
import kr.nadeuli.service.orikkirimanage.OrikkiriManageService;
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
@Service("OrikkiriManagerServiceImpl")
public class OrikkiriManageServiceImpl implements OrikkiriManageService {

    private final OrikkiriManageRepository orikkiriManageRepository;
    private final OrikkiriMapper orikkiriMapper;


    private final AnsQuestionRepository ansQuestionRepository;
    private final AnsQuestionMapper ansQuestionMapper ;

    @Override
    public void addOrikkiri(OrikkiriDTO orikkiriDTO) throws Exception {
        Orikkiri orikkiri = orikkiriMapper.orikkiriDTOToOrikkiri(orikkiriDTO);
        log.info(orikkiri);
        orikkiriManageRepository.save(orikkiri);
    }

    @Override
    public void updateOrikkiri(OrikkiriDTO orikkiriDTO) throws Exception {
        Orikkiri orikkiri = orikkiriMapper.orikkiriDTOToOrikkiri(orikkiriDTO);
        log.info(orikkiri);
        orikkiriManageRepository.save(orikkiri);
    }

    @Override
    public OrikkiriDTO getOrikkiri(long orikkiriId) throws Exception {
        return orikkiriManageRepository.findById(orikkiriId).map(orikkiriMapper::orikkiriToOrikkiriDTO).orElse(null);
    }

    @Override
    public void deleteOrikkiri(long orikkiriId) throws Exception {
        log.info(orikkiriId);
        orikkiriManageRepository.deleteById(orikkiriId);
    }

    @Override
    public void addAnsQuestion(AnsQuestionDTO ansQuestionDTO) throws Exception {
        AnsQuestion ansQuestion = ansQuestionMapper.ansQuestionDTOToAnsQuestion(ansQuestionDTO);
        log.info(ansQuestion);
        ansQuestionRepository.save(ansQuestion);
    }

    @Override
    public void updateAnsQuestion(AnsQuestionDTO ansQuestionDTO) throws Exception {
        AnsQuestion ansQuestion = ansQuestionMapper.ansQuestionDTOToAnsQuestion(ansQuestionDTO);
        log.info(ansQuestion);
        ansQuestionRepository.save(ansQuestion);
    }

    @Override
    public AnsQuestionDTO getAnsQuestion(long ansQuestionId) throws Exception {
        return ansQuestionRepository.findById(ansQuestionId).map(ansQuestionMapper::ansQuestionToAnsQuestionDTO).orElse(null);
    }

    @Override
    public List<AnsQuestionDTO> getAnsQuestionList(long orikkiriId, SearchDTO searchDTO) throws Exception {
        Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getPageSize());
        Page<AnsQuestion> ansQuestionPage;
        ansQuestionPage = ansQuestionRepository.findByOrikkiri(Orikkiri.builder().orikkiriId(orikkiriId).build(), pageable);
        log.info(ansQuestionPage);
        return ansQuestionPage.map(ansQuestionMapper::ansQuestionToAnsQuestionDTO).toList();
    }

    @Override
    public void deleteAnsQuestion(long ansQuestionId) throws Exception {
        log.info(ansQuestionId);
        ansQuestionRepository.deleteById(ansQuestionId);
    }

}
