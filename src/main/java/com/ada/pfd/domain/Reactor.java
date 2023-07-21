package com.ada.pfd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reactor.
 */
@Entity
@Table(name = "reactor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reactor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "working_volume", precision = 21, scale = 2)
    private BigDecimal workingVolume;

    @Column(name = "vessel_id")
    private String vesselId;

    @Column(name = "bottom_impeller_stirring_volume", precision = 21, scale = 2)
    private BigDecimal bottomImpellerStirringVolume;

    @Column(name = "minimum_temp_sensing_volume", precision = 21, scale = 2)
    private BigDecimal minimumTempSensingVolume;

    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Unit unit;

    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Block block;

    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MOC moc;

    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ImpellerType impellerType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reactor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reactor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reactor")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reactor" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reactor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWorkingVolume() {
        return this.workingVolume;
    }

    public Reactor workingVolume(BigDecimal workingVolume) {
        this.setWorkingVolume(workingVolume);
        return this;
    }

    public void setWorkingVolume(BigDecimal workingVolume) {
        this.workingVolume = workingVolume;
    }

    public String getVesselId() {
        return this.vesselId;
    }

    public Reactor vesselId(String vesselId) {
        this.setVesselId(vesselId);
        return this;
    }

    public void setVesselId(String vesselId) {
        this.vesselId = vesselId;
    }

    public BigDecimal getBottomImpellerStirringVolume() {
        return this.bottomImpellerStirringVolume;
    }

    public Reactor bottomImpellerStirringVolume(BigDecimal bottomImpellerStirringVolume) {
        this.setBottomImpellerStirringVolume(bottomImpellerStirringVolume);
        return this;
    }

    public void setBottomImpellerStirringVolume(BigDecimal bottomImpellerStirringVolume) {
        this.bottomImpellerStirringVolume = bottomImpellerStirringVolume;
    }

    public BigDecimal getMinimumTempSensingVolume() {
        return this.minimumTempSensingVolume;
    }

    public Reactor minimumTempSensingVolume(BigDecimal minimumTempSensingVolume) {
        this.setMinimumTempSensingVolume(minimumTempSensingVolume);
        return this;
    }

    public void setMinimumTempSensingVolume(BigDecimal minimumTempSensingVolume) {
        this.minimumTempSensingVolume = minimumTempSensingVolume;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Reactor unit(Unit unit) {
        this.setUnit(unit);
        return this;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Reactor block(Block block) {
        this.setBlock(block);
        return this;
    }

    public MOC getMoc() {
        return this.moc;
    }

    public void setMoc(MOC mOC) {
        this.moc = mOC;
    }

    public Reactor moc(MOC mOC) {
        this.setMoc(mOC);
        return this;
    }

    public ImpellerType getImpellerType() {
        return this.impellerType;
    }

    public void setImpellerType(ImpellerType impellerType) {
        this.impellerType = impellerType;
    }

    public Reactor impellerType(ImpellerType impellerType) {
        this.setImpellerType(impellerType);
        return this;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setReactor(null));
        }
        if (images != null) {
            images.forEach(i -> i.setReactor(this));
        }
        this.images = images;
    }

    public Reactor images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Reactor addImage(Image image) {
        this.images.add(image);
        image.setReactor(this);
        return this;
    }

    public Reactor removeImage(Image image) {
        this.images.remove(image);
        image.setReactor(null);
        return this;
    }

    public Set<Video> getVideos() {
        return this.videos;
    }

    public void setVideos(Set<Video> videos) {
        if (this.videos != null) {
            this.videos.forEach(i -> i.setReactor(null));
        }
        if (videos != null) {
            videos.forEach(i -> i.setReactor(this));
        }
        this.videos = videos;
    }

    public Reactor videos(Set<Video> videos) {
        this.setVideos(videos);
        return this;
    }

    public Reactor addVideo(Video video) {
        this.videos.add(video);
        video.setReactor(this);
        return this;
    }

    public Reactor removeVideo(Video video) {
        this.videos.remove(video);
        video.setReactor(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setReactor(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setReactor(this));
        }
        this.documents = documents;
    }

    public Reactor documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Reactor addDocument(Document document) {
        this.documents.add(document);
        document.setReactor(this);
        return this;
    }

    public Reactor removeDocument(Document document) {
        this.documents.remove(document);
        document.setReactor(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reactor)) {
            return false;
        }
        return id != null && id.equals(((Reactor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reactor{" +
            "id=" + getId() +
            ", workingVolume=" + getWorkingVolume() +
            ", vesselId='" + getVesselId() + "'" +
            ", bottomImpellerStirringVolume=" + getBottomImpellerStirringVolume() +
            ", minimumTempSensingVolume=" + getMinimumTempSensingVolume() +
            "}";
    }
}
