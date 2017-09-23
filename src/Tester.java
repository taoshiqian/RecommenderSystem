import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleToLongFunction;

public abstract class Tester {

    protected static MapID mapUser = new MapID();
    protected static MapID mapItem = new MapID();
    protected ALS als = new ALS();

    protected DataConvert dataConvert = null;

    public Tester() {
    }

    protected Tester(DataConvert dataConvert) {
        this.dataConvert = dataConvert;
    }

    protected abstract boolean isLimitedData(String[] words);

    protected boolean isLegalUser(String s) {
        if (s.length() != 12) return false;
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            if (!((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F'))) return false;
        }
        return true;
    }

    protected List<String> getLines(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    protected void ratingToResult(Rating rating) {
        for (int i = 0; i < rating.size(); i++) {
            Tuple tuple = rating.getTuple(i);
            String result = dataConvert.tupleToString(tuple);
            System.out.println(result);
        }
    }

    protected void linesToRating(Rating rating, List<String> lines) throws Exception{
        for (String line : lines) {
            String[] words = line.split("\\|");
            //if (words.length != 4) continue;//if (!isLegalUser(words[0]) || tuple.getValue() <= 0 || itemDuration <= 0)continue;   //System.out.println(line + " 不合法");// Tuple tuple = wordsToTuple(words);
            Tuple tuple = dataConvert.stringsToTuple(words);
            rating.add(tuple);
        }
        rating.setNumOfUsers(mapUser.getSize());
        rating.setNumOfItems(mapItem.getSize());
    }


    public void testFromFile(String filePath, boolean repeat, int factorNum, int numOfIter, double lambda) throws Exception {
        Rating ratingTrain = new Rating();
        List<String> lines = getLines(new File(filePath));
        linesToRating(ratingTrain,lines);
        Rating ratingRecommend = als.recommendOneForAllUsers(ratingTrain,repeat, factorNum, numOfIter, lambda);
        ratingToResult(ratingRecommend);
        System.out.println(mapUser.getSize()+"   "+mapItem.getSize());
    }

    public void testFromDirectory(String directory, boolean repeat, int factorNum, int numOfIter, double lambda) throws Exception {
        File fileDirectory = new File(directory);
        if(fileDirectory.isDirectory()==false){System.out.println("目录错误");return ;}
        File[] files = fileDirectory.listFiles();
        Rating ratingTrain = new Rating();
        for(File file:files) if(file.isFile()) {
             List<String> lines = getLines(file);
             linesToRating(ratingTrain,lines);
        }
        Rating ratingRecommend = als.recommendOneForAllUsers(ratingTrain, repeat, factorNum, numOfIter, lambda);
        ratingToResult(ratingRecommend);
        System.out.println(mapUser.getSize()+"   "+mapItem.getSize());
    }






    protected void testRMSE(String filePath) throws Exception { //测试用
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        List<String> lines = new ArrayList<>();
        String line = null;
        double sum = 0.0;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split("\\|");
            if (isLimitedData(words) == false) continue; //sum+=1;
            Tuple tuple = dataConvert.stringsToTuple(words);
            int userId = (int) tuple.getValue(0);
            int itemId = (int) tuple.getValue(1);
            // if(userId>=mapUser.getSize()||itemId>=mapItem.getSize()) continue;
            double score = (double) tuple.getValue(2);
            sum += Math.pow(score - als.predict(userId, itemId), 2.0);
            //System.out.println(sum);
            count++;
        }
        double rmse = Math.sqrt(sum / count);
        System.out.println(rmse);
    }
}
