import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Stack;
import java.util.Random;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

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
	private Set<Edge> uniqueEdges;
	private Map<Node,Set<Node>> adjacency;

	/**
	 * Load social network from file and visualize.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File file = new File("graph.gz");
		SocialNetwork network = new SocialNetwork();
		network.load(file);
		network.visualize();
	}

	public void load(File file) throws IOException {
		GraphFileScanner scanner = new GraphFileScanner(file);
		while (scanner.hasNextLine()) {
			List<String> line = scanner.nextLine();
            Node from = new Node(line.get(0));
            Node to = new Node(line.get(1));
            int count = Integer.parseInt(line.get(2));
            if (count > 5
					&& !from.getName().contains("follow")
					&& !to.getName().contains("follow")
					&& !from.getName().contains("retweet")
					&& !to.getName().contains("retweet"))
                add(new Edge(from, to));
		}

		int maxDegree = 0;
		for (Node node : adjacency.keySet()) {
			node.setCategory(null);
			maxDegree = Math.max(maxDegree, adjacency.get(node).size());
		}
		int category = 0;
		for (Node node : adjacency.keySet()) {
			if (node.getCategory() == null)
				setComponentCategory(node, category++);
			node.setScale(adjacency.get(node).size() * 10 / (double) maxDegree);
		}
	}

	/**
	 * Initialize new, empty social network.
	 */
	public SocialNetwork() {
		uniqueEdges = new HashSet<Edge>();
		adjacency = new HashMap<Node,Set<Node>>();
	}

	/**
	 * Add edge to social network (nodes of edge can be existing
	 * or new).
	 * @param edge
	 */
	public void add(Edge edge) {
		uniqueEdges.add(edge);
		updateAdjacency(edge.getFrom(), edge.getTo());
		updateAdjacency(edge.getTo(), edge.getFrom());
	}

	private void updateAdjacency(Node from, Node to) {
		if (! adjacency.containsKey(from))
			adjacency.put(from, new HashSet<Node>());

		adjacency.get(from).add(to);
	}

	public int getNumNodes() { return adjacency.size(); }
	public int getNumEdges() { return uniqueEdges.size(); }

	/**
	 * Visualize social network in simple interactive Swing window.
	 */
	public void visualize() {
		UndirectedGraph<Node, Edge> graph = makeGraph();

		// Set up layout algorithm
		FRLayout<Node, Edge> layout = new FRLayout<Node, Edge>(graph);
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
				public Shape transform(Node s) {
					Shape origShape = new Ellipse2D.Double(-5, -5, 10, 10);
					return AffineTransform
						.getScaleInstance(s.getScale(), s.getScale())
						.createTransformedShape(origShape);
				}
			});
		// Make vertex color depend on category
		vv.getRenderContext().setVertexFillPaintTransformer(
			new VertexCategoryPainter());
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

	private void setComponentCategory(Node root, int category) {
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);

		while (! stack.isEmpty()) {
			Node node = stack.pop();
			if (node.getCategory() == null) {
				node.setCategory(category);
				Set<Node> neighbors = adjacency.get(node);
				for (Node neighbor : neighbors) {
					stack.push(neighbor);
				}
			}
		}
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
		for (Node node : adjacency.keySet()) {
			graph.addVertex(node);
		}
		for (Edge edge : uniqueEdges) {
			graph.addEdge(edge, edge.getFrom(), edge.getTo());
		}
		return graph;
	}

	private static class VertexCategoryPainter
			implements Transformer<Node,Paint> {
		private static final Color DEFAULT_COLOR = Color.RED;
		private static final float TARGET_RGB_SUM = 1;
		private Map<Integer,Color> colorMap;
		private Random prng;

		public VertexCategoryPainter() {
			colorMap = new HashMap<Integer,Color>();
			prng = new Random((int) new Date().getTime());
		}

		@Override
		public Paint transform(Node s){
			if (s.getCategory() == null) {
				return DEFAULT_COLOR;
			} else if (colorMap.containsKey(s.getCategory())) {
				return colorMap.get(s.getCategory());
			} else {
				float r = prng.nextFloat();
				float g = Math.max(0, TARGET_RGB_SUM - r) * prng.nextFloat();
				float b = TARGET_RGB_SUM - r - g;
				Color color = new Color(r, g, b);
				colorMap.put(s.getCategory(), color);
				return color;
			}
		}
	}
}
