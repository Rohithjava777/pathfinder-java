module source.cspathfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens source.cspathfinder to javafx.fxml;
    exports source.cspathfinder;
}