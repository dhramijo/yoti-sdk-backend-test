package com.yoti.robotichover.services;

import com.yoti.robotichover.exception.ValidationException;
import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;
import com.yoti.robotichover.validation.RequestValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoboticHooverServiceImplTest {

  @Mock private RobotHooverProcessor robotHooverProcessor;

  @InjectMocks private RoboticHooverServiceImpl roboticHooverService;

  @Test
  void testShouldReturnRoboticHooverResponse() {

    RobotHooverRequest robotHooverRequest = buildRobotHooverRequest();
    RobotHooverResponse roboticHooverResponse = buildRobotHooverResponse();

    when(robotHooverProcessor.process(robotHooverRequest)).thenReturn(roboticHooverResponse);

    assertThatCode(() -> {
      RobotHooverResponse response = roboticHooverService.runningHoover(robotHooverRequest);
        assertEquals(1, response.getPatches());
        assertThat(response.getCoords().equals(new int[]{1, 3}));
    }).doesNotThrowAnyException();
  }


  @Test
  void testShouldThrowException() {

    RobotHooverRequest robotHooverRequest = buildRobotHooverRequest();
    robotHooverRequest.setRoomSize(new int[]{-5, 5});

    doThrow(new ValidationException("Room size is greater than 0 and the input value must be in [x,y] format"))
            .when(robotHooverProcessor).process(robotHooverRequest);

    ValidationException exceptionThrown = assertThrows(
            ValidationException.class, () -> roboticHooverService.runningHoover(robotHooverRequest));

    assertThat(exceptionThrown.getMessage()).isEqualTo("Room size is greater than 0 and the input value must be in [x,y] format");
  }

    private RobotHooverRequest buildRobotHooverRequest() {
        return RobotHooverRequest.builder()
                .roomSize(new int[]{5, 5})
                .initialCoordinate(new int[]{1,2})
                .patchesCoordinate(new int[][]{{1, 0}, {2, 2}, {2, 3}})
                .instructions("NNESEESWNWW")
                .build();
    }

    private RobotHooverResponse buildRobotHooverResponse() {
        return RobotHooverResponse.builder()
                .coords(new int[]{1, 3})
                .patches(1)
                .build();
    }
}