package asg.concert.service.domain;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import asg.concert.common.dto.PerformerDTO;
import asg.concert.service.mapper.PerformerMapper;

/**
 * Class to represent a Concert
 */


@Entity
@Table(name = "CONCERTS")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "BLURB",length = 1000)
    private String blurb;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "CONCERT_ID"))
    @Column(name = "DATE")
    private Set<LocalDateTime> dates = new HashSet<>();

    @ManyToMany
    @JoinTable(name="CONCERT_PERFORMER",joinColumns = {@JoinColumn(name = "CONCERT_ID")},inverseJoinColumns = {
            @JoinColumn(name = "PERFORMER_ID")})
    private Set<Performer> performers = new HashSet<>();

    public Concert() {
    }

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
    }

    public Concert(String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
    }

    public Concert(Long id, String title, String imageName, String blurb, List<LocalDateTime> dates, List<PerformerDTO> performers) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
        this.dates = new HashSet<>(dates);
        Set<Performer> performers1 = new HashSet<>();
        for (PerformerDTO performer: performers) {
            Performer performerDomain = PerformerMapper.toDomainModel(performer);
            performers1.add(performerDomain);
        }
        this.performers = performers1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Set<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(Set<Performer> performers) {
        this.performers = performers;
    }

}
