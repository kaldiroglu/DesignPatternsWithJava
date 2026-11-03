package dev.kaldiroglu.dp.creational.factoryMethod.gof.figure;

public class TextManipulator implements Manipulator{

    @Override
    public void downClick() {

    }

    @Override
    public void drag() {
        System.out.println("TextManipulator moves the text cursor");
    }

    @Override
    public void upClick() {

    }
}
