package GUI.Actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class HelpAction extends AbstractAction {
    private JFrame frame;
    private JLabel text;

    public HelpAction(){
        super();

        text = new JLabel();

        frame = new JFrame("Help");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.add(text);
        frame.setBounds(100, 50, 1000, 700);

        text.setBounds(10,10, 980, 680);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent){
        frame.setVisible(true);
        text.setText("<html>1) Modifying graph<br>" +
                "-To load graph from file input filename in corresponding text field" +
                " and press button 'Load graph from file'<br>" +
                "Graph in file is Json object with two fields 'vertexes' and 'edges', which are string<br>" +
                "Vertex's string contains vertexes, which are represented as &lt;label&gt;&lt;space&gt;&lt;x coord&gt;&lt;space&gt;&lt;y coord&gt;&lt;comma&gt;<br>" +
                "Edge's string contains edges, which are represented as &lt;start vertex label&gt;&lt;space&gt;&lt;finish vertex label&gt;&lt;space&gt;&lt;weight&gt;&lt;comma&gt;<br>" +
                "-To create random graph input vertex amount, edge amount, minimal and maximal edge weights as " +
                "integer decimal numbers in corresponding fields and press button 'Create random graph'.<br>" +
                "Remember that maximal amount of vertexes is ceil(10/max_weight)*ceil(9/max_weight);" +
                " min weight is positive and not bigger than max_weight; " +
                "edge amount is less than 2*vertex_amount-1; " +
                "max weight is less than 9<br>" +
                "-To create graph only with unary edges input vertex amount in corresponding field and press button 'Create unary graph'<br>" +
                "-To use mouse for making graph modification:<br>" +
                "* Move mouse into graph area on the left side of the application window<br>" +
                "* Fast left button click creates vertex if this position is free<br>" +
                "* Pressing left button for 2-5 seconds on position with vertex marks this vertex as start if start vertex doesn't" +
                " mark. Else vertex marked as finish<br>" +
                "* Pressing left button for more than 7 seconds on position with vertex deletes this vertex<br>" +
                "* For creating new edge make right button click on position with start vertex and then make right button click on position with finish vertex<br>" +
                "* For deleting edge make third button (wheel) click on position with start vertex and then make third button (wheel) click on position with finish vertex<br>" +
                "<br>2) Describing of buttons<br>" +
                "-Button 'Step back' returns algorithm to previous state<br>" +
                "-Button 'Step forward' makes one algorithm step<br>" +
                "-Button 'Reset algorithm' reset algorithm data like start and finish vertexes<br>" +
                "-Button 'Go to end' shows the algorithm result<br>" +
                "-Button 'Help' shows help message<br>" +
                "<br>3) Describing of visualization<br>" +
                "-Red transparent square is mark of position where was the last mouse click<br>" +
                "-Green vertex is usual vertex<br>" +
                "-Yellow vertex is vertex in open set of algorithm<br>" +
                "-Grey vertex is vertex in close set of algorithm<br>" +
                "-Red vertex is vertex in path to current vertex" +
                "-Cyan vertex is start vertex<br>" +
                "-Purple vertex is finish vertex<br>" +
                "-Blue vertex is current vertex<br>" +
                "<br>4) Some adds<br>" +
                "-In section 3) the lowest categories have the highest display priority<br>" +
                "-In case of same complexity estimation for several vertexes the first of these vertexes added to open set is preferred<br>" +
                "-To remove red transparent square on the graph move mouse cursor out of bounds of graph visualization area</html>");
    }
}
