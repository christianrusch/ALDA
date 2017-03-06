package alda.linear;

/**
 *  @author Christian Rusch - chru6903
 *  christianrusch.su@gmail.com
 */

public class MyALDAList<E> implements alda.linear.ALDAList<E> {
	
	private Node<E> tail = new Node<>(null);
	private Node<E> head = new Node<>(null, tail);
	
	private int nodeCount = 0;
	private int modCounter = 0;
	
	//
	//	Node class to contain the data and reference
	//	to the next node.
	//
	
	private static class Node<E> {
		
		private E data;
		private Node<E> next = null;
		
		public Node(E data) {
			this.data = data;
		}
		
		public Node(E data, Node<E> next) {
			this(data);
			this.next = next;
		}		
		
	}
	
	//
	//	Iterator, for stepping through the list:
	//
	
	@Override
	public java.util.Iterator<E> iterator() {
		
		return new MyALDAListIterator();
		
	}
	
	private class MyALDAListIterator implements java.util.Iterator<E> {
		
		private Node<E> currentNode = head.next;
		private Node<E> previousNode = head;
		private Node<E> removeNode = null;
		
		private int initCount = modCounter;

		//
		//	Returns true if currentNode and tail are different.
		//
		
		@Override
		public boolean hasNext() {
			
			return currentNode != tail;
			
		}
		
		//
		//	Sets removeNode to currentNode and stores the
		//	previousNode; it then steps into currentNode.next.
		//

		@Override
		public E next() {
			
			if(modCounter != initCount)
				throw new java.util.ConcurrentModificationException();
			if(!hasNext())
				throw new java.util.NoSuchElementException();
			
			if(removeNode != null)
				previousNode = removeNode;
			removeNode = currentNode;
			currentNode = currentNode.next;
			
			return removeNode.data;
			
		}
		
		//
		//	Re-links previousNode.next and drops removeNode;
		//	decrements listSize. remove() is then locked
		//	until next() has been called again.
		//
		
		public void remove() {
			
			if(modCounter != initCount)
				throw new java.util.ConcurrentModificationException();
			if(removeNode == null)
				throw new IllegalStateException();
			
			previousNode.next = removeNode.next;
			removeNode = null;
			
			nodeCount--;
			
		}	
	}
	
	//
	//	Assorted trivial methods.
	//
	
	public boolean isEmpty() {
		
		return nodeCount == 0;
		
	}
	
	@Override
	public int size() {
		
		return nodeCount;
		
	}
	
	@Override
	public void clear() {
		
		tail = new Node<>(null);
		head = new Node<>(null, tail);
		
		nodeCount = 0;
		modCounter++;
		
	}
	
	//
	//	If indexOf(element) returns -1, element is
	//	not in the list and false is returned; else
	//	an index is passed and true is returned. 
	//
	
	@Override
	public boolean contains(E element) {
		
		return indexOf(element) == -1 ? false : true;
		
	}
	
	//
	//	If the element is not in the list, returns -1;
	//	when the list has size, steps through the nodes' data
	//	and compares against element for equivalence; if found,
	//	returns the index, otherwise returns -1.
	//
	
	@Override
	public int indexOf(E element) {
		
		int index = 0;
		
		for(Node<E> currentNode = head.next; currentNode != tail; currentNode = currentNode.next) {
			if(currentNode.data == element || currentNode.data.equals(element)) {
				return index;
			}
			index++;
		}
		
		return -1;
		
	}
	
	//
	// If node exists at index, return its data.
	//
	
	@Override
	public E get(int index) {
		
		return getNode(index).data;
		
	}
	
	//
	//	If the index is not in range, throws exception;
	//	steps through list until index, returns node.
	//
	
	private Node<E> getNode(int index) {
		
		if(index < 0 || index > nodeCount-1)
			throw new IndexOutOfBoundsException();
		
		Node<E> currentNode = head.next;
		for(int i = 0; i < index; i++)
			currentNode = currentNode.next;
		
		return currentNode;
		
	}
	
	//
	// Adds element to end of list.
	//
	
	@Override
	public void add(E element) {
		
		add(nodeCount, element);
		
	}
	
	//
	//	If index is out of range, throws exception; else
	//	sets previousNode and inserts a new node at index.
	//	Increments listSize and modCount.
	//

	@Override
	public void add(int index, E element) {
		
		if(index < 0)
			throw new IndexOutOfBoundsException();
		
		Node<E> previousNode = head;
		if(!isEmpty() && index-1 >= 0)
			previousNode = getNode(index-1);
		Node<E> newNode = new Node<>(element, previousNode.next);
		previousNode.next = newNode;

		nodeCount++;
		modCounter++;
		
	}
	
	//
	//	If index is out of range, throws exception. Unlinks
	//	toRemove node, decrements listSize and increments
	//	modCount; returns removed data.
	//	
	
	@Override
	public E remove(int index) {
		
		if(isEmpty() || index < 0 || index > nodeCount-1)
			throw new IndexOutOfBoundsException();
		
		Node<E> previousNode = head;
		if(index > 0)
			previousNode = getNode(index-1);
		Node<E> toRemove = previousNode.next;
		previousNode.next = toRemove.next;
		
		nodeCount--;
		modCounter++;
		
		return toRemove.data;
		
	}
	
	//
	//	If element is not in list, returns false.
	//	While list has size, steps through list
	//	until element is found. Unlinks toRemove
	//	node, decrements listSize and increments
	//	modCount; returns removed data.
	//	
	
	@Override
	public boolean remove(E element) {
		
		if(!contains(element))
			return false;
		
		Node<E> currentNode = head;
		for(currentNode = head; currentNode.next != tail; currentNode = currentNode.next) {
			if(currentNode.next.data == element || currentNode.next.data.equals(element)) {
				currentNode.next = currentNode.next.next;
				break;
			}
		}
		
		nodeCount--;
		modCounter++;
		
		return true;
		
	}
	
	//
	//	Returns the list's toString() to comply with test
	//	requirements. Builds a bracketed string of comma
	//	and whitespace delimited toString() returns.
	//
	
	@Override
	public String toString() {
		
		String output = "[";
		
		if(nodeCount > 0) {
			for(Node<E> currentNode = head.next; currentNode != tail; currentNode = currentNode.next) {	
				output += currentNode.data.toString();
				if(currentNode.next != tail) {
					output += ", ";
				}
			}
		}
		
		output += "]";
		
		return output;
		
	}
	
}