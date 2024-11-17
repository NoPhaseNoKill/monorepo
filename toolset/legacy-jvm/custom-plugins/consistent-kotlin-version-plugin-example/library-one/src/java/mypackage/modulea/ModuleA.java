package mypackage.modulea;

import org.apache.commons.io.FilenameUtils;
import jakarta.activation.MimeType;

public class ModuleA extends MimeType {

    public String doWork() {
        String kotlinCompiledValue = SomeKotlinFile.INSTANCE.doSomething();
        System.out.println("Using SomeKotlinFile.doSomething() inside of java file. Value: " + kotlinCompiledValue);
        String javaValue = FilenameUtils.normalize("/a/../b");
        System.out.println("Returning java value of: " + javaValue + " from doWork()");
        return javaValue;
    }
}
