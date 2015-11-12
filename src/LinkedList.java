import java.io.Serializable;

class ListNode implements Serializable
{
	// Constructors
	ListNode( Word w )
	{
		this( w, null );
	}

	ListNode( Word w, ListNode n )
	{
		element = w;
		next    = n;
	}   
	// Friendly data; accessible by other package routines
	Word   element;
	ListNode next;
}


public class LinkedList implements Serializable
{    
	private ListNode header;
	public LinkedList( )
	{
		header = new ListNode( null );
	}

	public boolean isEmpty( )
	{
		return header.next == null;
	}
	/** make the list empty.*/
	public void makeEmpty( )
	{
		header.next = null;
	}

	/**
	 * return iterator that points to the header node.
	 */
	public LinkedListItr zeroth( )
	{
		return new LinkedListItr( header );
	}

	/* return iterator that points to the node next to header (can be null if the list is empty.)*/
	public LinkedListItr first( )
	{
		return new LinkedListItr( header.next );
	}

	/** insert a new node following the position pointed to by p.
	 * @param x item to be in the new node.
	 * @param p iterator of the position before the new node.
	 */
//	public void insert( Object x, LinkedListItr p )
//	{
//		if( p != null && p.current != null )
//			p.current.next = new ListNode( x, p.current.next );
//	}

	/**
	 * @param x object that we want to find.
	 * @return iterator that points to the first node that has x. 
	 *If x is not in the list, the iterator points to null.
	 *
	 */
	public LinkedListItr find( String s )
	{
		ListNode itr = header.next;

		while( itr != null && !itr.element.word.equals(s) )
			itr = itr.next;

		return new LinkedListItr( itr );
	}


	/**
	 * return iterator that points to a node before the first 
	 *node that has x. If there is no x in the list, return iterator *that points to the last node in the list.  
	 */
	public LinkedListItr findPrevious( Object x )
	{
		ListNode itr = header;

		while( itr.next != null && !itr.next.element.equals( x ) )
			itr = itr.next;

		return new LinkedListItr( itr );
	}


	/**
	 * remove the first node with x from the list.
	 * @param x is the item to be removed from the list.
	 */
	public void remove( Object x )
	{
		LinkedListItr p = findPrevious( x );

		if( p.current.next != null ) //mean x is found because p is not the last member.
			p.current.next = p.current.next.next; //move reference over x
	}


	public static void printList( LinkedList theList )
	{
		if( theList.isEmpty( ) )
			System.out.print( "Empty list" );
		else
		{
			LinkedListItr itr = theList.first( );
			for( ; !itr.isPastEnd( ); itr.advance( ) )
				System.out.println( itr.retrieve( ) + " " );
		}

		System.out.println( );
	}
	public int size(){
		int count = 0;
		LinkedListItr itr = new LinkedListItr(this.header.next);
		while(itr.current != null){
			count++;
			itr.advance();
		}
		return count;
	}
	public LinkedListItr last(){

		ListNode itr = header;

		while( itr.next != null )
			itr = itr.next;

		return new LinkedListItr( itr );
	}
	
	public void remove(LinkedListItr itr){
		this.remove(itr.current.element);
		
	}
	
	public boolean isin(Object x){
		LinkedListItr itr = new LinkedListItr(this.header.next);
		while(itr.current != null){
			if(itr.current.element == x) return true;
			itr.advance();
		}
		return false;
	}
	
	public void insert(Word w, LinkedListItr p){
		if(p!= null && p.current != null){
			p.current.next = new ListNode(w, p.current.next);
		}
//		ListNode temp =  p.current.next;
//		p.current.next = new ListNode(x, temp);
	}
	
	public void replace(Word w, LinkedListItr itr){
		if(itr!= null && itr.current != null){
			ListNode temp = itr.current.next;
			itr.current = new ListNode(w,temp);
		}
	}
	
	public void insertAtFront(Word w){
		insert(w, this.zeroth());
	}
	public Object removeAtLast(){
		LinkedListItr itr = new LinkedListItr(this.header);
		while(itr.current.next.next != null) itr.advance();
		Object temp = itr.current.next.element;
		itr.current.next = null;
		return temp;
	}
	
	public LinkedList reverseList(){
		LinkedListItr itr = new LinkedListItr(this.header.next);
		LinkedList reversedList = new LinkedList();
		while(itr.current != null){
			reversedList.insertAtFront(itr.current.element);
			itr.advance();
		}
		return reversedList;
	}

	public static LinkedList specificElements(LinkedList C, int[] p){
		LinkedList answer = new LinkedList();
		ListNode header = new ListNode(null);
		ListNode temp = new ListNode(null);
		header.next = temp;
		for(int i = 0; i < p.length; i++){
			LinkedListItr itr = new LinkedListItr(C.header.next);
				for(int j = 1; j < p[i]; j++){
					itr.advance();
				}
				temp.next = new ListNode(itr.current.element);
				temp = temp.next;		
		}
		answer.header = header.next;
		return answer;	
		
	}
	public void reverse(){
		rev(this.header.next, this.header);
	}
	public void rev(ListNode n, ListNode header){
		if(n.next.next != null) rev(n.next, header);
		else header.next = n.next;
		n.next.next = n;
		n.next = null;
	}

}