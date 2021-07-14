package GUI;

import Graph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;

public class MyCanvas extends JComponent implements MouseListener {
    private MyGraph graph;

    private static Logger logger = LogManager.getLogger(GUI.MyCanvas.class);

    private static JFrame mes_window = null;
    private static JLabel message = new JLabel("Nothing happened");

    private int step = 60;
    private int shift = step/8;
    private int cshift = step/2 - 1;

    //координаты начала ребра для удаления
    private int delete_x = 0;
    private int delete_y = 0;

    //координаты нажатия мышки
    private int mouse_x = -1;
    private int mouse_y = -1;

    //координаты начала ребра для добавления
    private int start_x = 0;
    private int start_y = 0;

    //координаты конца ребра
    private int finish_x = 0;
    private int finish_y = 0;
    private int append_click = 0;//количество кликов в режиме добавления ребра
    private int delete_click = 0;//количество кликов в режиме удаления ребра
    private long button_time = 0;//время зажатия левой кнопки мыши

    public MyCanvas(MyGraph graph){
        addMouseListener(this);
        this.graph = graph;
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setVerticalAlignment(SwingConstants.CENTER);
    }

    public void updateGraph(MyGraph graph){
        this.graph = graph;
        append_click = 0;
        delete_click = 0;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        message.setText("");
        if(mes_window != null) {//закрыть прошлое сообщение, если оно ещё открыто
            mes_window.dispose();
        }

        //получить координаты клика и выделить квадрат, где он был сделан
        mouse_x = mouseEvent.getX() / step;
        mouse_y = mouseEvent.getY() / step;
        repaint();

        if(mouseEvent.getButton() == 3) {//если нажата правя кнопка мыши
            if(append_click == 0) {//если это первый клик для текущего добавления, то запомнить его координаты
                start_x = mouseEvent.getX() / step;
                start_y = mouseEvent.getY() / step;
                message.setText("Start for new edge added!");
            }
            append_click += 1;//увеличить количество кликов для текущего добавления
        }

        if(mouseEvent.getButton() == 2) {//если нажата средняя кнопка мыши
            if(delete_click == 0) {//если это первый клик для текущего удаления, то запомнить его координаты
                delete_x = mouseEvent.getX() / step;
                delete_y = mouseEvent.getY() / step;
                message.setText("Start of edge for deleting added!");
            }
            delete_click += 1;//увеличить количество кликов для текущего удаления
        }

        if(mouseEvent.getButton() == 1) {//если нажата левая кнопка мыши, то запомнить время нажатия
            button_time = System.currentTimeMillis();
        }
    }

    private void addingVertex(MouseEvent mouseEvent) throws IndexOutOfBoundsException{//добавление вершины
        graph.resetGraph();
        graph.resetStartFinish();

        int x = mouseEvent.getX() / step;
        int y = mouseEvent.getY() / step;
        if (graph.getVertex(x, y).isEmpty()) {//если место свободно, то можно попытаться добавить вершину
            if(graph.checkRight(x, y).isPresent() && graph.checkLeft(x, y).isPresent()) {
                Vertex l_neighb = graph.checkLeft(x, y).get();
                Vertex r_neighb = graph.checkRight(x, y).get();
                if (l_neighb.isLinked(r_neighb) || r_neighb.isLinked(l_neighb)) {
                    throw new IndexOutOfBoundsException("Vertex can be place on edge!");
                }
            }
            if (graph.checkUp(x, y).isPresent() && graph.checkDown(x, y).isPresent()) {//если место свободно, то можно попытаться добавить вершину
                Vertex u_neighb = graph.checkUp(x, y).get();
                Vertex d_neighb = graph.checkDown(x, y).get();
                if (u_neighb.isLinked(d_neighb) || d_neighb.isLinked(u_neighb)) {
                    throw new IndexOutOfBoundsException("Vertex can be place on edge!");
                }
            }

            //если всё хорошо, то, наконец, добавить вершину
            graph.addVertex(graph.getSize() + 2 + "", x, y);
        } else {//выкинуть ошибку, что вершина здесь уже существует
            throw new IndexOutOfBoundsException("Vertex already exist here!");
        }
    }

    private void addingEdge(MouseEvent mouseEvent) throws IndexOutOfBoundsException{//добавление ребра
        graph.resetGraph();
        graph.resetStartFinish();

        append_click = 0;
        finish_x = mouseEvent.getX()/step;
        finish_y = mouseEvent.getY()/step;
        Vertex start, finish;

        //проверка, что в начале и конце ребра существуют вершины и ребро не диагональное
        if(graph.getVertex(start_x, start_y).isPresent() & graph.getVertex(finish_x, finish_y).isPresent() && !(start_x != finish_x && start_y != finish_y)) {
            start = graph.getVertex(start_x, start_y).get();
            finish = graph.getVertex(finish_x, finish_y).get();
            if(start.isLinked(finish)){//если вершины соединены, то выбросить ошибку
                throw new IndexOutOfBoundsException("Fail  to make edge");
            }
            int weight = Math.abs(start.getX() - finish.getX()) + Math.abs(start.getY() - finish.getY());
            try{//добавляем ребро
                graph.addEdge(start.getLabel(), finish.getLabel(), weight);
            }
            catch (IOException err){
                throw new IndexOutOfBoundsException(err.getMessage());
            }
        }
        else{
            throw new IndexOutOfBoundsException("Fail to make edge");
        }
    }

    private void deletingEdge(MouseEvent mouseEvent) throws IndexOutOfBoundsException{//удаление ребра
        graph.resetGraph();
        graph.resetStartFinish();

        delete_click = 0;
        finish_x = mouseEvent.getX()/step;
        finish_y = mouseEvent.getY()/step;
        Vertex start, finish;
        //если вершины в начале и конце ребра существуют, то пытаемся удалить ребро
        if(graph.getVertex(delete_x, delete_y).isPresent() & graph.getVertex(finish_x, finish_y).isPresent() && (delete_x != finish_x || delete_y != finish_y)) {
            start = graph.getVertex(delete_x, delete_y).get();
            finish = graph.getVertex(finish_x, finish_y).get();
            try{//пытаемся удалить ребро, если это возможно
                graph.deleteEdge(start.getLabel(), finish.getLabel());
            }
            catch (IndexOutOfBoundsException err){//иначе получаем ошибку
                logger.error(err.getMessage(), err);
                throw new IndexOutOfBoundsException(err.getMessage());
            }
        }
        else{
            throw new IndexOutOfBoundsException("Fail");
        }
    }

    private void deletingVertex(MouseEvent mouseEvent) throws IndexOutOfBoundsException{//удаляем вершину
        graph.resetGraph();
        graph.resetStartFinish();

        int x = mouseEvent.getX() / step;
        int y = mouseEvent.getY() / step;
        if (graph.getVertex(x, y).isPresent()) {//если есть что удалять, то удаляем эту вершину
            graph.deleteVertex(graph.getVertex(x, y).get().getLabel());
        } else {
            throw new IndexOutOfBoundsException("Vertex already exist here!");
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //создадим окно под сообщение
        mes_window = new JFrame("Message for user");
        mes_window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mes_window.setLayout(null);
        mes_window.setVisible(true);
        mes_window.add(message);
        mes_window.setBounds((int)this.getAlignmentX() + 810, 0, 320, 200);

        message.setBounds(10,10,300, 120);

        if(mouseEvent.getButton() == 1){//если клик левой кнопкой мыши
            button_time = System.currentTimeMillis() - button_time;//вычисляем время зажатия кнопки мыши
            if(button_time > 7000){//если это время больше семи секунд, то пытаемся выполнить удаление вершины
                try {
                    deletingVertex(mouseEvent);
                    message.setText("You delete vertex!");
                } catch (IndexOutOfBoundsException err) {
                    message.setText("<html>Fail to delete vertex!<br>Causes:<br>-This vertex doesn't exist</html>");
                    logger.error(err.getMessage(), err);
                }
            }
            else {
                if (button_time > 2000) {//если это время больше двух секунд, то пытаемся добавить старт или финиш
                    int x = mouseEvent.getX() / step;
                    int y = mouseEvent.getY() / step;
                    if(graph.getVertex(x, y).isPresent()) {//если вершина существует, то приступаем
                        if (graph.getStart().isEmpty()) {//если старта ещё нет, то добавляем его
                            graph.setStart(graph.getVertex(x, y).get().getLabel());
                            message.setText("Start added!");
                        } else {//иначе добавляем финиш
                            graph.setFinish(graph.getVertex(x, y).get().getLabel());
                            message.setText("Finish added!");
                        }
                    }
                    else{
                        message.setText("<html>Fail to append start or finish!<br>Causes:<br>-There is no vertex here</html>");
                    }
                }
                else {//иначе пытаемся добавить вершину

                    try {
                        addingVertex(mouseEvent);
                        message.setText("Vertex added!");
                    } catch (IndexOutOfBoundsException err) {
                        message.setText(err.getMessage());
                        logger.error(err.getMessage(), err);
                    }
                }
            }
        }
        if(append_click == 2){//если правая кнопка мыши нажата второй раз для текущего цикла добавления, пытаемся добавить ребро
            try{
                addingEdge(mouseEvent);
                message.setText("Edge added!");
            }
            catch (IndexOutOfBoundsException err){
                message.setText("<html>Fail to append edge!<br>Causes:<br>-It can already existed<br>-You tried to make diagonal edge<br>-Start or finish vertexes of your edge don't exist</html>");
                logger.error(err.getMessage(), err);
            }
        }

        if(delete_click == 2){//если средняя кнопка мыши нажата второй раз для текущего цикла удаления, пытаемся удалить ребро
            try{
                deletingEdge(mouseEvent);
                message.setText("Edge deleted!");
            }
            catch (IndexOutOfBoundsException err){
                message.setText("<html>Fail to delete edge!<br>Causes:<br>-Edge doesn't exist</html>");
                logger.error(err.getMessage(), err);
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        if(mes_window != null) {//закрываем окно сообщения, если оно открыто
            mes_window.dispose();
        }
        mouse_x = -1;
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics){
        Graphics2D g2d = (Graphics2D) graphics;
        drawEdges(graph, g2d);
        drawVertexes(graph, g2d);
        drawMouseTouch(mouse_x, mouse_y, g2d);
    }

    private void drawMouseTouch(int x, int y, Graphics2D g2d){//отрисовка прозрачного красного квадрата на месте клика мыши
        if(x == -1){
            return;
        }
        x = x*step;
        y = y*step;
        Color old_color = g2d.getColor();
        g2d.setColor(new Color(255,0,0, 80));
        g2d.fill(new Rectangle(x,y,step, step));
        g2d.setColor(old_color);
    }

    private void drawEdges(MyGraph graph, Graphics2D g2d){//рисуем рёбра
        int graph_size = graph.getSize();
        Vertex cur_ver;
        Vertex fin_ver;
        for(int i = 0; i < graph_size; i++){
            cur_ver = graph.getVertex(i);
            int edge_count = cur_ver.getEdgeAmount();
            Edge cur_edge;
            for(int j = 0; j < edge_count; j++){
                cur_edge = cur_ver.getEdge(j);
                fin_ver = graph.getVertex(cur_edge.getFinish()).get();
                //рисуем само ребро (линия)
                g2d.drawLine(cur_ver.getX()*step + cshift, cur_ver.getY()*step + cshift, fin_ver.getX()*step + cshift, fin_ver.getY()*step + cshift);

                //рисуем стрелки
                if(cur_ver.getX() < fin_ver.getX()){
                    drawRightArrow(fin_ver.getX(), fin_ver.getY(), g2d);
                    continue;
                }
                if(cur_ver.getX() > fin_ver.getX()){
                    drawLeftArrow(fin_ver.getX(), fin_ver.getY(), g2d);
                    continue;
                }
                if(cur_ver.getY() < fin_ver.getY()){
                    drawDownArrow(fin_ver.getX(), fin_ver.getY(), g2d);
                    continue;
                }
                if(cur_ver.getY() > fin_ver.getY()){
                    drawUpArrow(fin_ver.getX(), fin_ver.getY(), g2d);
                    continue;
                }
            }
        }
    }

    //рисование стрелки вправо
    private void drawRightArrow(int x, int y, Graphics2D g2d){
        g2d.drawLine(x*step,y*step + 3*step/8, x*step + step/8, y*step + cshift);
        g2d.drawLine(x*step,y*step + 5*step/8 - 1, x*step + step/8, y*step + cshift);
    }

    //рисование стрелки влево
    private void drawLeftArrow(int x, int y, Graphics2D g2d){
        g2d.drawLine(x*step + step - 1,y*step + 3*step/8, x*step + 7*step/8, y*step + cshift);
        g2d.drawLine(x*step + step - 1,y*step + 5*step/8 - 1, x*step + 7*step/8, y*step + cshift);
    }

    //рисование стрелки вверх
    private void drawUpArrow(int x, int y, Graphics2D g2d){
        g2d.drawLine(x*step + 3*step/8,y*step + step - 1, x*step + cshift, y*step + 7*step/8);
        g2d.drawLine(x*step + 5*step/8 -1,y*step + step - 1, x*step + cshift, y*step + 7*step/8);
    }

    //рисование стрелки вниз
    private void drawDownArrow(int x, int y, Graphics2D g2d){
        g2d.drawLine(x*step + 3*step/8,y*step, x*step + cshift, y*step + step/8);
        g2d.drawLine(x*step + 5*step/8 -1,y*step, x*step + cshift, y*step + step/8);
    }

    private void drawVertexes(MyGraph graph, Graphics2D g2d){//рисуем вершины
        int graph_size = graph.getSize();
        Vertex cur_ver;
        Color old_color = g2d.getColor();
        Color color = new Color(0,255,0);
        for(int i = 0; i < graph_size; i++){//все вершины
            cur_ver = graph.getVertex(i);
            drawVertex(cur_ver, color, old_color, g2d);
            g2d.setColor(old_color);
        }

        //вершины открытого списка
        ArrayList<Vertex> vertex_set = graph.getOpen_set();
        color = new Color(255,255,0);
        for(Vertex vertex : vertex_set){
            drawVertex(vertex, color, old_color, g2d);
            g2d.setColor(old_color);
        }

        //вершины закрытого списка
        vertex_set = graph.getClose_set();
        color = new Color(150,150,150);
        for(Vertex vertex : vertex_set){
            drawVertex(vertex, color, old_color, g2d);
            g2d.setColor(old_color);
        }

        //вершины из текущего пути
        ArrayList<String> path = graph.getPath();
        color = new Color(255,0,0);
        for (String label : path) {
            if(graph.getVertex(label).isPresent()) {
                cur_ver = graph.getVertex(label).get();
                drawVertex(cur_ver, color, old_color, g2d);
            }
            else{
                if(!path.isEmpty()){
                    System.out.println(path.get(0));
                }
            }
        }

        //начальная вершина
        if(graph.getStart().isPresent()) {
            color = new Color(0, 255, 255);
            drawVertex(graph.getStart().get(), color, old_color, g2d);
        }

        //конечная вершина
        if(graph.getFinish().isPresent()) {
            color = new Color(255, 0, 255);
            drawVertex(graph.getFinish().get(), color, old_color, g2d);
        }

        //текущая вершина
        if(graph.getCurVertex().isPresent()) {
            color = new Color(0, 0, 255);
            drawVertex(graph.getCurVertex().get(), color, old_color, g2d);
        }

    }

    //отрисовка вершины заданным цветом
    private void drawVertex(Vertex cur_ver, Color color, Color old_color, Graphics2D g2d){
        Ellipse2D circle = new Ellipse2D.Double(cur_ver.getX()*step + shift, cur_ver.getY()*step + shift, 3*step/4, 3*step/4);
        g2d.draw(circle);
        g2d.setColor(color);
        g2d.fill(circle);
        g2d.setColor(old_color);
        g2d.drawString(cur_ver.getLabel(), cur_ver.getX()*step + step/2 - cur_ver.getLabel().length()*4, cur_ver.getY()*step + 9*step/16);
    }
}
