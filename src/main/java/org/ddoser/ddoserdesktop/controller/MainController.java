package org.ddoser.ddoserdesktop.controller;

import static org.ddoser.ddoserdesktop.algorithm.DDOSAlgorithm.startDDOS;
import static org.ddoser.ddoserdesktop.algorithm.DDOSAlgorithm.stopSend;
import static org.ddoser.ddoserdesktop.util.AlertService.showAlert;
import static org.ddoser.ddoserdesktop.validation.DataChecker.isUrlValid;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import org.ddoser.ddoserdesktop.context.ApplicationContext;
import org.ddoser.ddoserdesktop.context.page.MainPageContext;
import org.ddoser.ddoserdesktop.validation.ErrorObject;

import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Контроллер главной страницы
 */
public class MainController {

  /**
   * Поле для ввода количества потоков
   */
  @FXML
  private ComboBox<Integer> threadCountComboBox;
  /**
   * Поле для ввода цели ДДОС'а
   */
  @FXML
  private TextField targetField;
  /**
   * Кнопка начала ДДОС'а
   */
  @FXML
  private Button startButton;
  /**
   * Кнопка окончания ДДОС'а
   */
  @FXML
  private Button stopButton;
  /**
   * График времени отклика сайта
   */
  @FXML
  private LineChart<Number, Number> ddosChart;
  @FXML
  private LineChart<Number, Number> memoryChart;
  @FXML
  private LineChart<Number, Number> cpuChart;

  /**
   * Инициализация параметров шаблона
   */
  @FXML
  public void initialize() {
    ddosChart.getData().add(new XYChart.Series<>());
    memoryChart.getData().add(new XYChart.Series<>());
    cpuChart.getData().add(new XYChart.Series<>());
    ApplicationContext.mainPageContext(
      new MainPageContext(ddosChart, memoryChart, cpuChart, startButton, stopButton));
    threadCountComboBox.getItems().addAll(
      IntStream.rangeClosed(1, Runtime.getRuntime().availableProcessors())
        .boxed().toList());
    styleAxis(ddosChart);
    styleAxis(memoryChart);
    styleAxis(cpuChart);
    styleChartTitle(ddosChart);
    styleChartTitle(memoryChart);
    styleChartTitle(cpuChart);
  }

  /**
   * Обработка события клика по кнопке начала ДДОС'а
   */
  @FXML
  private void handleStartButton() {
    ErrorObject urlValid = isUrlValid(targetField.getText());
    if (Boolean.FALSE.equals(urlValid.getCorrect())) {
      showAlert(Alert.AlertType.ERROR, "Ошибка", urlValid.getErrorMessage());
      return;
    }
    Integer virtualThreadsCount = threadCountComboBox.getValue();
    if (virtualThreadsCount == null) {
      showAlert(Alert.AlertType.ERROR, "Ошибка",
        "Поле 'количество потоков' должно быть заполнено");
      return;
    }

    startButton.setDisable(true);
    stopButton.setDisable(false);
    String target = targetField.getText();
    CompletableFuture.runAsync(() -> startDDOS(virtualThreadsCount, target));
  }

  /**
   * Обработка события клика по кнопке остановки ДДОС'а
   */
  @FXML
  private void handleStopButton() {
    ApplicationContext.mainPageContext().stopDDOSState();
    stopSend();
  }

  /**
   * Добавить стили для осей координат графика
   *
   * @param chart график
   */
  private void styleAxis(LineChart<Number, Number> chart) {
    for (Axis<Number> axis : List.of(chart.getXAxis(), chart.getYAxis())) {
      axis.setTickLabelFill(Color.WHITE);
      axis.lookupAll(".axis-label")
        .forEach(node -> ((Label) node).setTextFill(Color.WHITE));

      axis.setTickMarkVisible(false);
    }
  }

  /**
   * Добавить стили для заголовка графика
   *
   * @param chart график
   */
  private void styleChartTitle(LineChart<Number, Number> chart) {
    chart.lookupAll(".chart-title")
      .forEach(node -> ((Label) node).setTextFill(Color.WHITE));
  }
}
