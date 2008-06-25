package edu.wustl.common.querysuite.queryobject.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.util.Graph;

/**
 * @author Mandar Shidhore
 * @author chetan_patil
 * @version 1.0
 * @created 12-Oct-2006 15.00.04 AM
 * @hibernate.class table="QUERY_JOIN_GRAPH"
 * @hibernate.cache usage="read-write"
 */
public class JoinGraph extends BaseQueryObject implements IJoinGraph {
	private static final long serialVersionUID = 2671567170682456142L;

	private Graph<IExpressionId, IAssociation> graph;

	private Collection<GraphEntry> graphEntryCollection = new HashSet<GraphEntry>();

	/**
	 * Default Constructor
	 */
	public JoinGraph() {
		graph = new Graph<IExpressionId, IAssociation>();
	}

	/**
	 * Returns the identifier assigned to BaseQueryObject.
	 * 
	 * @return a unique id assigned to the Condition.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="JOIN_GRAPH_SEQ"
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the graphEntryList
	 * @hibernate.set name="graphEntryCollection" cascade="all-delete-orphan"
	 *                lazy="false"
	 * @hibernate.collection-key column="QUERY_JOIN_GRAPH_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.common.querysuite.queryobject.impl.GraphEntry"
	 * @hibernate.cache usage="read-write"
	 */
	public Collection<GraphEntry> getGraphEntryCollection() {
		return graphEntryCollection;
	}

	private void setGraphEntryCollection(
			Collection<GraphEntry> graphEntryCollection) {
		this.graphEntryCollection = graphEntryCollection;
	}

	/**
	 * This method refreshes the graphEntry collection with the entries in
	 * graph. This method is used during pre-processing query before persisting
	 * it.
	 */
	public void contemporizeGraphEntryCollectionWithJoinGraph() {
		graphEntryCollection.clear();

		for (IExpressionId vertex : graph.getVertices()) {
			Set<IExpressionId> outgoingVertices = graph
					.getOutNeighbours(vertex);
			for (IExpressionId outgoingVertex : outgoingVertices) {
				IAssociation association = this.getAssociation(vertex,
						outgoingVertex);

				GraphEntry graphEntry = new GraphEntry(vertex, outgoingVertex,
						association);
				graphEntryCollection.add(graphEntry);
			}
		}
	}

	/**
	 * To check wether there is an association between two Expression ids.
	 * 
	 * @param parentExpressionId
	 *            The parent Expression id.
	 * @param childExpressionId
	 *            The child Expression id.
	 * @return true if the graph contains an association between the specified
	 *         expressionIds.
	 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#containsAssociation(edu.wustl.common.querysuite.queryobject.IExpressionId,
	 *      edu.wustl.common.querysuite.queryobject.IExpressionId)
	 */
	public boolean containsAssociation(IExpressionId parentExpressionId,
			IExpressionId childExpressionId) {
		return graph.containsEdge(parentExpressionId, childExpressionId);
	}

	/**
	 * To get the association between two Expression ids.
	 * 
	 * @param parentExpressionId
	 *            The parent Expression id.
	 * @param childExpressionId
	 *            The child Expression id.
	 * @return The association betweent the thwo Expression ids.
	 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#getAssociation(edu.wustl.common.querysuite.queryobject.IExpressionId,
	 *      edu.wustl.common.querysuite.queryobject.IExpressionId)
	 */
	public IAssociation getAssociation(IExpressionId parentExpressionId,
			IExpressionId childExpressionId) {
		return graph.getEdge(parentExpressionId, childExpressionId);
	}

	/**
	 * Checks if the graph is connected by getting the root IExpressionId The
	 * traversing is done on root and if more than one root found, the graph is
	 * considered to be disjoint and a MultipleRootsException is thrown
	 * 
	 * @return true if graph is connected; false if graph is disjoint
	 */
	public boolean isConnected() {
		return graph.isConnected();
	}

	/**
	 * @param parentExpressionId
	 *            The parent Expression id to be added in joingraph.
	 * @param childExpressionId
	 *            The child Expression id to be added in joingraph.
	 * @param association
	 *            The association between two expression ids.
	 * @return previous association for the given expressionId's which was
	 *         overwritten by this association; null if no association existed
	 *         previously.
	 * @throws CyclicException
	 *             if adding this edge will cause a cycle in the graph
	 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#putAssociation(edu.wustl.common.querysuite.queryobject.IExpressionId,
	 *      edu.wustl.common.querysuite.queryobject.IExpressionId,
	 *      edu.wustl.common.querysuite.queryobject.IAssociation)
	 */
	public IAssociation putAssociation(IExpressionId parentExpressionId,
			IExpressionId childExpressionId, IAssociation association)
			throws CyclicException {
		if (graph.willCauseNewCycle(parentExpressionId, childExpressionId)) {
			throw new CyclicException(
					"Adding this association causes a cycle in the graph.");
		}
		return graph
				.putEdge(parentExpressionId, childExpressionId, association);
	}

	/**
	 * Removes the association from the graph.
	 * 
	 * @param firstExpressionId
	 *            The parent Expression id
	 * @param secondExpressionId
	 *            The child Expression id
	 * @return true if the association between the specified expressions
	 *         existed.
	 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#removeAssociation(edu.wustl.common.querysuite.queryobject.IExpressionId,
	 *      edu.wustl.common.querysuite.queryobject.IExpressionId)
	 */
	public boolean removeAssociation(IExpressionId firstExpressionId,
			IExpressionId secondExpressionId) {
		return graph.removeEdge(firstExpressionId, secondExpressionId) != null;
	}

	/**
	 * Removes the specified id from the list of IExpressionId if one exists
	 * 
	 * @param id
	 *            The Expression id to be removed.
	 * @return true upon removing specified existing id; false otherwise
	 */
	public boolean removeIExpressionId(IExpressionId id) {
		return graph.removeVertex(id);
	}

	/**
	 * For each element in IExpressionId list, the root node will be checked for
	 * incoming edges for that element.The node having no incomming edges will
	 * be treated as Root node.
	 * 
	 * @return root node of the expression, null if no root exists for the
	 *         expression tree
	 * @throws MultipleRootsException
	 *             if more than 1 roots exists.
	 */
	public IExpressionId getRoot() throws MultipleRootsException {
		Set<IExpressionId> unReachableNode = graph.getUnreachableVertices();

		if (unReachableNode.size() == 0) {
			return null;
			// Prafull: instead of throwing exception it will return null.
			// throw new MultipleRootsException("No Root Exist for the Joing
			// Graph!!!!");
		}

		if (unReachableNode.size() != 1) {
			throw new MultipleRootsException(
					"Multiple Root Exist for the Joing Graph");
		}
		return unReachableNode.iterator().next();
	}

	/**
	 * To add vertex in joingraph.
	 * 
	 * @param expressionId
	 *            the expression to be added in join graph.
	 * @return true upon adding vertex to existing vertex list; false otherwise
	 */
	public boolean addIExpressionId(IExpressionId expressionId) {
		return graph.addVertex(expressionId);
	}

	/**
	 * To get the list of Parents of the given ExpressionId.
	 * 
	 * @param childExpressionId
	 *            the Child Expression Id reference.
	 * @return The List parent of ExpressionId for th given childExpressionId.
	 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph#getParentList(edu.wustl.common.querysuite.queryobject.IExpressionId)
	 */
	public List<IExpressionId> getParentList(IExpressionId childExpressionId) {
		return asList(graph.getInNeighbours(childExpressionId), exprIdCmp);
	}

	/**
	 * To get the path of the given ExpressionId from the root Expression.
	 * 
	 * @param expressionId
	 *            reference to ExpressionId
	 * @return the List of paths of the given ExpressionId from root. returns
	 *         empty path list if there is no path.
	 * @throws MultipleRootsException
	 *             if more than 1 roots exists.
	 */
	public List<List<IExpressionId>> getPaths(IExpressionId expressionId)
			throws MultipleRootsException {
		return asList(graph.getVertexPaths(getRoot(), expressionId));
	}

	/**
	 * To the the List of intermediate ExpressionId between the given soure &
	 * target ExpressionIds with the given List of association.
	 * 
	 * @param source
	 *            The Source Expression Id.
	 * @param target
	 *            The Target ExpressionId.
	 * @param associations
	 *            The List of Associations.
	 * @return List of intermediate ExpressionIds for matching List of
	 *         Associations. null, if there is no path between source & target
	 *         or There is no matching path with the given associations.
	 */
	public List<IExpressionId> getIntermediateExpressions(IExpressionId source,
			IExpressionId target, List<IAssociation> associations) {
		// getting path between two vertices.
		Set<List<IExpressionId>> reachablePaths = graph.getVertexPaths(source,
				target);

		for (List<IExpressionId> rechablePath : reachablePaths) {
			// compairing path size & the association list size.
			// This path includes source & target ExprssionIds.
			// For maching path, if there are 2 associations in list, there
			// there will be 3 expressions in obtained path.
			if (rechablePath.size() == associations.size() + 1) {
				// check whether its matching ot not.
				boolean isMatching = true;
				for (int index = 0; index < rechablePath.size() - 1; index++) {
					IExpressionId fromExpId = rechablePath.get(index);
					IExpressionId toExpId = rechablePath.get(index + 1);
					// check for association match
					if (associations.get(index).equals(
							graph.getEdge(fromExpId, toExpId))) {
						fromExpId = toExpId;
					} else {
						// association is not matching, so this path is not the
						// required path.
						isMatching = false;
						break;
					}
				}
				if (isMatching) {
					// matching path found, so logic terminates here.....Their
					// will be only one matching path.
					// Remove source & target ExprssionIds from path.
					rechablePath.remove(0);
					rechablePath.remove(rechablePath.size() - 1);
					return rechablePath;
				}
			}
		}

		// No matching path found !!!!
		return null;
	}

	/**
	 * To get the first path of the given ExpressionId from the root Expression.
	 * 
	 * @param expressionId
	 *            regerence to ExpressionId
	 * @return the List of vertices representing path of the given ExpressionId
	 *         from root. returns empty path list if there is no path.
	 * @throws MultipleRootsException
	 *             if more than 1 roots exists.
	 */
	public List<IExpressionId> getPath(IExpressionId expressionId)
			throws MultipleRootsException {
		Set<List<IExpressionId>> paths = graph.getVertexPaths(getRoot(),
				expressionId);

		if (paths.isEmpty()) {
			return new ArrayList<IExpressionId>();
		}
		return paths.iterator().next();
	}

	/**
	 * To get the edge path of the given ExpressionId from the root Expression.
	 * 
	 * @param expressionId
	 *            reference to ExpressionId
	 * @return the List of paths of the given ExpressionId from root. returns
	 *         empty path list if there is no path.
	 * @throws MultipleRootsException
	 *             if there are multpile roots present in join graph.
	 */
	public List<List<IAssociation>> getEdgesPaths(IExpressionId expressionId)
			throws MultipleRootsException {
		return asList(graph.getEdgePaths(getRoot(), expressionId));
	}

	/**
	 * To get the first edge path of the given ExpressionId from the root
	 * Expression.
	 * 
	 * @param expressionId
	 *            regerence to ExpressionId
	 * @return the List of Associations representing path of the given
	 *         ExpressionId from root. returns empty path list if there is no
	 *         path.
	 * @throws MultipleRootsException
	 *             if there are multpile roots present in join graph.
	 */
	public List<IAssociation> getEdgePath(IExpressionId expressionId)
			throws MultipleRootsException {
		Set<List<IAssociation>> paths = graph.getEdgePaths(getRoot(),
				expressionId);

		if (paths.isEmpty()) {
			return new ArrayList<IAssociation>();
		}
		return paths.iterator().next();
	}

	/**
	 * @param expressionId
	 *            the expr id whose children are to be found.
	 * @return List of Vertices directly reachable from the given vertex.
	 *         Returns null if vertex is not present in graph, Returns empty
	 *         list if vertex has no directly reachable node.
	 */
	public List<IExpressionId> getChildrenList(IExpressionId expressionId) {
		return asList(graph.getOutNeighbours(expressionId), exprIdCmp);
	}

	/**
	 * To get the expressions having multiple parent nodes.
	 * 
	 * @return the List of expression ids having multiple parent nodes.
	 */
	public List<IExpressionId> getNodesWithMultipleParents() {
		List<IExpressionId> nodes = new ArrayList<IExpressionId>();
		for (IExpressionId expression : graph.getVertices()) {
			if (graph.getInNeighbours(expression).size() > 1) {
				nodes.add(expression);
			}
		}
		return nodes;
	}

	public List<IExpressionId> getAllRoots() {
		return asList(graph.getUnreachableVertices(), exprIdCmp);
	}

	private static <T> List<T> asList(Set<T> set) {
		return new ArrayList<T>(set);
	}

	private static <T> List<T> asList(Set<T> set, Comparator<? super T> cmp) {
		List<T> res = new ArrayList<T>(set);
		Collections.sort(res, cmp);
		return res;
	}

	private static final Comparator<IExpressionId> exprIdCmp = new Comparator<IExpressionId>() {

		public int compare(IExpressionId o1, IExpressionId o2) {
			Integer i1 = o1.getInt();
			Integer i2 = o2.getInt();
			return i1.compareTo(i2);
		}
	};
}