import java.util.Random;
public class Choice {
    public static void main(String[] args) {
        Random random = new Random();
        final int ALTERNATIVES = 3;
        switch (random.nextInt(ALTERNATIVES)) {
            case 0 :
                System.out.println("0");
                break;
            case 1 :
                System.out.println("1");
                break;
            case 2 :
                System.out.println("2");
                break;
        }
    }
}