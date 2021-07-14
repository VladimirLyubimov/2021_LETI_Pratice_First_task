package FileIO;

import Graph.GraphDeserializer;
import Graph.GraphSerializer;
import Graph.MyGraph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Converter {
    private static Logger logger = LogManager.getLogger(Graph.GraphDeserializer.class);

    public static String makeJson(MyGraph graph) {//делаем из графа json-строку
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(MyGraph.class, new GraphSerializer()).create();
        String json = gson.toJson(graph);
        return json;
    }

    public static MyGraph fromJson(String json){//делаем из json-строки граф, если строка повреждена, то получаем пустой граф
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(MyGraph.class, new GraphDeserializer()).create();
        MyGraph graph;
        try{
            graph = gson.fromJson(json, MyGraph.class);
        }
        catch (JsonSyntaxException err){
            logger.error(err.getMessage(), err);
            graph = new MyGraph();
        }
        return graph;
    }
}
