package asg.concert.service.mapper;

import asg.concert.common.dto.PerformerDTO;
import asg.concert.service.domain.Performer;
import asg.concert.common.types.Genre;
public class PerformerMapper {


    public static Performer toDomainModel(PerformerDTO performerDTO){
        Performer performer = new Performer(performerDTO.getId(),
                performerDTO.getName(),
                performerDTO.getImageName(),
                performerDTO.getGenre(),
                performerDTO.getBlurb());
        return performer;
    }
    public static PerformerDTO toDto(Performer performer){
        PerformerDTO performerDTO = new PerformerDTO(performer.getId(),
                performer.getName(),
                performer.getImageName(),
                performer.getGenre(),
                performer.getBlurb());
        return performerDTO;
    }
}
