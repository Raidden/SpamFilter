package Bayes;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileWriter;

public class Model {

    public void saveModel(HashMap<String, Word> words, String modelName) throws IOException {
        File file = new File(modelName + ".json");
        file.createNewFile();
        Gson gson = new GsonBuilder().create();
        String result = gson.toJson(words);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(result);
        fileWriter.close();
    }

    public HashMap<String, Word> readModel(File file) throws IOException {
        Gson gson = new Gson();
        JsonReader jsonreader = new JsonReader(new FileReader(file));
        Type type = new TypeToken<HashMap<String, Word>>() {
        }.getType();
        return gson.fromJson(jsonreader, type);
    }
}
