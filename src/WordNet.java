import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class WordNet {

	private Map<String, List<Integer>> dict = new HashMap<String, List<Integer>>();

	private Map<Integer, String> reverseDict = new HashMap<Integer, String>();

	private SAP sapObj;

	private boolean dfs(int v, Digraph G, Map<Integer, Boolean> visit,
			Stack<Integer> S) {
		visit.put(v, true);
		S.push(v);
		for (int w : G.adj(v)) {
			if (S.contains(w))
				return false;
			if (!visit.containsKey(w))
				if (!dfs(w, G, visit, S))
					return false;
		}
		S.pop();
		return true;
	}

	private boolean isDAG(Digraph G) {
		Map<Integer, Boolean> visit = new HashMap<Integer, Boolean>();
		Stack<Integer> S = new Stack<Integer>();
		for (int i = 0; i < G.V(); i++) {
			if (!visit.containsKey(i))
				if (!dfs(i, G, visit, S))
					return false;
		}
		// check multiple roots
		int cnt = 0;
		for (int i = 0; i < G.V(); i++) {
			int n = 0;
			Iterator<Integer> it = G.adj(i).iterator();
			while (it.hasNext()) {
				n += 1;
				it.next();
			}
			if (n == 0)
				cnt += 1;
		}
		if (cnt != 1)
			return false;
		return true;
	}

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms)
			throws IllegalArgumentException, IOException {
		// reading synsets
		BufferedReader in = new BufferedReader(new FileReader(synsets));
		String line = null;
		int maxID = 0;
		while ((line = in.readLine()) != null) {
			String items[] = line.split(",");
			String nouns[] = items[1].split(" ");
			List<Integer> nounList = null;
			for (int i = 0; i < nouns.length; i++) {
				if (!dict.containsKey(nouns[i]))
					nounList = new LinkedList<Integer>();
				else
					nounList = dict.get(nouns[i]);
				nounList.add(Integer.parseInt(items[0]));
				dict.put(nouns[i], nounList);
			}
			reverseDict.put(Integer.parseInt(items[0]), items[1]);
			maxID = Math.max(maxID, Integer.parseInt(items[0]));
		}
		in.close();
		// reading hypernyms
		Digraph G = new Digraph(maxID + 1);
		in = new BufferedReader(new FileReader(hypernyms));
		while ((line = in.readLine()) != null) {
			String items[] = line.split(",");
			int v = Integer.parseInt(items[0]);
			for (int i = 1; i < items.length; i++)
				G.addEdge(v, Integer.parseInt(items[i]));
		}
		in.close();
		if (!isDAG(G))
			throw new IllegalArgumentException();
		// initializing
		sapObj = new SAP(G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return dict.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return dict.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		// System.out.println(nounA+"\t"+nounB);
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		return sapObj.length(dict.get(nounA), dict.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB))
			throw new IllegalArgumentException();
		int id = sapObj.ancestor(dict.get(nounA), dict.get(nounB));
		return reverseDict.get(id);
	}

	// for unit testing of this class
	public static void main(String[] args) throws IllegalArgumentException,
			IOException {
		WordNet wordnet = new WordNet(args[0], args[1]);
		wordnet.distance("wolf", "fish");
	}

}
