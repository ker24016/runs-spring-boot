package edu.byui.ker24016.runsspringboot;

import edu.byui.ker24016.runsspringboot.model.Run;
import edu.byui.ker24016.runsspringboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

@SpringBootApplication
public class RunsSpringBootApplication implements CommandLineRunner {
    public static final Runnable ON_SKIP = () -> System.out.println("Skipped!");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss");
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

    @SuppressWarnings("RedundantThrows")
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Booting up...");
        System.out.println("Welcome!");
        try (Scanner scanner = new Scanner(System.in)) {
            Operation choice = null;
            while (choice != Operation.EXIT) {
                Operation.printOperations();
                System.out.print("What would you like to do? ");
                String opChoice = scanner.nextLine();
                if (opChoice.isBlank()) {
                    continue;
                }
                if (opChoice.length() != 1) {
                    System.out.println("Invalid option!");
                    continue;
                }
                choice = Operation.tryParseOperation(opChoice.charAt(0));

                switch (choice) {
                    case null -> System.out.println("Invalid option!");
                    case INVALID -> System.out.println("Invalid option!");
                    case CREATE -> {
                        Run.RunBuilder runBuilder = Run.builder();
                        
                        int runNum = (int) runs.count() + 1;
                        System.out.printf("Run Number %d\n", runNum);
                        runBuilder.id(runNum);

                        System.out.print("Time played (MM/dd/yyyy hh:mm:ss, or blank for \"now\"): ");
                        runBuilder.datePlayed(readT(scanner, input -> {
                            try {
                                return Optional.of(Instant.from(DATE_TIME_FORMATTER.parse(input)));
                            } catch (DateTimeParseException e) {
                                return Optional.empty();
                            }
                        }).orElseGet(Instant::now));

                        System.out.print("Iron source: ");
                        readT(scanner, (name) -> ironSources.findTopByNameEqualsIgnoreCase(name))
                                .ifPresentOrElse(runBuilder::ironSource, ON_SKIP);

                        System.out.print("Enter type: ");
                        readT(scanner, name -> enterTypes.findTopByNameEqualsIgnoreCase(name))
                                .ifPresentOrElse(runBuilder::enterType, ON_SKIP);

                        System.out.print("Gold source: ");
                        readT(scanner, name -> goldSources.findTopByNameEqualsIgnoreCase(name))
                                .ifPresentOrElse(runBuilder::goldSource, ON_SKIP);

                        System.out.print("Spawn biome: ");
                        readT(scanner, name -> biomes.findTopByNameEqualsIgnoreCase(name))
                                .ifPresentOrElse(runBuilder::spawnBiome, ON_SKIP);

                        System.out.print("Seed: ");
                        readLong(scanner)
                                .ifPresentOrElse(runBuilder::seed, ON_SKIP);

                        System.out.print("Gold dropped: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::goldDropped, ON_SKIP);

                        System.out.print("Blaze rods: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::blazeRods, ON_SKIP);

                        System.out.print("Blazes killed: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::blazesKilled, ON_SKIP);

                        System.out.print("Flint picked up: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::flintPickedUp, ON_SKIP);

                        System.out.print("Gravel mined: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::gravelMined, ON_SKIP);

                        System.out.print("Total deaths: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::totalDeaths, ON_SKIP);

                        System.out.print("Jumps: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::jumps, ON_SKIP);

                        System.out.print("Eyes used: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::eyesUsed, ON_SKIP);

                        System.out.print("Diamond picks crafted: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::diamondPicksCrafted, ON_SKIP);

                        System.out.print("Ender pearls used: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::enderPearlsUsed, ON_SKIP);

                        System.out.print("Obsidian placed: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::obsidianPlaced, ON_SKIP);

                        System.out.print("Diamond swords crafted: ");
                        readInt(scanner).ifPresentOrElse(runBuilder::diamondSwordCrafted, ON_SKIP);

                        runs.save(runBuilder.build());
                        System.out.println("Whew! We did it.");
                    }
                    case VIEW -> {
                        int runCount = (int) runs.count();
                        if (runCount < 1) {
                            System.out.println("Nothing to view!");
                            break;
                        }
                        System.out.printf("There are %d runs recorded.\n", runCount);
                        System.out.print("Starting run #: ");
                        int start = readInt(scanner, 1, runCount).orElseGet(() -> {
                            System.out.println("Using default (1)");
                            return 1;
                        });
                        System.out.print("Ending run #: ");
                        int end = readInt(scanner, start, runCount).orElseGet(() -> {
                            System.out.printf("Using default (%d)", start);
                            return start;
                        });
                        List<Run> matchedRuns = runs.getAllByIdBetweenOrderByIdAsc(start, end);
                        System.out.println(Run.getPrettyHeader());
                        for (int i = 0; i < matchedRuns.size(); i++) { // Paginate results
                            System.out.println(matchedRuns.get(i).prettyString());
                            if (i % 10 == 9 && i < matchedRuns.size() - 1) { // 10 is the magic number (page size). 9 is magic number - 1.
                                System.out.println("Press enter to see more...");
                                scanner.nextLine();
                                System.out.println(Run.getPrettyHeader());
                            }
                        }
                    }
                    case UPDATE -> {
                        int runCount = (int) runs.count();
                        if (runCount < 1) {
                            System.out.println("Nothing to update!");
                            break;
                        }
                        System.out.printf("There are %d runs recorded.\n", runCount);
                        System.out.print("Edit Run #: ");
                        int runNum;
                        while (true) {
                            var optRunNum = readInt(scanner, 1, runCount);

                            if (optRunNum.isPresent()) {
                                runNum = optRunNum.get();
                                break;
                            }
                            System.out.println("You can't skip this one!");
                        }
                        Run run = runs.getRunById(runNum);
                        System.out.println("Valid options:");
                        // This is not extensible at all. Please don't do this. In my case, this is not going to be
                        // maintained, it's just a proof that it can be done.
                        for (String field : new String[]{"id", "date_played", "iron_source", "enter_type", 
                                "gold_source", "spawn_biome", "seed", "gold_dropped", "blaze_rods", "blazes_killed", 
                                "flint_picked_up", "gravel_mined", "total_deaths", "jumps", "eyes_used", 
                                "diamond_picks_crafted", "ender_pearls_used", "obsidian_placed", "diamond_sword_crafted"}) {
                            System.out.println(field);
                        }
                        System.out.print("Edit what?: ");
                        String option = scanner.nextLine();
                        switch (option) {
                            case "id":
                                System.out.print("Run Number: ");
                                readInt(scanner).ifPresent(run::setId); break;
                            case "date_played":
                                System.out.print("Time played (MM/dd/yyyy hh:mm:ss, or blank for \"now\"): ");
                                run.setDatePlayed(readT(scanner, input -> {
                                try {
                                    return Optional.of(Instant.from(DATE_TIME_FORMATTER.parse(input)));
                                } catch (DateTimeParseException e) {
                                    return Optional.empty();
                                }
                            }).orElseGet(Instant::now)); break;
                            case "iron_source":
                                System.out.print("Iron source: ");
                                readT(scanner, (name) -> ironSources.findTopByNameEqualsIgnoreCase(name))
                                        .ifPresentOrElse(run::setIronSource, ON_SKIP);
                                break;
                            case "enter_type":
                                System.out.print("Enter type: ");
                                readT(scanner, name -> enterTypes.findTopByNameEqualsIgnoreCase(name))
                                        .ifPresentOrElse(run::setEnterType, ON_SKIP);
                                break;
                            case "gold_source":
                                System.out.print("Gold source: ");
                                readT(scanner, name -> goldSources.findTopByNameEqualsIgnoreCase(name))
                                        .ifPresentOrElse(run::setGoldSource, ON_SKIP);
                                break;
                            case "spawn_biome":
                                System.out.print("Spawn biome: ");
                                readT(scanner, name -> biomes.findTopByNameEqualsIgnoreCase(name))
                                        .ifPresentOrElse(run::setSpawnBiome, ON_SKIP);
                                break;
                            case "seed":
                                System.out.print("Seed: ");
                                readLong(scanner)
                                        .ifPresentOrElse(run::setSeed, ON_SKIP);
                                break;
                            case "gold_dropped":
                                System.out.print("Gold dropped: ");
                                readInt(scanner).ifPresentOrElse(run::setGoldDropped, ON_SKIP);
                                break;
                            case "blaze_rods":
                                System.out.print("Blaze rods: ");
                                readInt(scanner).ifPresentOrElse(run::setBlazeRods, ON_SKIP);
                                break;
                            case "blazes_killed":
                                System.out.print("Blazes killed: ");
                                readInt(scanner).ifPresentOrElse(run::setBlazesKilled, ON_SKIP);
                                break;
                            case "flint_picked_up":
                                System.out.print("Flint picked up: ");
                                readInt(scanner).ifPresentOrElse(run::setFlintPickedUp, ON_SKIP);
                                break;
                            case "gravel_mined":
                                System.out.print("Gravel mined: ");
                                readInt(scanner).ifPresentOrElse(run::setGravelMined, ON_SKIP);
                                break;
                            case "total_deaths":
                                System.out.print("Total deaths: ");
                                readInt(scanner).ifPresentOrElse(run::setTotalDeaths, ON_SKIP);
                                break;
                            case "jumps":
                                System.out.print("Jumps: ");
                                readInt(scanner).ifPresentOrElse(run::setJumps, ON_SKIP);
                                break;
                            case "eyes_used":
                                System.out.print("Eyes used: ");
                                readInt(scanner).ifPresentOrElse(run::setEyesUsed, ON_SKIP);
                                break;
                            case "diamond_picks_crafted":
                                System.out.print("Diamond picks crafted: ");
                                readInt(scanner).ifPresentOrElse(run::setDiamondPicksCrafted, ON_SKIP);
                                break;
                            case "ender_pearls_used":
                                System.out.print("Ender pearls used: ");
                                readInt(scanner).ifPresentOrElse(run::setEnderPearlsUsed, ON_SKIP);
                                break;
                            case "obsidian_placed":
                                System.out.print("Obsidian placed: ");
                                readInt(scanner).ifPresentOrElse(run::setObsidianPlaced, ON_SKIP);
                                break;
                            case "diamond_sword_crafted":
                                System.out.print("Diamond swords crafted: ");
                                readInt(scanner).ifPresentOrElse(run::setDiamondSwordCrafted, ON_SKIP);
                                break;
                            default:
                                System.out.println("Invalid input.");
                        }
                    }
                    case DELETE -> {
                        int runCount = (int)runs.count();
                        if (runCount < 1) {
                            System.out.println("Nothing to delete!");
                            break;
                        }
                        readInt(scanner, 1, runCount)
                                .ifPresent(runNum -> {
                                    runs.deleteById(runNum);
                                    System.out.println("Deleted!");
                                });
                    }
                    case EXIT -> System.out.println("Goodbye!");
                }
            }
        }
    }

    /**
     *
     * @param scanner
     * @return An empty optional if the default is requested (empty input).
     */
    public Optional<Integer> readInt(Scanner scanner) {
        return readInt(scanner, Integer.MIN_VALUE);
    }

    public Optional<Integer> readInt(Scanner scanner, int min) {
        return readInt(scanner, min, Integer.MAX_VALUE);
    }

    public Optional<Integer> readInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.isBlank()) {
                    return Optional.empty();
                }
                int parsed = Integer.parseInt(input);
                if (Math.clamp(parsed, min, max) != parsed) {
                    System.out.println("That's outside the valid range. Please try again:\n> ");
                    continue;
                }

                return Optional.of(parsed);
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number, or enter to skip.\n> ");
            }
        }
    }

    /**
     *
     * @param scanner
     * @return An empty optional if the default is requested (empty input).
     */
    public Optional<Long> readLong(Scanner scanner) {
        return readLong(scanner, Long.MIN_VALUE);
    }

    public Optional<Long> readLong(Scanner scanner, long min) {
        return readLong(scanner, min, Long.MAX_VALUE);
    }

    public Optional<Long> readLong(Scanner scanner, long min, long max) {
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.isBlank()) {
                    return Optional.empty();
                }
                long parsed = Long.parseLong(input);
                if (Math.clamp(parsed, min, max) != parsed) {
                    System.out.println("That's outside the valid range. Please try again:\n> ");
                    continue;
                }
                return Optional.of(parsed);
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number, or enter to skip.\n> ");
            }
        }
    }

    /**
     * 
     * @param scanner
     * @param parser Parses a string into a T. Returns an empty optional for no match. This is different from the return!
     * @return Reads from a scanner to a T. Returns an empty optional for a blank input. This is different from the parser!
     * @param <T>
     */
    public <T> Optional<T> readT(Scanner scanner, Function<String, Optional<T>> parser) {
        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.isBlank()) {
                    return Optional.empty();
                }
                var t = parser.apply(input);
                if (t.isPresent()) {
                    return t;
                }
                System.out.println("Invalid input. Please try again, or hit enter to skip.\n> ");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again, or hit enter to skip.\n> ");
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
