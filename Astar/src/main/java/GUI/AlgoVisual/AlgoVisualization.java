package GUI.AlgoVisual;

import Algo.AWithStar;
import Graph.MyGraph;
import Graph.Vertex;

import java.util.ArrayList;

public class AlgoVisualization {
    private ArrayList<Vertex> open_set;
    private ArrayList<Vertex> close_set;

    private ArrayList<String> path;
    private String start_label = "";
    private String finish_label = "";

    //координаты финиша
    private int f_x;
    private int f_y;

    public AlgoVisualization(){}

    public void prepare(MyGraph graph) throws IndexOutOfBoundsException{//подготовка к алгоритму
        open_set = graph.getOpen_set();
        close_set = graph.getClose_set();

        //проверка стартовой и конечной вершин
        if(graph.getStart().isPresent()) {
            start_label = graph.getStart().get().getLabel();
        }
        if(graph.getFinish().isPresent()) {
            finish_label = graph.getFinish().get().getLabel();
        }

        if(!graph.isVertexExist(start_label)){
            throw new IndexOutOfBoundsException("Start vertex with name " + start_label + " doesn't exist!");
        }

        path = AWithStar.checkNodes(graph, start_label, finish_label);
        if(!path.isEmpty()){
            throw new IndexOutOfBoundsException("Finish vertex is incorrect!");
        }

        path = new ArrayList<>();

        open_set.add(graph.getVertex(start_label).get());

        f_x = graph.getVertex(finish_label).get().getX();
        f_y = graph.getVertex(finish_label).get().getY();

        //обнуление начальной вершины
        graph.getVertex(start_label).get().setPathVal(0);
        graph.getVertex(start_label).get().setTotalVal(0);
    }

    public boolean makeStep(MyGraph graph){//шаг алгоритма А*
        if(!open_set.isEmpty()){
            Vertex cur_vertex = AWithStar.findMin(open_set);
            graph.setCurVertex(cur_vertex);
            AWithStar.makePath(graph, cur_vertex.getLabel(), path);
            graph.setPath(path);

            if(cur_vertex.getLabel().equals(finish_label)){//если финиш, то закончить с успехом
                return true;
            }

            AWithStar.openVertex(graph, cur_vertex, open_set, close_set, f_x, f_y);

            close_set.add(cur_vertex);
            open_set.remove(cur_vertex);
        }
        else {//пути нет, то закончить с успехом
            path = new ArrayList<>();
            path.add("No path!");
            graph.setPath(path);
            graph.setCurVertex(null);
            return true;
        }
        return false;//алгоритм пока не завершён, так что вернуть неудачу
    }
}
