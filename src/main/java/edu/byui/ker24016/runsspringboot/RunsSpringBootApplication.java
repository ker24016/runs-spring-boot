package edu.byui.ker24016.runsspringboot;

import edu.byui.ker24016.runsspringboot.model.*;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class RunsSpringBootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RunsSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Booting up...");
        try (StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .build()) {
            try (var sessionFactory =
                         new MetadataSources(registry)
                                 .addAnnotatedClasses(Biome.class,
                                         EnterType.class,
                                         GoldSource.class,
                                         IronSource.class,
                                         Run.class,
                                         Split.class,
                                         SplitId.class,
                                         SplitType.class)
                                 .buildMetadata()
                                 .buildSessionFactory()) {
                System.out.println("Connecting...");
                Session session = sessionFactory.openSession();
                System.out.println("Welcome!");
                try (Scanner scanner = new Scanner(System.in)) {
                    Operation choice = null;
                    while (choice != Operation.EXIT) {
                        Operation.printOperations();
                        System.out.print("What would you like to do? ");
                        String input = scanner.nextLine();
                        if (input.isBlank()) {
                            continue;
                        }
                        if (input.length() != 1) {
                            System.out.println("Invalid option!");
                            continue;
                        }
                        choice = Operation.tryParseOperation(input.charAt(0));

                        switch (choice) {
                            case null -> System.out.println("Invalid option!");
                            case INVALID -> System.out.println("Invalid option!");
                            case CREATE -> {
                                System.out.print("Run Number: ");
                                int runNum;
                                try {
                                    runNum = scanner.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("That's not a number.");
                                    break;
                                }
                                System.out.print("Date (ISO instant, or blank for \"now\"): ");
                                String dateInput = scanner.nextLine();
                                Instant datePlayed;
                                if (dateInput.isBlank()) {
                                    datePlayed = Instant.now();
                                } else {
                                    try {
                                        datePlayed = Instant.parse(dateInput);
                                    } catch (DateTimeParseException e) {
                                        System.out.println("That's invalid. I don't blame you tbh.");
                                        break;
                                    }
                                }
                                System.out.print("Iron source: ");
                                // TODO


                            }
                            case VIEW -> {
                            }
                            case UPDATE -> {
                            }
                            case DELETE -> {
                            }
                            case EXIT -> System.out.println("Goodbye!");
                        }
                    }
                }

            }
        }
    }

    public enum Operation {
        INVALID('¡', "Invalid"), // Must be on top, must not be the only one. See printOperations.
        CREATE('c', "Create a run"),
        VIEW('v', "View runs"),
        UPDATE('u', "Update a run"),
        DELETE('d', "Delete a run"),
        EXIT('x', "Exit");

        private final char key;
        private final String description;

        Operation(char key, String description) {
            this.key = key;
            this.description = description;
        }

        public static void printOperations() {
            Operation[] operations = Operation.values();
            for (int i = 1; i < operations.length; i++) {
                Operation operation = operations[i];
                System.out.printf("%d. %s (%c)", i, operation.description, operation.key);
            }
        }

        public static Operation tryParseOperation(char key) {
            for (Operation operation : Operation.values()) {
                if (operation.key == key) {
                    return operation;
                }
            }
            return null;
        }
    }

}
}
