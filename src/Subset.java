import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Subset {
	 public static void main(String[] args) {
		 int k = Integer.parseInt(args[0]);
		 List<String> queue = new ArrayList<String>();
		 while(!StdIn.isEmpty()) queue.add(StdIn.readString());
		 Collections.shuffle(queue);
		 for(int i=0;i<k;i++) StdOut.println(queue.get(i));
	 }
}
