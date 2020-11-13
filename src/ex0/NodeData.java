package ex0;

import java.util.Collection;
import java.util.HashSet;

/** a node class for the use of the Graph_DS class,
 *  contains a key and a collection of neighboring nodes.
 */
public class NodeData implements node_data {

    private static int _ID = 0;                                           //a static variable to make a unique key when initializing
    private int _key;                                                     //the value and id of the node
    private int _tag = 0;                                                 //a tag variable for graph algorithms
    private HashSet<node_data> _neighbors = new HashSet<node_data>();             //a collection of all the neighboring nodes
    String _info;                                                         //meta data info

    /** NodeData constructor,
     * creates a unique key by incrementing the static ID every initialization.
     */
    public NodeData(){
        _key = _ID;
        _ID++;
    }

    /** sets the nodes key,
     * mainly for copying algorithm.
     * @param key
     */
    public void setKey(int key){
        _key = key;
    }

    /** return the nodes key.
     * @return
     */
    @Override
    public int getKey() {
        return _key;
    }

    /** returns a collection of the node neighbors.
     * @return
     */
    @Override
    public Collection<node_data> getNi() {
        return _neighbors;
    }

    /** return if the node has a neighboring node with the key.
     * @param key
     * @return
     */
    @Override
    public boolean hasNi(int key) {
        for(node_data node : _neighbors){     //check every node in the neighbors collection
            if(node.getKey() == key){         //if there is a neighbor with the matching key
                return true;                  //return the result
            }
        }
        return false;                         //default return if we cant find the key
    }

    /** add the node t as a neighbor to this node.
     * if t is already neighboring this node than nothing changes.
     * a node will not point more than once to a neighboring node.
     * @param t
     */
    @Override
    public void addNi(node_data t) {
        if(t != null && !_neighbors.contains(t) && this.getKey() != t.getKey()){      //checks if t is already a neighbor
            _neighbors.add(t);                                                        //adds t to the neighbor collection
            t.addNi(this);                                                         //recursively does the same for this node in t
        }                                                                             //the recursion will run no more twice
    }

    /** removes the node from the neighbor collection from this node.
     *  removing a none existing node will not return or cause an error.
     * @param node
     */
    @Override
    public void removeNode(node_data node) {
        if(node != null && _neighbors.contains(node)){            //checks if the node is not null
            _neighbors.remove(node);                              //and if the node is neighboring this node
            node.removeNode(this);                                //remove the node from the neighbors collection
        }                                                         //does the same recursively to this node with node
    }                                                             //the recursion will run no more than twice

    /** return the stored info.
     * @return
     */
    @Override
    public String getInfo() {
        return _info;
    }

    /** sets the node's info.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        _info = s;
    }

    /** return the node's tag.
     * @return
     */
    @Override
    public int getTag() {
        return _tag;
    }

    /** sets the node's tag.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        _tag = t;
    }

    /** toString override.
     *  return a string with the node's details.
     *  the string contains [key] : [neighbor 1] [neighbor 2] ... [neighbor n]. [tag].
     * @return
     */
    public String toString(){
        String info = "[" + _key + "]:";     //set the string header with the key
        for(node_data ni : _neighbors){      //for every neighbor in the collection
            info += " " + ni.getKey();       //we add it to info string
        }
        info +=". tag = " + _tag + ".";      //finally adds the tag to close the string
        return info;
    }
}
