package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez & Juan O. Lopez
 * @author James O Rodriguez Feliciano
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	@Override
	public void add(E e) {
		/* Special case: Be careful when the new value is the smallest */
		Node<E> newNode = new Node<>(e);
		if(this.isEmpty()) // list empty, item is first node
		{
			this.head = newNode;
		}
		else if(this.head.getValue().compareTo(e)> 0) // item is smallest item of the list
		{
			newNode.setNext(head);
			head = newNode;
		}
		else // item belongs after the head
		{
			Node<E> curNode = head;
			for(int i = 0; i<this.size(); i++) 
			{
				if(curNode.getNext() == null) // item is biggest
				{
					curNode.setNext(newNode);
					break;
				}
				// item is greater than head but less than last item 
				if(curNode.getNext().getValue().compareTo(e)>0) 
				{
					newNode.setNext(curNode.getNext());
					curNode.setNext(newNode);
					break;
				}
				curNode = curNode.getNext(); // iterate
			}
		
		}
		this.currentSize++; // increase size, very important
		

	}

	@Override
	public boolean remove(E e) {
		/* Special case: Be careful when the value is found at the head node */
		Node<E> rmNode = head, curNode = head;
		if(this.isEmpty()) return false;
		if(curNode.getValue().compareTo(e) == 0) // OFF WITH ITS HEAD!
		{
			head = head.getNext(); // set head to next 
			rmNode.clear(); // eliminate
			this.currentSize--; // decrease size
			return true;
		}
		
		for(int i = 0; i<this.size()-1; i++)
		{
			// node to remove found
			if(curNode.getNext().getValue().compareTo(e) == 0)
			{
				rmNode = curNode.getNext();
				curNode.setNext(rmNode.getNext());
				rmNode.clear();
				this.currentSize--; // decrease size
				return true;
			}
			
			curNode = curNode.getNext();
		}
		/* if it loop through entire list,
		 * then the node is not in this list
		 * and thus, false must be returned 
		 * */
		return false;
	}

	@Override
	public E removeIndex(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		/* Special case: Be careful when index = 0 */
		Node<E> rmNode = head, curNode = head;
		E value = null;
		if(index == 0) // remove head
		{
			head = head.getNext();
			value = rmNode.getValue();
			rmNode.clear();
			this.currentSize--;
		}
		else
		{
			for(int i = 1; i<this.size(); i++) 
			{
				// node to remove found
				if(i==index)
				{
					rmNode = curNode.getNext();
					value = rmNode.getValue();
					curNode.setNext(rmNode.getNext());
					rmNode.clear();
					this.currentSize--; // decrease size
					break;
				}
					
				curNode = curNode.getNext();
			}
		}
		
		return value;
	}

	@Override
	public int firstIndex(E e) {
		Node<E> curNode = head;
		int target = -1;
		for(int i = 0; i<this.size(); i++)
		{
			if(curNode.getValue().compareTo(e) == 0)
			{
				target = i;
				break;
			}
			curNode = curNode.getNext(); // iterate
		}
		return target;
	}

	@Override
	public E get(int index) {
		/*
		 * make sure the index is actually within range
		 * */
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		Node<E> curNode = this.head;
		E value = null;
		for(int i = 0; i<this.size(); i++) 
		{
			if(index == i) // index reached
			{
				value = curNode.getValue();
				break;
			}
			curNode = curNode.getNext();
		}
		return value;
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}
