package asg.concert.service.mapper;

import asg.concert.common.dto.PerformerDTO;
import asg.concert.service.domain.Concert;
import asg.concert.common.dto.ConcertDTO;
import asg.concert.service.domain.Performer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConcertMapper {

    public static Concert toDomainModel(ConcertDTO concertDTO){
        Concert fullConcert = new Concert(concertDTO.getId(),
                concertDTO.getTitle(),
                concertDTO.getImageName(),
                concertDTO.getBlurb(),
                concertDTO.getDates(),
                concertDTO.getPerformers());
        return fullConcert;
    }
    public static ConcertDTO toDto(Concert concert){
        Set<Performer> performers = concert.getPerformers();
        List<PerformerDTO> performerDTOS = new ArrayList<PerformerDTO>();
        for(Performer performer:performers){
            PerformerDTO performerDTO = PerformerMapper.toDto(performer);
            performerDTOS.add(performerDTO);
        }
        ConcertDTO concertDTO = new ConcertDTO(concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlurb());
        concertDTO.getDates().addAll(concert.getDates());
        concertDTO.setPerformers(performerDTOS);
        return concertDTO;
    }
}
