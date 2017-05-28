package gr.personal.story.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import gr.personal.story.domain.Geolocation;
import gr.personal.story.domain.Story;
import gr.personal.story.repository.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick Kanakis on 1/5/2017.
 */

@Service
public class NewStoriesService {

    Logger logger = LoggerFactory.getLogger(NewStoriesService.class);

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    private CacheManager cacheManager;

    @CachePut(cacheNames = "NewStoriesOfUser", key = "#userId")
    @HystrixCommand(fallbackMethod = "newStoriesOfUserFallback")
    public List<Story> getNewStoriesOfUser(String userId) {
        Assert.hasLength(userId, "getNewStoriesOfUser input was null or empty");

        return storyRepository.findNewStoriesOfUser(userId);
    }

    @CachePut(cacheNames = "NewStoriesOfLocation", key = "#geolocation")
    @HystrixCommand(fallbackMethod = "newStoriesOfLocationFallback")
    public List<Story> getNewStoriesOfLocation(Geolocation geolocation) {
        Assert.notNull(geolocation,"getNewStoriesOfLocation input is null");
        return  storyRepository.findNewStoriesOfLocation(geolocation);
    }

    @CachePut(cacheNames = "NewStoriesOfGroup", key = "#groupId")
    @HystrixCommand(fallbackMethod = "newStoriesOfGroupFallback")
    public List<Story> getNewStoriesOfGroup(String groupId) {
        Assert.hasLength(groupId, "getNewStoriesOfGroup input was null or empty");
        return storyRepository.findNewStoriesOfGroup(groupId);
    }

    private List<Story> newStoriesOfUserFallback(String userId, Throwable t) {
        logger.error("New Stories Fallback for UserId "+userId+". Returning list from cache", t);
        if (cacheManager.getCache("NewStoriesOfUser") != null && cacheManager.getCache("NewStoriesOfUser").get(userId) != null) {
            return cacheManager.getCache("NewStoriesOfUser").get(userId, List.class);
        }
        else {
            logger.error("New Stories Fallback for userId: "+ userId +". Cache is empty.");
            return new ArrayList<>();
        }
    }

    private List<Story> newStoriesOfLocationFallback(Geolocation geolocation, Throwable t) {
        logger.error("New Stories Fallback for Location " + geolocation+". Returning list from cache", t);
        if (cacheManager.getCache("NewStoriesOfLocation") != null && cacheManager.getCache("NewStoriesOfLocation").get(geolocation) != null) {
            return cacheManager.getCache("NewStoriesOfLocation").get(geolocation, List.class);
        }
        else {
            logger.error("New Stories Fallback for userId: "+ geolocation +". Cache is empty.");
            return new ArrayList<>();
        }
    }

    private List<Story> newStoriesOfGroupFallback(String groupId, Throwable t) {
        logger.error("New Stories Fallback for GroupId "+groupId+". Returning list from cache", t);
        if (cacheManager.getCache("NewStoriesOfGroup") != null && cacheManager.getCache("NewStoriesOfGroup").get(groupId) != null) {
            return cacheManager.getCache("NewStoriesOfGroup").get(groupId, List.class);
        }
        else {
            logger.error("New Stories Fallback for groupId: "+ groupId +". Cache is empty.");
            return new ArrayList<>();
        }
    }

}
