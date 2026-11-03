package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.variety1;

public class DirectorFactory extends EmployeeFactory {

	@Override
	public Director create() {
		Director director = new Director(EmployeeRandomizer.createId(), EmployeeRandomizer.createName(),
				EmployeeRandomizer.createYear(), "Management", 40, 5000);
		return director;
	}

}
