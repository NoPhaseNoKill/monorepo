package integraboost;


public class App1 {

    public static void main(String[] args) {
        SomeClass someClass = new SomeClass(10);

        if(someClass.getInitialValue() == 10) {
            System.out.println("Success");
        }
    }
}
