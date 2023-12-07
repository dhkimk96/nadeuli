package kr.nadeuli.service.orikkirimanage;

import kr.nadeuli.entity.AnsQuestion;
import kr.nadeuli.entity.Orikkiri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnsQuestionRepository extends JpaRepository<AnsQuestion, Long> {

    Page<AnsQuestion> findByOrikkiri(Orikkiri orikkiri, Pageable pageable);
}
