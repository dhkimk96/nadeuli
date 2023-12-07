package kr.nadeuli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrikkiriScheduleDTO {

    private Long orikkiriScheduleId;
    private Long scheduleMemberNum;
    private String meetingDongNe;
    private LocalDateTime meetingDay;
    private OrikkiriDTO orikkiri;

}
