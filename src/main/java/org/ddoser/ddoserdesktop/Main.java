package org.ddoser.ddoserdesktop;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Главный класс для запуска приложения
 */
public class Main extends Application {

  /**
   * Загрузка параметров в приложение
   *
   * @param args параметры приложения
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Загрузить первоначальную страницу
   *
   * @param stage главный контейнер в шаблоне
   * @throws IOException в случае не найденного файла шаблона в resources
   */
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1100, 800);
    stage.setScene(scene);
    stage.setTitle("Главная");
    stage.show();
  }
}
