import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ser 
{
		
	public static void main(String args[]) throws IOException
	{
		int number;
		
		ServerSocket ser1 =new ServerSocket(8006);
		Socket s1 = ser1.accept();
	
		Scanner sc =new Scanner(s1.getInputStream());
		
		
		
		
		DataInputStream din = new DataInputStream(s1.getInputStream()); 
		
		
		//Ο server διαβαζει το μηνυμα που του στελνουμε 
		number = din.readInt();
			
		int b[] = new int[number];
		for(int i=0; i<number;i++)
		{
			b[i] = din.readInt();
		}
		

		int m=0, parity_bit=0;
		
		//Υπολογιζει ποσα parity bits περιεχει το μηνυμα
		boolean f=false;
		while(f==false)
		{
			if(Math.pow(2, m)<number)
			{	
				parity_bit++;
				m++;
			}
			else
			{ f=true;}
		}
	
		//Αρχικα ελεγχει αν το μηνυμα εχει σφάλμα
		int[] pb = new int[parity_bit];
		
		for(int h=0;h<parity_bit;h++)
		{
			pb[h] = 0; //Αρχικοποιούμε την κάθε τιμη parity bit 
			
		
					
					
													//Υπολογίζουμε για κάθε parity bit των αριθμό των 1
													//και για να μην υπάρχει σφάλμα πρέπει κάθε τιμή του πίνακα
													//pb να είναι 0
													
			
			int metr1=0;
			
			int pass = (int) Math.pow(2, h);
			
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
				pb[h]=0;
			}
			else
			{
				pb[h]=1;
			}
			
			
		}
		
		//Για να εντοπίσουμε το λάθος αρκέι να προσθέσουμε τις τιμές του πίνακα pb 
		//που είναι 1
		
		int error_place=0;// αν το error_place παραμένει 0 στη συνέχεια 
						  // τότε δεν έχουμε σφάλμα
						  //ο πίνακας pb περιέχει μόνο τιμές 0 και 1	
		for(int i=0;i<pb.length;i++)
		{
			if(pb[i]==1)
			{
				error_place= (int) (error_place+  Math.pow(2, i));
			}

		}
		
		if(error_place == 0)
		{
			//Δεν υπάρχει σφάλμα
			//Ξεκινα την απομονωση του αρχικου μηνυματος απο τα parity bits του
			int[] c = new int[number-parity_bit];
			int j=0,k=0;
			for(int i=1;i<=b.length;i++)
			{
				if(Math.pow(2, j) != i)
				{
					c[k]=b[i-1];
					k++;
				}
				else
				{j++;}
				
			}
			//Στελνει το μηνυμα πισω στο client για να το προβαλει
			DataOutputStream dout = new DataOutputStream(s1.getOutputStream());
			dout.writeInt(error_place);
			dout.writeInt(number-parity_bit);
			for(int i=0; i<number-parity_bit;i++)
			{
				dout.writeInt(c[i]);
			}


		}
		else
		{
			//Υπάρχει σφάλμα  στη θέση error_place
			//Για τη διόρθωση του αρκέι να αλλάξουμε τη τιμή  του b[error_place-1] 
			if(b[error_place-1]==0)
			{
				b[error_place-1]=1;
			}
			else
			{
				b[error_place-1]=0;
			}
			
			//Ξεκινα την απομονωση του αρχικου μηνυματος απο τα parity bits του
			int[] c = new int[number-parity_bit];
			int j=0,k=0;
			for(int i=1;i<=b.length;i++)
			{
				if(Math.pow(2, j) != i)
				{
					c[k]=b[i-1];
					k++;
				}
				else
				{j++;}
				
			}
			//Στελνει το μηνυμα πισω στο client για να το προβαλει
			DataOutputStream dout = new DataOutputStream(s1.getOutputStream());
			
			dout.writeInt(number-parity_bit);
			for(int i=0; i<number-parity_bit;i++)
			{
				dout.writeInt(c[i]);
			}

			
		}
		
				
		
		
				
	}
	
}
