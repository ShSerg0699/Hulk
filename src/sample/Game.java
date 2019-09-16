package sample;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Game {
    private HashMap<KeyCode, Boolean> keys;
    private ArrayList<Block> platforms;
    private ArrayList<Block> bonuses;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;

    private final int BLOCK_SIZE = 45;
    private final int HERO_SIZE = 85;
    private final int ENEMY_SIZE = 65;

    private Pane appRoot;
    private Pane gameRoot;
    private FlowPane statsRoot;

    private Character player;
    private boolean playerSide = true;
    private boolean playerDead = false;


    private int timeCount = 300;
    private int timeFlag = 50;
    private Text score;
    private Text life;
    private Text time;

    private int levelNumber = 0;
    private int levelWidth;


    public Game() {
        this.appRoot = new Pane();
        this.gameRoot = new Pane();
        this.statsRoot = new FlowPane();
        this.player = new Character(this);
        this.keys = new HashMap<>();
        this.platforms = new ArrayList<>();
        this.bonuses = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.score = new Text("SCORE: " + player.getScore());
        this.life = new Text("LIFE: " + player.getScore());
        this.time = new Text("TIME: " + timeToString());
    }

    public ArrayList<Block> getPlatforms() {
        return platforms;
    }

    public ArrayList<Block> getBonuses() {
        return bonuses;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public int getHeroSize() {
        return HERO_SIZE;
    }

    public int getEnemySize() {
        return ENEMY_SIZE;
    }

    public Pane getGameRoot() {
        return gameRoot;
    }

    public int getLevelWidth(){
        return levelWidth;
    }

    public Character getPlayer(){
        return player;
    }

    private void initContent() {
        Image backgroundImg = new Image("bg.jpg");
        ImageView backgroundIV = new ImageView(backgroundImg);

        backgroundIV.setFitHeight(14 * BLOCK_SIZE);
        backgroundIV.setFitWidth(212 * BLOCK_SIZE);

        levelWidth = LevelData.levels[levelNumber][0].length() * BLOCK_SIZE;
        for (int i = 0; i < LevelData.levels[levelNumber].length; i++) {
            String line = LevelData.levels[levelNumber][i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Block platformFloor = new Block(Block.BlockType.PLATFORM, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                          platforms.add(platformFloor);
                          gameRoot.getChildren().add(platformFloor);
                          break;
                    case '2':
                        Block brick = new Block(Block.BlockType.BRICK, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(brick);
                        gameRoot.getChildren().add(brick);
                        break;
                    case '3':
                        Block bonus = new Block(Block.BlockType.BONUS, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        bonuses.add(bonus);
                        gameRoot.getChildren().add(bonus);
                        break;
                    case '4':
                        Block stone = new Block(Block.BlockType.STONE, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(stone);
                        gameRoot.getChildren().add(stone);
                        break;
                    case '5':
                        Block Building_1 = new Block(Block.BlockType.BUILDING_1, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(Building_1);
                        gameRoot.getChildren().add(Building_1);
                        break;
                    case '6':
                        Block Building_2 = new Block(Block.BlockType.BUILDING_2, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(Building_2);
                        gameRoot.getChildren().add(Building_2);
                        break;
                    case '7':
                        Block Building_3 = new Block(Block.BlockType.BUILDING_3, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(Building_3);
                        gameRoot.getChildren().add(Building_3);
                        break;
                    case '8':
                        Block Building_4 = new Block(Block.BlockType.BUILDING_4, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(Building_4);
                        gameRoot.getChildren().add(Building_4);
                        break;
                    case '*':
                        Block InvisibleBlock = new Block(Block.BlockType.INVISIBLE_BLOCK, j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE);
                        platforms.add(InvisibleBlock);
                        gameRoot.getChildren().add(InvisibleBlock);
                        break;
                    case 'E':
                        Enemy enemy = new Enemy(j * BLOCK_SIZE, ENEMY_SIZE);
                        enemies.add(enemy);
                        enemy.setTranslateX(j * BLOCK_SIZE);
                        enemy.setTranslateY((i - 0.4) * BLOCK_SIZE);
                        gameRoot.getChildren().add(enemy);
                }
            }
        }
        player.setTranslateX(0);
        player.setTranslateY(400);
        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
                backgroundIV.setLayoutX(-(offset - 640));
            }
        });
        score.setFill(Color.WHITE);
        score.setFont(Font.font("Arial", FontWeight.BOLD,24));
        life.setFill(Color.WHITE);
        life.setFont(Font.font("Arial", FontWeight.BOLD,24));
        time.setFill(Color.WHITE);
        time.setFont(Font.font("Arial", FontWeight.BOLD,24));
        statsRoot.setHgap(20);
        statsRoot.getChildren().addAll(score, life, time);
        gameRoot.getChildren().addAll(player);
        appRoot.getChildren().addAll(backgroundIV, statsRoot, gameRoot);
    }

    private void updateStatus(Stage primaryStage) {
        if(!Integer.toString(player.getScore()).equals(score.toString())){
            score.setText("SCORE: " + player.getScore());
        }
        if(!player.getStringLife().equals(life.toString())){
            life.setText("Life: " + player.getStringLife());
            if(player.getLife() == 0){
                player.getAnimation().setOffsetX(540);
                player.getAnimation().setOffsetY(255);
                player.getAnimation().stop();
                playerDead = true;
                finishLooseGame(primaryStage);
            }
        }
        if(timeFlag == 0){
            timeFlag = 50;
            timeCount--;
            time.setText("TIME: " + timeToString());
            if(timeCount == 0){
                playerDead = true;
                finishLooseGame(primaryStage);
            }
        }
        timeFlag--;
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            if (Math.abs(player.getTranslateX() - enemy.getTranslateX()) > 6 * BLOCK_SIZE) {
                enemy.setShotDelay(0);
                if (Math.abs(enemy.getTranslateX() - enemy.getMiddle()) > 3 * BLOCK_SIZE) {
                    enemy.reverseSpeed();
                }
                if (enemy.getSpeed() > 0) {
                    enemy.getAnimation().setOffsetY(193);
                } else {
                    enemy.getAnimation().setOffsetY(0);
                }
                enemy.getAnimation().play();
                enemy.move(enemy.getSpeed());
            } else {
                if (player.getTranslateX() - enemy.getTranslateX() > 0) {
                    enemy.getAnimation().setOffsetY(579);
                } else {
                    enemy.getAnimation().setOffsetY(386);
                }
                enemy.getAnimation().play();
                enemy.setShotDelay(enemy.getShotDelay() + 1);
                if (enemy.getShotDelay() == 90) {
                    enemy.setShotDelay(0);
                    Bullet bullet = new Bullet (this, enemy, 16, 4, 3);
                    if (bullet.getSpeed() > 0){
                        bullet.setTranslateX(enemy.getTranslateX() + ENEMY_SIZE);
                        bullet.setTranslateY(enemy.getTranslateY() + 17);
                    } else {
                        bullet.setTranslateX(enemy.getTranslateX() - bullet.getWidth());
                        bullet.setTranslateY(enemy.getTranslateY() + 17);
                    }
                    gameRoot.getChildren().add(bullet);
                }
            }

        }
    }

    private void updateBullets(){
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()){
            Bullet nextBullet = bulletIterator.next();
            nextBullet.moveX(nextBullet.getSpeed());
            if (!nextBullet.getFlagActivity()){
                bulletIterator.remove();
            }
        }
    }

    private void update(Stage primaryStage) {
        if (isPressed(KeyCode.UP) && player.getTranslateY() >= 5) {
            player.jumpPlayer();
        }
        if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5) {
            playerSide = false;
            player.getAnimation().setOffsetY(85);
            player.getAnimation().play();
            player.moveX(-player.getSpeed());
        }
        if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 40 <= levelWidth - 5) {
            playerSide = true;
            player.getAnimation().setOffsetY(0);
            player.getAnimation().play();
            player.moveX(player.getSpeed());
        }
        if (isPressed(KeyCode.SPACE)) {
            Enemy removeEnemy;
            if (playerSide) {
                player.getAnimation().setOffsetY(170);
            } else {
                player.getAnimation().setOffsetY(255);
            }
            player.getAnimation().setCycleCount(1);
            player.getAnimation().play();
            removeEnemy = player.isEnemyKilled();
            if (removeEnemy != null) {
                removeEnemy.die();
                enemies.remove(removeEnemy);
            }
        }


        if (player.getPlayerPoint().getY() < 10) {
            player.setPlayerPoint(player.getPlayerPoint().add(0, 1));
        }
        player.moveY((int) player.getPlayerPoint().getY());

        if (player.getTranslateX() > (levelWidth - BLOCK_SIZE)) {
            playerDead = true;
            finishWinGame(primaryStage);
        }
    }

    private String timeToString(){
        return Integer.toString(timeCount);
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void playNewGame(Stage primaryStage) {
        initContent();
        Scene scene = new Scene(appRoot, 1200, 600);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> {
            keys.put(event.getCode(), false);
             player.getAnimation().stop();
        });
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(primaryStage);
                updateEnemies();
                updateBullets();
                updateStatus(primaryStage);
                if(playerDead){
                    stop();
                }

            }
        };
        timer.start();
    }

    private void finishWinGame(Stage primaryStage){
        VBox endRoot = new VBox();
        GridPane scoreRoot = new GridPane();
        scoreRoot.setPadding(new Insets(10,10,10,10));
        scoreRoot.setHgap(5);
        scoreRoot.setVgap(5);

        Text win = new Text("YOU WIN");
        win.setFill(Color.DARKGREEN);
        win.setFont(Font.font("Verdana", FontWeight.BOLD,52));


        TextField playerName = new TextField();
        playerName.setPromptText("Enter your name");
        GridPane.setConstraints(playerName, 0,0);
        scoreRoot.getChildren().add(playerName);

        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        scoreRoot.getChildren().add(submit);

        int totalScore = timeCount + player.getScore() * 10 + player.getLife() * 100;
        Label score = new Label("Your score: " + Integer.toString(totalScore));
        score.setTextFill(Color.DARKGREEN);
        score.setFont(Font.font("Verdana", FontWeight.BOLD,24));
        endRoot.setTranslateX(500);
        endRoot.setTranslateY(200);
        endRoot.setSpacing(30);
        endRoot.getChildren().addAll(win, score, scoreRoot);
        appRoot.getChildren().addAll(endRoot);

        submit.setOnAction(event -> {
            String name = playerName.getText();
            if(!name.isEmpty()){
                Table table = new Table();
                table.add(name, totalScore);
            }
            Menu menu = new Menu();
            menu.openMenu(primaryStage);
        });


    }

    private void finishLooseGame(Stage primaryStage) {
        /*StackPane endRoot = new StackPane();
        Text loose = new Text("YOU LOSE");
        loose.setFill(Color.DARKGREEN);
        loose.setFont(Font.font("Verdana", FontWeight.BOLD,52));
        endRoot.setAlignment(Pos.CENTER);
        endRoot.getChildren().add(loose);
        appRoot.getChildren().addAll(endRoot);


        Pane root = new Pane();
        Image backgroundImg = new Image("21.jpg");
        ImageView backgroundIV = new ImageView(backgroundImg);
        backgroundIV.setFitHeight(600);
        backgroundIV.setFitWidth(1200);

        MenuItem retry = new MenuItem("RETRY");
        MenuItem exit = new MenuItem("EXIT");
        SubMenu menu = new SubMenu(retry, exit);
        MenuBox menuBox= new MenuBox(menu);

        retry.setOnMouseClicked(event -> {
            Game game = new Game();
            game.playNewGame(primaryStage);
        });
        exit.setOnMouseClicked(event ->System.exit(0));

        root.getChildren().addAll(backgroundIV, menuBox);
        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();*/
        VBox endRoot = new VBox();
        endRoot.setTranslateX(400);
        endRoot.setTranslateY(200);
        endRoot.setSpacing(30);

        Text lose = new Text("YOU LOSE");
        lose.setFill(Color.DARKGREEN);
        lose.setFont(Font.font("Verdana", FontWeight.BOLD,52));
        lose.setTranslateX(50);

        MenuItem retry = new MenuItem("RETRY");
        MenuItem exit = new MenuItem("EXIT");
        SubMenu subMenu = new SubMenu(retry, exit);
        subMenu.setTranslateX(0);
        subMenu.setTranslateY(0);
        retry.setOnMouseClicked(event -> {
            Game game = new Game();
            game.playNewGame(primaryStage);
        });
        exit.setOnMouseClicked(event ->{
            Menu menu = new Menu();
            menu.openMenu(primaryStage);
        });


        endRoot.getChildren().addAll(lose, subMenu);
        appRoot.getChildren().addAll(endRoot);
    }
}
