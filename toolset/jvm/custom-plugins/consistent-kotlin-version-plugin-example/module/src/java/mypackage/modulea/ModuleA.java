package mypackage.modulea;

import org.apache.commons.io.FilenameUtils;
import jakarta.activation.MimeType;

public class ModuleA extends MimeType {

    public String doWork() {
        return FilenameUtils.normalize("/a/../b");
    }
}
