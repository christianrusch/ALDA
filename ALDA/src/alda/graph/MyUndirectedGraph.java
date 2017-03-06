package alda.graph;

import java.util.List;
import java.util.*;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {
	
	private Map<T, HashMap<T, Edge<T>>> graph = new HashMap<T, HashMap<T, Edge<T>>>();
	private int edgeCount = 0;
	
	private class Edge<T> implements Comparable<Edge<T>> {
		T from;
		T to;
		int edgeWeight;
		
		public Edge(T v1, T v2, int cost) {
			this.from = v1;
			this.to = v2;
			this.edgeWeight = cost;
		}
		
		public int getCost() {
			return this.edgeWeight;
		}
		
//		private T getConnectedVertex(T vertex) {
//			if(vertex.equals(start))
//				return finish;
//			else if(vertex.equals(finish))
//				return start;
//			else
//				throw new NoSuchElementException();
//		}
		
		@Override
		public int compareTo(Edge<T> e) {
			if(this.edgeWeight > e.edgeWeight)
				return 1;
			else if(this.edgeWeight < e.edgeWeight)
				return -1;
			return 0;
		}
		
		public String toString() {
			return from.toString() + " : " + to.toString() + " : " + String.valueOf(edgeWeight);
		}
	}

	@Override
	public int getNumberOfNodes() {
		return graph.size();
	}

	@Override
	public int getNumberOfEdges() {
		return edgeCount;
	}
	
	private boolean isVertex(T vertex) {
		return graph.containsKey(vertex);
	}

	@Override
	public boolean add(T vertex) {
		if( isVertex(vertex) )
			return false;
		graph.put(vertex, new HashMap<T, Edge<T>>());
		return true;
	}

	@Override
	public boolean connect(T vertex1, T vertex2, int cost) {
		if( !isVertex(vertex1) || !isVertex(vertex2) || cost <= 0 )
			return false;
		Edge<T> edge = new Edge<>(vertex1, vertex2, cost);
		graph.get(vertex1).put(vertex2, edge);
		graph.get(vertex2).put(vertex1, edge);
		edgeCount++;
		return true;
	}

	@Override
	public boolean isConnected(T vertex1, T vertex2) {
		return ( graph.get(vertex1).containsKey(vertex2) /* || graph.get(vertex2).containsKey(vertex1) */ );
	}

	@Override
	public int getCost(T vertex1, T vertex2) {
		if( !isVertex(vertex1) || !isVertex(vertex2) )
			return -1;

		Deque<T> vertexChain = new LinkedList<T>(breadthFirstSearch(vertex1, vertex2));
		int cost = 0;
		
		if( vertexChain.size() == 1 && graph.get(vertex1).containsKey(vertex1) ) {
			return graph.get(vertex1).get(vertex1).getCost();
		}
		
		T vertex = null;
		T previousVertex = null;
		
		while(!vertexChain.isEmpty()) {
			vertex = vertexChain.poll();
			if(previousVertex != null) {
				cost += graph.get(previousVertex).get(vertex).getCost();
			}
			previousVertex = vertex;
		}
		return cost;
	}

	@Override
	public List<T> depthFirstSearch(T start, T end) {
		if( !isVertex(start) || !isVertex(end) )
			return new LinkedList<T>();
		
		Stack<T> queue = new Stack<T>();
		Map<T, T> pathMap = new HashMap<T, T>();
		LinkedList<T> returnList = new LinkedList<T>();
		T activeVertex = start;
		
		queue.add(start);
		pathMap.put(start, null);
		
		while( !queue.isEmpty() ) {
			activeVertex = queue.pop();
			for(T adjacentVertex : graph.get(activeVertex).keySet()) {
				if(!pathMap.containsKey(adjacentVertex)) {
					pathMap.put(adjacentVertex, activeVertex);
					queue.push(adjacentVertex);
				}
			}
		}
		
		for(T v = end; pathMap.get(v) != v; v = pathMap.get(v)) {
			returnList.addFirst(v);
		}
		return returnList;
	}

	@Override
	public List<T> breadthFirstSearch(T start, T end) {
		if( !isVertex(start) || !isVertex(end) )
			return new LinkedList<T>();

		Queue<T> queue = new LinkedList<T>();
		Map<T, T> pathMap = new HashMap<T, T>();
		LinkedList<T> returnList = new LinkedList<T>();
		T activeVertex = start;
		
		queue.add(start);
		pathMap.put(start, null);
		
		while( !queue.isEmpty() )
		{
			activeVertex = queue.poll();
			for(T adjacentVertex : graph.get(activeVertex).keySet())
			{
				if(!pathMap.containsKey(adjacentVertex))
				{
					pathMap.put(adjacentVertex, activeVertex);
					queue.offer(adjacentVertex);
				}
			
			}
		
		}
		
		for(T v = end; pathMap.get(v) != v; v = pathMap.get(v)) {
			returnList.addFirst(v);
		}
		return returnList;
	}

	@Override
	public UndirectedGraph<T> minimumSpanningTree() {
		if(graph.isEmpty())
			return new MyUndirectedGraph<T>();
		
		MyUndirectedGraph<T> mst = new MyUndirectedGraph<T>();
		PriorityQueue<Edge<T>> edgeQueue = new PriorityQueue<>();
		Queue<T> vertexQueue = new LinkedList<T>();
		HashMap<T, T> pathMap = new HashMap<>();
		HashMap<T, Integer> costMap = new HashMap<>();
		
		T startingVertex = this.graph.keySet().iterator().next();
		T fromVertex = startingVertex;
		T toVertex = null;
		
		Edge<T> bestEdge = null;
		
		costMap.put(startingVertex, 0);
		pathMap.put(startingVertex, null);
		mst.add(startingVertex);
		vertexQueue.add(startingVertex);
		
		System.out.println("Starting vertex: " + startingVertex.toString() + " : Graph vertices: " + this.getNumberOfNodes());
		
		while(mst.getNumberOfEdges() < graph.size()-1) {
		
			System.out.println("\nMST vertices: " + mst.getNumberOfNodes() + " MST edges: " + mst.getNumberOfEdges() + " : Graph vertices: " + this.getNumberOfNodes() + " : Graph edges: " + this.getNumberOfEdges());
			
			while(!vertexQueue.isEmpty()) {
				System.out.println("In vertexQueue.");
				T v = vertexQueue.poll();
				fromVertex = v;
				for(T w : this.graph.get(fromVertex).keySet()){
					toVertex = w;
					int cost = graph.get(fromVertex).get(toVertex).getCost();
					if(costMap.containsKey(fromVertex))
						cost += costMap.get(fromVertex);
					if(!mst.isVertex(toVertex)) {
						Edge<T> e = new Edge<T>(fromVertex, toVertex, cost);
						edgeQueue.add(e);
						System.out.println("      "+e.toString()+" added to priority queue (minheap).");
					}
				}
			}
			System.out.println("\nEdgeQueue:");
			edgeQueue.forEach(e->System.out.println(e.toString() + " > "));
			System.out.println();
			do {
				bestEdge = edgeQueue.poll();
				System.out.println("Poll: " + bestEdge.from.toString() + " : " + bestEdge.to.toString() + " : " + String.valueOf(bestEdge.getCost()));
			}while(mst.isVertex(bestEdge.to) && !edgeQueue.isEmpty());
				
			vertexQueue.add(bestEdge.to);
			pathMap.put(bestEdge.to, bestEdge.from);
			costMap.put(bestEdge.to, costMap.get(bestEdge.from) + this.graph.get(bestEdge.from).get(bestEdge.to).getCost());
			mst.add(bestEdge.to);
			System.out.println(bestEdge.to.toString());
			mst.connect(bestEdge.from, bestEdge.to, this.graph.get(bestEdge.from).get(bestEdge.to).getCost());
			System.out.println("Edge from " + bestEdge.from.toString() + " to " + bestEdge.to.toString() + " with weight " + costMap.get(bestEdge.to) + " polled from queue and added to graph.");
		}
		System.out.println("\n\n");
		System.out.println("Vertices: " + String.valueOf(mst.graph.keySet().size()) + " Edges: " + String.valueOf(mst.edgeCount));
		System.out.println("\n\n\n\n\n\n\n\n\n\n");
		return mst;
	}

}
