package org.ddoser.ddoserdesktop.context.page;

import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;

/**
 * Контекст главной страницы
 *
 * @param ddosChart   график ДДОС'а
 * @param memoryChart график потребления памяти
 * @param cpuChart    график нагрузки CPU
 * @param startButton кнопка начала ДДОС'а
 * @param stopButton  кнопка окончания ДДОС'а
 */
public record MainPageContext(LineChart<Number, Number> ddosChart,
                              LineChart<Number, Number> memoryChart,
                              LineChart<Number, Number> cpuChart,
                              Button startButton,
                              Button stopButton) {

  /**
   * Перевести экран в состояние остановки ДДОС'а
   */
  public void stopDDOSState() {
    startButton.setDisable(false);
    stopButton.setDisable(true);
  }
}
