package com.meemaw.test.testconainers.kafka;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class KafkaTestExtension implements BeforeAllCallback {

  private static final KafkaTestContainer KAFKA = KafkaTestContainer.newInstance();

  public static KafkaTestContainer getInstance() {
    return KAFKA;
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    start(KAFKA).forEach(System::setProperty);
  }

  public static Map<String, String> start() {
    return start(KAFKA);
  }

  public static Map<String, String> start(KafkaTestContainer kafka) {
    if (!KAFKA.isRunning()) {
      System.out.println("[TEST-SETUP]: Starting kafka container ...");
      kafka.start();
      kafka.createTopics();
    }
    String bootstrapServers = kafka.getBootstrapServers();
    System.out.println(
        String.format("[TEST-SETUP]: Connecting to kafka bootstrap.servers=%s", bootstrapServers));
    return Collections.singletonMap("kafka.bootstrap.servers", bootstrapServers);
  }

  public static void stop() {
    KAFKA.stop();
  }
}
