package Graph;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GraphDeserializer implements JsonDeserializer<MyGraph> {
    private static Logger logger = LogManager.getLogger(Graph.GraphDeserializer.class);

    @Override
    public MyGraph deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String ver_st = jsonObject.get("vertexes").getAsString();
        if(!ver_st.matches("(\\w+\\s\\d+\\s\\d+,)*")){//проверяем список вершин на форматную корректность
            return new MyGraph();
        }

        String edge_st = jsonObject.get("edges").getAsString();
        if(!edge_st.matches("(\\w+\\s\\w+\\s\\d+,)*")){//проверяем список рёбер на форматную корректность
            return new MyGraph();
        }

        //удаляем последние запятые в списках рёбер и вершин, если там есть хотя бы один элемент
        if(ver_st.length() > 0){
            ver_st = ver_st.substring(0, ver_st.length()-1);
        }

        if(edge_st.length() > 0){
            edge_st = edge_st.substring(0, edge_st.length()-1);
        }

        //разбиваем строки-списки на массивы строк, представляющих вершину или ребро
        String[] vertex_list = ver_st.split(",");
        String[] edge_list = edge_st.split(",");
        MyGraph graph;
        try{//если по входным данным граф строится, то возвращаем его, иначе возвращаем пустой граф
            graph = new MyGraph(edge_list, vertex_list);
        }
        catch (IndexOutOfBoundsException err){
            logger.error(err.getMessage(), err);
            graph = new MyGraph();
        }

        return graph;
    }
}
