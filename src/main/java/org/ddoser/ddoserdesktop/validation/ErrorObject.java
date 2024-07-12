package org.ddoser.ddoserdesktop.validation;

/**
 * Информация о валидации
 */
public class ErrorObject {

  /**
   * Признак прохождения проверки
   */
  private final boolean isCorrect;
  /**
   * Сообщение об ошибке
   */
  private final String errorMessage;

  /**
   * Конструктор
   */
  private ErrorObject(Boolean isCorrect, String errorMessage) {
    this.isCorrect = isCorrect;
    this.errorMessage = errorMessage;
  }

  /**
   * Создать объект успешной проверки
   * @return объект успешной проверки
   */
  public static ErrorObject valid() {
    return new ErrorObject(true, null);
  }

  /**
   * Создать объект проваленной проверки
   * @return объект проваленной проверки
   */
  public static ErrorObject invalid(String errorMessage) {
    return new ErrorObject(false, errorMessage);
  }

  /**
   * Получить признак корректности проверки
   * @return признак корректности проверки
   */
  public boolean getCorrect() {
    return isCorrect;
  }

  /**
   * Получить сообщение об ошибке
   * @return сообщение об ошибке
   */
  public String getErrorMessage() {
    return errorMessage;
  }
}
