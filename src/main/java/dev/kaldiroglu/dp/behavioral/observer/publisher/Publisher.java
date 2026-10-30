package dev.kaldiroglu.dp.behavioral.observer.publisher;

import java.time.LocalDate;

public class Publisher {
	private Publication newsweek;
	private Publication fourFourTwo;
	
	public Publisher() {
		newsweek = new Newsweek("Newsweek");
		fourFourTwo = new FourFourTwo("FourFourTwo");
	}	
	
	public void publishNewsweek(){
		System.out.println("\n New Newsweek On The Way");
		newsweek.publish(LocalDate.now().toString());
	}
	
	public void publishFourFourTwo(){
		System.out.println("\n New FourFourTwo On The Way");
		fourFourTwo.publish(LocalDate.now().toString());
	}

	public Publication getNewsweek() {
		return newsweek;
	}

	public Publication getFourFourTwo() {
		return fourFourTwo;
	}
}
