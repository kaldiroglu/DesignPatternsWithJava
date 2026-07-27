package dev.kaldiroglu.dp.structural.decorator.io;

import java.io.*;

public class DataInputOutputStreamDemo {
	private static final String[] items = { "Thinking in Java", "JSF Applied", "Java Tutorial", "Java Security", "Swing Programming" };
	private static final double[] prices = { 34.99, 29.99, 35.99, 32.99, 40.99 };
	private static final int[] units = { 2, 3, 6, 2, 5 };
	private static final char separator = ':';

	public static void main(String[] args) {
		File file = new File("/Users/akin/Desktop/Invoice.txt");

		try {
			FileOutputStream fos = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(fos);
			createInvoice(dos);

			FileInputStream fis = new FileInputStream(file);
			DataInputStream dis = new DataInputStream(fis);
			 readInvoice(dis);
		} catch (FileNotFoundException e) {
			System.out.println("Problem when writing the invoice: " + e.getMessage());
		}
	}

	private static void readInvoice(DataInputStream dis) {
		double totalPrice = 0;
		double priceRead;
		int unit;
		try {
			System.out.print(dis.readUTF()); //Reading "   - - - I N V O I C E - - -    "
			for (int i = 0; i < items.length; i++) {
				char c = ' ';
				while(( c = dis.readChar()) != separator)
					System.out.print(c);
				System.out.print(c);	//Reading ':'
				System.out.print(dis.readChar()); //Reading '\t' 
				unit = dis.readInt();
				System.out.print(unit);
				System.out.print(dis.readChar()); //Reading '\t' 
				priceRead = dis.readDouble();
				totalPrice += unit * priceRead;
				System.out.print(priceRead); 
				System.out.print(dis.readChar()); //Reading '\r' 
			}
			dis.close();
			
			System.out.println("\nTotal of Invoice: $" + totalPrice);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void createInvoice(DataOutputStream dos) {

		try {
			dos.writeUTF("   - - - I N V O I C E - - -    ");
			dos.writeChar('\n');

			for (int i = 0; i < items.length; i++) {
				dos.writeChars(items[i]);
				dos.writeChar(separator);
				dos.writeChar('\t');
				dos.writeInt(units[i]);
				dos.writeChar('\t');
				dos.writeDouble(prices[i]);
				dos.writeChar('\n');
			}
			dos.close();
		} catch (IOException e) {
			System.out.println("Problem when writing the invoice: " + e.getMessage());
		}
	}
}

