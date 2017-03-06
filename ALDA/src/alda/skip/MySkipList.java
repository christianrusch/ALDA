package alda.skip;

import java.util.Iterator;

class MySkipList<T extends Comparable<T>> /* implements Iterable<T> */ {

	private SkipListNode<T> head;
//	private SkipListNode<T> tail;
	private SkipListNode<T> from;
	private int length;
	private int level;
	
	public MySkipList() {
		 head = new SkipListNode<T>(null);
//		 tail = new SkipListNode<T>(null);
		 length = 0;
		 
		 head.setNext(null);
		 from = head;
//		 tail.setPrevious(head);
	}
	
	private static class SkipListNode<T> {
		
		private T data;
		private SkipListNode<T> next;
		private SkipListNode<T> previous;
		private SkipListNode<T> down;
		
		/*
		 * Constructor
		 */
		public SkipListNode(T data) {
			this.data = data;
		}
		
		/*
		 * Next methods
		 */
		private SkipListNode<T> getNext() {
			return next;
		}
		
		private void setNext(SkipListNode<T> node) {
			this.next = node;
		}
		
		/*
		 * Previous methods
		 */
		private SkipListNode<T> getPrevious() {
			return previous;
		}
		
		private void setPrevious(SkipListNode<T> node) {
			this.previous = node;
		}
		
		
		/*
		 * Down methods
		 */
		private SkipListNode<T> getDown() {
			return down;
		}
		
		private void setDown(SkipListNode<T> node) {
			this.down = node;
		}
		
		
		/*
		 * Data methods
		 */
		private T getData() {
			return data;
		}
		
		private void setData(T data) {
			this.data = data;
		}
		
		
	}

	public void insert(T data) {
		SkipListNode<T> newNode = new SkipListNode<T>(data);
		if(length == 0) {
			head.setNext(newNode);
			newNode.setPrevious(head);
			newNode.setNext(null);
//			tail.setPrevious(newNode);
		} else {
			from.setNext(newNode);
			newNode.setPrevious(from);
		}
		length++;
	}
	
	public SkipListNode<T> searchNode(T data) {
		for(SkipListNode<T> activeNode = head; activeNode != null; ) {
			if(activeNode.getData().compareTo(data) > 0) {
				
			}
			from = activeNode;
		}
		return null;
	}
	
	public String toString() {
		String string = "";
		for(SkipListNode<T> node = head; node != null; node.getNext()) {
			string += node.toString() + " ";
		}
		return string;
	}

//	@Override
//	public Iterator<T> iterator() { 
//		return new SkipListIterator(this, 0);
//	}
	
}
