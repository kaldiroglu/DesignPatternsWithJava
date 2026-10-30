package dev.kaldiroglu.dp.behavioral.visitor.file.pattern1;

import java.util.List;

/** The same files with a second visitor. No file class changes. */
public class AccessMain {

	public static void main(String[] args) {
		List<File> files = List.of(new TextFile("Notes.txt"), new XMLFile("Pay.xml"));
		Visitor visitor = new AccessVisitor(false);

		for (File file : files) {
			file.open();
			if (file.accept(visitor))
				file.read();
			file.close();
		}
	}
}
