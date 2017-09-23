import org.javatuples.Tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class RatingDao {
    public static Rating readFromFileWithNum(String filePath) throws Exception {
        Rating rating = new Rating();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = reader.readLine();
        rating.setNumOfUsers(Integer.parseInt(line));
        line = reader.readLine();
        rating.setNumOfItems(Integer.parseInt(line));
        while ((line = reader.readLine()) != null) {
            rating.add(rating.stringToTriplet(line, "\t"));
            //Tuple node = rating.stringToTriplet(line,"\t");
            //rateList.add(node);
        }
        return rating;
    }

    public static Rating readFromFileWithoutID(MapID mapUser,MapID mapItem,String filePath) throws Exception {
        Rating rating = new Rating();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] word = line.split("|");

        }
        return rating;
    }

    public static void testMap(Map map){
        map.put("哈哈",1);
    }
}
