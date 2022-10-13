package Data;

public class Location {
    String location;

    // we might need fields like address, country, zip code

    public Location(String location) {
        this.location = location;
    }

    // TODO: make location validated
    public Boolean isValidLocation() {
        return true;
    }

    // TODO: match two locations if they are the same
    public Boolean locationMatches() {
        return true;
    }
}
