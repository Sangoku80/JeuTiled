package com.mygdx.game.Tests;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaToJson {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(4);

        Gson gson = new Gson();
        String json = gson.toJson(list);

        try (FileWriter writer = new FileWriter("core/src/com/mygdx/game/Tests/list.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSON written to list.json");
    }
}
