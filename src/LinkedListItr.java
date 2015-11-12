import java.io.Serializable;

public class LinkedListItr implements Serializable
{
	ListNode current;    //interested position 

	/**
	 * @param theNode any node in the list
	 */
	LinkedListItr( ListNode theNode )
	{
		current = theNode;
	}


	/**  see if current has passed the last element of the list.
	 * @return true if current is null
	 */
	public boolean isPastEnd( )
	{
		return current == null;
	}

	/**
	 * @return item stored in current, or null if    
	 * current is not in a list.
	 */
	public Object retrieve( )
	{
		return isPastEnd( ) ? null : current.element;
	}

	/**
	 * move current to the next position in the list.
	 * If current is null, do nothing.
	 */
	public void advance( )
	{
		if( !isPastEnd( ) )
			current = current.next;
	}
	
	


}


