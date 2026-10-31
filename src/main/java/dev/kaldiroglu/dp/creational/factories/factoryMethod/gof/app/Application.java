package dev.kaldiroglu.dp.creational.factories.factoryMethod.gof.app;

import java.util.ArrayList;
import java.util.List;

public abstract class Application {
    private List<Document> documents;

    protected Application() {
        documents = new ArrayList<>();
    }

    public abstract Document createDocument();

    public void newDocument(){
        Document document = createDocument();
        documents.add(document);
        document.open();
    }

    public void openDocument(Document document){
        document.open();
    }
}
