package alda.tree;

/**
 *  @author Christian Rusch - chru6903
 *  christianrusch.su@gmail.com
 */

public class BinarySearchTreeNode<T extends Comparable<T>> {

	private T data;
	private BinarySearchTreeNode<T> left;
	private BinarySearchTreeNode<T> right;

	public BinarySearchTreeNode(T data) {
		this(data, null, null);
	}
	
	public BinarySearchTreeNode(T data, BinarySearchTreeNode<T> left, BinarySearchTreeNode<T> right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}

	public boolean add(T data) {
		return (this.contains(data)) ? false : ((add(data, this) == null) ? false : true);
	}
	
	private BinarySearchTreeNode<T> add(T data, BinarySearchTreeNode<T> node) {
		if(node == null) {
			return new BinarySearchTreeNode<T>(data);
		}
		int comparison = data.compareTo(node.data);
		if(comparison < 0)
			node.left = add(data, node.left);
		else if(comparison > 0)
			node.right = add(data, node.right);
		else
			;
		return node;
	}

	private T findMin() {
		return findMin(this);
	}
	
	private T findMin(BinarySearchTreeNode<T> node) {
		if(node == null)
			return null;
		else if(node.left == null)
			return node.data;
		return findMin(node.left);
	}
	
	private T findMax() {
		return findMax(this);
	}
	
	private T findMax(BinarySearchTreeNode<T> node) {
		if(node == null)
			return null;
		else if(node.right == null)
			return node.data;
		return findMax(node.right);
	}

	public BinarySearchTreeNode<T> remove(T data) {
		return remove(data, this);
	}
	
	private BinarySearchTreeNode<T> remove(T data, BinarySearchTreeNode<T> node) {
		if(node == null)
			return node;
		
		int comparison = node.data.compareTo(data);
		
		if(comparison > 0)
			node.left = remove(data, node.left);
		else if(comparison < 0)
			node.right = remove(data, node.right);
		else if(node.left != null && node.right != null) {
			node.data = findMin(node.right);
			node.right = remove(node.data, node.right);
		}
		else
			node = (node.left != null) ? node.left : node.right;
		return node;
	}

	public boolean contains(T data) {
		return contains(data, this);
	}
	
	private boolean contains(T data, BinarySearchTreeNode<T> node) {
		if(node == null)
			return false;
		
		int comparison = data.compareTo(node.data);
		
		if(comparison < 0)
			return contains(data, node.left);
		else if(comparison > 0)
			return contains(data, node.right);
		else
			return true;
	}

	public int size() {
		return size(this);
	}
	
	private int size(BinarySearchTreeNode<T> node) {
		if(node == null)
			return 0;
		else
			return size(node.left) + size(node.right) + 1;
	}

	public int depth() {
		return depth(this)-1;
	}
	
	private int depth(BinarySearchTreeNode<T> node) {
		if(node == null)
			return 0;
	    return 1 + Math.max(depth(node.left), depth(node.right));
	}

	public String toString() {
		return toString(this);
	}
	
	private String toString(BinarySearchTreeNode<T> node) {
		if(node == null)
			return "";
		return ((node.left != null) ? toString(node.left) + ", " : "") + node.data.toString() + ((node.right != null) ? ", " + toString(node.right) : ""); 
	}
}
