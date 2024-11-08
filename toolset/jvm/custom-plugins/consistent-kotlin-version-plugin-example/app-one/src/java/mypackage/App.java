package mypackage;

import mypackage.modulea.ModuleA;
import org.slf4j.LoggerFactory;

public class App extends ModuleA {

    public static void main(String[] args) {
        new App().doWork();
    }
}
