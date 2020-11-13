package ex0;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/** a class for algorithms for Graph_DS class.
 * contains the graph the algorithms affect.
  */
public class Graph_Algo implements graph_algorithms {

    private graph _graph;

    public Graph_Algo(){}           //basic constructor

    public Graph_Algo(graph g0) {   //constructor that sets the graph
        _graph = g0;
    }

    /** set the graph the algorithms are affecting.
      * @param g
     */
    @Override
    public void init(graph g) {
        _graph = g;
    }

    /** create and return a deep copy of the graph.
     * @return
     */
    @Override
    public graph copy() {
        Graph_DS newGr = new Graph_DS();                                  //initialize the new graph
        List<node_data> nodes = (List<node_data>) _graph.getV();
        for(node_data node : nodes){                                      //for every node in the graph, create a new node for newGr
            NodeData tempN = new NodeData();
            tempN.setKey(node.getKey());                                  //copy the key of the original nodes
            newGr.addNode(tempN);                                         //add the new nodes
        }
        for(node_data node: nodes){                                       //for every node in the graph, copy its neighbors
            HashSet<node_data> nis = (HashSet<node_data>) node.getNi();   //get the neighbors of every node in the graph
            int srcKey = node.getKey();
            for(node_data ni : nis){                                      //for every neighbors a node has, connect the copied node
                newGr.connect(srcKey, ni.getKey());                       //to its copied neighbors
            }
        }
        return newGr;
    }

    /** return the result of if graph is connected.
     * if you can reach every node from any node by
     * moving on the edges, the graph is connected
     * @return
     */
    @Override
    public boolean isConnected() {                                       //the function utilize the BFS algorithm
        clearTag();                                                      //clear the tags from previous algorithms
        LinkedList<node_data> q = new LinkedList<node_data>();           //create a queue to hold the nodes that are checked
        List<node_data> nodes = (List<node_data>) _graph.getV();
        if(nodes.size() == 0 || nodes.size() == 1){                      //if the graph is empty or is just a node
            return true;                                                 //the graph is connected
        }
        node_data src = nodes.get(0);                                    //the starting node for the algorithm
        q.add(src);
        while(!q.isEmpty()){                                             //if the queue is empty then we checked every node
            src = q.remove();                                            //remove the node we checked
            HashSet<node_data> nis = (HashSet<node_data>) src.getNi();   //get the src neighbors
            for(node_data ni : nis){                                     //for every node in src's neighbors,
                if(ni.getTag() == 0){                                    //check if the node was visited (default 0 = false)
                    q.add(ni);                                           //if not add it to the queue of unvisited nodes
                }
                ni.setTag(1);                                            //after the node was checked set the tag accordingly
            }
        }
        for(node_data node1 : nodes){                                    //check every node int the graph
           if(node1.getTag() == 0){                                      //if there is a node that wasn't visited
               return false;                                             //then the algorithm couldn't reach the node because he wasn't connected
           }
        }
        return true;
    }

    /** return the shortest path from the one node to another.
     * if src isn't connected to dest returns -1.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public int shortestPathDist(int src, int dest) {                        //the function utilize the BFS algorithm
        clearTag();                                                         //clear the tags from previous algorithms
        if(src == dest){                                                    //if src = dest, the distant is 0
            return 0;
        }
        int dist = 1;                                                       //the distant that will be returned
        LinkedList<node_data> q = new LinkedList<node_data>();                      //create a queue to hold the nodes that are checked
        node_data node = _graph.getNode(src);
        q.add(node);
        while(!q.isEmpty() && node.getKey() != dest){                       //keep searching the dest node until found or run out of nodes
            node = q.remove();                                              //remove the node that was checked
            HashSet<node_data> nis = (HashSet<node_data>) node.getNi();     //get the neighbors of the node
            for(node_data ni : nis){                                        //check every neighbor ni in node
                if(ni.getKey() == dest){                                    //if found return dist
                    dist = node.getTag() + 1;                               //the node tag also signifies the node distant from the src
                    return dist;
                }
                if(ni.getTag() == 0 && ni.getKey() != src){                 //checks if ni is unvisited or not the src node
                    q.add(ni);                                              //add the unvisited ni to checking queue
                    ni.setTag(node.getTag() + 1);                           //set the tag as the distant from the src
                }
            }
        }
        return -1;                                                          //default return if the nodes are not connected
    }

    /** return a list of the nodes in the shortest path from src to dest.
     * return an empty list if there is no path.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {            //the function utilize the BFS algorithm
        clearTag();                                                     //clear the tags from previous algorithms
        int dist = shortestPathDist(src, dest);                         //use the shortestPathDist() to set the tags as a guide to the path
        ArrayList<node_data> path = new ArrayList<node_data>();         //the path list
        if(dist == -1){                                                 //if there is no path return an empty list
            return path;
        }
        node_data node = _graph.getNode(dest);                          //add the dest node to the list
        path.add(node);
        if(src == dest){                                                //if dest = src, the path is only the dest
            return path;
        }
        HashSet<node_data> nis;                                         //HashSet to hold the node's neighbors
        for(int i = dist-1; i > 0; i--){                                //a loop to add all the nodes to the path
            nis = (HashSet<node_data>) node.getNi();                    //set a new set of neighbors
            for(node_data ni : nis){                                    //check every neighbor ni from nis
                if(ni.getTag() == i){                                   //if ni has a tag of it's index in the path add it
                    path.add(ni);                                       //the distant from src is the same as tag and the index of the path
                    node = ni;                                          //set node as ni to continue from him
                    break;                                              //the wanted node was found no need to keep looping
                }
            }
        }
        path.add(_graph.getNode(src));                                  //add the final node src and return it
        return path;
    }

    //a private function to clear the tags to stop conflicting algorithms
    private void clearTag(){
        List<node_data> nodes = (List<node_data>) _graph.getV();        //set every node in the graph to default tag of 0
        for(node_data node : nodes){
            node.setTag(0);
        }
    }
}
