package dev.kaldiroglu.dp.behavioral.templateMethod.pattern;

import java.util.ArrayList;
import java.util.List;

public abstract class Application {
	private List<Document> documents = new ArrayList<>();

	void openDocument(String fileName) {
		if (canOpenDocument(fileName)) {
			Document doc = createDocument(fileName);
			addDocument(doc);
		}
	}

	abstract boolean canOpenDocument(String fileName);
	
	abstract Document createDocument(String fileName);
	
	void addDocument(Document doc) {
		documents.add(doc);
	}
}