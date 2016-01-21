package lab2AndAnd;

import java.util.Iterator;
import java.util.LinkedList;

/** 
 * 
 * @author Andreas Andersson Systemutveckling
 * 10/9-15
 * Hashtable is contructed by Entry objects. The class can return the size of the amount elements by Count(), 
 * return an LinkedList<Object> with all Values from the diffrent Entry:s or all the Keys in a Object[].
 * It can also Add and Remove Entry:s by Remove and Put. Get() returns a key to an Entry and HashIndex gives
 * an index to the LinkedList<Entry>.
 *
 */
public class Hashtable {

	private LinkedList<Object> insertionOrder = new LinkedList<Object>();
	private LinkedList<Entry>[] table;

	/**
	 * Constructor that creates a HashTable with the size from the Parameter 
	 * and fills it with LinkedList<Entry> :s. 
	 * @param size - an int with the HastTable Size.
	 */
	public Hashtable(int size) {
		table = (LinkedList<Entry>[]) new LinkedList<?>[size];
		for (int i = 0; i < size; i++) {
			table[i] = new LinkedList<Entry>();
		}
	}
	
	/**
	 * This method creates a HashValue/HashCode (Index) within the Hashtables
	 * size and returns it.
	 * @param key - The Object this method will calculate with to create the Index/HashCode
	 * @return int
	 */
	private int hashIndex(Object key) {
		int hashCode = key.hashCode();
		hashCode = hashCode % table.length;
		return (hashCode < 0) ? -hashCode : hashCode;
	}
	
	/**
	 * This Method takes an Object as a Key in the Parameter and
	 * returns the Keys Element/Value if it Exists.
	 * @param key -Reference to the Element.
	 * @return Object
	 */
	public Object get(Object key){
		int hashIndex=hashIndex(key);
		LinkedList<Entry> entries=table[hashIndex];
		Iterator<Entry> it =entries.listIterator();
		
		while(it.hasNext()){
			Entry entry=it.next();
			if(entry.equals(key)){
				return entry.getValue();
			}
		}
		return null;
	}

	
	/**
	 * Returns the number of elements.
	 * @return int
	 */
	public int count(){
		return insertionOrder.size();	
	}
	
	/**
	 * Creates a new Entry and puts it in the Hashtable, and the
	 * Element/Value in InsertionOrder.
	 * it Recieves two Parameters, Object Key and Object Value that is used to
	 * create the Entry.
	 * @param key -Reference-point to the Entry
	 * @param value -Information/Value for the Entry to carry.
	 */
	public void put(Object key, Object value) {

		int index = hashIndex(key); 

		if (get(key) == null) { 
			table[index].addFirst(new Entry(key, value));
			insertionOrder.addLast(value);

			//System.out.println("*Put* genomförd." + key + ", " + value + " är Tillagt");

		} else {
			//System.out.println("*Put* kunde inte utföras.");
		}
	}
	
	/**
	 * Removes an Entry by the Reference to an Entry via the Parameter Key.
	 * @param key -Reference to the Entry that will be Removed.
	 */
	public void remove(Object key) {

		Object toBeRemoved = get(key);
		int index=hashIndex(key);
		if (toBeRemoved != null) {
			
			for(int i=0;i<table[index].size();i++){
				
				if(table[index].get(i).equals(key)){
					
					table[index].remove(i);
					insertionOrder.remove(toBeRemoved);
				}
			
			}

		//	System.out.println("*Remove* genomförd.");

		} else {
			System.out.println("*Remove* kunde inte utföras.");
		}
	}
	
	/**
	 * Returns a LinkedList<Object> with all Elements from the diffrent Entry:s.
	 * @return LinkedList<Object>
	 */
	public LinkedList<Object> getInsertionOrder(){
		return insertionOrder;
	}
	
	/**
	 * Returns the Keys to all Entry objects in an Array.
	 * @return Object[]
	 */
	public Object[] getKeys() {
		Object[] keys = new Object[count()];
		int j = 0;
		for (int i = 0; i < table.length; i++) {
			for (int k = 0; k < table[i].size(); k++) {
				if (j < keys.length) {
					keys[j] = table[i].get(k).getKey();
					j++;
				}
			}
		}
		return keys;
	}
	
}
