package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/** a class that simulate a weighted unidirectional graph.
 *  the graph is build on nodes of numbers.
 */
public class WGraph_DS implements weighted_graph {

    private LinkedList<node_info> _nodes = new LinkedList<node_info>();            //list of the nodes for the user
    private HashMap<Integer, NodeInfo> _nodesMap = new HashMap<Integer, NodeInfo>();       //hashMap of nodes and their keys, for quick management of the graph
    private int _eCount = 0;                                               //edge size
    private int _mCount = 0;                                               //number of modification done to the graph

    /**  creates an empty graph */
    public WGraph_DS() {}

    /** return the node by its node_id (key).
     *  if the node does not exist return null.
     * @param key - the node_id
     * @return node_info, null if none
     */
    @Override
    public node_info getNode(int key) {
        if (_nodesMap.containsKey(key)) {
            return _nodesMap.get(key);
        }
        return null;
    }

    /** checks if the edge between the the 2 nodes exist.
     *  the nodes are called by their id's (key)
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (_nodesMap.containsKey(node1) && _nodesMap.containsKey(node2)) {       //check if the nodes exist in the graph
            NodeInfo NODE1 = (NodeInfo) this.getNode(node1);
            NodeInfo NODE2 = (NodeInfo) this.getNode(node2);
            return NODE1.hasNi(NODE2);                                            //if one node is the neighbor of the other
        }                                                                         //than there is an edge
        return false;
    }

    /** return the weight of the edge between the 2 nodes.
     * if the edge does not exist return -1.
     * the nodes are called by their id's (key)
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (_nodesMap.containsKey(node1) && _nodesMap.containsKey(node2)) {      //checks if the nodes exist in the graph
            NodeInfo NODE1 = (NodeInfo) this.getNode(node1);
            NodeInfo NODE2 = (NodeInfo) this.getNode(node2);
            return NODE1.getEdge(NODE2);
        }
        return -1;
    }

    /** adds a node to the graph,
     * any number can be the key of the node.
     * if the node already exist nothing changes.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!_nodesMap.containsKey(key)) {               //checks if the node exist in the graph
            NodeInfo node = new NodeInfo(key);           //create the new node and add it to the graph
            _nodesMap.put(key, node);
            _nodes.add(node);
            _mCount++;
        }
    }

    /** connect 2 node in the graph to create an edge,
     *  the function set the weight of the new edge.
     *  if the edge already exist simply set the new weight to the existing edge.
     *  the weight cannot be equal or smaller than 0.
     *  a node cannot be connected to himself.
     *  in case of invalid weight or nodes nothing changes.
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (node1 != node2 && w > 0 && _nodesMap.containsKey(node1) && _nodesMap.containsKey(node2)) {   //checks if nodes and weight are valid
            NodeInfo NODE1 = _nodesMap.get(node1);
            NodeInfo NODE2 = _nodesMap.get(node2);
            if(NODE1.hasNi(NODE2)){                //if edge exist update the weight
                NODE1.setEdge(NODE2, w);
            }
            else{                                  //else create a new edge
                NODE1.addNi(NODE2, w);
                _eCount++;                         //update the edge size
            }
            _mCount++;
        }
    }

    /** return a collection of all the nodes in the graph.
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV() {
        return _nodes;
    }

    /** return a collection all the neighboring nodes of a specific node
     *  the node is called by its id (key)
     * @param node_id
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        return _nodesMap.get(node_id).getNis();
    }

    /** remove a node from the graph.
     * also break all edge connected to the node.
     * if the node does not exist nothing changes.
     * the node is called by its id (key)
     * @param key
     * @return the removed node.
     */
    @Override
    public node_info removeNode(int key) {
        if (_nodesMap.containsKey(key)) {                                            //checks if the node exist in the graph
            NodeInfo node = (NodeInfo) this.getNode(key);
            HashSet<node_info> nis = node.getNis();                                  //get the collection of neighbors from the node
            if (!nis.isEmpty()) {                                                    //check if the node has neighbors
                LinkedList<node_info> nisTemp = new LinkedList<node_info>(nis);              //copy the list of neighbors to a template list
                for (node_info ni : nisTemp) {
                    NodeInfo niTemp = (NodeInfo) ni;                                 //use a template to cast ni to NodeInfo
                    niTemp.removeNi(node);                                           //for every neighbor niTemp the node has, the node is removed from
                    _eCount--;                                                       //update the edge size
                }
            }
            _nodes.remove(node);                                                     //remove the node from the graph
            _nodesMap.remove(node.getKey());
            _mCount++;
            return node;
        }
        return null;
    }

    /** remove the edge between the 2 nodes.
     *  if the edge or the nodes do not exist nothing changes.
     *  the nodes are called by their id (key)
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {                               //check if the edge exist
            NodeInfo NODE1 = (NodeInfo) this.getNode(node1);
            NodeInfo NODE2 = (NodeInfo) this.getNode(node2);
            NODE1.removeNi(NODE2);                                 //remove the node
            _eCount--;
            _mCount++;
        }
    }

    /** return the number of nodes in the graph.
     * @return
     */
    @Override
    public int nodeSize() {
        return _nodes.size();     //the list of nodes tracks it's size for us
    }

    /** return the number of edges in the graph.
     * @return
     */
    @Override
    public int edgeSize() {
        return _eCount;
    }

    /** return the number of modification done to the graph.
     * @return
     */
    @Override
    public int getMC() {
        return _mCount;
    }

    /** a toString function for the graph.
     * prints the number of nodes edges in the graph,
     * along with a list of every node and his neighbors edges and tags.
     * @return
     */
    public String toString(){
        String sGraph = "nodes: "+_nodes.size()+", edges: "+_eCount;     //the basic information of the graph
        Collection<node_info> graphList = this.getV();
        for(node_info node : graphList){                                 //for every node in the graph
            NodeInfo nodeI = (NodeInfo) node;                            //cast it to NodeInfo
            sGraph += "\n" + nodeI.toString();                           //add its information to the main info String
        }
        return sGraph;
    }

    /** create a string of information from the graph
     *  for the purpose of storing it in a save file.
     *  mainly used for the WGraph_Algo class.
     * @return
     */
    public String toSave(){
        String sGraph = "";
        Collection<node_info> graphList = this.getV();
        for(node_info node : graphList){               //for every node in the graph
            NodeInfo nodeI = (NodeInfo) node;          //cast to NodeInfo
            sGraph += nodeI.toSave() + "\n";           //add the node information to the save string
        }
        return sGraph;
    }

    /////////////////////////////////--NodeInfo--/////////////////////////////////////////////

    //a class of nodes, used to store the information of main class of WGraph_DS.
    private class NodeInfo implements node_info {

        private int _key;                                               //the id of the node
        private double _tag = 0;                                        //tag of the node, mainly for algorithmic purposes
        private String _info;                                           //the nodes information mainly used for algorithmic purposes
        private HashMap<node_info, Double> _niMap = new HashMap<node_info, Double>();   //hashMap of the edges the node is connected to. stores the edge's weight.
        private HashSet<node_info> _neighbors = new HashSet<node_info>();       //hashSet of neighboring nodes for the user's use.

        //create a node with the specific key
        public NodeInfo(int key) {
            _key = key;
        }

        /* adds a node as neighbor and created a weighted edge.
         *  the neighboring node will also add the the node as a neighbor with the same edge.
         *  neighbor cannot be the node himself.
         *  in case of invalid neighbor nothing changes.
         */
        public void addNi(NodeInfo node, double weight) {
            if (node != null && !_niMap.containsKey(node) && _key != node.getKey()) {      //checks if node is a valid neighbor
                _niMap.put(node, weight);                                                  //adds the edge information to the node
                _neighbors.add(node);                                                      //adds node to the neighbor collection
                node.addNi(this, weight);                                            //recursively does the same for this node_info in node
            }                                                                              //the recursion will run no more twice
        }

        /* remove the node from list of neighbors and remove the edge.
         *  in case of none existing neighbor nothing will change.
         */
        public void removeNi(NodeInfo node) {
            if (node != null && _niMap.containsKey(node)) {            //checks if the node is not null and is a neighbor
                _niMap.remove(node);                                   //remove the edge between the nodes
                _neighbors.remove(node);                               //remove the node from the neighbors collection
                node.removeNi(this);                              //does the same recursively to this node_info with node
            }                                                          //the recursion will run no more than twice
        }

        //return if the node hase the specific neighbor
        public boolean hasNi(node_info node) {
            return _neighbors.contains(node);
        }

        //return a HashSet of all the neighboring nodes
        public HashSet<node_info> getNis() {
            return _neighbors;
        }

        /* return the weight of the edge between this node and the inputted node.
         * if the edge does not exist return -1
         */
        public double getEdge(node_info ni) {
            if (_niMap.containsKey(ni)) {
                return _niMap.get(ni);
            }
            return -1;
        }

        /* update the weight of the edge between this node and the inputted node.
         * the function does the same to the inputted node from his side.
         * if the edge does not exist nothing changes.
         */
        public void setEdge(node_info ni, double weight){
            if(_neighbors.contains(ni) && _niMap.get(ni) != weight){    //checks if the edge exist and has a different weight
                _niMap.replace(ni, weight);                             //update the weight of the edge
                NodeInfo niTemp = (NodeInfo) ni;                        //cast the inputted node
                niTemp.setEdge(this, weight);                        //recursively does the same to the inputted node
            }                                                           //the recursion will not run more than twice
        }

        //return the key of the node (id)
        @Override
        public int getKey() {
            return _key;
        }

        //return the info of the node
        @Override
        public String getInfo() {
            return _info;
        }

        //sets the node's info
        @Override
        public void setInfo(String s) {
            _info = s;
        }

        //return the tage of the node
        @Override
        public double getTag() {
            return _tag;
        }

        //sets the node's tag
        @Override
        public void setTag(double t) {
            _tag = t;
        }

        /* a toString function.
         * return a string that hold all the information of the node
         * prints the node's key and all his neighbors and edges
         */
        public String toString(){
            String info = "[" + _key + "]:";                        //set the string header with the key
            for(node_info ni : _neighbors){                         //for every neighbor in the collection
                info += " ["+ni.getKey()+","+this.getEdge(ni)+"]";  //we add it to info string
            }
            info +=". tag = " + _tag + ".";                         //adds the tag to close the string
            return info;
        }

        /* return a string with the nodes information.
         * mainly used to save the node info in a file.
         */
        public String toSave(){
            String info = "[" + _key + "]";                          //set the string header with the key
            for(node_info ni : _neighbors){                          //for every neighbor
                info += "["+ni.getKey()+"|"+this.getEdge(ni)+"]" ;   //add its key and the weight of the edge
            }
            return info;
        }
    }
}
