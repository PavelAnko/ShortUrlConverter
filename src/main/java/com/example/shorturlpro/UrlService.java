package com.example.shorturlpro;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlRandomLinkGenerator urlRandomLinkGenerator;
    public UrlService(UrlRepository urlRepository, UrlRandomLinkGenerator urlRandomLinkGenerator) {
        this.urlRepository = urlRepository;
        this.urlRandomLinkGenerator = urlRandomLinkGenerator;
    }

    @Transactional
    public String saveUrl(UrlDTO url) throws URISyntaxException {
        var urlRecord = urlRepository.findByUrl(url.getUrl());
        if (urlRecord == null) {
            urlRecord = new UrlRecord();
            urlRecord.setUrl(url.getUrl());
            urlRecord.setShortUrl(urlRandomLinkGenerator.generateRandomString(url.getUrl()));
            urlRepository.save(urlRecord);
        }

        return urlRecord.getShortUrl();
    }

    @Transactional
    public String getUrl(String shortUrl) {
        var url = urlRepository.findByShortUrl(shortUrl);
        if (url==null)
            return null;

        url.setLastAccess(new Date());
        url.setCount(url.getCount() + 1);
        urlRepository.save(url);

        return url.getUrl();
    }

    @Transactional(readOnly = true)
    public List<UrlStatDTO> getStatistics(Pageable pageable) {
        var urls = urlRepository.findAll(pageable);
        var result = new ArrayList<UrlStatDTO>();

        for (var url : urls) {
            var stat = new UrlStatDTO();
            stat.setLastAccess(url.getLastAccess());
            stat.setRedirects(url.getCount());
            stat.setUrl(url.getUrl());

            result.add(stat);
        }

        return result;
    }
}
