package edu.byui.ker24016.runsspringboot;

import edu.byui.ker24016.runsspringboot.model.*;
import edu.byui.ker24016.runsspringboot.repository.*;
import org.hibernate.Session;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Limit;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

@SpringBootApplication
public class RunsSpringBootApplication implements CommandLineRunner {
    protected Biomes biomes;
    protected EnterTypes enterTypes;
    protected GoldSources goldSources;
    protected IronSources ironSources;
    protected Runs runs;
    protected Splits splits;
    protected SplitTypes splitTypes;

    @Autowired
    public RunsSpringBootApplication(Biomes biomes, EnterTypes enterTypes, GoldSources goldSources, IronSources ironSources, Runs runs, Splits splits, SplitTypes splitTypes) {
        this.biomes = biomes;
        this.enterTypes = enterTypes;
        this.goldSources = goldSources;
        this.ironSources = ironSources;
        this.runs = runs;
        this.splits = splits;
        this.splitTypes = splitTypes;
    }

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
                                Run.RunBuilder runBuilder = Run.builder();

                                System.out.print("Run Number: ");
                                try {
                                    runBuilder.id(scanner.nextInt());
                                } catch (InputMismatchException e) {
                                    System.out.println("That's not a number.");
                                    break;
                                }

                                System.out.print("Date (ISO instant, or blank for \"now\"): ");
                                String dateInput = scanner.nextLine();
                                if (dateInput.isBlank()) {
                                    runBuilder.datePlayed(Instant.now());
                                } else {
                                    try {
                                        runBuilder.datePlayed(Instant.parse(dateInput));
                                    } catch (DateTimeParseException e) {
                                        System.out.println("That's invalid. I don't blame you tbh.");
                                        break;
                                    }
                                }

                                System.out.print("Iron source: ");
                                List<IronSource> ironSourceMatches = ironSources.findFirstByNameEqualsIgnoreCase(scanner.nextLine(), Limit.of(1));
                                if (ironSourceMatches.isEmpty()) {
                                    System.out.println("Couldn't find a matching iron source!");
                                } else {
                                    runBuilder.ironSource(ironSourceMatches.getFirst());
                                }

                                System.out.print("Enter type: ");
                                List<EnterType> enterTypeMatches = enterTypes.findFirstByNameEqualsIgnoreCase(scanner.nextLine(), Limit.of(1));
                                if (enterTypeMatches.isEmpty()) {
                                    System.out.println("Couldn't find a matching enter type!");
                                } else {
                                    runBuilder.enterType(enterTypeMatches.getFirst());
                                }

                                System.out.print("Gold source: ");
                                List<GoldSource> goldSourceMatches = goldSources.findFirstByNameEqualsIgnoreCase(scanner.nextLine(), Limit.of(1));
                                if (goldSourceMatches.isEmpty()) {
                                    System.out.println("Couldn't find a matching gold source!");
                                } else {
                                    runBuilder.goldSource(goldSourceMatches.getFirst());
                                }

                                System.out.print("Spawn biome: ");
                                List<Biome> biomeMatches = biomes.findFirstByNameEqualsIgnoreCase(scanner.nextLine(), Limit.of(1));
                                if (biomeMatches.isEmpty()) {
                                    System.out.println("Couldn't find a matching biome!");
                                } else {
                                    runBuilder.spawnBiome(biomeMatches.getFirst());
                                }

                                System.out.print("Seed: ");
                                try {
                                    runBuilder.seed(scanner.nextLong());
                                } catch (InputMismatchException e) {
                                    System.out.println("Skipped!");
                                }

                                System.out.print("Gold dropped: ");
                                intOrSkip(scanner, runBuilder::goldDropped);

                                System.out.print("Blaze rods: ");
                                intOrSkip(scanner, runBuilder::blazeRods);

                                System.out.print("Blazes killed: ");
                                intOrSkip(scanner, runBuilder::blazesKilled);

                                System.out.print("Flint picked up: ");
                                intOrSkip(scanner, runBuilder::flintPickedUp);

                                System.out.print("Gravel mined: ");
                                intOrSkip(scanner, runBuilder::gravelMined);

                                System.out.print("Total deaths: ");
                                intOrSkip(scanner, runBuilder::totalDeaths);

                                System.out.print("Jumps: ");
                                intOrSkip(scanner, runBuilder::jumps);

                                System.out.print("Eyes used: ");
                                intOrSkip(scanner, runBuilder::eyesUsed);

                                System.out.print("Diamond picks crafted: ");
                                intOrSkip(scanner, runBuilder::diamondPicksCrafted);

                                System.out.print("Ender pearls used: ");
                                intOrSkip(scanner, runBuilder::enderPearlsUsed);

                                System.out.print("Diamond swords crafted: ");
                                intOrSkip(scanner, runBuilder::diamondSwordCrafted);

                                runs.save(runBuilder.build());
                                System.out.println("Whew! We did it.");
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

    private static void intOrSkip(Scanner scanner, Consumer<Integer> consumer) {
        try {
            consumer.accept(scanner.nextInt());
        } catch (InputMismatchException e) {
            System.out.println("Skipped!");
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
