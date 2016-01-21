package Hashtable;

/** 
 * 
 * @author Andreas Andersson Systemutveckling
 * 10/9-15
 * Entry is one of the keyelements to create a Hashtable. The Entry
 * carries a Key, and a Value and returns this information by getValue
 * and getKey. The method Equals is used to compare two Object to see if 
 * their keys are the same.
 *
 */

public class Entry {
		
	private Object value,key;
	
	/**
	 * Constructor that recieves two Parameters,Object and Object.
	 *	One Object is used to Locate the Information, and the other Object
	 *	is the Information. 
	 * @param key - The "Key" to locate the information.
	 * @param value -The Value/Information this class will carry.
	 */
	
	public Entry(Object key, Object value){
	this.key = key;
	this.value = value;
	}

	/**
 	* This method returns a Boolean, whether it's true or false that
 	* the two object is equal to eachother. Is used to compare "Key-Objects".
 	* @return boolean
 	*/
	@Override
	public boolean equals(Object obj) {
		
	Entry keyToCompare = new Entry(obj, null);
	return key.equals(keyToCompare.getKey());
	}
	
	/**
	 * getValue() is used to return the Value/Information.
	 * @return Object
	 */
	public Object getValue(){
		return value;
	}
	/**
	 * getKey() is used to return the Key which is the reference to Value.
	 * @return Object
	 */
	public Object getKey(){
		return key;
	}
}
