package kr.nadeuli.service.member;

import kr.nadeuli.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepotRepository extends JpaRepository<Report, Long> {

}
