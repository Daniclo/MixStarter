module org.daniclo.mixstarter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.daniclo.mixstarter to javafx.fxml;
    exports org.daniclo.mixstarter;
    exports org.daniclo.mixstarter.controller;
    opens org.daniclo.mixstarter.controller to javafx.fxml;
}