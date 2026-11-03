package dev.kaldiroglu.dp.creational.factoryMethod.company.solution.fm3;

public class DirectorFactoryImpl implements DirectorFactory{

	@Override
	public Director create(String name, String department, int bonus) {
		Director director = new Director(EmployeeRandomizer.createId(), name,
				EmployeeRandomizer.createYear(), department, 40, bonus);
		return director;
	}
}
