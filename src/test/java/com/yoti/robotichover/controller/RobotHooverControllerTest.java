package com.yoti.robotichover.controller;

import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;
import com.yoti.robotichover.services.RoboticHooverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class RobotHooverControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JacksonTester<RobotHooverRequest> json;

  @Test
  void testRobotHoverCleanRoom() throws Exception {

    RobotHooverRequest robotHooverRequest = roboticHooverRequest();

    mockMvc.perform(
            post("/api/v1/clean")
                .content(json.write(robotHooverRequest).getJson())
                    .contentType(APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.coords[0]").value(roboticHooverResponse().getCoords()[0]))
        .andExpect(jsonPath("$.coords[1]").value(roboticHooverResponse().getCoords()[1]))
        .andExpect(jsonPath("$.patches").value(roboticHooverResponse().getPatches()));
  }

  private RobotHooverRequest roboticHooverRequest() {
    int[] ROOM_SIZE = {5, 5};
    int[] COORDS = {1, 2};
    int[][] PATCHES = {{1, 0}, {2, 2}, {2, 3}};

    return RobotHooverRequest.builder()
            .instructions("NNESEESWNWW")
            .patchesCoordinate(PATCHES)
            .roomSize(ROOM_SIZE)
            .initialCoordinate(COORDS)
            .build();
  }

  private RobotHooverResponse roboticHooverResponse() {
    int[] COORDS = {1, 3};
    int PATCHES = 1;
    return RobotHooverResponse.builder()
            .coords(COORDS)
            .patches(PATCHES)
            .build();
  }
}