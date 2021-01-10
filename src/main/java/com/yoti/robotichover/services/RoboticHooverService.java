package com.yoti.robotichover.services;

import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;

public interface RoboticHooverService {

  RobotHooverResponse runningHoover(RobotHooverRequest robotHooverRequest);

}
