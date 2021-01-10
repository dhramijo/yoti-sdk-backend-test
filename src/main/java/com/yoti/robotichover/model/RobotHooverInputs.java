package com.yoti.robotichover.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class RobotHooverInputs {

  private final Point roomSize;
  private final Point hooverInitialPosition;
  private final Set<Point> patchesPositions;
  private final List<Character> directionCommands;
}
