package dev.kaldiroglu.dp.behavioral.visitor.animal.pattern2;

public class CatFeeder implements Feeder{

	@Override
	public void feed(Animal a) {
		System.out.println("Feeding the cat " + a.getName());
	}

}
