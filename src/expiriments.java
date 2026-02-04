import java.io.FileWriter;
import java.util.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class expiriments {
    public static int costTotal(Heap heap){
        return heap.totalCuts + heap.totalLinks + heap.totalHeapifyCosts();
    }
    // Experiment 1: Insert 1..n in random order into an empty heap, then deleteMin once.
    public static Heap exp1(int n, boolean lazyMelds, boolean lazyDecreaseKeys, Set<Integer> costs){
        Heap heap = new Heap(lazyMelds, lazyDecreaseKeys);
        List<Integer> keys = new ArrayList<>();
        for(int i=1;i<=n;i++) keys.add(i);
        Collections.shuffle(keys);
        for(int k : keys){
            if (heap.size() %1000 ==0){
                System.out.print("#");
            }
            int cost = costTotal(heap);
             heap.insert(k, null);
             cost = costTotal(heap)-cost;
             costs.add(cost);
            //System.out.println("Insert " + k + " cost: " + cost + "Total cost: " + costTotal(heap));
            }

        int beforeDeleteMinCost = costTotal(heap);
        heap.deleteMin();
        int deleteMinCost = costTotal(heap)-beforeDeleteMinCost;
        costs.add(deleteMinCost);
       // System.out.println("DeleteMin cost: " + deleteMinCost + " Total cost: " + costTotal(heap));

        return heap;
    }

    // Experiment 2: Insert 1..n in random order, deleteMin once, then repeatedly delete the current maximum (by pointer)
    // until only 46 elements remain.
    public static Heap exp2(int n, boolean lazyMelds, boolean lazyDecreaseKeys, Set<Integer> costs){
        Heap heap = new Heap(lazyMelds, lazyDecreaseKeys);
        List<Integer> keys = new ArrayList<>();
        List<Heap.HeapItem> items = new ArrayList<>(n+1);
        for(int i=0;i<=n;i++) items.add(null); // index by key

        for(int i=1;i<=n;i++) keys.add(i);
        Collections.shuffle(keys);
        for(int k : keys){
            if (heap.size() %1000 ==0){
                System.out.print("$");
            }
            int cost = costTotal(heap);
            Heap.HeapItem it = heap.insert(k, null);
            cost = costTotal(heap)-cost;
            costs.add(cost);

            items.set(k, it);
        }

        int beforeDeleteMinCost = costTotal(heap);
        heap.deleteMin();
        int deleteMinCost = costTotal(heap)-beforeDeleteMinCost;
        costs.add(deleteMinCost);

        int currentMax = n;
        while(heap.size() > 46){
            if (heap.size() %1000 ==0){
                System.out.print(".");
            }
            if(currentMax!= heap.size()+1){
                System.out.println("error in exp2");
            }
            if (currentMax==1){
                System.out.println("error in exp2");
            }
            // find next existing max by descending from currentMax
            Heap.HeapItem maxItem = items.get(currentMax);

            beforeDeleteMinCost = costTotal(heap);
            heap.delete(maxItem);
            deleteMinCost = costTotal(heap)-beforeDeleteMinCost;
            costs.add(deleteMinCost);

            // advance
            currentMax--;
        }
        return heap;

    }

    // Experiment 3: Insert 1..n random, deleteMin, then perform ceil(0.1*n) times decreaseKey on the current maximum (by pointer) to make its key 0,
    // (so 90% positive keys, rest 0), then deleteMin again.
    public static Heap exp3(int n, boolean lazyMelds, boolean lazyDecreaseKeys, Set<Integer> costs){
        Heap heap = new Heap(lazyMelds, lazyDecreaseKeys);
        List<Integer> keys = new ArrayList<>();
        List<Heap.HeapItem> items = new ArrayList<>(n+1);
        for(int i=0;i<=n;i++) items.add(null);

        for(int i=1;i<=n;i++) keys.add(i);
        Collections.shuffle(keys);
        for(int k : keys){
            int cost = costTotal(heap);
            Heap.HeapItem it = heap.insert(k, null);
            cost = costTotal(heap)-cost;
            costs.add(cost);

            items.set(k, it);
        }
        int beforeDeleteMinCost = costTotal(heap);
        heap.deleteMin();
        int deleteMinCost = costTotal(heap)-beforeDeleteMinCost;
        costs.add(deleteMinCost);

        int decreaseCount = (int)Math.ceil(0.1 * n);
        int currentMax = n;
        int done = 0;
        while(done < decreaseCount){
            Heap.HeapItem it = items.get(currentMax);
            int diff = it.key; // reduce it to 0
            int cost = costTotal(heap);
            heap.decreaseKey(it, diff);
            cost = costTotal(heap)-cost;
            costs.add(cost);
            // now key is 0; keep pointer in items
            done++;
            currentMax--;
        }

        beforeDeleteMinCost = costTotal(heap);
        heap.deleteMin();
        deleteMinCost = costTotal(heap)-beforeDeleteMinCost;
        costs.add(deleteMinCost);

        return heap;
    }


    public static void printCosts(Heap heap, Set<Integer> costs, BufferedWriter w) throws IOException {
        // print all values in one CSV line
        w.write("," + heap.size());        
        w.write("," + heap.numTrees());
        w.write("," + heap.totalLinks());
        w.write("," + heap.totalCuts());
        w.write("," + heap.totalHeapifyCosts());
        w.write("," + Collections.max(costs));
        w.newLine();
    }

    public static void printCostHeaders(BufferedWriter w) throws IOException {
        w.write("Time(ns)");
        w.write(",Size");
        w.write(",NumTrees");
        w.write(",TotalLinks");
        w.write(",TotalCuts");
        w.write(",TotalHeapifyCosts");
        w.write(",MaxOperationCost");
        w.newLine();
    }

    // Simple runner to demonstrate the experiments
    public static void main(String[] args) {

        String fileName = "my_results.csv";          // <-- create file with this name
        Path path = Path.of(fileName);

        try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {




        Set<Integer> c1 = new HashSet<>()  ;
        Set<Integer> c2 = new HashSet<>()  ;
        Set<Integer> c3 = new HashSet<>()  ;
        boolean lazyDecreaseKeys = true;
        boolean lazyMelds = true;
        int n = 464646; // default size for experiments
        //n = 5000; // for quick testing
        int loops = 1;
        for(boolean lazyDecreaseKeysOption : new boolean[]{true, false}){
            for(boolean lazyMeldsOption : new boolean[]{true, false}){
                lazyDecreaseKeys = lazyDecreaseKeysOption;
                lazyMelds = lazyMeldsOption;
            w.write("Experiment results for n=" + n + ", lazyMelds=" + lazyMelds + ", lazyDecreaseKeys=" + lazyDecreaseKeys);
            System.out.println("Experiment results for n=" + n + ", lazyMelds=" + lazyMelds + ", lazyDecreaseKeys=" + lazyDecreaseKeys + "\n");

            w.newLine();
        printCostHeaders(w);

        for(int i=0;i<loops;i++)  // run multiple times to see variation
        {
            c1.clear();
            //System.out.println("Experiment 1 (n=1000, lazy=true,true)");
            long startTotal = System.nanoTime();
            Heap heap = exp1(n, lazyMelds, lazyDecreaseKeys, c1);
            long endTotal = System.nanoTime();  
            w.write(String.valueOf(endTotal - startTotal));
            printCosts(heap, c1, w);        
        }
        w.write("done 1");
        w.newLine();
        printCostHeaders(w);
        w.newLine();

        for(int i=0;i<loops;i++)  // run multiple times to see variation
        {
            c2.clear();
            //System.out.println("Experiment 1 (n=1000, lazy=true,true)");
            long startTotal = System.nanoTime();
            Heap heap = exp2(n, lazyMelds, lazyDecreaseKeys, c2);
            long endTotal = System.nanoTime();  
            w.write(String.valueOf(endTotal - startTotal));
            printCosts(heap, c2, w);        
        }
        w.write("done 2");
        w.newLine();
        printCostHeaders(w);
        w.newLine();

        for(int i=0;i<loops;i++)  // run multiple times to see variation
        {
            c3.clear();
            //System.out.println("Experiment 1 (n=1000, lazy=true,true)");
            long startTotal = System.nanoTime();
            Heap heap = exp3(n, lazyMelds, lazyDecreaseKeys, c3);
            long endTotal = System.nanoTime();  
            w.write(String.valueOf(endTotal - startTotal));
            printCosts(heap, c3, w);        
        }
        w.write("done 3");
        w.newLine();
        


    }}


    

        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV: " + fileName, e);
        } // <-- close the file happens automatically here
    }
    
}
