package com.yoti.robotichover.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RobotHooverRequest {

  private int[] roomSize;

  @JsonProperty("coords")
  private int[] initialCoordinate;

  @JsonProperty(value = "patches")
  private int[][] patchesCoordinate;

  private String instructions;

}
