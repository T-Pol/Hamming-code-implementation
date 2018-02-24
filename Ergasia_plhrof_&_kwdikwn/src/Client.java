import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import java.util.Scanner;


public class Client {

	/**
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		int number=0;
		Scanner s = new Scanner(System.in);

		Socket soc = new Socket("127.0.0.1",8006);
		Scanner s1 = new Scanner(soc.getInputStream());
		
		
		
		//Διαβαζουμε τον αριθμο των bits και στη συνεχεια τα bits
		boolean r1=false;
		while(r1==false)
		{
		
		System.out.println("Enter positive number of bits: ");
		number= s.nextInt();
		if(number>0)
		{r1=true;}
		}
		
		
		
		
		int a[] =new int[number];
		boolean r2;
		for(int i=0 ; i < number ; i++)
	     {
			r2=false;
			while(r2==false)
			{
				System.out.println("Enter bit number" + (i+1) + " (values  0 or 1):");
				a[i] = s.nextInt();
				if(a[i]==0 | a[i]==1)
				{r2 =true;}
			}
	     }

		System.out.print("You entered:");
	    for(int i=0 ; i < number ; i++)
	    {
	     System.out.print(a[i]);
	    }
	    System.out.println();
		
	    
	    boolean f=false;
	    int par_bit=0;
	    
	    //υπολογιζουμε τον αριθμο των parity bits που απαιτουνται 
	    //για τη μετατροπη των αρχικων bits σε bits hamming κωδικα
	    
	    while(f==false)
	    {
	    	if(number+par_bit+1<=Math.pow(2, par_bit))
	    	{
	    		f=true;
	    	}
	    	else
	    	{
	    		par_bit++;
	    	}
	    }
	    
	    System.out.println("Parity bits needed: "+par_bit);
	    
	    
	   
	    //Δημιουργουμε τον πινακα μαζι με τον αριθμο των parity bits
	    //Καλουμε την συναρτηση generateCode για να δημιουργησουμε το τελικο μήνυμα
	    int b[] = generateCode(a, par_bit);
		
		System.out.println("Generated code is:");
		for(int i=0 ; i < b.length ; i++) {
			System.out.print(b[i]);
		}
		System.out.println();

	    
		//Ζηταμε από το χρήστη να μας δώσει  πόσα σφάλματα θέιε να περιέχει το μήνυμα 
		
		//Κατα τον ορισμό του HAMMING ο κώσικας δε μπορει να διορθώσει παρα μόνο ένα σφάλμα ανα μήνυμα 
		//Θα μπορουσε να τροποποιηθει για να μπορεί να ελέγχει περισσότερα, αλλά τότε δεν θα ίσχυε ο HAMMING κώδικας
		int er=0;
		boolean r3=false;
		while(r3==false)
		{
		System.out.println("Enter the number of errors :");
		er= s.nextInt();
		if(er>=0)
		{r3=true;}
		}
		if(er==0)
		{System.out.println("No error has been added!");}
		else
		{
			int[] randomspicked = new int[er];
			SecureRandom r =new SecureRandom();
			
			for(int i=0; i<er;i++)
			{
				
				boolean z = false;
				while(z == false)
				{
					int pick = r.nextInt(number);
					
					
						boolean g=false;
						int j=0;
						while(g==false & j<er)
						{
							if(randomspicked[j] == pick)
							{	
								g=true;		
							}
							else
							{
								j++;
							}
						}
						
						if(g==false)
						{
							z=true;
							randomspicked[i]=pick;
							if(b[pick]==0)
							{b[pick]=1;}
							else
							{b[pick]=0;}
							System.out.println("Error in position "+(pick+1)+" has been added!");
						}
				}
			}
			
			//Προβάλουμε το μήνυμα με τα σφάλματα και το στέλνουμε στο server
			System.out.println("Generated code with errors is:");
			for(int i=0 ; i < b.length ; i++) {
				System.out.print(b[i]);
			}
			System.out.println();
		}
		
		//Στέλνουμε το μήνυμα στον server
		
		DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
		
		dout.writeInt(b.length);
		for(int i=0 ; i < b.length ; i++) 
		{
			dout.writeInt(b[i]);
		}

		
		DataInputStream din = new DataInputStream(soc.getInputStream());
		
	//Απάντηση του server
		
		int result= din.readInt();
		int c[] = new int[result];
 		
		System.out.println("Message from server:");
		for(int i=0; i<result; i++)
		{
			c[i]=din.readInt();
			System.out.print(c[i]);
		}
		System.out.println();
		
		boolean change=true;
		int l=0;
		while (change==true & l<c.length)
		{
			if(a[l]!=c[l])
			{
				System.out.println("Sorry , hamming canot correct multiple errors!!!");
				change=false;
			}
			l++;
		}
			
		
	}
	
	static int[] generateCode(int a[], int parity_count) {
		
		int b[];
		
		
		int i=0, j=0, k=0;
		
		
		// Το μηκος του b ειναι ισο με το αθροισμα του πινακα a και του αριθμου των parity bits 
		b = new int[a.length + parity_count];
		
		// Τοποθετουμε τον αριθμο 2 στη θεση του καθε parity bit μεχρι να το υπολογισουμε
		
		for(i=1 ; i <= b.length ; i++) {
			if(Math.pow(2, j) == i) {
			// Εντοπιζουμε τη καθε θεση parity bit καθως αυτες βρισκονται στις δυναμεις του 2.
				
				b[i-1] = 2;
				j++;
			}
			else {
				b[k+j] = a[k];
				k++;
			}
		}

				
		for(i=0 ; i < parity_count ; i++) {
			// Χρησιμοποιωντας even parity για καθε θεση parity bit καλουμε την συναρτηση getParity
			
			b[((int) Math.pow(2, i))-1] = getParity(b, i);
		}
		return b;
	}
	
	static int getParity(int b[], int power) {
		
		
		
		//Για τον υπολογισμου τις τιμης του καθε parity bit εργαζομαστε συμφωνα με τα παρακάτω
		//πρέπει για κάθε rity bit να ελεγξουμε την τιμη των αντιστοιχων bit ανάλογα με την απόστα ση που μας ορίζει το κάθε bit
		//Για παράδειγμα για το parity bit 1 πρέπει να ελέγχουμε την τιμή κάθε bit ανα ένα
		//Για το 2 πρέπει να ελέγχουμε την τιμη κάθε δυο bit κολλητα
		int parity = 0;
		
		int metr1=0;
		
		int pass = (int) Math.pow(2, power);
		
		int i=pass-1;
		//System.out.print("Checking positions: ");
		while(i<b.length)
		{
			/*if(b[i]==2)
			{
				System.out.println("Ypologismos parity bit "+i);
				System.out.print("Checking positions: ");
			}*/
		
			
			//Με τη παρακάτω διαδικασία καταφέρνουμε να ελέγξουμε τους ασους για κάθε parity biot ανάλογα με τις θέσεις που πρέπει κάθε φορά
			int j=i;
			while(j<pass+i & j<b.length)
			{
				//System.out.print(j);
				if(b[j]==1)
				{
					metr1++;
				}
				
				j++;
			}
				
			i=j+pass;
		}
		
		//System.out.println();
		
		if(metr1%2==0)
		{
			parity=0;	//Αφου έχουμε υπολογίσει των αριθμό των 1  ακολουθούμε των κανόνα για τον υπολογισμό των parity bit 
						//ορίζουμε ως 1 την τιμή του parity bit αν ο αριθμος των 1 ειναι περιττός και ως 0 αν είναι αρτιος
		}
		else
		{
			parity=1;
		}
		return parity;
	}

	
	

}
	

