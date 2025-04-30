import gov.nasa.jpf.vm.Verify;

public class VerifyRandomize {
    public static void main(String[] args) {
        Boolean random1 = Verify.getBoolean();
        Boolean random2 = Verify.getBoolean();

        FlipFlop ff1= new FlipFlop();
        FlipFlop ff2= new FlipFlop();

        if (random1) {
            System.out.println("1-set");
            ff1.set();
        } else {
            System.out.println("1-reset");
            ff1.reset();
        }
        if (random2) {
            System.out.println("2-set");
            ff2.set();
        } else {
            System.out.println("2-reset");
            ff2.reset();
        }
        if(!ff1.value && !ff2.value){
            System.out.println("oh oh");
            int x=1/0;
        }
    }
    static class FlipFlop{
        public boolean value;

        public void set(){
            value=true;
        }
        public void reset(){
            value=false;
        }
    }
}
