package com.yoti.robotichover.services;


import com.yoti.robotichover.model.RobotHooverInputs;
import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;
import com.yoti.robotichover.validation.RequestValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RobotHooverProcessorTest {

  @Mock private RequestValidation requestValidation;

  @InjectMocks private RobotHooverProcessor robotHooverProcessor;

  @Test
  void testProcessRobotHoverRequest() {

    RobotHooverRequest robotHooverRequest = buildRobotHooverRequest();

    when(requestValidation.validateRoboticHoover(robotHooverRequest)).thenReturn(buildRobotHooverInputs());

    assertThatCode(() -> {
      RobotHooverResponse robotHooverResponse = robotHooverProcessor.process(robotHooverRequest);
      assertEquals(1, robotHooverResponse.getPatches());
      assertThat(robotHooverResponse.getCoords().equals(new int[]{1, 3}));
    }).doesNotThrowAnyException();

  }

   private RobotHooverRequest buildRobotHooverRequest() {
       return RobotHooverRequest.builder()
               .roomSize(new int[]{5, 5})
               .initialCoordinate(new int[]{1,2})
               .patchesCoordinate(new int[][]{{1, 0}, {2, 2}, {2, 3}})
               .instructions("NNESEESWNWW")
               .build();
   }

    private RobotHooverInputs buildRobotHooverInputs() {
        return RobotHooverInputs.builder()
                .roomSize(new Point(5,5))
                .hooverInitialPosition(new Point(1, 2))
                .patchesPositions(patches())
                .directionCommands(Arrays.asList('N', 'N', 'E', 'S', 'E', 'E', 'S', 'W', 'N', 'W', 'W'))
                .build();
    }

    private Set<Point> patches() {
        Set<Point> patchesSet = new HashSet<>();
        int[][] PATCHES = {{1, 0}, {2, 2}, {2, 3}};
        for (int[] patch : PATCHES) {
            patchesSet.add(new Point(patch[0], patch[1]));
        }
        return patchesSet;
    }
}
