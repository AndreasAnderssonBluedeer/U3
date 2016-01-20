package lab2AndAnd;
/**
 * 
 * @author Andreas Andersson Systemutveckling
 * 10/9-15
 * TestHashtable is only made to test the Hashtable quick and simple by
 * prints in the Console. It calls the Hashtable methods: Put,Remove,Count.
 *
 */
public class TestHashtable {
	
	/**
	 * Start method. Tests the Hashtable with diffrent Methods.
	 * @param args - String array with command-line arguments
	 */
	public static void main(String [] args){
		Hashtable ht=new Hashtable(1);
		

		ht.put("God dag", "Bonjour");
		ht.put("Hund", "Chien");
		ht.put("Fisk", "Poisson");
		ht.put("Sko", "Chaussures");
		ht.put("Tala", "Parle");
		ht.put("Kök", "Cuisine");
		ht.put("Apa", "Singe");
		ht.put("Affär", "Magasin");
		ht.put("Jordgubbe", "Fraise");	
		
		System.out.println("Antal element: "+ht.count());
		System.out.println();
		
		ht.put(1, "HEJ");
		System.out.println("Antal element: "+ht.count());
		System.out.println();
		
		ht.put("Then", "DÅ");
		System.out.println("Antal element: "+ht.count());
		System.out.println();
		
		ht.put(1, "HEeJ");
		System.out.println("Antal element: "+ht.count());
		System.out.println();	
		
		System.out.println("Hämta NR :2- "+ht.get("Hello"));
		System.out.println("Antal element: "+ht.count());
		System.out.println();
		
		ht.remove("Hello");
		System.out.println("Antal element: "+ht.count());
		System.out.println();
		
		ht.remove("Kök");
		System.out.println("Antal element: "+ht.count());
		System.out.println();
		
		ht.put("Hello", "HEJ");
		System.out.println("Antal element: "+ht.count());
		System.out.println();	
		
		System.out.println(ht.getInsertionOrder());
		System.out.println("Antal element: "+ht.count());
		System.out.println();
	}

}
