package com.studyspace.suggestion.service;

import com.studyspace.combo.entity.ComboPlan;
import com.studyspace.combo.repository.ComboPlanRepository;
import com.studyspace.space.entity.Floor;
import com.studyspace.space.entity.SpaceUnit;
import com.studyspace.space.enums.SpaceStatus;
import com.studyspace.suggestion.dto.request.SuggestSpaceRequest;
import com.studyspace.suggestion.dto.response.SuggestedComboResponse;
import com.studyspace.suggestion.dto.response.SuggestedSpaceResponse;
import com.studyspace.suggestion.entity.SuggestionRule;
import com.studyspace.suggestion.repository.SuggestionRuleRepository;
import com.studyspace.space.repository.SpaceUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceSuggestionService {

    private final SuggestionRuleRepository suggestionRuleRepository;
    private final SpaceUnitRepository spaceUnitRepository;
    private final ComboPlanRepository comboPlanRepository;

    @Transactional(readOnly = true)
    public List<SuggestedSpaceResponse> suggest(SuggestSpaceRequest request) {
        List<SuggestionRule> matchedRules =
                suggestionRuleRepository.findAllByIsActiveTrueAndMinPeopleLessThanEqualAndMaxPeopleGreaterThanEqualOrderByPriorityDescIdAsc(
                        request.getPartySize(),
                        request.getPartySize()
                );

        if (matchedRules.isEmpty()) {
            return List.of();
        }

        return matchedRules.stream()
                .flatMap(rule -> buildSuggestionsForRule(rule, request.getPartySize()).stream())
                .sorted(Comparator
                        .comparing(SuggestedSpaceResponse::getMatchedRulePriority, Comparator.reverseOrder())
                        .thenComparing(SuggestedSpaceResponse::getCapacity)
                        .thenComparing(SuggestedSpaceResponse::getSpaceUnitId))
                .toList();
    }

    private List<SuggestedSpaceResponse> buildSuggestionsForRule(SuggestionRule rule, Integer partySize) {
        List<SpaceUnit> candidates = spaceUnitRepository
                .findAllBySpaceTypeAndStatusAndIsDirectlyRentableOrderByCapacityAscIdAsc(
                        rule.getTargetSpaceType(),
                        SpaceStatus.ACTIVE,
                        true
                );

        List<ComboPlan> activeCombos = comboPlanRepository
                .findAllByApplicableSpaceTypeAndIsActiveTrueOrderByDurationMinutesAscIdAsc(rule.getTargetSpaceType());

        return candidates.stream()
                .filter(space -> matchesRule(space, rule, partySize))
                .map(space -> toSuggestedSpace(space, rule, activeCombos))
                .filter(item -> !item.getApplicableCombos().isEmpty())
                .toList();
    }

    private boolean matchesRule(SpaceUnit space, SuggestionRule rule, Integer partySize) {
        if (space.getCapacity() < partySize) {
            return false;
        }

        if (rule.getMinCapacity() != null && space.getCapacity() < rule.getMinCapacity()) {
            return false;
        }

        if (rule.getMaxCapacity() != null && space.getCapacity() > rule.getMaxCapacity()) {
            return false;
        }

        if (rule.getApplicablePriceGroup() != null) {
            return rule.getApplicablePriceGroup().equalsIgnoreCase(nullSafe(space.getPriceGroup()));
        }

        return true;
    }

    private SuggestedSpaceResponse toSuggestedSpace(SpaceUnit space, SuggestionRule rule, List<ComboPlan> activeCombos) {
        List<SuggestedComboResponse> combos = activeCombos.stream()
                .filter(combo -> matchesCombo(space, combo))
                .map(combo -> SuggestedComboResponse.builder()
                        .id(combo.getId())
                        .name(combo.getName())
                        .durationMinutes(combo.getDurationMinutes())
                        .price(combo.getPrice())
                        .build())
                .toList();

        SpaceUnit room = resolveRoom(space);
        Floor floor = room != null ? room.getFloor() : null;

        return SuggestedSpaceResponse.builder()
                .spaceUnitId(space.getId())
                .spaceName(space.getName())
                .spaceType(space.getSpaceType())
                .capacity(space.getCapacity())
                .priceGroup(space.getPriceGroup())
                .roomId(room != null ? room.getId() : null)
                .roomName(room != null ? room.getName() : null)
                .floorId(floor != null ? floor.getId() : null)
                .floorName(floor != null ? floor.getName() : null)
                .matchedRulePriority(rule.getPriority())
                .applicableCombos(combos)
                .build();
    }

    private boolean matchesCombo(SpaceUnit space, ComboPlan combo) {
        if (combo.getMinCapacity() != null && space.getCapacity() < combo.getMinCapacity()) {
            return false;
        }

        if (combo.getMaxCapacity() != null && space.getCapacity() > combo.getMaxCapacity()) {
            return false;
        }

        if (combo.getApplicablePriceGroup() != null) {
            return combo.getApplicablePriceGroup().equalsIgnoreCase(nullSafe(space.getPriceGroup()));
        }

        return true;
    }

    private SpaceUnit resolveRoom(SpaceUnit space) {
        if (space.getSpaceType() == com.studyspace.space.enums.SpaceType.ROOM) {
            return space;
        }

        SpaceUnit current = space.getParent();
        while (current != null && current.getSpaceType() != com.studyspace.space.enums.SpaceType.ROOM) {
            current = current.getParent();
        }
        return current;
    }

    private String nullSafe(String value) {
        return value == null ? "" : value.trim();
    }
}