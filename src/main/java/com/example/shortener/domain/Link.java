package com.example.shortener.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Link {
    @Id
    private String hash;
    private String url;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "statistic_id")
    private Statistic statistic;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer expire;

    public void setUrl(String url) {
        this.url = url.toLowerCase();
    }
}
