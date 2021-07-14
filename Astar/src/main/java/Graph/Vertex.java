package Graph;

import java.util.ArrayList;

public class Vertex {
    private final String label;//имя вершины
    private int x;
    private int y;
    private String came_from = "";//предыдущая вершина в пути
    private int path_val = 1000000;//вес пути до этой вершины
    private int total_val = 1000000;//суммарная оценка этой вершины
    private ArrayList<Edge> neighbours;//список рёбер, выходящих из этой вершины

    public Vertex(String label, int x, int y){//конструктор
        this.label = label;;
        this.x = x;
        this.y = y;
        neighbours = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getCameFrom() {
        return came_from;
    }

    public int getPathVal() {
        return path_val;
    }

    public int getTotalVal() {
        return total_val;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCameFrom(String came_from) {
        this.came_from = came_from;
    }

    public void setPathVal(int path_val) {
        this.path_val = path_val;
    }

    public void setTotalVal(int total_val) {
        this.total_val = total_val;
    }

    public void addEdge(String neighbour_label, int weight){//добавление ребра по имени конечной вершины и его веса
        neighbours.add(new Edge(label, neighbour_label, weight));
    }

    public int getEdgeAmount(){
        return neighbours.size();
    }//получение количества рёбер

    public Edge getEdge(int pos){//получение ребра по его индексу в списке рёбер
        if(pos < 0 || pos > 4){
            return null;
        }
        return neighbours.get(pos);
    }

    public void deleteEdge(int pos){
        neighbours.remove(pos);
    }//удаление ребра по его индексу в списке рёбер

    public boolean isLinked(Vertex vertex){// проверяет наличие ребра между вершинами
        for(Edge edge : neighbours){
            if(edge.getFinish().equals(vertex.getLabel())){
                return true;
            }
        }
        return false;
    }

    public void resetVertex(){//сброс вершины в начальное состояние
        path_val = 1000000;
        total_val = 1000000;
        came_from = "";
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }

        if(obj instanceof Vertex){
            Vertex ver = (Vertex) obj;
            if(label.equals(ver.getLabel()) && x == ver.getX() && y == ver.getY()){
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        String st = label + " " + x + ";" + y + " " + ". Edges: ";
        for(Edge edge : neighbours){
            st += edge.getStart() + " " + edge.getFinish() + " : " + edge.getWeight() + " ||| ";
        }

        return st;
    }
}
