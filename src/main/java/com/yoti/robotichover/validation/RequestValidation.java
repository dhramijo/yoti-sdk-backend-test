package com.yoti.robotichover.validation;

import com.yoti.robotichover.exception.ValidationException;
import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverInputs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class RequestValidation {

  private static final Logger log = LoggerFactory.getLogger(RequestValidation.class);

  private static final String INSTRUCTIONS = "[^NSEW]";

  public RobotHooverInputs validateRoboticHoover(RobotHooverRequest robotHooverRequest) {
    log.info("Validating a new Robotic Hoover request");

    final int[] roomSize = robotHooverRequest.getRoomSize();
    final int[] initialCoordinate = robotHooverRequest.getInitialCoordinate();
    validRoomSize(roomSize);
    validateInitialPosition(initialCoordinate, roomSize);

    final int[][] patches = robotHooverRequest.getPatchesCoordinate();
    final Set<Point> patchesInput = validatePatches(patches, roomSize);
    final List<Character> directionCommands = validateInstructions(robotHooverRequest.getInstructions());

    return RobotHooverInputs.builder()
        .roomSize(new Point(roomSize[0], roomSize[1]))
        .hooverInitialPosition(new Point(initialCoordinate[0], initialCoordinate[1]))
        .patchesPositions(patchesInput)
        .directionCommands(directionCommands)
        .build();
  }

  private void validRoomSize(int[] roomSize) {
    log.info("Validating the room size");
    if (roomSize.length == 0 || roomSize[0] < 1 || roomSize[1] < 1) {
      throw new ValidationException("Room size is greater than 0 and the input value must be in [x,y] format");
    }
  }

  private void validateInitialPosition(int[] coordinate, int[] roomSize) {
    log.info("Validating the initial size position");
    if (coordinate.length != 2 || coordinate[0] > roomSize[0]
        || coordinate[1] > roomSize[1] || coordinate[0] < 0 || coordinate[1] < 0) {
      throw new ValidationException("The initial position is greater than 0, input value must be in [x,y] format and inside the room");
    }
  }

  protected Set<Point> validatePatches(int[][] patches, int[] roomSize) {
    log.info("Validating the patches");
    if (null != patches) {
      Set<Point> patchesSet = new HashSet<>();
      for (int[] patch : patches) {
        if (patch.length == 2) {
          int currentX = patch[0];
          int currentY = patch[1];
          if (currentX < roomSize[0] && currentY < roomSize[1]) {
            patchesSet.add(new Point(currentX, currentY));
          }
        }
      }
      return patchesSet;
    }
    throw new ValidationException("Patches input must be in [x,y] format");
  }

  protected List<Character> validateInstructions(String instructions) {
    log.info("Validating the instructions");
    Pattern pattern = Pattern.compile(INSTRUCTIONS);
    boolean patternMatcher = pattern.matcher(instructions.toUpperCase()).find();
    List<Character> instructionsList;
    if (!patternMatcher && !instructions.isEmpty()) {
      instructionsList = instructions.toUpperCase().chars()
          .mapToObj(e -> (char) e)
          .collect(Collectors.toList());
    } else {
      throw new ValidationException("Instructions must be a sequence of [N, S, E, W] commands");
    }
    return instructionsList;
  }
}
