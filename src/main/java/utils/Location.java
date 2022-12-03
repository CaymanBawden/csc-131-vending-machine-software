package utils;

public class Location {
    public String address, city, state, zipCode;

    public Location(String location) {
        String[] locationData = location.split(";");
        address = locationData[0];
        city = locationData[1];
        state = locationData[2];
        zipCode = locationData[3];
    }

    public void print() {
        System.out.printf("%s %s %s %s\n", address, city, state, zipCode);
    }
}
