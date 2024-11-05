import org.apache.commons.io.FilenameUtils;
import jakarta.activation.MimeType;

public class ModuleA extends MimeType {

    public String doWork() {
        String result = SomeKotlinFile.INSTANCE.doSomething();
        System.out.println("Calling kotlin function from java. Result was:" + result);
        return FilenameUtils.normalize("/a/../b");
    }
}
