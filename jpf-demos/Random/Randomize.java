import java.util.Random;

public class Randomize {
    public static void main(String[] args) {
        Random random = new Random();
        if (random.nextBoolean()) {
            System.out.println("if");
        } else {
            System.out.println("else");
        }
    }
}