
public class Node {
	private Node next;
	private int number;
	public Node(int num) {
		next = null;
		number = num;
	}
	
	public void output() {
		System.out.println(number);
		if(hasNext())
			next.output();
	}
	
	public Node setNext(Node node) {
		next = node;
		return this;
	}
	private boolean hasNext() {
		return this.next != null;
	}
	
	public Node Remove(Node firstNode, int[] removalRequests, int removalRequests_Length ) 
	{ 
		// idea: go through the list, and remove each node without restarting from beginning.
		// do this by:
		// 1. keeping track of how many nodes are removed
		// 2. always starting traversal from the node before the last removed node.
		
		// initialize variables
		int countRemoved = 0;
		int counter = 0;
		Node currentNode = firstNode;
		int indexRemove = 0;
		int indexStop = 0;
		
		
		// loop through all the removal requests
		for(int i = 0; i < removalRequests_Length; i++)
		{
			// index to remove is given by the input - how many we removed
			// (since given index does not account for missing nodes after removals)
			// - 1 to account for 0 based indexing.
			indexRemove = removalRequests[i] - countRemoved - 1;
			
			// stop the traversal right before we get to the node we want to remove.
			indexStop = indexRemove - 1;			
			while(currentNode != null)
			{
				if(counter == indexStop)
				{
					RemoveNext(currentNode);
					countRemoved++;
					
					// after removing, we stop traversal and compute the next index to stop at
					break;
				}
				counter++;
				currentNode = currentNode.next;
			}
		}
		
		return firstNode;
	} 

	public void RemoveNext(Node firstNode)
	{
		// since we are guaranteed to be given an index that is not empty...
		// just set the firstNode.next to be the node-to-remove's next node,
		// assuming the end of the list is always signified by next == null;

		firstNode.next = firstNode.next.next;
	}

}
