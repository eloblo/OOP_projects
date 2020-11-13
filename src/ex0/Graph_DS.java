package ex0;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/** a graph class that represent a undiractinal graph
 *  contains a collection of nodes and the number of edges and vertices
 */
public class Graph_DS implements graph {

    private LinkedList<node_data> _nodes = new LinkedList<node_data>();   //collection of nodes for the use of outside user
    private HashMap<Integer, node_data> _graphMap = new HashMap<Integer, node_data>();     //a private hashMap for efficient management of the class functions
    private int _eSize = 0;                                               //edge number in the graph
    private int _mCount = 0;                                              //modification count for debugging purposes

    /** return the node with the corresponding key.
     *  if the node does not exist return null
     * @param key - the node_id
     * @return
     */
    @Override
    public node_data getNode(int key) {
        if(_graphMap.containsKey(key)){
            return _graphMap.get(key);
        }
        return null;
    }

    /** check if the graph contains an edge between the nodes.
     *  returns the result.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(_graphMap.containsKey(node1) && _graphMap.containsKey(node2)){     //checks if graph contains the nodes
            node_data Node1 = _graphMap.get(node1);                           //from the keys get the nodes
            node_data Node2 = _graphMap.get(node2);
            HashSet<node_data> nis = (HashSet<node_data>) Node1.getNi();      //get hashSet of neighbors from the first node
            return nis.contains(Node2);                                       //if one of the nodes is a neighbors of the other there is an edge
        }
        return false;   //default answer if the nodes don't fill the function requirements
    }

    /** add a node n to graph.
     *  adding a node twice does not change the graph structure,
     *  the graph will not have duplicate nodes.
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if(n != null && !_graphMap.containsValue(n)){      //check if n is not null and if he is already in the graph
            _graphMap.put(n.getKey(), n);                  //adds n to the hashMap by its key
            _nodes.add(n);                                 //adds n to to collection of nodes
            _mCount++;
        }
    }

    /** connect two existing nodes in the graph.
     * @param node1
     * @param node2
     */
    @Override
    public void connect(int node1, int node2) {
        if(node1 != node2 && !this.hasEdge(node1, node2)){       //checks if the nodes are different and if they are already connected
            _graphMap.get(node1).addNi(_graphMap.get(node2));    //connect the nodes
            _eSize++;                                            //an edge was created so the number of edges and modifications is updated
            _mCount++;
        }
    }

    /** return the collection of all the nodes in the graph.
     * @return
     */
    @Override
    public Collection<node_data> getV() {
        return _nodes;
    }

    /** return a collection of neighbors of a specific node.
     *  if the node does not exist return an empty collection.
     * @param node_id
     * @return
     */
    @Override
    public Collection<node_data> getV(int node_id) {
        if(_graphMap.containsKey(node_id)){
            return this.getNode(node_id).getNi();
        }
        Collection<node_data> nis = new HashSet<node_data>();
        return nis;
    }

    /** removes a specific node from the graph
     * and all his edges.
     * the function returns the removed node.
     * @param key
     * @return
     */
    @Override
    public node_data removeNode(int key) {
        if(_graphMap.containsKey(key)) {                                           //checks if the node exist in the graph
            node_data node = this.getNode(key);
            Collection<node_data> nis = node.getNi();                              //get the collection of neighbors from the node
            if(!nis.isEmpty()){                                                    //check if the node has neighbors
                LinkedList<node_data> nisTemp = new LinkedList<node_data>(nis);            //copy the list of neighbors to a template list
                for(node_data ni : nisTemp){                                       //as it can create conflict with nis set
                    ni.removeNode(node);                                           //for every neighbor ni the node has, the node is removed from
                    _eSize--;                                                      //the ni's collection of neighbors
                }                                                                  //every removed neighbor is an edge being severed so the count is updated
            }
            _nodes.remove(node);                                                   //remove the node from the graph
            _graphMap.remove(node.getKey());
            _mCount++;
            return node;
        }
        return null;
    }

    /** remove the edge between node1 and node2.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(this.hasEdge(node1, node2)){                              //checks if the edge exist
            this.getNode(node1).removeNode(this.getNode(node2));     //remove the nodes from each other's neighbors collection
            _eSize--;                                                //update the edge and modification count
            _mCount++;
        }
    }

    /** returns the number of nodes in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return _nodes.size();
    }

    /** returns the numbers of edges the graph.
     * @return
     */
    @Override
    public int edgeSize() {
        return _eSize;
    }

    /** returns the number modification done to the graph.
      * @return
     */
    @Override
    public int getMC() {
        return _mCount;
    }

    /** a toString function of Graph_DS class.
     * return the number of nodes, edge number, modification count
     * and every node in the graph.
     * @return
     */
    public String toString(){
        String info = "size : " + _nodes.size() + ", edges : " + _eSize + ", modifications : " + _mCount;    //the basic info of the graph
        for(node_data node : _nodes){                                                                 //add a table of the nodes info
            info += "\n" + node.toString();
        }
        return info;
    }
}
