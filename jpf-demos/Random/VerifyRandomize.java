import gov.nasa.jpf.vm.Verify;

public class VerifyRandomize {
    public static void main(String[] args) {
        Boolean random = Verify.getBoolean();
        if (random) {
            System.out.println("if");
        } else {
            System.out.println("else");
        }
    }
}
