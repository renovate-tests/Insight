package com.rebrowse;

import java.time.Duration;

public final class Rebrowse {

  public static final Duration TIMEOUT = Duration.ofSeconds(30);

  public static final String API_BASE = "https://api.rebrowse.io";
  public static final String VERSION = "1.0.0";

  private static volatile int maxNetworkRetries = 0;

  public static volatile String apiKey;

  private Rebrowse() {}

  /**
   * Sets the maximum number of times requests will be retried before giving up.
   *
   * @param numRetries the maximum number of times requests will be retried before giving up.
   */
  public static synchronized void maxNetworkRetries(int numRetries) {
    maxNetworkRetries = numRetries;
  }

  public static int maxNetworkRetries() {
    return maxNetworkRetries;
  }
}