package com.yoti.robotichover.services;


import com.yoti.robotichover.exception.ValidationException;
import com.yoti.robotichover.model.RobotHoover;
import com.yoti.robotichover.model.RobotHooverInputs;
import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;
import com.yoti.robotichover.validation.RequestValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.List;
import java.util.Set;

@Component
public class RobotHooverProcessor {

  private static final Logger log = LoggerFactory.getLogger(RobotHooverProcessor.class);

  @Autowired
  private RequestValidation requestValidation;

  public RobotHooverResponse process(RobotHooverRequest robotHooverRequest) {

    RobotHooverInputs robotHooverInputs = requestValidation.validateRoboticHoover(robotHooverRequest);

    log.info("Executing direction commands: " + robotHooverInputs.getDirectionCommands());

    RobotHoover robotHoover = RobotHoover.builder().build();
    robotHoover.setHooverPosition(robotHooverInputs.getHooverInitialPosition());
    robotHoover.setCountStain(0);

    Set<Point> stains = robotHooverInputs.getPatchesPositions();
    List<Character> characters = robotHooverInputs.getDirectionCommands();
    Point roomSize = robotHooverInputs.getRoomSize();

    characters.forEach(command -> {
          Point directionPoint = commandInstruction(robotHoover.getHooverPosition(), roomSize, command);
          robotHoover.setHooverPosition(directionPoint);

          if (isStainAHooverPosition(robotHoover.getHooverPosition(), stains)) {
            robotHoover.setCountStain(robotHoover.getCountStain() + 1);
            stains.remove(robotHoover.getHooverPosition());
          }
        });

    log.info("Process completed");

    return RobotHooverResponse.builder()
        .coords(
            new int[]{robotHoover.getHooverPosition().x, robotHoover.getHooverPosition().y})
        .patches(robotHoover.getCountStain())
        .build();
  }

  private Point commandInstruction(Point hooverPosition, Point roomSize, Character command) {
    switch (Character.toUpperCase(command)) {
      case 'N':
        return moveNorth(roomSize, hooverPosition);
      case 'S':
        return moveSouth(hooverPosition);
      case 'E':
        return moveEast(roomSize, hooverPosition);
      case 'W':
        return moveWest(hooverPosition);
      default:
        log.error("Not valid direction command: " + command);
    }
    throw new ValidationException("Inserted not a valid instruction command");
  }

  private Point moveNorth(@NotNull Point roomSize, @NotNull Point currentPosition) {
    int upperRoomEdge = roomSize.y;
    if (currentPosition.y < upperRoomEdge) {
      return new Point(currentPosition.x, currentPosition.y + 1);
    }
    return currentPosition;
  }

  private Point moveSouth(@NotNull Point currentPosition) {
    if (currentPosition.y > 0) {
      return new Point(currentPosition.x, currentPosition.y - 1);
    }
    return currentPosition;
  }

  private Point moveEast(@NotNull Point roomSize, @NotNull Point currentPosition) {
    int easternRoomEdge = roomSize.x;
    if (currentPosition.x < easternRoomEdge) {
      return new Point(currentPosition.x + 1, currentPosition.y);
    }
    return currentPosition;
  }

  private Point moveWest(@NotNull Point currentPosition) {
    if (currentPosition.x > 0) {
      return new Point(currentPosition.x - 1, currentPosition.y);
    }
    return currentPosition;
  }

  private boolean isStainAHooverPosition(Point hooverInitialPosition, Set<Point> patchesPosition) {
    return patchesPosition.stream()
            .anyMatch(stain -> stain.equals(hooverInitialPosition));
  }

}
