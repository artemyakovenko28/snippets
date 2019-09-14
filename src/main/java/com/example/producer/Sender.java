package com.example.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.*;

public class Sender {

  private static int threadCounter = 0;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(Sender.class);

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void send(String payload) {
    LOGGER.info("sending payload='{}'", payload);
    kafkaTemplate.send("sender.t", payload);
    ExecutorService executor = Executors.newFixedThreadPool(10, r -> {
      Thread thread = new Thread(r);
      thread.setName("worker-" + (threadCounter++));
      thread.setDaemon(true);
    });

    CompletableFuture.runAsync(() -> {
      System.out.println("Hello world");
    });
  }
}
