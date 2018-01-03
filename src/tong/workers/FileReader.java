package tong.workers;

import tong.objects.City;
import tong.objects.Clause;
import tong.utils.CnfUtil;
import tong.utils.TourUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FileReader {

    public FileReader() {}

    /*
    This reads the file and ignores the boilerplate information until it gets to the coordinates.
     */
    public void tspFileReader(Map<Integer, City> cities, File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            if(line.equals("NODE_COORD_SECTION")) {
                while(scanner.hasNext()) {
                    String[] values = scanner.nextLine().split(" ");
                    City city = new City(Integer.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]));
                    cities.put(city.getCityNum(), city);
                }
            }
        }
    }

    public void satFileReader(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()) {
            String line = scanner.nextLine();

            if(line.contains("p cnf")) {
                while(scanner.hasNext()) {
                    String[] clause = scanner.nextLine().split(" ");
                    Clause c = new Clause(Integer.valueOf(clause[0]), Integer.valueOf(clause[1]), Integer.valueOf(clause[2]));
                    CnfUtil.addClause(c);
                }
            }
        }

    }

    public void tspFileReader(List<City> cities, File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.equals("NODE_COORD_SECTION")) {
                while (scanner.hasNext()) {
                    String[] values = scanner.nextLine().split(" ");
                    City city = new City(Integer.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]));
                    cities.add(city);
                    TourUtil.addCity(city);
                }
            }
        }
    }
}
