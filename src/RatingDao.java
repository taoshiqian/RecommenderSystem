import org.javatuples.Tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class RatingDao {
    public static Rating readFromFile(String filePath) throws Exception {
        Rating rating = new Rating();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            rating.add(rating.stringToTriplet(line,"\t"));
            //Tuple node = rating.stringToTriplet(line,"\t");
            //rateList.add(node);
        }
        return rating;
    }
}
