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
		
		
		//� server �������� �� ������ ��� ��� ��������� 
		number = din.readInt();
			
		int b[] = new int[number];
		for(int i=0; i<number;i++)
		{
			b[i] = din.readInt();
		}
		

		int m=0, parity_bit=0;
		
		//���������� ���� parity bits �������� �� ������
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
	
		//������ ������� �� �� ������ ���� ������
		int[] pb = new int[parity_bit];
		
		for(int h=0;h<parity_bit;h++)
		{
			pb[h] = 0; //������������� ��� ���� ���� parity bit 
			
		
					
					
													//������������ ��� ���� parity bit ��� ������ ��� 1
													//��� ��� �� ��� ������� ������ ������ ���� ���� ��� ������
													//pb �� ����� 0
													
			
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
			
				
				//�� �� �������� ���������� ������������ �� ��������� ���� ����� ��� ���� parity biot ������� �� ��� ������ ��� ������ ���� ����
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
		
		//��� �� ����������� �� ����� ����� �� ����������� ��� ����� ��� ������ pb 
		//��� ����� 1
		
		int error_place=0;// �� �� error_place ��������� 0 ��� �������� 
						  // ���� ��� ������ ������
						  //� ������� pb �������� ���� ����� 0 ��� 1	
		for(int i=0;i<pb.length;i++)
		{
			if(pb[i]==1)
			{
				error_place= (int) (error_place+  Math.pow(2, i));
			}

		}
		
		if(error_place == 0)
		{
			//��� ������� ������
			//������ ��� ��������� ��� ������� ��������� ��� �� parity bits ���
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
			//������� �� ������ ���� ��� client ��� �� �� ��������
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
			//������� ������  ��� ���� error_place
			//��� �� �������� ��� ����� �� ��������� �� ����  ��� b[error_place-1] 
			if(b[error_place-1]==0)
			{
				b[error_place-1]=1;
			}
			else
			{
				b[error_place-1]=0;
			}
			
			//������ ��� ��������� ��� ������� ��������� ��� �� parity bits ���
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
			//������� �� ������ ���� ��� client ��� �� �� ��������
			DataOutputStream dout = new DataOutputStream(s1.getOutputStream());
			
			dout.writeInt(number-parity_bit);
			for(int i=0; i<number-parity_bit;i++)
			{
				dout.writeInt(c[i]);
			}

			
		}
		
				
		
		
				
	}
	
}
