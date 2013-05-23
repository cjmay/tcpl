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
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class SocialNetwork {
    private Set<Connection> connections;
    private Set<String> nodes;

    /**
     * Load social network from file and visualize.
     * @param args
     * @throws IOException
     */
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
            int count = Integer.parseInt(pieces[2]);
            if (count > 25)
                network.add(new Connection(from, to));
        }
        reader.close();

        network.visualize();
    }

    /**
     * Initialize new, empty social network.
     */
    public SocialNetwork() {
        connections = new HashSet<Connection>();
        nodes = new HashSet<String>();
    }

    /**
     * Add connection to social network (nodes of connection can be existing
     * or new).
     * @param connection
     */
    public void add(Connection connection) {
        connections.add(connection);
        nodes.add(connection.getFrom());
        nodes.add(connection.getTo());
    }

    public int getNumNodes() { return nodes.size(); }
    public int getNumConnections() { return connections.size(); }

    /**
     * Visualize social network in simple interactive Swing window.
     */
    public void visualize() {
        UndirectedGraph<String, Connection> graph = makeGraph();

        // Set up layout algorithm
        FRLayout<String, Connection> layout =
            new FRLayout<String, Connection>(graph);
        layout.setAttractionMultiplier(0.75);
        layout.setRepulsionMultiplier(0.75);
        layout.setMaxIterations(1000);
        // Set dimensions of box in which graph will be laid out, can be
        // smaller or larger than actual window size
        layout.setSize(new Dimension(1024,768));

        // Create interactive viewer and set window size
        VisualizationViewer<String, Connection> vv =
            new VisualizationViewer<String, Connection>(layout);
        vv.setPreferredSize(new Dimension(1024,768));
        // Label vertices using their toString method
        vv.getRenderContext().setVertexLabelTransformer(
            new ToStringLabeller<String>());
        // Put vertex labels east (right) of vertices
        vv.getRenderer().getVertexLabelRenderer().setPosition(
            Position.E);
        // Make vertex shape a small circle
        vv.getRenderContext().setVertexShapeTransformer(
            new Transformer<String,Shape>() {
                @Override
                public Shape transform(String s){
                    return new Ellipse2D.Double(-5, -5, 10, 10);
                }
            });
        // Color edges gray to more easily see vertex labels
        vv.getRenderContext().setEdgeDrawPaintTransformer(
            new Transformer<Connection,Paint>() {
                @Override
                public Paint transform(Connection s) {
                    return Color.GRAY;
                }
            });
        
        // Use default interactive mouse behavior
        DefaultModalGraphMouse<String, Connection> gm = 
        		new DefaultModalGraphMouse<String, Connection>();
        gm.setMode(DefaultModalGraphMouse.Mode.TRANSFORMING);
        vv.setGraphMouse(gm);

        // Create containing frame and visualize!
        JFrame frame = new JFrame("Social Network");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public String toString() {
        return makeGraph().toString();
    }

    /**
     * Make UndirectedGraph from social network and return it.
     * @return
     */
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
