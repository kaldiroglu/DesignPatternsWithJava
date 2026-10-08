package dev.kaldiroglu.dp.behavioral.observer.publisher;

public class InstitutionalSubscriber extends AbstractSubscriber  {

	public InstitutionalSubscriber(String name) {
		super(name);
	}
	
	public void receive(Publication publication) {
		putOnShelf(publication);
	}

	public void putOnShelf(Publication publication) {
		System.out.println(publication.getIssue() + " is on the shelf of " + name);
	}
}
