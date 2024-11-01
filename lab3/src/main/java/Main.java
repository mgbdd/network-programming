import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write a location: ");
        String location = scanner.nextLine();
        LocationExplorer explorer = new LocationExplorer(location, scanner);


        explorer.startExploring();
        scanner.close();
    }

}
