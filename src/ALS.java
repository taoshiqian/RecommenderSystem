import Jama.Matrix;
import org.javatuples.Decade;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ALS {
    private int factorNum = 5;       //分解因子数（或者叫 特征值数量），即分解后小矩阵的宽度。
    private int numOfIter = 20;      //迭代次数
    private double lambda = 0.01;     //正则化参数
    private int numOfUsers = 2000;   //用户数量
    private int numOfItems = 2000;   //物品数量（电影……）
    private List<Tuple>[] itemsOfUserRated; //该用户评价过的物品集合,下标是用户i
    private List<Tuple>[] usersOfRatedItem; //评价过该物品的用户集合,下标是物品j
    //private Matrix R;         //评分矩阵,太稀疏，升级版肯定不能用
    private Matrix users;     //分解的用户矩阵
    private Matrix items;     //分解的物品矩阵

    private void sortByID(List<Tuple>[] list) {
        for (int i = 0; i < list.length; i++) {
            Collections.sort(list[i], new Comparator<Tuple>() {
                @Override
                public int compare(Tuple o1, Tuple o2) { //小到大
                    return Double.compare(new Double(o1.getValue(0).toString()), new Double(o2.getValue(0).toString()));
                }
            });
        }
    }

    private void init(Rating rating) {
        itemsOfUserRated = new ArrayList[numOfUsers];
        usersOfRatedItem = new ArrayList[numOfItems];
        for (int i = 0; i < numOfUsers; i++)
            itemsOfUserRated[i] = new ArrayList<Tuple>();
        for (int i = 0; i < numOfItems; i++)
            usersOfRatedItem[i] = new ArrayList<Tuple>();
        for (int i = 0; i < rating.size(); i++) {
            int userId = rating.getUserId(i);
            int itemId = rating.getItemId(i);
            double rateScore = rating.getRateScore(i);
            //if (userId > numOfUsers) numOfUsers = userId;//更新用户编号最大值
            //if (itemId > numOfItems) numOfItems = itemId;//更新物品编号最大值
            itemsOfUserRated[userId].add(new Pair<Integer, Double>(itemId, rateScore));
            usersOfRatedItem[itemId].add(new Pair<Integer, Double>(userId, rateScore));
        }
        sortByID(itemsOfUserRated);
        sortByID(usersOfRatedItem);
        users = Matrix.random(factorNum, numOfUsers);
        items = Matrix.random(factorNum, numOfItems);
        for (int itemId = 0; itemId < numOfItems; itemId++) {
            double sum = 0.0;   //将item矩阵的第1行置为平均分
            for (int j = 0; j < usersOfRatedItem[itemId].size(); j++) {
                sum += (Double) usersOfRatedItem[itemId].get(j).getValue(1);
            }
            if (usersOfRatedItem[itemId].size() == 0) sum = 0.0;
            else sum /= usersOfRatedItem[itemId].size();
            items.set(0, itemId, sum);
        }
    }

    public double predict(int userId, int itemId) {
        double ret = 0;
        for (int f = 0; f < factorNum; f++) {
            ret += users.get(f, userId) * items.get(f, itemId);
        }
        return ret;
    }

    private void updateUser(int userId) throws Exception {
        if (itemsOfUserRated[userId].size() == 0) return;
        Matrix subMatOfItems = new Matrix(factorNum, itemsOfUserRated[userId].size());
        for (int f = 0; f < factorNum; f++) {
            for (int itemId = 0; itemId < itemsOfUserRated[userId].size(); itemId++) {
                subMatOfItems.set(f, itemId, items.get(f, (Integer) itemsOfUserRated[userId].get(itemId).getValue(0)));
            }
        }
        Matrix XtX = subMatOfItems.times(subMatOfItems.transpose());
        for (int f = 0; f < factorNum; f++) {
            XtX.set(f, f, XtX.get(f, f) + lambda * itemsOfUserRated[userId].size());  //LAMBDA*M
        }
        Matrix subMatOfRate = new Matrix(1, itemsOfUserRated[userId].size());
        for (int itemId = 0; itemId < itemsOfUserRated[userId].size(); itemId++) {
            subMatOfRate.set(0, itemId, (Double) itemsOfUserRated[userId].get(itemId).getValue(1));
        }
        Matrix XtY = subMatOfItems.times(subMatOfRate.transpose());
        users.setMatrix(0, factorNum - 1, userId, userId, XtX.solve(XtY));
    }

    private void updateItem(int itemId) throws Exception {
        if (usersOfRatedItem[itemId].size() == 0) return;
        Matrix subMatOfUsers = new Matrix(factorNum, usersOfRatedItem[itemId].size());
        for (int f = 0; f < factorNum; f++) {
            for (int userId = 0; userId < usersOfRatedItem[itemId].size(); userId++) {
                subMatOfUsers.set(f, userId, users.get(f, (Integer) usersOfRatedItem[itemId].get(userId).getValue(0)));
            }
        }
        Matrix XtX = subMatOfUsers.times(subMatOfUsers.transpose());
        for (int f = 0; f < factorNum; f++) {
            XtX.set(f, f, XtX.get(f, f) + lambda * usersOfRatedItem[itemId].size());  //LAMBDA*M
        }
        Matrix subMatOfRate = new Matrix(usersOfRatedItem[itemId].size(), 1);
        for (int userId = 0; userId < usersOfRatedItem[itemId].size(); userId++) {
            subMatOfRate.set(userId, 0, (Double) usersOfRatedItem[itemId].get(userId).getValue(1));
        }
        Matrix XtY = subMatOfUsers.times(subMatOfRate);
        items.setMatrix(0, factorNum - 1, itemId, itemId, XtX.solve(XtY));
    }

    private void learnALS() throws Exception {
        for (int iter = 0; iter < numOfIter; iter++) {
            for (int userId = 0; userId < numOfUsers; userId++)
                updateUser(userId);
            for (int itemId = 0; itemId < numOfItems; itemId++)
                updateItem(itemId);
            //rmseOfTestFile();
        }
    }

    public void rmseOfTestFile(String testFilePath) throws Exception {
        Rating rating = RatingDao.readFromFileWithNum(testFilePath);
        //if (rating.size() == 0) { System.out.println("没有测试数据");return;}
        double sum = 0.0;
        for (int i = 0; i < rating.size(); i++) {
            int userId = rating.getUserId(i);
            int itemId = rating.getItemId(i);
            double RateScore = rating.getRateScore(i);
            double predictScore = predict(userId, itemId);
            double error = RateScore - predictScore;
            sum += Math.pow(error, 2);
            //sum += Math.abs(error);
            //System.out.println(sum+"-----"+error+"---"+i);
            //System.out.println(userId + "," + itemId + "," + RateScore + "," + predictScore);
        }
        double rmse = Math.sqrt(sum / rating.size());
        //double mse = sum / rating.size();
        System.out.println(rmse);
    }

    private boolean findFromList(List<Tuple> list, int targetID) {
        int left = 0, right = list.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int middleID = Integer.parseInt(list.get(mid).getValue(0).toString());
            if (middleID == targetID) return true;
            else if (middleID < targetID) left = mid + 1;
            else right = mid - 1;
        }
        return false;
    }

    public List limitedRecommendByUserId(int userId,List<Integer> itemList,int num, boolean repeat) {//用户ID，推荐多少个，是否推荐重复的
        ArrayList<Pair<Integer, Double>> recommendList = new ArrayList<Pair<Integer, Double>>();
        for (int i=0;i<itemList.size();i++) {
            int itemId = itemList.get(i);
            if (repeat == false && findFromList(itemsOfUserRated[userId], itemId)) continue;
            recommendList.add(new Pair<Integer, Double>(itemId, predict(userId, itemId)));
        }
        Collections.sort(recommendList, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                return Double.compare(o2.getValue1(), o1.getValue1());
            }
        });
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < Math.min(recommendList.size(), num); i++) {
            ret.add(recommendList.get(i).getValue0());
        }
        return ret;
    }
    public List<Pair<Integer, Double>> recommendByUserId(int userId,int num, boolean repeat) {//用户ID，推荐多少个，是否推荐重复的
        ArrayList<Pair<Integer, Double>> recommendList = new ArrayList<Pair<Integer, Double>>();
        for (int itemId = 0; itemId < numOfItems; itemId++) {
            if (repeat == false && findFromList(itemsOfUserRated[userId], itemId)) continue;
            recommendList.add(new Pair<Integer, Double>(itemId, predict(userId, itemId)));
        }
        Collections.sort(recommendList, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                return Double.compare(o2.getValue1(), o1.getValue1());
            }
        });
        return recommendList.subList(0,Math.min(num,recommendList.size()));
    }

    public Rating recommendOneForAllUsers(Rating rating,boolean repeat,int factorNum, int numOfIter, double lambda) throws Exception{
        train(rating, factorNum,numOfIter,lambda);
        Rating ratingOfRecommend = new Rating();
        for(int userId=0;userId<numOfUsers;userId++){
            List<Pair<Integer, Double>> list = recommendByUserId(userId,1,repeat);
            if(list.size()==0) ratingOfRecommend.add(new Triplet<Integer,Integer,Double>(userId,-1,0.0));
            else ratingOfRecommend.add(new Triplet<Integer,Integer,Double>(userId,list.get(0).getValue0(),list.get(0).getValue1()));
        }
        return ratingOfRecommend;
    }

    public void train(Rating rating, int factorNum, int numOfIter, double lambda//, int numOfUsers, int numOfItems
    ) throws Exception {
        this.factorNum = factorNum;
        this.numOfIter = numOfIter;
        this.lambda = lambda;
        this.numOfUsers = rating.getNumOfUsers() ;
        this.numOfItems = rating.getNumOfItems() ;
        init(rating);
        learnALS();
        //rmseOfTestFile();
    }
}
