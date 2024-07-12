package org.ddoser.ddoserdesktop.util;

import javafx.scene.control.Alert;

/**
 * Сервис создания уведомлений
 */
public class AlertService {

  /**
   * Конструктор
   */
  private AlertService() {
  }

  /**
   * Создать уведомление
   *
   * @param alertType тип уведомления
   * @param title     заголовок
   * @param message   сообщение
   */
  public static void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(message);
    alert.showAndWait();
  }
}
