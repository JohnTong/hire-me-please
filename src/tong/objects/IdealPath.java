package tong.objects;

public class IdealPath {
    private String route;
    private double distance;

    public IdealPath() {}
    public IdealPath(String route, double distance) {
        this.route = route;
        this.distance = distance;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Ideal path is " + route + " with a distance of " + distance + " units.";
    }
}