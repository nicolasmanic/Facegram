package gr.personal.story.controller;

import com.google.common.collect.ImmutableList;
import gr.personal.story.domain.Geolocation;
import gr.personal.story.domain.StoryImpl;
import gr.personal.story.service.NewStoriesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nick Kanakis on 14/5/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewStoriesControllerTest {

    @InjectMocks
    private NewStoriesController newStoriesController;

    @Mock
    private NewStoriesService newStoriesService;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(newStoriesController).build();
    }

    @Test
    public void shouldGetNewStoriesOfUser() throws Exception{

        StoryImpl story = new StoryImpl();
        story.setId("test");

        when(newStoriesService.getNewStoriesOfUser("test")).thenReturn(ImmutableList.of(story));

        mockMvc.perform(get("/newStories/user/test")).andExpect(jsonPath("$[0].id").value(story.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetNewStoriesOfLocation() throws Exception{

        StoryImpl story = new StoryImpl();
        story.setId("test");

        Geolocation geolocation = new Geolocation();
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);

        when(newStoriesService.getNewStoriesOfLocation(any(Geolocation.class))).thenReturn(ImmutableList.of(story));

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("latitude",String.valueOf(geolocation.getLatitude()));
        params.add("longitude",String.valueOf(geolocation.getLongitude()));

        mockMvc.perform(get("/newStories/location").params(params))
                .andExpect(jsonPath("$[0].id").value(story.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetNewStoriesOfGroup() throws Exception{

        StoryImpl story = new StoryImpl();
        story.setId("test");

        when(newStoriesService.getNewStoriesOfGroup("test")).thenReturn(ImmutableList.of(story));

        mockMvc.perform(get("/newStories/group/test")).andExpect(jsonPath("$[0].id").value(story.getId()))
                .andExpect(status().isOk());
    }


}