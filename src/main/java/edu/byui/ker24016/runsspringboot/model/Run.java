package edu.byui.ker24016.runsspringboot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@Table(name = "run", schema = "runs", indexes = {
        @Index(name = "fk_run_iron_source_idx", columnList = "iron_source"),
        @Index(name = "fk_run_enter_type1_idx", columnList = "enter_type"),
        @Index(name = "fk_run_gold_source1_idx", columnList = "gold_source"),
        @Index(name = "fk_run_biome1_idx", columnList = "spawn_biome")
})
@NoArgsConstructor
@AllArgsConstructor
public class Run {
    private static final String PRETTY_HEADER = "Run # | Date | Iron | Enter | Gold | Spawn | Seed | Gold Dropped | Rods | Blazes | Flint | Gravel | Deaths | Jumps | Eyes | Dia Picks | Pearls Used | Obsidian Placed | Dia Sword";
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date_played") private Instant datePlayed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iron_source")
    private IronSource ironSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enter_type")
    private EnterType enterType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gold_source")
    private GoldSource goldSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spawn_biome")
    private Biome spawnBiome;

    @Column(name = "seed") private Long seed;

    @Column(name = "gold_dropped") private Integer goldDropped;

    @Column(name = "blaze_rods") private Integer blazeRods;

    @Column(name = "blazes_killed") private Integer blazesKilled;

    @Column(name = "flint_picked_up") private Integer flintPickedUp;

    @Column(name = "gravel_mined") private Integer gravelMined;

    @Column(name = "total_deaths") private Integer totalDeaths;

    @Column(name = "jumps") private Integer jumps;

    @Column(name = "eyes_used") private Integer eyesUsed;

    @Column(name = "diamond_picks_crafted") private Integer diamondPicksCrafted;

    @Column(name = "ender_pearls_used") private Integer enderPearlsUsed;

    @Column(name = "obsidian_placed") private Integer obsidianPlaced;

    @Column(name = "diamond_sword_crafted") private Integer diamondSwordCrafted;

    public String prettyString() {
        return new StringBuilder(id)
                .append(" | ")
                .append(datePlayed)
                .append(" | ")
                .append(ironSource.getName())
                .append(" | ")
                .append(enterType.getName())
                .append(" | ")
                .append(goldSource.getName())
                .append(" | ")
                .append(spawnBiome.getName())
                .append(" | ")
                .append(seed)
                .append(" | ")
                .append(goldDropped)
                .append(" | ")
                .append(blazeRods)
                .append(" | ")
                .append(blazesKilled)
                .append(" | ")
                .append(flintPickedUp)
                .append(" | ")
                .append(gravelMined)
                .append(" | ")
                .append(totalDeaths)
                .append(" | ")
                .append(jumps)
                .append(" | ")
                .append(eyesUsed)
                .append(" | ")
                .append(diamondPicksCrafted)
                .append(" | ")
                .append(enderPearlsUsed)
                .append(" | ")
                .append(obsidianPlaced)
                .append(" | ")
                .append(diamondSwordCrafted)
                .toString();
    }

    public static String getPrettyHeader() {
        return PRETTY_HEADER;
    }
}