package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Character extends Pane {
    private SpriteAnimation animation;
    private int speed = 7;
    private Point2D playerPoint = new Point2D(0, 0);
    private boolean canJump = true;
    private int score = 0;
    private int life = 3;
    private Game game;

    public Character(Game game) {
        Image heroImg = new Image("hulk.png");
        ImageView imageView = new ImageView(heroImg);
        int count = 6;
        int column = 6;
        int offsetX = 0;
        int offsetY = 0;
        int width = 90;
        int height = 85;
        imageView.setFitHeight(85);
        imageView.setFitWidth(85);
        imageView.setViewport(new Rectangle2D(offsetX, offsetY, width, height));
        animation = new SpriteAnimation(
                imageView,
                Duration.millis(1000),
                count, column,
                offsetX, offsetY,
                width, height);
        this.game = game;
        getChildren().addAll(imageView);
    }

    public void moveX(int x) {

        boolean moveRight = x > 0;
        for (int i = 0; i < Math.abs(x); i++) {
            for (Block platform : game.getPlatforms()) {
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (moveRight) {
                        if (this.getTranslateX() + game.getHeroSize() == platform.getTranslateX()) {
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else {
                        if (this.getTranslateX() == platform.getTranslateX() + game.getBlockSize()) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
                for (Enemy enemy : game.getEnemies()) {
                    if (this.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        if (moveRight) {
                            if (this.getTranslateX() + game.getHeroSize() == enemy.getTranslateX()) {
                                this.setTranslateX(this.getTranslateX());
                                return;
                            }
                        } else {
                            if (this.getTranslateX() == enemy.getTranslateX() + game.getEnemySize()) {
                                this.setTranslateX(this.getTranslateX());
                                return;
                            }
                    }
                    }
                }
            }
            this.setTranslateX(this.getTranslateX() + (moveRight ? 1 : -1));
            isBonusEat();
            isBulletHit();
        }
    }

    public void moveY(int y) {
        boolean moveDown = y > 0;
        for (int i = 0; i < Math.abs(y); i++) {
            for (Block platform : game.getPlatforms()) {
                if (getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (moveDown) {
                        if (this.getTranslateY() + game.getHeroSize() == platform.getTranslateY()) {
                            this.setTranslateY(this.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    } else {
                        if (this.getTranslateY() == platform.getTranslateY() + game.getBlockSize()) {
                            this.setTranslateY(this.getTranslateY() + 1);
                            playerPoint = new Point2D(0, 10);
                            return;
                        }
                    }
                }
            }
            this.setTranslateY(getTranslateY() + (moveDown ? 1 : -1));
            if (this.getTranslateY() > 640) {
                this.setTranslateX(0);
                this.setTranslateY(400);
                life--;

                game.getGameRoot().setLayoutX(0);
            }
            isBonusEat();
            isBulletHit();
        }
    }

    public void jumpPlayer() {
        if (canJump) {
            playerPoint = playerPoint.add(0, -30);
            canJump = false;
        }
    }

    public void isBonusEat() {
        Block removeBonus = null;
        for (Block bonus : game.getBonuses()) {
            if (this.getBoundsInParent().intersects(bonus.getBoundsInParent())) {
                removeBonus = bonus;
                score++;
            }
        }
        game.getBonuses().remove(removeBonus);
        game.getGameRoot().getChildren().remove(removeBonus);
    }

    public Enemy isEnemyKilled() {
        Enemy removeEnemy = null;
        for (Enemy enemy : game.getEnemies()) {
            if (this.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                removeEnemy = enemy;
                score += 10;
            }
        }
        return removeEnemy;
    }

    public void isBulletHit() {
        Bullet removeBullet = null;
        for (Bullet bullet : game.getBullets()){
            if (this.getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                removeBullet = bullet;
                life--;
            }
        }
        game.getBullets().remove(removeBullet);
        game.getGameRoot().getChildren().remove(removeBullet);
    }

    public int getSpeed() {
        return speed;
    }

    public int getScore() {
        return score;
    }

    public String getStringLife() {
        return Integer.toString(life);
    }

    public int getLife() {
        return life;
    }

    public SpriteAnimation getAnimation() {
        return animation;
    }

    public Point2D getPlayerPoint() {
        return playerPoint;
    }

    public void setPlayerPoint(Point2D playerPoint) {
        this.playerPoint = playerPoint;
    }
}