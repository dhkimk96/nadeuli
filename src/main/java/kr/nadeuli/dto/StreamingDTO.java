package kr.nadeuli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamingDTO {

    private String channelName;
    private String channelId;

    private String channelStatus;
    private String streamKey;

    private String url;

}
