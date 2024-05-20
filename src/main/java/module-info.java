module org.daniclo.mixstarter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.hibernate.orm.core;
    requires java.logging;
    requires jakarta.persistence;
    requires static lombok;

    opens org.daniclo.mixstarter to javafx.fxml;
    exports org.daniclo.mixstarter;
    exports org.daniclo.mixstarter.controller;
    opens org.daniclo.mixstarter.controller to javafx.fxml;
}