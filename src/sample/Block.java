package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Block extends Pane {

    public enum BlockType {
        PLATFORM, BRICK, BONUS, INVISIBLE_BLOCK, STONE, BUILDING_1, BUILDING_2, BUILDING_3, BUILDING_4
    }


    public Block(BlockType blockType, int x, int y, int blockSize) {
        Image blocksImg = new Image("block.png");
        ImageView block = new ImageView(blocksImg);
        block.setFitWidth(blockSize);
        block.setFitHeight(blockSize);
        setTranslateX(x);
        setTranslateY(y);

        switch (blockType) {
            case PLATFORM:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                break;
            case BRICK:
                block.setViewport(new Rectangle2D(16, 0, 16, 16));
                break;
            case BONUS:
                block.setViewport(new Rectangle2D(420, 130, 60, 60));
                break;
            case INVISIBLE_BLOCK:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                block.setOpacity(0);
                break;
            case STONE:
                block.setViewport(new Rectangle2D(0, 16, 16, 16));
                break;
            case BUILDING_1:
                block.setViewport(new Rectangle2D(0, 320, 156, 98));
                block.setFitWidth(blockSize * 8);
                block. setFitHeight(blockSize * 4);
                break;
            case BUILDING_2:
                block.setViewport(new Rectangle2D(160, 320, 86, 82));
                block.setFitWidth(blockSize * 4);
                block. setFitHeight(blockSize * 3);
                break;
            case BUILDING_3:
                block.setViewport(new Rectangle2D(250, 320, 117, 111));
                block.setFitWidth(blockSize * 6);
                block. setFitHeight(blockSize * 5);
                break;
            case BUILDING_4:
                block.setViewport(new Rectangle2D(370, 320, 48, 91));
                block.setFitWidth(blockSize * 2);
                block. setFitHeight(blockSize * 4);
                break;
        }
        getChildren().add(block);
    }
}
