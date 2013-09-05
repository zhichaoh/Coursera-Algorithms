import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
	
	private class Node {
		public Item value = null;
		public Node next = null;
		public Node prev = null;
	}
	
	private int size;
	
	private Node head; 
	
	private Node tail;
	
	public Deque() {                     // construct an empty deque
		size = 0;
		head = tail = null;
	}
   
	public boolean isEmpty() {          // is the deque empty?
		return size == 0;
	}
	
	public int size() {                 // return the number of items on the deque
		return size;
	}
	
	public void addFirst(Item item) {   // insert the item at the front
		if(item==null) throw new java.lang.NullPointerException();
		Node currentNode = new Node();
		currentNode.value = item;
		currentNode.next = head;
		if(head!=null) head.prev = currentNode;
		head = currentNode;
		size ++;
		if(tail==null) tail = head;
	}
	
	public void addLast(Item item) {    // insert the item at the end
		if(item==null) throw new java.lang.NullPointerException();
		Node currentNode = new Node();
		currentNode.value = item;
		currentNode.prev = tail;
		if(tail!=null) tail.next = currentNode;
		tail = currentNode;
		size ++;
		if(head==null) head = tail;
	}
	
	public Item removeFirst() {         // delete and return the item at the front
		if(size==0) throw new java.util.NoSuchElementException();
		Item result = head.value;
		if(size==1) head = tail = null;
		else {
			head = head.next;
			head.prev = null;
		}
		size --;
		return result;
	}
	
	public Item removeLast() {          // delete and return the item at the end
		if(size==0) throw new java.util.NoSuchElementException();
		Item result = tail.value;
		if(size==1) head = tail = null;
		else {
			tail = tail.prev;
			tail.next = null;
		}
		size --;
		return result;
	}
	
	public Iterator<Item> iterator() {  // return an iterator over items in order from front to end
		return new DequeIterator();
	}
	
	private class DequeIterator implements Iterator<Item> {
		
		private Node currentNode = head;
		
		@Override
		public boolean hasNext() {
			return currentNode!=null;
		}

		@Override
		public Item next() {
			if (!hasNext()) throw new java.util.NoSuchElementException();
			Item item = currentNode.value;
			currentNode = currentNode.next;
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}
