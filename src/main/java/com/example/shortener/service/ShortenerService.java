package com.example.shortener.service;

import com.example.shortener.domain.Link;
import com.example.shortener.domain.Statistic;
import com.example.shortener.domain.User;
import com.example.shortener.domain.util.HashGenerator;
import com.example.shortener.repo.LinkRepo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Log
public class ShortenerService {

    private LinkRepo linkRepo;

    @Autowired
    public void setLinkRepo(LinkRepo linkRepo) {
        this.linkRepo = linkRepo;
    }

    public Link createShortUrl(Link link, User user) {
        log.info("Shortening " + link.getUrl());
        //Проверка на наличие записи в БД
        Link linkFromDB = linkRepo.findByUrl(link.getUrl());
        if (linkFromDB != null) {
            log.info("link: " + linkFromDB.getUrl() + " found in DB");
            return linkFromDB;
        }
        String hash = HashGenerator.getRandomHash();
        link.setHash(hash);
        link.setUser(user);
        Statistic linkStatistic = new Statistic();
        LocalDateTime currentDateTime = LocalDateTime.now();
        linkStatistic.setCreatedDateTime(currentDateTime);
        if (link.getExpire() != null) {
            linkStatistic.setExpiredDateTime(currentDateTime.plusSeconds(link.getExpire()));
        }
        link.setStatistic(linkStatistic);
        return linkRepo.save(link);
    }

    public Link getLongURLFromHash(String hash, HttpServletRequest request) {
        Link link = linkRepo.findByHash(hash);
        if (link != null) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Statistic linkStat = link.getStatistic();
            linkStat.incrementClickCount();
            linkStat.setLastClickDateTime(currentDateTime);
            if (!link.getStatistic().getUniqueIpList().contains(request.getRemoteAddr())) {
                link.getStatistic().getUniqueIpList().add(request.getRemoteAddr());
                linkStat.incrementUniqClickCount();
            }
            return linkRepo.save(link);
        } else {
            return null;
        }
    }
    public Link getUserLink(String hash) {
        deleteExpiredLinks();
        return linkRepo.findByHash(hash);
    }
    public List<Link> getUserLinks(User user) {
        deleteExpiredLinks();
        return linkRepo.findByUserId(user.getId());
    }
    public void deleteLink(@PathVariable("hash") String hash) throws Exception {
        Link link = linkRepo.findByHash(hash);
        if(link!=null) {
            linkRepo.delete(link);
        } else throw new Exception("URL not found");
    }

    public Link updateLink(@RequestBody Map<String, Object> payload, @PathVariable("hash") String hash) throws Exception {
        if (payload.containsKey("expire")) {
            long expireTime = Long.parseLong((String) payload.get("expire"));
            Link link = linkRepo.findByHash(hash);
            if (expireTime == 0L) {
                link.getStatistic().setExpiredDateTime(null);
            } else {
                link.getStatistic().setExpiredDateTime(LocalDateTime.now().plusSeconds(expireTime));
            }
            return linkRepo.save(link);
        } else throw new Exception("Specify an attribute \"Expire \"");
    }

    private void deleteExpiredLinks() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        for (Link link : linkRepo.findAll()) {
            Statistic linkStatistic = link.getStatistic();
            if (linkStatistic.getExpiredDateTime() != null) {
                if (linkStatistic.getExpiredDateTime().isBefore(currentDateTime)) {
                    linkRepo.delete(link);
                }
            }
        }
    }
}
