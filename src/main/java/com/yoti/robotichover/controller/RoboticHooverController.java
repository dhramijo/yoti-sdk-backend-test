package com.yoti.robotichover.controller;


import com.yoti.robotichover.model.RobotHooverRequest;
import com.yoti.robotichover.model.RobotHooverResponse;
import com.yoti.robotichover.services.RoboticHooverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoboticHooverController {

  @Autowired
  public RoboticHooverService roboticHooverService;

  @PostMapping(value = "/api/v1/clean")
  public HttpEntity<RobotHooverResponse> roboticHooverResponse(@RequestBody RobotHooverRequest robotHooverRequest) {
    return ResponseEntity.ok().body(roboticHooverService.runningHoover(robotHooverRequest));
  }
}
