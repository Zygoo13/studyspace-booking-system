package com.studyspace.suggestion.dto.response;

import com.studyspace.space.enums.SpaceType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SuggestedSpaceResponse {

    private Long spaceUnitId;
    private String spaceName;
    private SpaceType spaceType;
    private Integer capacity;
    private String priceGroup;

    private Long roomId;
    private String roomName;

    private Long floorId;
    private String floorName;

    private Integer matchedRulePriority;
    private List<SuggestedComboResponse> applicableCombos;
}