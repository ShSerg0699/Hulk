package sample;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MenuBox extends Pane {
    VBox subMenu;
    public MenuBox(VBox subMenu){
        this.subMenu = subMenu;

        Rectangle bg = new Rectangle(1200,800, Color.LIGHTBLUE);
        bg.setOpacity(0.4);
        getChildren().addAll(bg,subMenu);
    }
    public void setSubMenu(VBox subMenu){
        getChildren().remove(this.subMenu);
        this.subMenu = subMenu;
        getChildren().add(this.subMenu);
    }
}