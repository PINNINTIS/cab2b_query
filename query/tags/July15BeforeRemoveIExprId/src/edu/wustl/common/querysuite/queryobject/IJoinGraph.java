
package edu.wustl.common.querysuite.queryobject;

import java.util.List;

import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.metadata.associations.IAssociation;
/**
 * A rooted, directed acyclic graph with expressions as vertices, and
 * associations as edges.<br>
 * The graph will always contain all the expressions' ids (obtained from
 * {@link edu.wustl.common.querysuite.queryobject.IConstraints}) as vertices. The vertices
 * will be added to/removed from the joingraph as and when expressions are added
 * to/removed from
 * {@link edu.wustl.common.querysuite.queryobject.IConstraints}. The methods
 * in joingraph can only add/remove associations among the vertices. <br>
 * If v1 and v2 are two vertices, the direction will be v1->v2 if v2 is a
 * subexpression of v1. This user of the API will generally build the joingraph
 * based on metadata, and in cases of ambiguity, the user will help forming this
 * graph. <br>
 * This graph basically determines the join conditions in the query. e.g. for
 * each edge (v1, v2) between two vertices v1 and v2, there will be a join
 * between the functional classes of the expressions denoted by v1 and v2; and
 * the join condition is determined by the info in the
 * {@link edu.wustl.common.querysuite.queryobject.IAssociation}.
 * @version 1.0
 * @updated 11-Oct-2006 02:57:02 PM
 */
public interface IJoinGraph extends IBaseQueryObject
{

	/**
	 * To get the association between two Expression ids.
	 * @param parentExpressionId The parent Expression id.
	 * @param childExpressionId The child Expression id.
	 * @return The association betweent the thwo Expression ids.
	 */
	IAssociation getAssociation(IExpressionId parentExpressionId, IExpressionId childExpressionId);

	/**
	 * To put the association in the joingraph.
	 * @param parentExpressionId The parent Expression id to be added in joingraph.
	 * @param childExpressionId The child Expression id to be added in joingraph.
	 * @param association The association between two expression ids.
	 * @return previous association for the given expressionId's which was
	 *         overwritten by this association; null if no association existed
	 *         previously.
	 * @throws CyclicException
	 *             if adding this edge will cause a cycle in the graph
	 */
	IAssociation putAssociation(IExpressionId parentExpressionId, IExpressionId childExpressionId,
			IAssociation association) throws CyclicException;

	/**
	 * Removes the association from the graph.
	 * @param parentExpressionId The parent Expression id
	 * @param childExpressionId The child Expression id
	 * @return true if the association between the specified expressions existed.
	 */
	boolean removeAssociation(IExpressionId parentExpressionId, IExpressionId childExpressionId);

	/**
	 * indicates if the graph is a connected graph. for building queries
	 * (DCQL/CQL) the graph should be connected. So this can be used by UI and
	 * querybuilding code for validation purposes.
	 * @return true if the graph is connected graph, else returns false.
	 */
	boolean isConnected();

	/**
	 * To check wether there is an association between two Expression ids.
	 * @param parentExpressionId The parent Expression id.
	 * @param childExpressionId The child Expression id.
	 * @return  true if the graph contains an association between the specified
	 * expressionIds.
	 */
	boolean containsAssociation(IExpressionId parentExpressionId, IExpressionId childExpressionId);

	/**
	 * For each element in IExpressionId list, the root node will be checked 
	 * for incoming edges for that element.The node having no incomming edges 
	 * will be treated as Root node. 
	 * @return root node of the join graph, null if no root exists for the expression tree
	 * @throws MultipleRootsException if more than 1 roots exists.
	 */
	IExpressionId getRoot() throws MultipleRootsException;

	/**
	 * To get the list of Parents of the given ExpressionId.
	 * @param childExpressionId the Child Expression Id reference.
	 * @return The List parent of ExpressionId for th given childExpressionId. 
	 */
	List<IExpressionId> getParentList(IExpressionId childExpressionId);

	/**
	 * To get the list of children of the given ExpressionId.
	 * @param expressionId the expr id whose children are to be found.
	 * @return children of given expressionId. 
	 */
	List<IExpressionId> getChildrenList(IExpressionId expressionId);
	
	List<IExpressionId> getAllRoots();
}
