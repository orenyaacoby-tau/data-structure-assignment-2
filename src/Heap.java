
import java.util.LinkedList;
import java.util.List;

/**
 * Heap
 *
 * An implementation of Fibonacci heap over positive integers 
 * with the possibility of not performing lazy melds and 
 * the possibility of not performing lazy decrease keys.
 *
 */
public class Heap
{
    public int size = 0;
    public int totalMarkedNodes = 0;
    public int totalCuts = 0;
    public int totalHeapifyCosts = 0;
    public int totalLinks = 0;
    public final boolean lazyMelds;
    public final boolean lazyDecreaseKeys;
    public HeapItem min;
    private HeapNodeList rootList; //list of root nodes
    
    
    /**
     *
     * Constructor to initialize an empty heap.
     *
     */
    public Heap(boolean lazyMelds, boolean lazyDecreaseKeys)
    {
        this.lazyMelds = lazyMelds;
        this.lazyDecreaseKeys = lazyDecreaseKeys;
        // student code can be added here
        
    }

    /**
     * 
     * pre: key > 0
     *
     * Insert (key,info) into the heap and return the newly generated HeapNode.
     *
     */
    public HeapItem insert(int key, String info)   
    {    
        HeapNode node = new HeapNode(); //creating the new node 
        node.next = node;
        node.prev = node;
        node.rank = 0;

        HeapItem newitem = new HeapItem(); //creating the nodeItem
        newitem.key = key;
        newitem.info = info;

        node.item = newitem;
        newitem.node= node;

        Heap heap2 = new Heap(this.lazyMelds,this.lazyDecreaseKeys); //creating new heap
        heap2.min = newitem;
        heap2.size = 1;

        this.meld(heap2);  //melding the two heaps

        return newitem; 
    }

    /**
     * 
     * Return the minimal HeapNode, null if empty.
     *
     */
    public HeapItem findMin()
    {
        return this.min; // should be replaced by student code
    }

    /**
     * 
     * Delete the minimal item.
     *
     */
    public void deleteMin()
    {
        return; // should be replaced by student code
    }

    /**
     * 
     * pre: 0<=diff<=x.key
     * 
     * Decrease the key of x by diff and fix the heap.
     * 
     */


    public void decreaseKey(HeapItem x, int diff) 
    {     
        if(this.lazyDecreaseKeys == false){

            x.key -= diff;
            heapifyUp(x.node);
            return;
        }
        
        x.key -= diff;
        if(x.node.parent == null){    //if the node is the root
                if(x.key < this.min.key) min = x;
                return;
            }

        if(x.node.parent.item.key <= x.key) return; //doesnt violte the heap role 

        cascadingCut(x.node);
        if(x.key < this.min.key) {
        this.min = x;
    }

    }




    /**
     * 
     * Delete the x from the heap.
     *
     */
    public void delete(HeapItem x) 
    {    
        this.decreaseKey(x, x.key);
        this.deleteMin();
        return; // should be replaced by student code
    }


    /**
     * 
     * Meld the heap with heap2
     * pre: heap2.lazyMelds = this.lazyMelds AND heap2.lazyDecreaseKeys = this.lazyDecreaseKeys
     *
     */
    public void meld(Heap heap2)  
    {
        if(heap2 == null || heap2.min == null) return;  //if heap2 is empty 

        this.totalCuts += heap2.totalCuts;
        this.totalHeapifyCosts += heap2.totalHeapifyCosts;
        this.totalMarkedNodes += heap2.totalMarkedNodes;
        this.size += heap2.size;


        if(this.min == null){  //if heap is empty 
            this.min = heap2.min;
            return; 
        }

        HeapNode nodeA = this.min.node;
        HeapNode nodeB = heap2.min.node;
        HeapNode nextA = nodeA.next;
        HeapNode nextB = nodeB.next;

        this.rootList.link(heap2.rootList);

        /*  //old linking method
        nodeA.next = nextB;  //linking the two lists
        nextB.prev = nodeA;

        nodeB.next = nextA;
        nextA.prev = nodeB;
         */

        if(this.min.key > heap2.min.key){  //updating the min 
            this.min = heap2.min;
        }

        if(this.lazyMelds == true) return;

        this.successive_linking();
        return; // should be replaced by student code           
    }
    
    
    /**
     * 
     * Return the number of elements in the heap
     *   
     */
    public int size()
    {
        return this.size; 
    }


    /**
     * 
     * Return the number of trees in the heap.
     * 
     */
    public int numTrees()
    {
        return this.rootList.size; // should be replaced by student code
    }
    
    
    /**
     * 
     * Return the number of marked nodes in the heap.
     * 
     */
    public int numMarkedNodes()
    {
        return this.totalMarkedNodes; 
    }
    
    
    /**
     * 
     * Return the total number of links.
     * 
     */
    public int totalLinks()
    {
        return this.totalLinks; // should be replaced by student code
    }
    
    
    /**
     * 
     * Return the total number of cuts.
     * 
     */
    public int totalCuts()
    {
        return 46; // should be replaced by student code
    }
    

    /**
     * 
     * Return the total heapify costs.
     * 
     */
    public int totalHeapifyCosts()
    {
        return 46; // should be replaced by student code
    }
    
    
    /**
     * Class implementing a node in a Heap.
     *  
     */
    public static class HeapNode{
        public HeapItem item;
        public HeapNode child;
        public HeapNode next;
        public HeapNode prev;
        public HeapNode parent;
        public int rank;
        public boolean marked = false;
    }
    
    /**
     * Class implementing an item in a Heap.
     *  
     */
    public static class HeapItem{
        public HeapNode node;
        public int key;
        public String info;
    }

    /**
     * class implementing a list of HeapNodes
     * @param node
     */
    public static class HeapNodeList implements Iterable<HeapNode>{
        public HeapNode start;
        public HeapNode end;
        public int size = 0;

        public HeapNodeList(HeapNode node){
            this.start = node;
            this.end = node;
            this.size = 1;
        }
        public HeapNodeList(){
            this.start = null;
            this.end = null;
            this.size = 0;
        }

        public void add(HeapNode node){
            this.end.next = node;
            node.prev = this.end;
            this.end = node;
            this.size ++;
            if (this.start == null){
                this.start = node;
            }
        }
        public void remove(HeapNode node){
            assert this.size !=0;
            if(this.size == 1){
                this.start = null;
                this.end = null;
            } else {
                if(node == this.start){
                    this.start = node.next;
                }
                else{
                    if(node == this.end){
                        this.end = node.prev;
                    }
                    else{
                        node.prev.next = node.next;
                        node.next.prev = node.prev;
                    }
            }
            }
            this.size --;
        }
      //  @pre // afterNode in list
        public void insertAfter(HeapNode node, HeapNode afterNode){
            assert this.size !=0;
            HeapNode nextNode = afterNode.next;
            afterNode.next = node;
            node.prev = afterNode;
            node.next = nextNode;
            nextNode.prev = node;
        }
        public void link(HeapNodeList other){
            this.end.next = other.start;
            other.start.prev = this.end;
            this.end = other.end;
            this.size += other.size;
        }

        @Override
        public java.util.Iterator<HeapNode> iterator() {
            return new java.util.Iterator<HeapNode>() {
                private HeapNode current = start;
                private int count = 0;

                @Override
                public boolean hasNext() {
                    return count < size;
                }

                @Override
                public HeapNode next() {
                    HeapNode temp = current;
                    current = current.next;
                    count++;
                    return temp;
                }
            };
        }

    }

    /**==================== helper_methods ====================*/
    private void heapifyUp(HeapNode node) {
    while (node.parent != null && node.parent.item.key > node.item.key) { //while the node is not a root and we...
        totalHeapifyCosts++;                                              //violate the heap rule 
        HeapItem parentItem = node.parent.item;
        HeapItem childItem = node.item;

        node.parent.item = childItem;  //replacing between the child and the parent 
        node.item = parentItem;

        node.parent.item.node = node.parent;
        node.item.node = node;

        node = node.parent;
    }
    }



    private void cascadingCut(HeapNode node){
        do{
            HeapNode parent = node.parent;
            this.totalCuts ++;
            parent.rank --;
            if(parent.child == node){        //cutting the child 
                if(node.next == node) parent.child = null;
                else parent.child = node.next;
            }
        node.next.prev =  node.prev;
        node.prev.next = node.next;
        node.next = node;
        node.prev = node;
        node.parent = null;
        node.marked = false;

        Heap tmpHeap = new Heap(this.lazyMelds ,this.lazyDecreaseKeys);  //creating new heap
        tmpHeap.min = node.item;
        this.meld(tmpHeap);

        if(parent.parent == null) break;  //if the parent is a root 

        if(parent.marked == false){       //if the parent is not marked 
            parent.marked = true;
            this.totalMarkedNodes ++;
            break;
        }
        node = parent;
        }
        while(true);
        }

    private void successive_linking(){
        int[] rankArray = new int[this.size + 1]; //array to store the nodes of each rank, all zeros
        HeapNode[] nodesByRank = new HeapNode[this.size + 1]; //array to store the nodes of each rank, all nulls


        for (HeapNode root : this.rootList) {
            int rank = root.rank;
            if (rankArray[rank] == 0){
                rankArray[rank] =1;
                nodesByRank[rank] = root;
            } else {
                while (rankArray[rank] != 0) {
                    root = link(root, nodesByRank[rank]); //linking the two nodes
                    this.totalLinks++; //updating the total links
                    rankArray[rank] = 0;
                    nodesByRank[rank] = null;
                    rank++;
                    assert rank == root.rank;
                }
                rankArray[rank] =1;
                nodesByRank[rank] = root;
            }
        }
        return;
    }

    /**@pre // ranks must be equal, 
     *  both nodes must be root nodes
    */
    private HeapNode link(HeapNode nodeA, HeapNode nodeB){
        assert (nodeA.rank != nodeB.rank); // ranks must be equal
        if(nodeA.item.key > nodeB.item.key){
            HeapNode temp = nodeA;
            nodeA = nodeB;
            nodeB = temp;
        }
        // nodeA<nodeB
        this.rootList.remove(nodeB); // removing nodeB from the root list

        if(nodeA.rank == 0){
            nodeA.child = nodeB;
            nodeB.parent = nodeA;
            nodeA.rank = 1;
        } else {
            HeapNode child = nodeA.child;
            nodeB.next = child;
            nodeB.prev = child.prev;
            child.prev = nodeB;
            nodeB.prev.next = nodeB;
        }
        nodeB.parent = nodeA;
        nodeA.rank ++;
        return nodeA;
    }

}
