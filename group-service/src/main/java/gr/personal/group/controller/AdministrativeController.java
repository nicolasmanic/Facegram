package gr.personal.group.controller;

import gr.personal.group.aop.annotations.LogExecutionTime;
import gr.personal.group.domain.GenericJson;
import gr.personal.group.domain.Group;
import gr.personal.group.domain.GroupRequest;
import gr.personal.group.service.AdministrativeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created by Nick Kanakis on 17/6/2017.
 */
@RestController
@RequestMapping("/administrative")
public class AdministrativeController {

    private static final Logger logger = LoggerFactory.getLogger(AdministrativeController.class);
    @Autowired
    private AdministrativeService administrativeService;

    @LogExecutionTime
    @RequestMapping(value = "/follow/{groupId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#oauth2.hasScope('server')")
    public ResponseEntity<String> follow(@PathVariable String groupId){
        logger.debug("Entering follow (groupId={})", groupId);
        String result = administrativeService.followGroup(groupId);
        logger.debug("Exiting follow (groupId={})", groupId);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @LogExecutionTime
    @RequestMapping(value = "/unfollow/{groupId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("#oauth2.hasScope('server')")
    public ResponseEntity<String> unfollow(@PathVariable String groupId){
        logger.debug("Entering unfollow ( groupId={})", groupId);
        String result = administrativeService.unfollowGroup(groupId);
        logger.debug("Exiting unfollow ( groupId={})", groupId);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @LogExecutionTime
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericJson createGroup(Principal principal, @RequestBody GroupRequest groupRequest){
        logger.debug("Entering createGroup (username = {})",principal.getName());
        String result = administrativeService.createGroup(principal.getName(), groupRequest);
        logger.debug("Exiting createGroup (username = {})",principal.getName());
        return new GenericJson(result);
    }

    @LogExecutionTime
    @RequestMapping(value = "/retrieve/{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> retrieveGroup(@PathVariable String groupId){
        logger.debug("Entering retrieveGroup (groupId = {})", groupId);
        Group group = administrativeService.retrieveGroup(groupId);
        logger.debug("Exiting retrieveGroup (groupId = {})", groupId);
        return  ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(group);
    }


    @LogExecutionTime
    @RequestMapping(value = "/retrieveMyGroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Group>> retrieveMyGroups(Principal principal){
        logger.debug("Entering retrieveMyGroups (username = {})", principal.getName());
        List<Group> groups = administrativeService.retrieveMyGroups(principal.getName());
        logger.debug("Exiting retrieveMyGroups (username = {})", principal.getName());
        return  ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(groups);
    }

    @LogExecutionTime
    @RequestMapping(value = "/delete/{groupId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericJson deleteGroup(Principal principal, @PathVariable String groupId){
        logger.debug("Entering deleteGroup (username ={}, groupId = {})", principal.getName(), groupId);
        String result = administrativeService.deleteGroup(groupId, principal.getName());
        logger.debug("Exiting deleteGroup (username ={}, groupId = {})", principal.getName(), groupId);
        return new GenericJson(result);
    }

    @LogExecutionTime
    @RequestMapping(value = "/update/{groupId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericJson updateGroup(Principal principal, @RequestBody GroupRequest groupRequest, @PathVariable String groupId){
        logger.debug("Entering updateGroup (username ={}, groupId = {})", principal.getName(), groupId);
        String result = administrativeService.updateGroup(groupId, groupRequest, principal.getName());
        logger.debug("Exiting updateGroup (username ={}, groupId = {})", principal.getName(), groupId);
        return new GenericJson(result);
    }
}
