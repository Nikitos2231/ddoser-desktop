package org.ddoser.ddoserdesktop.algorithm;

import java.util.concurrent.atomic.AtomicInteger;

import org.ddoser.ddoserdesktop.context.ApplicationContext;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.util.Duration;

/**
 * Алгоритм заполнения графика
 */
public class LineChartAlgorithm {

  /**
   * Максимальное количество точек на графике
   */
  private static final int MAX_DATA_POINTS = 50;
  /**
   * Кратность количества запросов
   */
  private static final int MULTIPLICITY_REQUEST = 50;
  /**
   * Количество превышения лимитов
   */
  private static final int LIMIT_EXCEEDED = 20;
  /**
   * Фиксированный сдвиг оси
   */
  private static final int FIXED_SHIFT = 1000;
  /**
   * Счетчик превышений лимитов
   */
  private static final AtomicInteger COUNT_LIMITED = new AtomicInteger(0);

  /**
   * Конструктор
   */
  private LineChartAlgorithm() {
  }

  /**
   * Добавить точку на график
   *
   * @param duration      длительность запроса
   * @param requestNumber номер запроса
   */
  public static void addDataToDdosSeries(long duration, int requestNumber) {
    Platform.runLater(() -> {
      // Если кратность задачи не равна заданной кратности - пропускаем добавление точки на график
      if (requestNumber % MULTIPLICITY_REQUEST != 0) {
        return;
      }
      LineChart<Number, Number> ddosLineChart =
        ApplicationContext.mainPageContext().ddosChart();

      XYChart.Series<Number, Number> series =
        ApplicationContext.mainPageContext().ddosChart().getData().getFirst();
      // Если точек больше, чем заданное максимальное количество - удаляем первую точку
      if (series.getData().size() >= MAX_DATA_POINTS) {
        series.getData().removeFirst();
        COUNT_LIMITED.incrementAndGet();
      }

      series.getData().add(new Data<>(requestNumber, duration));

      // Если превышен лимит точек заданное количество раз, то сдвигаем ось x
      if (COUNT_LIMITED.get() == LIMIT_EXCEEDED) {
        NumberAxis xAxis =
          (NumberAxis) ddosLineChart.getXAxis();
        xAxis.setAutoRanging(false);

        Data<Number, Number> firstData = series.getData().getFirst();
        Data<Number, Number> lastData = series.getData().getLast();

        xAxis.setLowerBound(firstData.getXValue().doubleValue());
        xAxis.setUpperBound(lastData.getXValue().doubleValue() + FIXED_SHIFT);

        final double currentLowerBound = xAxis.getLowerBound();
        final double currentUpperBound = xAxis.getUpperBound();

        final Timeline timeline = new Timeline();
        final KeyFrame keyFrame = new KeyFrame(
          Duration.seconds(2),
          new KeyValue(xAxis.lowerBoundProperty(), currentLowerBound),
          new KeyValue(xAxis.upperBoundProperty(), currentUpperBound)
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        COUNT_LIMITED.set(0);
      }
    });
  }
}
