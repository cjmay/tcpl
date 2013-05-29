import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
	private Set<Edge> edges;
	private Set<Node> nodes;

	/**
	 * Load social network from file and visualize.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SocialNetwork network = new SocialNetwork();

		// Read network from file using GraphFileScanner
		File file = new File("user_edges.graph.gz");
		GraphFileScanner scanner = new GraphFileScanner(file);
		while (scanner.hasNextLine()) {
			List<String> line = scanner.nextLine();
            Node from = new Node(line.get(0));
            Node to = new Node(line.get(1));
            int count = Integer.parseInt(line.get(2));
            if (count > 20 && !from.getName().contains("follow") && !to.getName().contains("follow") && !from.getName().contains("retweet") && !to.getName().contains("retweet"))
                network.add(new Edge(from, to));
		}

		network.visualize(0.75, 0.75, 700);
	}

	/**
	 * Initialize new, empty social network.
	 */
	public SocialNetwork() {
		edges = new HashSet<Edge>();
		nodes = new HashSet<Node>();
	}

	/**
	 * Add edge to social network (nodes of edge can be existing
	 * or new).
	 * @param edge
	 */
	public void add(Edge edge) {
		edges.add(edge);
		nodes.add(edge.getFrom());
		nodes.add(edge.getTo());
	}

	public int getNumNodes() { return nodes.size(); }
	public int getNumEdges() { return edges.size(); }

	/**
	 * Visualize social network in simple interactive Swing window
	 * with default layout parameters.
	 */
	public void visualize() {
		visualize(1.75, 0.75, 700);
	}
	
	/**
	 * Visualize social network in simple interactive Swing window.
	 * @param attractionMultiplier factor by which nodes are attracted to
	 *                             one another
	 * @param repulsionMultiplier factor by which nodes are repulsed by
	 *                            one another
	 * @param maxIterations number of iterations for which the layout
	 *                      algorithm will run (don't set this too high!)
	 */
	public void visualize(double attractionMultiplier,
			double repulsionMultiplier, int maxIterations) {
		UndirectedGraph<Node, Edge> graph = makeGraph();

		// Set up layout algorithm
		FRLayout<Node, Edge> layout =
			new FRLayout<Node, Edge>(graph);
		layout.setAttractionMultiplier(attractionMultiplier);
		layout.setRepulsionMultiplier(repulsionMultiplier);
		layout.setMaxIterations(maxIterations);
		// Set dimensions of box in which graph will be laid out, can be
		// smaller or larger than actual window size
		layout.setSize(new Dimension(1024,768));

		// Create interactive viewer and set window size
		VisualizationViewer<Node, Edge> vv =
			new VisualizationViewer<Node, Edge>(layout);
		vv.setPreferredSize(new Dimension(1024,768));
		// Label vertices using their toString method
		vv.getRenderContext().setVertexLabelTransformer(
			new ToStringLabeller<Node>());
		// Put vertex labels east (right) of vertices
		vv.getRenderer().getVertexLabelRenderer().setPosition(
			Position.E);
		// Make vertex shape a small circle
		vv.getRenderContext().setVertexShapeTransformer(
			new Transformer<Node,Shape>() {
				@Override
				public Shape transform(Node s){
					return new Ellipse2D.Double(-5, -5, 10, 10);
				}
			});
		// Color edges gray to more easily see vertex labels
		vv.getRenderContext().setEdgeDrawPaintTransformer(
			new Transformer<Edge,Paint>() {
				@Override
				public Paint transform(Edge s) {
					return Color.GRAY;
				}
			});
		
		// Use default interactive mouse behavior
		DefaultModalGraphMouse<Node, Edge> gm = 
				new DefaultModalGraphMouse<Node, Edge>();
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
	private UndirectedGraph<Node, Edge> makeGraph() {
		UndirectedGraph<Node, Edge> graph = 
			new UndirectedSparseGraph<Node, Edge>();
		for (Node node : nodes) {
			graph.addVertex(node);
		}
		for (Edge edge : edges) {
			graph.addEdge(edge, edge.getFrom(), edge.getTo());
		}
		return graph;
	}
}
