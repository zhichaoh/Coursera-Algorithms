import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BaseballElimination {

	private int[] w;

	private int[] l;

	private int[] r;

	private int[][] g;

	private Map<String, Integer> teamNames;

	private String[] nameList;

	private int numTeams;

	public BaseballElimination(String filename) throws Exception {
		// create a baseball division from given filename in format specified
		// below
		Scanner input = new Scanner(new File(filename));
		numTeams = input.nextInt();
		this.w = new int[numTeams];
		this.l = new int[numTeams];
		this.r = new int[numTeams];
		this.g = new int[numTeams][numTeams];
		this.nameList = new String[numTeams];
		this.teamNames = new HashMap<String, Integer>();
		for (int n = 0; n < numTeams; n++) {
			String name = input.next();
			teamNames.put(name, n);
			w[n] = input.nextInt();
			l[n] = input.nextInt();
			r[n] = input.nextInt();
			for (int m = 0; m < numTeams; m++)
				g[n][m] = input.nextInt();
			nameList[n] = name;
		}
		input.close();
	}

	public int numberOfTeams() {
		return this.numTeams;
	}

	public Iterable<String> teams() {
		// all teams
		return teamNames.keySet();
	}

	public int wins(String team) {
		// number of wins for given team
		if (!teamNames.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return w[teamNames.get(team)];
	}

	public int losses(String team) {
		// number of losses for given team
		if (!teamNames.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return l[teamNames.get(team)];
	}

	public int remaining(String team) {
		// number of remaining games for given team
		if (!teamNames.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		return r[teamNames.get(team)];
	}

	public int against(String team1, String team2) {
		// number of remaining games between team1 and team2
		if (!teamNames.containsKey(team1) || !teamNames.containsKey(team2))
			throw new java.lang.IllegalArgumentException();
		int a = teamNames.get(team1);
		int b = teamNames.get(team2);
		return g[a][b];
	}

	public boolean isEliminated(String team) {
		// is given team eliminated?
		if (!teamNames.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		int x = teamNames.get(team);

		for (int i = 0; i < this.numTeams; i++) {
			if (w[x] + r[x] - w[i] < 0)
				return true;
		}

		int numMatches = this.numTeams * (this.numTeams - 1) / 2;
		int nodeID = 0;

		FlowNetwork fn = new FlowNetwork(numMatches + numTeams + 2);
		int s = numMatches + numTeams;
		int t = s + 1;

		for (int i = 0; i < numTeams; i++) {
			for (int j = i + 1; j < numTeams; j++) {
				if (i == j)
					continue;
				fn.addEdge(new FlowEdge(s, nodeID, g[i][j])); // source to match
																// nodes
				fn.addEdge(new FlowEdge(nodeID, numMatches + i,
						Integer.MAX_VALUE)); // match to team nodes
				fn.addEdge(new FlowEdge(nodeID, numMatches + j,
						Integer.MAX_VALUE)); // match to team nodes
				nodeID += 1;
			}
			fn.addEdge(new FlowEdge(numMatches + i, t, Math.max(0, w[x] + r[x]
					- w[i]))); // game nodes to target
		}

		new FordFulkerson(fn, s, t);

		for (FlowEdge e : fn.adj(s)) {
			if (e.flow() != e.capacity())
				return true;
		}
		return false;
	}

	public Iterable<String> certificateOfElimination(String team) {
		// subset R of teams that eliminates given team; null if not eliminated
		if (!teamNames.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		int x = teamNames.get(team);
		int numMatches = this.numTeams * (this.numTeams - 1) / 2;
		int nodeID = 0;

		List<String> nList = new ArrayList<String>();
		for (int i = 0; i < this.numTeams; i++) {
			if (w[x] + r[x] - w[i] < 0)
				nList.add(nameList[i]);
		}
		if (nList.size() > 0)
			return nList;

		FlowNetwork fn = new FlowNetwork(numMatches + numTeams + 2);
		int s = numMatches + numTeams;
		int t = s + 1;

		for (int i = 0; i < numTeams; i++) {
			for (int j = i + 1; j < numTeams; j++) {
				if (i == j)
					continue;
				fn.addEdge(new FlowEdge(s, nodeID, g[i][j])); // source to match
																// nodes
				fn.addEdge(new FlowEdge(nodeID, numMatches + i,
						Integer.MAX_VALUE)); // match to team nodes
				fn.addEdge(new FlowEdge(nodeID, numMatches + j,
						Integer.MAX_VALUE)); // match to team nodes
				nodeID += 1;
			}
			fn.addEdge(new FlowEdge(numMatches + i, t, Math.max(0, w[x] + r[x]
					- w[i]))); // game nodes to target
		}

		FordFulkerson FF = new FordFulkerson(fn, s, t);

		boolean flag = false;
		for (FlowEdge e : fn.adj(s)) {
			if (e.flow() != e.capacity()) {
				flag = true;
				break;
			}
		}
		if (!flag)
			return null;
		else {
			List<Integer> nodeList = this.BFSRes(fn, s);
			List<String> nl = new ArrayList<String>();
			for (Integer v : nodeList) {
				if (FF.inCut(v) && v >= numMatches) {
					nl.add(this.nameList[v - numMatches]);
				}
			}
			return nl;
		}
	}

	private List<Integer> BFSRes(FlowNetwork graph, int node) {
		Queue<Integer> Q = new Queue<Integer>();
		boolean[] visited = new boolean[graph.V()];
		Q.enqueue(node);
		visited[node] = true;
		List<Integer> nodeList = new ArrayList<Integer>();
		while (!Q.isEmpty()) {
			int cn = Q.dequeue();
			for (FlowEdge e : graph.adj(cn)) {
				int t = -1;
				if (e.from() == cn)
					t = e.to();
				else
					t = e.from();
				if (e.residualCapacityTo(t) > 0) {
					if (!visited[t]) {
						Q.enqueue(t);
						visited[t] = true;
						nodeList.add(t);
					}
				}
			}
		}
		return nodeList;
	}

	public static void main(String[] args) throws Exception {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
					StdOut.print(t + " ");
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}
}
