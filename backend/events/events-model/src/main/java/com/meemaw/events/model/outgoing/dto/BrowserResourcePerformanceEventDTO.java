package com.meemaw.events.model.outgoing.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class BrowserResourcePerformanceEventDTO extends AbstractBrowserEventDTO {

  String name;
  double startTime;
  double duration;
  String initiatorType;
  String nextHopProtocol;
}
