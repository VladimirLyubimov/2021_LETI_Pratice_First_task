package Graph;

public class Edge {
    private final String start;
    private final String finish;
    private final int weight;

    public Edge(String start, String finish, int weight){
        this.start = start;//имя начальной вершины
        this.finish= finish;//имя конечной вершины
        this.weight = weight;//вес
    }

    public int getWeight(){
        return weight;
    }

    public String getFinish(){
        return finish;
    }

    public String getStart(){
        return start;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }

        if(obj instanceof Edge){
            Edge edge = (Edge) obj;
            if(start.equals(edge.getStart()) && finish.equals(edge.getFinish()) && weight == edge.getWeight()){
                return true;
            }
        }

        return false;
    }
}
