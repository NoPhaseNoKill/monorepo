package integraboost;

import javax.activation.MimeType;

public class CapabilityConflictAvoidanceExampleApp {

    public static void main(String[] args) {
        System.out.println(MimeType.class.getProtectionDomain().getCodeSource().getLocation());
    }
}
