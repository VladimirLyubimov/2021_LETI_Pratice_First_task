package Algo;

import Graph.*;

import java.util.ArrayList;
import java.util.Collections;

public class AWithStar {
    public static ArrayList<String> doAlgo(MyGraph graph) throws IndexOutOfBoundsException{//реализация алгоритма А*
        ArrayList<Vertex> open_set = graph.getOpen_set();
        ArrayList<Vertex> close_set = graph.getClose_set();
        String start_label = "";
        String finish_label = "";

        //проверка корректности начальной и конечной вершин
        if(graph.getStart().isPresent()) {
            start_label = graph.getStart().get().getLabel();
        }
        if(graph.getFinish().isPresent()) {
            finish_label = graph.getFinish().get().getLabel();
        }

        if(!graph.isVertexExist(start_label)){
            throw new IndexOutOfBoundsException("Start vertex with name " + start_label + " doesn't exist!");
        }

        ArrayList<String> path = checkNodes(graph, start_label, finish_label);
        if(!path.isEmpty()){
            graph.setPath(path);
            return path;
        }

        //подготовка к выполнению
        path = new ArrayList<>();

        open_set.add(graph.getVertex(start_label).get());

        //получение координат конечной вершины
        int f_x = graph.getVertex(finish_label).get().getX();
        int f_y = graph.getVertex(finish_label).get().getY();
        //обнуление стартовой вершины
        graph.getVertex(start_label).get().setPathVal(0);
        graph.getVertex(start_label).get().setTotalVal(0);

        //сам алгоритм
        while(!open_set.isEmpty()){
            Vertex cur_vertex = findMin(open_set);//берём вершину с наименьшей оценкой

            if(cur_vertex.getLabel().equals(finish_label)){//если она финишная, то получаем путь
                makePath(graph, finish_label, path);
                graph.setPath(path);
                return path;
            }

            openVertex(graph, cur_vertex, open_set, close_set, f_x, f_y);//оцениваем всех не закрытых соседей текущей вершины

            close_set.add(cur_vertex);//помещаем в список закрытых вершин
            open_set.remove(cur_vertex);//удаляем из списка открытых вершин
        }

        //случай, если пути нет
        path = new ArrayList<>();
        path.add("No path!");
        graph.setPath(path);
        return path;
    }

    //раскрываем вершину
    public static void openVertex(MyGraph graph, Vertex cur_vertex, ArrayList<Vertex> open_set, ArrayList<Vertex> close_set, int f_x, int f_y){
        int neighbour_amount = cur_vertex.getEdgeAmount();
        for(int i = 0; i < neighbour_amount; i++){//просмотрим каждого соседа
            Edge cur_edge = cur_vertex.getEdge(i);
            Vertex cur_neighbour = graph.getVertex(cur_edge.getFinish()).get();

            if(close_set.contains(cur_neighbour)){//если сосед в закрытых вершинах, то переходим к следующему
                continue;
            }

            int temp_path_val = cur_vertex.getPathVal() + cur_edge.getWeight();//вес пути в соседа
            boolean need_update = false;

            if(!open_set.contains(cur_neighbour)){//добавляем соседа в открытые вершины, если его там нет
                open_set.add(cur_neighbour);
            }


            if(temp_path_val < cur_neighbour.getPathVal()){//если путь в соседа через текущую вершину дешевле, то обновляем св-ва соседа
                need_update = true;
            }

            if(need_update){//обновляем св-ва соседа, если надо
                cur_neighbour.setCameFrom(cur_vertex.getLabel());
                cur_neighbour.setPathVal(temp_path_val);
                int heuristic = Math.abs(cur_neighbour.getX() - f_x) + Math.abs(cur_neighbour.getY() - f_y);
                cur_neighbour.setTotalVal(temp_path_val + heuristic);
            }
        }
    }

    //проверяем стартовую и конечную вершины
    public static ArrayList<String> checkNodes(MyGraph graph, String start_label, String finish_label){
        ArrayList<String> path = new ArrayList<>();
        if(!graph.isVertexExist(finish_label)){//существует ли конечная вершина
            path.add("No path! Finish vertex doesn't exist!");
            return path;
        }

        if(start_label.equals(finish_label)){//различные ли это вершины
            path.add("Start and finish vertexes are same!");
            return path;
        }

        return path;
    }

   public static Vertex findMin(ArrayList<Vertex> open_set){//находи в открытых вершинах вершину с минимальной оценкой, если таких несколько, то возвращаем первую добавленную в открытые вершины
        Vertex res = open_set.get(0);
        for(Vertex vertex: open_set){
            if(res.getTotalVal() > vertex.getTotalVal()){
                res = vertex;
            }
        }

        return res;
    }

  public static void makePath(MyGraph graph, String finish, ArrayList<String> path){//строим путь из финишной вершины в стартовую, если он есть
        path.clear();
        String prev = graph.getVertex(finish).get().getCameFrom();
        path.add(finish);

        while(!prev.equals("")){
            path.add(prev);
            prev = graph.getVertex(prev).get().getCameFrom();
        }

        Collections.reverse(path);
    }
}
