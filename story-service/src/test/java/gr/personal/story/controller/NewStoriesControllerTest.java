package gr.personal.story.controller;

import com.google.common.collect.ImmutableList;
import gr.personal.story.domain.Geolocation;
import gr.personal.story.domain.Story;
import gr.personal.story.service.StoriesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static gr.personal.story.helper.FakeDataGenerator.generateStory;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Nick Kanakis on 14/5/2017.
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("noEureka")
public class NewStoriesControllerTest {
    @MockBean
    @Qualifier("NewStoriesService")
    private StoriesService newStoriesService;
    private MockMvc mockMvc;
    @InjectMocks
    private NewStoriesController newStoriesController;

    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(newStoriesController).build();
    }

    @Test
    public void shouldGetNewStoriesOfUser() throws Exception{
        Story story = generateStory();

        when(newStoriesService.getStoriesOfUser("test")).thenReturn(ImmutableList.of(story));

        mockMvc.perform(get("/newStories/user/test")).andExpect(jsonPath("$[0].id").value(story.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetNewStoriesOfLocation() throws Exception{
        Story story = generateStory();
        Geolocation geolocation = new Geolocation();
        geolocation.setLatitude(0);
        geolocation.setLongitude(0);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("latitude",String.valueOf(geolocation.getLatitude()));
        params.add("longitude",String.valueOf(geolocation.getLongitude()));

        when(newStoriesService.getStoriesOfLocation(any(Geolocation.class))).thenReturn(ImmutableList.of(story));

        mockMvc.perform(get("/newStories/location").params(params))
                .andExpect(jsonPath("$[0].id").value(story.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetNewStoriesOfGroup() throws Exception{
        Story story = generateStory();

        when(newStoriesService.getStoriesOfGroup("test")).thenReturn(ImmutableList.of(story));

        mockMvc.perform(get("/newStories/group/test")).andExpect(jsonPath("$[0].id").value(story.getId()))
                .andExpect(status().isOk());
    }
}
