
public class LinkedListSearch {
	
	public static void main(String args[])
	{
		Node node = new Node(1);
		node.setNext(new Node(2).setNext(new Node(3)));
		node.output();
		int[] requests = {2,3};
		Node newNode = node.Remove(node, requests, 2);
		newNode.output();
	}
	
	
}
