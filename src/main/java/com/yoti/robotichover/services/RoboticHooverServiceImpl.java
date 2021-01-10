package com.yoti.robotichover.services;

import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoboticHooverServiceImpl implements RoboticHooverService {

  private static final Logger log = LoggerFactory.getLogger(RoboticHooverServiceImpl.class);

  @Autowired
  private RobotHooverProcessor robotHooverProcessor;

  @Override
  public RobotHooverResponse runningHoover(RobotHooverRequest robotHooverRequest) {
    log.info("New Robot Hoover Request Started");
    return robotHooverProcessor.process(robotHooverRequest);
  }
}
