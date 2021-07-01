package asg.concert.service.mapper;

import asg.concert.common.dto.ConcertSummaryDTO;
import asg.concert.service.domain.ConcertSummary;

public class ConcertSummaryMapper {
    public static ConcertSummary toDomainModel(ConcertSummaryDTO summaryDTO){
        ConcertSummary concertSummary = new ConcertSummary(summaryDTO.getId(),
                summaryDTO.getTitle(),
                summaryDTO.getImageName());
        return concertSummary;
    }

    public static ConcertSummaryDTO toDto(ConcertSummary concertSummary){
        ConcertSummaryDTO concertSummaryDTO = new ConcertSummaryDTO(concertSummary.getId(),
                concertSummary.getTitle(),
                concertSummary.getImageName());
        return concertSummaryDTO;
    }
}
