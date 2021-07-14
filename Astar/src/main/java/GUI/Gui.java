package GUI;

import Algo.AWithStar;
import FileIO.FromFile;
import FileIO.ToFile;
import GUI.Actions.HelpAction;
import GUI.AlgoVisual.AlgoVisualization;
import Graph.GraphSerializer;
import Graph.MyGraph;
import Graph.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.Timer;

public class Gui {
    private JFrame window;
    private MyCanvas graph_drawer;

    private JButton help;
    private JButton random_grah;
    private JButton unar_grah;
    private JButton from_file;
    private JButton to_file;
    private JButton step_forward;
    private JButton step_back;
    private JButton go_end;
    private JButton go_start;

    private JTextField filename;
    private JTextField vertex_amount;
    private JTextField edge_amount;
    private JTextField min_weight;
    private JTextField max_weight;

    private JLabel filename_text;
    private JLabel vertex_amount_text;
    private JLabel edge_amount_text;
    private JLabel min_weight_text;
    private JLabel max_weight_text;
    private JLabel status_text;
    private JLabel time_text;

    private MyGraph graph;
    private final AlgoVisualization AVisual = new AlgoVisualization();
    private String start;
    private String finish;
    private boolean[] states = new boolean[] {false/*prepare algo*/, false/*finish reached*/};

    private static Logger logger = LogManager.getLogger(GUI.Gui.class);

    public MyGraph getGraph(){
        return graph;
    }

    public Gui(MyGraph gr){
        //главное окно
        this.graph = gr;
        window = new JFrame("Practise project");
        window.setSize(1200, 680);
        window.setLayout(null);

        //канва
        graph_drawer = new MyCanvas(this.graph);
        graph_drawer.setBounds(0,0, 600, 540);
        graph_drawer.setBorder(BorderFactory.createEtchedBorder());
        window.add(graph_drawer);

        Font font = new Font("Arial", Font.PLAIN, 12);
        Insets inset = new Insets(0,0,0,0);

        //вывод текущего времени в формате чч:мм::сс
        time_text = new JLabel("00:00:00");
        time_text.setBounds(610, 10, 200, 10);
        window.add(time_text);
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                time_text.setText(format.format(new java.util.Date()));
            }
        });
        timer.start();

        //текст последнего действия пользователя
        status_text = new JLabel();
        status_text.setBounds(100, 550, 1000, 120);
        status_text.setText("Messages for user:");
        status_text.setBorder(BorderFactory.createEtchedBorder());
        window.add(status_text);

        //кнопка помощи
        help = new JButton(new HelpAction());
        help.setBounds(860, 30, 200, 30);
        help.setFont(font);
        help.setMargin(inset);
        help.setText("Help");
        window.add(help);


        //кнопка случайной генерации
        int[] graph_param = new int[4];
        random_grah = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    graph_param[0] = Integer.parseInt(vertex_amount.getText());
                    graph_param[1] = Integer.parseInt(edge_amount.getText());
                    graph_param[2] = Integer.parseInt(min_weight.getText());
                    graph_param[3] = Integer.parseInt(max_weight.getText());
                }
                catch (NumberFormatException err){
                    logger.error(err.getMessage(), err);
                    status_text.setText("<html>Messages for user:<br>- Wrong number format! Please input integer decimal numbers!</html>");
                    return;
                }
                try {
                    makeRandomGraph(graph_param);
                    graph_drawer.updateGraph(graph);
                    graph_drawer.repaint();
                    status_text.setText("<html>Messages for user:<br>- You successfully build new random graph</html>");
                }
                catch (IOException err) {
                    logger.error(err.getMessage(), err);
                    if (graph_param[3] == 0 || graph_param[2] == 0) {
                        status_text.setText("<html>Messages for user:<br>- Minimal or maximal edge weight is zero. </html>");
                    } else {
                        status_text.setText("<html>Messages for user:<br>- Wrong input data for random generation!<br>- Remember:<br>all numbers must be non-negative;<br>max amount of vertexes is " + (int) (Math.ceil((double) 10 / (double) graph_param[3]) * Math.ceil((double) 9 / (double) graph_param[3])) + " and you have " + graph_param[0] + ";<br>max amount of edges is " + (2 * graph_param[0] - 2) + " and you have " + graph_param[1] + ";<br>maximal edge weight must be less than 9, more than 1 and not less than minimal weight, which is " + graph_param[2] + ", and you have max weight " + graph_param[3] + ";<br>for make graph with only unary edges use another button 'Create unary graph'</html>");
                    }
                }
            }
        });
        random_grah.setText("Create random graph");
        random_grah.setFont(font);
        random_grah.setMargin(inset);
        random_grah.setBounds(610, 300, 200, 30);
        window.add(random_grah);

        //кнопка генерации единичного графа
        random_grah = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int vertex_count = 0;
                try {
                    vertex_count = Integer.parseInt(vertex_amount.getText());
                }
                catch (NumberFormatException err){
                    logger.error(err.getMessage(), err);
                    status_text.setText("<html>Messages for user:<br>- Wrong number format! Please input integer decimal numbers!</html>");
                    return;
                }
                try {
                    makeUnarGraph(vertex_count);
                    graph_drawer.updateGraph(graph);
                    graph_drawer.repaint();
                    status_text.setText("<html>Messages for user:<br>- You successfully build new unary graph</html>");
                }
                catch (IOException err){
                    status_text.setText("<html>Messages for user:<br>- Wrong input data for unary graph generation!<br>- Remember:<br>all numbers must be non-negative;<br>max amount of vertexes is " + (int)(Math.ceil((double)10/(double)graph_param[3])*Math.ceil((double)9/(double)graph_param[3])) + " and you have " + graph_param[0] + "/html");
                    logger.error(err.getMessage(), err);
                }
            }
        });
        random_grah.setText("Create unary graph");
        random_grah.setFont(font);
        random_grah.setMargin(inset);
        random_grah.setBounds(860, 300, 200, 30);
        window.add(random_grah);

        //кнопка сохранения в файл
        to_file = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean success = ToFile.write(graph, filename.getText());
                if(success){
                    status_text.setText("<html>Messages for user:<br>- Graph saved into file with name "+ filename.getText() +"</html>");
                }
                else{
                    status_text.setText("<html>Messages for user:<br>- Fail to save into file with name "+ filename.getText() +"<br>Check that:<br>filename is correct</html>");
                }
            }
        });
        to_file.setText("Save graph into file");
        to_file.setFont(font);
        to_file.setMargin(inset);
        to_file.setBounds(860, 140, 200, 30);
        window.add(to_file);

        //кнопка загрузки из файла
        from_file = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    makeGraph(filename.getText());
                    graph_drawer.updateGraph(graph);
                    graph_drawer.repaint();
                    status_text.setText("<html>Messages for user:<br>- Graph loaded from file with name "+ filename.getText() +"<br>- If file data was corrupted empty graph created<br>- Corrupted data means:<br>Invalid Json object<br>Invalid data in vertex or edge lists</html>");
                }
                catch (IOException err){
                    status_text.setText("<html>Messages for user:<br>- Fail to load from file with name "+ filename.getText() +"<br>Check that:<br>filename is correct</html>");
                    logger.error(err.getMessage(), err);
                }
            }
        });
        from_file.setText("Load graph from file");
        from_file.setFont(font);
        from_file.setMargin(inset);
        from_file.setBounds(860, 190, 200, 30);
        window.add(from_file);

        //кнопка шага алгоритма вперёд
        step_forward = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!states[0]){
                    try {
                        AVisual.prepare(graph);
                        states[0] = true;
                    }
                    catch (IndexOutOfBoundsException err){
                        status_text.setText("<html>Messages for user:<br>- Wrong parameters for A* algorithm! Check that:<br>You set start and finish vertexes;<br>Start and finish vertexes are different vertexes;<br>Use 'Reset algorithm' button to reset algorithm data</html>");
                        logger.error(err.getMessage(), err);
                        return;
                    }
                }
                if(!states[1]) {
                    states[1] = AVisual.makeStep(graph);
                    graph_drawer.repaint();
                    status_text.setText("<html>Messages for user:<br>- You make an algorithm step</html>");
                }
                else{
                    graph.setCurVertex(null);
                    graph_drawer.repaint();
                    status_text.setText("<html>Messages for user:<br>- End of algorithm reached. Result is:<br>"+ graph.getPath().toString() +"</html>");
                }
            }
        });
        step_forward.setText("Step forward");
        step_forward.setFont(font);
        step_forward.setMargin(inset);
        step_forward.setBounds(860, 400, 200, 30);
        window.add(step_forward);

        //кнопка шага алгоритма назад
        step_back = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ArrayList<Vertex> close_set = graph.getClose_set();
                ArrayList<Vertex> open_set = graph.getOpen_set();
                ArrayList<Vertex> remove_set = new ArrayList<>();
                if(!close_set.isEmpty()){
                    Vertex cur_ver = close_set.get(close_set.size()-1);
                    close_set.remove(cur_ver);
                    for(Vertex vertex : open_set){
                        if(cur_ver.isLinked(vertex)){
                            remove_set.add(vertex);
                        }
                    }
                    for(Vertex vertex : remove_set){
                        vertex.resetVertex();
                        open_set.remove(vertex);
                    }
                    open_set.add(cur_ver);
                    if(!(close_set.size() == 0)){
                        cur_ver = close_set.get(close_set.size() - 1);
                    }
                    graph.setCurVertex(cur_ver);
                    AWithStar.makePath(graph, cur_ver.getLabel(), graph.getPath());
                    graph.setPath(graph.getPath());
                    graph_drawer.repaint();
                    status_text.setText("<html>Messages for user:<br>- You make a step back</html>");
                }
                else{
                    status_text.setText("<html>Messages for user:<br>- You are in the start! No more steps back can be made!</html>");
                }
            }
        });
        step_back.setText("Step back");
        step_back.setFont(font);
        step_back.setMargin(inset);
        step_back.setBounds(610, 400, 200, 30);
        window.add(step_back);

        //кнопка алгоритма в конец
        go_end = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                states[0] = false;
                states[1] = false;
                graph.resetGraph();
                graph.setCurVertex(null);
                states[0] = true;
                states[1] = true;
                try {
                    ArrayList<String> path = AWithStar.doAlgo(graph);
                }
                catch (IndexOutOfBoundsException err){
                    status_text.setText("<html>Messages for user:<br>- Wrong parameters for A* algorithm! Check that:<br>You set start and finish vertexes;<br>Start and finish vertexes are different vertexes;<br>Use 'Reset algorithm' button to reset algorithm data</html>");
                    logger.error(err.getMessage(), err);
                    return;
                }
                graph_drawer.repaint();
                status_text.setText("<html>Messages for user:<br>- End of algorithm reached. Result is:<br>"+ graph.getPath().toString() +"</html>");
            }
        });
        go_end.setText("Go to end");
        go_end.setFont(font);
        go_end.setMargin(inset);
        go_end.setBounds(860, 450, 200, 30);
        window.add(go_end);

        //кнопка алгоритма в начало
        go_start = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                graph.resetGraph();
                graph.resetStartFinish();
                graph.setCurVertex(null);
                states[0] = false;
                states[1] = false;
                status_text.setText("<html>Messages for user:<br>Algorithm and graph returned to original state! Start and finish are reset!</html>");
                graph_drawer.repaint();
            }
        });
        go_start.setText("Reset algorithm");
        go_start.setFont(font);
        go_start.setMargin(inset);
        go_start.setBounds(610, 450, 200, 30);
        window.add(go_start);

        //поле для получения имени файла
        filename = new JTextField();
        filename.setToolTipText("Input filename");
        filename.setBounds(860, 100, 200, 30);
        window.add(filename);

        //поле для получения количества вершин
        vertex_amount = new JTextField();
        vertex_amount.setToolTipText("Input vertex amount");
        vertex_amount.setBounds(610, 50, 200, 30);
        window.add(vertex_amount);

        //поле для получения количества рёбер
        edge_amount = new JTextField();
        edge_amount.setToolTipText("Input edge amount");
        edge_amount.setBounds(610, 120, 200, 30);
        window.add(edge_amount);

        //поле для получения минимального веса ребра
        min_weight = new JTextField();
        min_weight.setToolTipText("Input minimal edge weight");
        min_weight.setBounds(610, 190, 200, 30);
        window.add(min_weight);

        //поле для получения максимального веса ребра
        max_weight = new JTextField();
        max_weight.setToolTipText("Input maximal edge weight");
        max_weight.setBounds(610, 260, 200, 30);
        window.add(max_weight);

        //текстовые поля для вывода различных подписей
        filename_text = new JLabel("Input filename:");
        filename_text.setBounds(860, 80, 200, 20);
        window.add(filename_text);

        vertex_amount_text = new JLabel("Input vertex amount:");
        vertex_amount_text.setBounds(610, 30, 200, 20);
        window.add(vertex_amount_text);

        filename_text = new JLabel("Input edge amount:");
        filename_text.setBounds(610, 100, 200, 20);
        window.add(filename_text);

        filename_text = new JLabel("Input minimal edge weight:");
        filename_text.setBounds(610, 170, 200, 20);
        window.add(filename_text);

        filename_text = new JLabel("Input maximal edge weight:");
        filename_text.setBounds(610, 240, 200, 20);
        window.add(filename_text);

        //установка параметров основного окна
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void makeRandomGraph(int[] graph_param) throws IOException {//генерация случайного графа
        try {
            graph = new MyGraph(graph_param[0], graph_param[1], graph_param[2], graph_param[3]);
        }
        catch (IndexOutOfBoundsException err){
            logger.error(err.getMessage(), err);
            throw new IOException("Problem with random graph generation");
        }
    }

    public void makeUnarGraph(int vertex_count) throws IOException {//генерация единичного графа
        try {
            graph = new MyGraph(vertex_count);
        }
        catch (IndexOutOfBoundsException err){
            logger.error(err.getMessage(), err);
            throw new IOException("Problem with random graph generation");
        }
    }

    public void makeGraph(String filename) throws IOException{//получение графа из файла
        Optional<MyGraph> optional = FromFile.read(filename);
        if(optional.isPresent()){
            graph = optional.get();
        }
        else{
            throw new IOException("Problem with read graph from file");
        }

    }
}
