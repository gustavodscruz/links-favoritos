package com.example.links.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.links.service.GifService;

@Component
public class WipeGifCacheScheduler {
    
    @Autowired
    private GifService gifService;

    @Scheduled(cron = "0 */3 * * * *")
    public void cleanGifCache(){
        gifService.wipeCache();    
    }
}
