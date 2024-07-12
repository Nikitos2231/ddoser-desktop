package org.ddoser.ddoserdesktop.context;

import org.ddoser.ddoserdesktop.context.page.MainPageContext;

/**
 * Контекст приложения
 */
public class ApplicationContext {

  /**
   * Контекст главной страницы
   */
  private static MainPageContext mainPageContext = null;

  /**
   * Конструктор
   */
  private ApplicationContext() {
  }

  /**
   * Получить контекст главной страницы
   */
  public static MainPageContext mainPageContext() {
    return mainPageContext;
  }

  /**
   * Установить контекст главной страницы
   */
  public static void mainPageContext(MainPageContext context) {
    mainPageContext = context;
  }
}
