package lab2AndAnd;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 * 
 * @author Andreas Andersson Systemutveckling
 * 10/9-15
 * This class is an app that translates Swedish to French by a GUI.
 * The App fetches Keys and Values from the Hashtable and lets the user
 * choose one word at the time to translate.
 *
 */
public class FrenchTranslaterApp {
	
	private String[] wordArr;
	private int size=1;
	private Hashtable ht=new Hashtable(size);
	
	/**
	 * The constructor puts a couple of words into the HashTable and calls the
	 * "Start" method.
	 */
	public FrenchTranslaterApp(){
		
		ht.put("God dag", "Bonjour");
		ht.put("Hund", "Chien");
		ht.put("Fisk", "Poisson");
		ht.put("Sko", "Chaussures");
		ht.put("Tala", "Parle");
		ht.put("Kök", "Cuisine");
		ht.put("Apa", "Singe");
		ht.put("Affär", "Magasin");
		ht.put("Jordgubbe", "Fraise");	
		
		start();
	}
	/**
	 * This Method creates and shows the entire GUI. It Fetches all keys
	 * from the Hashtable and put them into an String[] that is shown in the GUI.
	 * The User chooses a key and a JOptionPane will show the result of the Translation.
	 */
	public void start(){
		wordArr= new String[ht.count()];
		for(int i=0;i<ht.count();i++){
			wordArr[i]= (String) ht.getKeys()[i];
		}
		
		JFrame frame = new JFrame ("");
			
			String word = (String) JOptionPane.showInputDialog(frame,
					"Välj ett ord och tryck Ok, eller tryck Avbryt för att avsluta.", "The Frenchenizer",
					JOptionPane.QUESTION_MESSAGE,
					null,
					wordArr,
					wordArr[0]);
			
			
			if(word!=null){
				JOptionPane.showMessageDialog(null,"Svenska: "+ word+"\nFranska: "+ ht.get(word));
				
				start();
				}
				else{
					System.exit(0);
				}
	}
	/**
	 * Start method.
	 * @param args - String array with command-line arguments
	 */
	public static void main(String [] args){
		new FrenchTranslaterApp();
	}
}
