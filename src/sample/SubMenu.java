package sample;

import javafx.scene.layout.VBox;

public class SubMenu extends VBox {
    public SubMenu(MenuItem...items){
        setSpacing(30);
        setTranslateX(400);
        setTranslateY(250);
        for(MenuItem item : items){
            getChildren().addAll(item);
        }
    }
}
