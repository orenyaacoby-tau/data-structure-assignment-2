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
    public final boolean lazyMelds;
    public final boolean lazyDecreaseKeys;
    public HeapItem min;
    
    
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
        return null; // should be replaced by student code
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

        nodeA.next = nextB;  //linking the two lists
        nextB.prev = nodeA;

        nodeB.next = nextA;
        nextA.prev = nodeB;

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
        return; // should be replaced by student code
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
        return 46; // should be replaced by student code
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
            this.totalMarkedNodes ++
            break;
        }
        node = parent;
        }
        while(true);
        }

    private void successive_linking(){
        return;
    }


    

}
