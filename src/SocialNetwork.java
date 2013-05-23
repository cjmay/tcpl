import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Set;
import java.util.HashSet;

import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
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

        File file = new File("user_edges.graph");
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            String pieces[] = line.split("\\s+");
            String from = pieces[0];
            String to = pieces[1];
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
            new FRLayout<String, Connection>(graph);
        layout.setSize(new Dimension(950,700));

        BasicVisualizationServer<String, Connection> server =
            new BasicVisualizationServer<String, Connection>(layout);
        server.setPreferredSize(new Dimension(1024,768));
        server.getRenderContext().setVertexLabelTransformer(
            new ToStringLabeller<String>());
        //server.getRenderer().getVertexLabelRenderer().setPosition(
        //    Position.CNTR);
        //server.getRenderContext().setVertexShapeTransformer(
        //    new Transformer<String,Shape>() {
        //        @Override
        //        public Shape transform(String s){
        //            return new Ellipse2D.Double(-40, -10, 80, 20);
        //        }
        //    });
        //server.getRenderContext().setVertexFillPaintTransformer(
        //    new Transformer<String,Paint>() {
        //        @Override
        //        public Paint transform(String s) {
        //            return Color.GREEN;
        //        }
        //    });

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
