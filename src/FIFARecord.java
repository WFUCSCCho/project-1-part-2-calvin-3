
import java.util.Objects;

public class FIFARecord implements Comparable<FIFARecord> {

    //core fields
    private final String playerSlug;
    private final String name;
    private final String fullName;
    private final String bestPosition;
    private final int overallRating;   //ordering my bst by overallRating
    private final int potential;

    // parameterized constructor
    public FIFARecord(String playerSlug, String name,String bestPosition, String fullName, int overallRating, int potential) {
        this.playerSlug = playerSlug;
        this.name = name;
        this.bestPosition = bestPosition;
        this.fullName = fullName;
        this.overallRating = overallRating;
        this.potential = potential;
    }

    // toString method - to show key info and the players overall rating
    @Override
    public String toString() {
        return "FIFARecord{" +
                "name='" + name + '\'' +
                ", overall=" + overallRating +
                ", potential=" + potential +
                ", best_position='" + bestPosition + '\'' +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FIFARecord other)) return false;
        return Objects.equals(this.playerSlug, other.playerSlug);
    }

    // compareTo methods
    @Override
    public int compareTo(FIFARecord o) {
        // Order by overall_Rating DESC (better players first)
        int byOverall = Integer.compare(o.overallRating, this.overallRating);
        if (byOverall != 0) return byOverall;

        // tie-break by name, then by playerSlug to achieve total order
        // because a lot of players have the same rating
        int byName = this.name.compareToIgnoreCase(o.name);
        if (byName != 0) return byName;

        return this.playerSlug.compareToIgnoreCase(o.playerSlug);
    }

    //public int hashCode() { return Objects.hash(playerSlug, version); }

    // getters
    public int getOverallRating() {
        return overallRating;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBestPosition() {
        return bestPosition;
    }

    public int getPotential() {
        return potential;
    }
}
