import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final String LETTERS = "RLRFR";
    
    public static final int NUMBER_OF_THREADS = 1000;
    public static final int ROUTE_LENGTH = 100;

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            new Thread(
                    () -> {
                        String route = generateRoute(LETTERS, ROUTE_LENGTH);
                        int frequency = (int) route.chars().filter(ch -> ch == 'R').count();

                        synchronized (sizeToFreq) {
                            if (sizeToFreq.containsKey(frequency)) {
                                sizeToFreq.put(frequency, sizeToFreq.get(frequency) + 1);
                            } else {
                                sizeToFreq.put(frequency, 1);
                            }
                        }
                    }
            ).start();
        }

        Map.Entry<Integer, Integer> numberOfRepetitions = sizeToFreq
                .entrySet().stream().max(Map.Entry.comparingByValue()).get();
        System.out.println("Самое частое количество повторений " + numberOfRepetitions.getKey() +
                " (встретилось " + numberOfRepetitions.getValue() + " раз)");

        System.out.println("Другие размеры:");
        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(x -> System.out.println("- " + x.getKey() + " (" + x.getValue() + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
