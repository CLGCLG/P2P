package test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ReadObject {
	public static void main(String[] args) {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream("F:\\workspace\\taotao\\test\\src\\test\\object.txt"));
			Person p = (Person) ois.readObject();
			System.out.println(p);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
