module org.ddoser.ddoserdesktop {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.kordamp.bootstrapfx.core;
  requires spring.web;
  requires spring.jcl;
  requires spring.core;
  requires org.slf4j;
  requires java.management;

  opens org.ddoser.ddoserdesktop to javafx.fxml;
  exports org.ddoser.ddoserdesktop;
    exports org.ddoser.ddoserdesktop.controller;
    opens org.ddoser.ddoserdesktop.controller to javafx.fxml;
}
