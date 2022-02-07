package com.example.shortener.controller;

import com.example.shortener.domain.Link;
import com.example.shortener.domain.User;
import com.example.shortener.domain.util.UrlValidator;
import com.example.shortener.repo.LinkRepo;
import com.example.shortener.service.ShortenerService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@Log
public class MainController {
    private LinkRepo linkRepo;
    private ShortenerService shortenerService;

    @Autowired
    public void setShortenerService(ShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @Autowired
    public void setLinkRepo(LinkRepo linkRepo) {
        this.linkRepo = linkRepo;
    }

    @PostMapping(path = "/link", consumes = "application/json")
    public Link createShortUrl(
            @RequestBody Link link,
            @AuthenticationPrincipal User user
    ) throws Exception {
        log.info("Received url to shorten: " + link.getUrl());
        //Проверка на соответствие формату (HTTP/HTTPS)
        if (UrlValidator.validateUrl(link)) {
            Link shortUrl = shortenerService.createShortUrl(link, user);
            log.info("Shortened url to: " + shortUrl.getHash());
            return shortUrl;
        } else {
            throw new Exception("Please enter a valid URL");
        }
    }

    @GetMapping(path = "/{hash}")
    public ResponseEntity<String> redirectShorter(
            @PathVariable("hash") String hash,
            HttpServletRequest request) {
        log.info("Received shortened hash to redirect: " + hash);
        Link link = shortenerService.getLongURLFromHash(hash, request);
        if (link != null) {
            log.info("Shortener link found, URL: " + link.getUrl());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location", link.getUrl());
            return new ResponseEntity<>(httpHeaders, HttpStatus.PERMANENT_REDIRECT);
        } else {
            log.info("Shortener link not found");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/link")
    public List<Link> getUserLinks(@AuthenticationPrincipal User user) {
        return shortenerService.getUserLinks(user);
    }

    @GetMapping(path = "/link/{hash}")
    public Link getLink(@PathVariable("hash") String hash) throws Exception {
        Link link = shortenerService.getUserLink(hash);
        if (link != null) {
            return link;
        } else {
            throw new Exception("URL not found");
        }
    }

    @DeleteMapping(path = "/link/{hash}")
    public void delete(@PathVariable("hash") String hash) throws Exception {
        shortenerService.deleteLink(hash);
    }

    @PutMapping(path = "/link/{hash}", consumes = "application/json")
    public void update(@RequestBody Map<String, Object> payload, @PathVariable("hash") String hash) throws Exception {
       shortenerService.updateLink(payload, hash);
    }
}