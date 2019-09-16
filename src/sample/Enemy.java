package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Enemy extends Pane {

    private SpriteAnimation animation;
    private int speed = -2;
    private int middle;
    private int shotDelay = 0;

    public Enemy(int middle, int enemySize) {
        Image solderImg = new Image("solder.png");
        ImageView solder = new ImageView(solderImg);
        int count = 4;
        int column = 4;
        int offsetX = 0;
        int offsetY = 0;
        int width = 160;
        int height = 193;
        solder.setFitHeight(enemySize);
        solder.setFitWidth(enemySize);
        solder.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(
                solder,
                Duration.millis(1800),
                count, column,
                offsetX, offsetY,
                width, height);
        this.middle = middle;
        getChildren().add(solder);
    }

    public void move(int x) {
        boolean moveRight = x > 0;
        for (int i = 0; i < Math.abs(x); i++) {
            this.setTranslateX(this.getTranslateX() + (moveRight ? 1 : -1));
        }
    }

    public void die(){
        animation.setOffsetY(772);
        animation.setCycleCount(1);
        animation.setDelay(Duration.millis(500));
        animation.play();
    }

    public int getSpeed() {
        return speed;
    }

    public void reverseSpeed() {
        speed *= -1;
    }

    public int getMiddle() {
        return middle;
    }

    public int getShotDelay(){
        return shotDelay;
    }

    public void setShotDelay(int x) {
        shotDelay = x;
    }

    public SpriteAnimation getAnimation() {
        return animation;
    }
}
