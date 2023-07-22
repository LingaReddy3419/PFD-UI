package com.ada.pfd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A General.
 */
@Entity
@Table(name = "general")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class General implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(unique = true)
    private Operations operations;

    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(unique = true)
    private Action action;

    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(unique = true)
    private ModeOfCharging modeOfCharging;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "general", cascade = CascadeType.PERSIST)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "general", cascade = CascadeType.PERSIST)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    private Set<Video> videos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "general", cascade = CascadeType.PERSIST)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "general" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public Set<Image> getImages() {
        return images;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public void setVideos(Set<Video> videos) {
        this.videos = videos;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public General id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operations getOperations() {
        return this.operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public General operations(Operations operations) {
        this.setOperations(operations);
        return this;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public General action(Action action) {
        this.setAction(action);
        return this;
    }

    public ModeOfCharging getModeOfCharging() {
        return this.modeOfCharging;
    }

    public void setModeOfCharging(ModeOfCharging modeOfCharging) {
        this.modeOfCharging = modeOfCharging;
    }

    public General modeOfCharging(ModeOfCharging modeOfCharging) {
        this.setModeOfCharging(modeOfCharging);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof General)) {
            return false;
        }
        return id != null && id.equals(((General) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "General{" +
            "id=" + getId() +
            "}";
    }
}
