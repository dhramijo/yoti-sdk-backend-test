package com.yoti.robotichover.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.awt.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RobotHoover {

  private int countStain;
  private Point hooverPosition;
}
