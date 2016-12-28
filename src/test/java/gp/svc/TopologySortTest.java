package gp.svc;

import java.util.List;

import com.gp.quickflow.DirectedGraph;
import com.gp.quickflow.TopologicalSort;

public class TopologySortTest {

	public static void main(String[] args){
		
		DirectedGraph<TNode> dg = new DirectedGraph<TNode>();
		TNode n1 = new TNode(1);
		dg.addNode(n1);
		TNode n2 = new TNode(2);
		dg.addNode(n2);
		TNode n3 = new TNode(3);
		dg.addNode(n3);
		TNode n4 = new TNode(4);
		dg.addNode(n4);
		TNode n5 = new TNode(5);
		dg.addNode(n5);
		TNode n6 = new TNode(6);
		dg.addNode(n6);
		TNode n7 = new TNode(7);
		dg.addNode(n7);
		
		dg.addEdge(n1, n2);
		dg.addEdge(n1, n3);
		dg.addEdge(n3, n2);
		dg.addEdge(n2, n4);
		dg.addEdge(n2, n5);
		dg.addEdge(n3, n4);
		dg.addEdge(n5, n3);
		dg.addEdge(n4, n5);
		dg.addEdge(n5, n6);
		dg.addEdge(n6, n7);
		dg.addEdge(n5, n7);
		
		List<TNode> rst = TopologicalSort.sort(dg);
		
		System.out.println(rst);
		
	}
	
	static public class TNode{
		
		public TNode(int nid){
			this.nid = nid;
		}
		
		private int nid;

		public int getNid() {
			return nid;
		}

		public void setNid(int nid) {
			this.nid = nid;
		}

		@Override
		public String toString(){
			return String.valueOf(nid);
		}
	}
}
