package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Table {

    public static void makeTable(GridPane root) {

        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(5);
        root.setHgap(5);

        Map<String, Integer> data = new HashMap();
        try {
            Scanner lineScanner = new Scanner(new File("RecordsTable.txt"));
            while (lineScanner.hasNextLine()) {
                Scanner wordScanner = new Scanner(lineScanner.nextLine());
                while (wordScanner.hasNext()) {
                    String name = wordScanner.next();
                    String score = wordScanner.next();
                    if( !data.containsKey(name)){
                        data.put(name, Integer.parseInt(score));
                        continue;
                    }
                    if (Integer.parseInt(score) > data.get(name)){
                        data.put(name, Integer.parseInt(score));
                    }
                }
            }
            lineScanner.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        List<Map.Entry<String, Integer>> table = new ArrayList<>(data.entrySet());
        table.sort((o1, o2) -> {
                    if (o2.getValue().equals(o1.getValue())) {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                    return o2.getValue().compareTo(o1.getValue());
                }
        );

        int i = 0;
        for (Map.Entry<String, Integer> line : table) {
            Text name = new Text(line.getKey());
            name.setFill(Color.WHITE);
            name.setFont(Font.font("Verdana", FontWeight.BOLD,32));
            Text score = new Text(Integer.toString(line.getValue()));
            score.setFill(Color.WHITE);
            score.setFont(Font.font("Verdana", FontWeight.BOLD,32));
            GridPane.setConstraints(name, 0, i);
            root.getChildren().add(name);
            GridPane.setConstraints(score, 1, i);
            root.getChildren().add(score);
            i++;
            if (i > 10) {
                break;
            }
        }
    }

    public void add(String name, int totalScore){
        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter("RecordsTable.txt", true));
            fileWriter.write(name  + " " + Integer.toString(totalScore));
            fileWriter.newLine();
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}