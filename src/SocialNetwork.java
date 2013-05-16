import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import java.awt.Font;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class SocialNetwork {
    private Set<Connection> connections;
    private Set<String> nodes;

    public static void main(String[] args) throws IOException {
        SocialNetwork network = new SocialNetwork();

        Scanner scanner = new Scanner(new File(args[0]));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String from = lineScanner.next();
            String to = lineScanner.next();
            int count = lineScanner.nextInt();
            if (count > 25)
                network.add(new Connection(from, to));
        }

        network.visualize();
    }

    public SocialNetwork() {
        connections = new HashSet<Connection>();
        nodes = new HashSet<String>();
    }

    public void add(Connection connection) {
        connections.add(connection);
        nodes.add(connection.getFrom());
        nodes.add(connection.getTo());
    }

    public int getNumNodes() { return nodes.size(); }
    public int getNumConnections() { return connections.size(); }

    public void visualize() {
        UndirectedGraph<String, Connection> graph = makeGraph();

        Layout<String, Connection> layout =
            new ISOMLayout<String, Connection>(graph);
        layout.setSize(new Dimension(1000,700));

        BasicVisualizationServer<String, Connection> server =
            new BasicVisualizationServer<String, Connection>(layout);
        server.setPreferredSize(new Dimension(1024,768));
        server.getRenderContext().setVertexLabelTransformer(
            new ToStringLabeller<String>());
        server.getRenderer().getVertexLabelRenderer().setPosition(
            Position.CNTR);
        server.getRenderContext().setVertexShapeTransformer(
            new Transformer<String,Shape>() {
                @Override
                public Shape transform(String s){
                    return new Ellipse2D.Double(-40, -10, 80, 20);
                }
            });
        server.getRenderContext().setVertexFontTransformer(
            new Transformer<String, Font>(){
                @Override
                public Font transform(String arg0) {
                    Font font = new Font("Arial Unicode MS", Font.PLAIN, 11);
                    return font;
                }
            });
        server.getRenderContext().setVertexFillPaintTransformer(
            new Transformer<String,Paint>() {
                @Override
                public Paint transform(String s) {
                    return Color.GREEN;
                }
            });

        JFrame frame = new JFrame("Social Network");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(server);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public String toString() {
        return makeGraph().toString();
    }

    private UndirectedGraph<String, Connection> makeGraph() {
        UndirectedGraph<String, Connection> graph = 
            new UndirectedSparseGraph<String, Connection>();
        for (String node : nodes) {
            graph.addVertex(node);
        }
        for (Connection connection : connections) {
            graph.addEdge(connection, connection.getFrom(), connection.getTo());
        }
        return graph;
    }
}
