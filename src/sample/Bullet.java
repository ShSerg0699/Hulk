package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Bullet extends Pane {
    private Enemy enemy;
    private int width;
    private int height;
    private int speed;
    private boolean flagActivity;
    private Game game;

    public Bullet(Game game, Enemy enemy, int width, int height, int speed) {
        this.game = game;
        this.enemy = enemy;
        this.width = width;
        this.height = height;
        this.speed = - speed;
        flagActivity = true;
        Image bulletImg = new Image("block.png");
        ImageView bullet = new ImageView(bulletImg);
        bullet.setFitHeight(height);
        bullet.setFitWidth(width);
        if (this.game.getPlayer().getTranslateX() - this.enemy.getTranslateX() > 0) {
            bullet.setViewport(new Rectangle2D(420, 200, 22, 9));
            reverseSpeed();
        } else {
            bullet.setViewport(new Rectangle2D(420, 190, 22, 9));
        }
        this.game.getBullets().add(this);
        getChildren().add(bullet);
    }

    public void moveX(int x) {
        boolean moveRight = x > 0;
        for (int i = 0; i < Math.abs(x); i++) {
            for (Block block : game.getPlatforms()) {
                if (this.getBoundsInParent().intersects(block.getBoundsInParent())) {
                    if (moveRight) {
                        if (this.getTranslateX() + width == block.getTranslateX()) {
                            flagActivity = false;
                            game.getGameRoot().getChildren().remove(this);
                            return;
                        }
                    } else {
                        if (this.getTranslateX() == block.getTranslateX() + game.getBlockSize()) {
                            flagActivity = false;
                            game.getGameRoot().getChildren().remove(this);
                            return;
                        }
                    }
                }
            }
            if ((this.getTranslateX() <= 0) || (this.getTranslateX() == game.getLevelWidth())) {
                flagActivity = false;
                game.getGameRoot().getChildren().remove(this);
                return;
            }
            this.setTranslateX(this.getTranslateX() + (moveRight ? 1 : -1));
        }
    }

    public int getSpeed() {
        return speed;
    }

    public boolean getFlagActivity() {
        return flagActivity;
    }

    public void reverseSpeed() {
        speed *= -1;
    }
}
