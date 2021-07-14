package Graph;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GraphSerializer implements JsonSerializer<MyGraph>{
    @Override
    public JsonElement serialize(MyGraph src, Type typeOfSrc, JsonSerializationContext context){
        JsonObject result = new JsonObject();

        ArrayList<String> vertex_list = new ArrayList<>();
        ArrayList<String> edge_list = new ArrayList<>();
        Vertex cur_ver;
        for(int i = 0; i < src.getSize(); i++){//получение строки, содержащей список вершин и строки, содержащей список рёбер
            cur_ver = src.getVertex(i);
            vertex_list.add(cur_ver.getLabel() + " " + cur_ver.getX() + " " + cur_ver.getY());
            for(int j = 0; j < cur_ver.getEdgeAmount(); j++){
                Edge cur_edge = cur_ver.getEdge(j);
                edge_list.add(cur_ver.getLabel() + " " + cur_edge.getFinish() + " " + cur_edge.getWeight());
            }
        }
        StringBuilder ver_st  = new StringBuilder("");
        for(String st : vertex_list){
            ver_st.append(st).append(',');
        }

        StringBuilder edge_st  = new StringBuilder("");
        for(String st : edge_list){
            edge_st.append(st).append(',');
        }

        result.addProperty("vertexes", new String(ver_st));
        result.addProperty("edges", new String(edge_st));

        return result;
    }
}
