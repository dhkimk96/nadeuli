package kr.nadeuli.mapper;

import kr.nadeuli.dto.ReportDTO;
import kr.nadeuli.entity.Report;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring", uses = {ProductMapper.class,
    PostMapper.class, NadeuliDeliveryMapper.class})
public interface ReportMapper {


  ReportDTO reportToReportDTO(Report report);

  Report reportDTOToReport(ReportDTO reportDTO);


}