package com.example.shortener.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDateTime;
    @Column(name = "expired_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime expiredDateTime;
    private int clickCount;
    private int uniqueClickCount;
    @Column(name = "last_click", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastClickDateTime;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "statistic")
    @JsonIgnore
    private Link link;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "unique_ip", joinColumns = @JoinColumn(name = "link_hash"))
    private Set<String> uniqueIpList = new HashSet<>();

    public void incrementClickCount() {
        clickCount++;
    }

    public void incrementUniqClickCount() {
        uniqueClickCount++;
    }
}
