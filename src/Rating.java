import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Rating {

    private int numOfUsers = 1;
    private int numOfItems = 1;
    private List<Tuple> rateList;

    public Rating() {
        rateList = new ArrayList<>();
    }

    public List<Tuple> getRatings() {
        return rateList;
    }

    public Tuple getTuple(int index){return rateList.get(index);}

    public void add(Tuple tuple) {
        rateList.add(tuple);
    }

    public int getUserId(int index) {
        return (Integer) rateList.get(index).getValue(0);
    }

    public int getItemId(int index) {
        return (Integer) rateList.get(index).getValue(1);
    }

    public double getRateScore(int index) {
        return (Double) rateList.get(index).getValue(2);
    }

    public int size() {
        return rateList.size();
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(int numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(int numOfItems) {
        this.numOfItems = numOfItems;
    }

    public Tuple stringToTriplet(String line, String splitStr) {
        String[] fields = line.split(splitStr);
        if (fields.length != 4) {
            throw new IllegalArgumentException("Each line must contain 4 fields");
        }
        int userId = Integer.parseInt(fields[0]);
        int movieId = Integer.parseInt(fields[1]);
        double rating = Double.parseDouble(fields[2]);
        //long timestamp = Long.parseLong(fields[3]);
        return new Triplet<Integer, Integer, Double>(userId, movieId, rating);
    }

    //public Rating append(Rating rating2){ }
}