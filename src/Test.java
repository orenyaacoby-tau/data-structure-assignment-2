
import java.util.*;


public class Test {
    public static boolean sort(Integer[] a){
        Heap heap = new Heap(true, true);
        List<Integer> lst1 = Arrays.asList(a);
        List<Integer> lst2 = new ArrayList<>();
        Collections.shuffle(lst1);
        //System.out.println(     lst1);
        for(Integer x : lst1){
            heap.insert(x, null);            
        }
        while (heap.size>0) {
           int min = heap.findMin().key;
           heap.deleteMin();
           lst2.add(min);
            
        }
        lst1.sort(null);
       // System.out.println(lst1);
       // System.out.println(lst2);
       return lst1.equals(lst2);
        
    }
    public static void main(String[] args){
        Integer[] a = {1,2,3,4,5,6,7,8,9,10};
        for (int i=0;i<100;i++){
            boolean x = sort(a);
            if (!x){
                System.out.println("error");
            }
        }
        System.out.println(sort(a));
        System.out.println("done");
    }
    
}
