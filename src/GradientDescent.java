import com.sun.xml.internal.bind.v2.runtime.output.DOMOutput;
import org.javatuples.Triplet;
import org.javatuples.Tuple;
import sun.management.snmp.jvmmib.JvmThreadInstanceTableMeta;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class GradientDescent {
    private String trainFilePath = null;//training文件路径
    private String testFilePath = null; //test文件路径
    private int factorNum = 20;       //分解因子数（或者叫 特征值数量），即分解后小矩阵的宽度。
    private int maxOfIter = 200;      //迭代次数
    private double alpha = 0.02;      //学习速率
    private double lambda = 0.01;     //正则化参数
    private int maxOfUsers = 2000;    //最大用户数量
    private int maxOfItems = 2000;    //最大物品数量（电影……）
    private double[][] users;    //分解后的用户矩阵
    private double[][] items;    //分解后的物品矩阵（电影……）
    private double[] userBias;   //针对用户的打分偏见，用于优化
    private double[] itemBias;   //针对物品的打分偏见，用于优化
    private double sumRateScore = 0.0;  //评分总和，用于求平均
    private double avgRateScore = 0.0;  //平均评分，用于优化

    private void init(Rating rating) throws Exception {
        double divisor = Math.sqrt(factorNum);
        if (divisor == 0.0) {
            System.out.println("除以0啦，赶紧看看");
            return;
        }

        userBias = new double[maxOfUsers];
        users = new double[maxOfUsers][factorNum];
        for (int i = 0; i < maxOfUsers; i++) {
            userBias[i] = 0.0;
            for (int j = 0; j < factorNum; j++) {
                users[i][j] = Math.random() / divisor;
            }
        }

        items = new double[maxOfItems][factorNum];
        itemBias = new double[maxOfItems];
        for (int i = 0; i < maxOfItems; i++) {
            itemBias[i] = 0.0;
            for (int j = 0; j < factorNum; j++) {
                items[i][j] = Math.random() / divisor;
            }
        }


        for (int i = 0; i < rating.size(); i++) {
            int userId = rating.getUserId(i);
            int itemId = rating.getItemId(i);
            double rateScore = rating.getRateScore(i);
            if (userId > maxOfUsers) maxOfUsers = userId;
            if (itemId > maxOfItems) maxOfItems = itemId;
            sumRateScore += rateScore;
        }
        if (rating.size() > 0) avgRateScore = sumRateScore / rating.size();
        else System.out.println("除以0，赶紧看看读取数据的哪个地方错了？");
    }

    private double predict(int userId, int itemId) {
        double ret = avgRateScore + userBias[userId] + itemBias[itemId];
        for (int f = 0; f < factorNum; f++) {
            ret += users[userId][f] * items[itemId][f];
        }
        return ret;
    }

    private void learn(Rating rating) {
        for (int iter = 0; iter < maxOfIter; iter++) {
            for (int i = 0; i < rating.size(); i++) {
                int userId = rating.getUserId(i);
                int itemId = rating.getItemId(i);
                double RateScore = rating.getRateScore(i);
                double predictScore = predict(userId, itemId);
                double error = RateScore - predictScore;
                userBias[userId] += alpha * (error - lambda * userBias[userId]);
                itemBias[itemId] += alpha * (error - lambda * itemBias[itemId]);
                for (int f = 0; f < factorNum; f++) {
                    users[userId][f] += alpha * (items[itemId][f] * error - lambda * users[userId][f]);
                    items[itemId][f] += alpha * (users[userId][f] * error - lambda * items[itemId][f]);
                }
            }
            alpha *= 0.9;
        }
    }

    public void rmseOfTest() throws Exception {
        Rating rating = RatingDao.readFromFile(testFilePath);
        double sum = 0.0;
        for(int i=0;i<rating.size();i++){
            int userId = rating.getUserId(i);
            int itemId = rating.getItemId(i);
            double RateScore = rating.getRateScore(i);
            double predictScore = predict(userId, itemId);
            double error = RateScore - predictScore;
            sum += Math.pow(error,2);
            System.out.println(userId+","+itemId+","+RateScore+","+predictScore);
        }
        double rmse = Math.sqrt(sum/rating.size());
        System.out.println(rmse);
    }

    public void train(String trainFilePath, String testFilePath, int factorNum, int maxOfIter, double alpha, double lambda//, int maxOfUsers, int maxOfItems
    ) throws Exception {
        this.trainFilePath = trainFilePath;
        this.testFilePath = testFilePath;
        this.factorNum = factorNum;
        this.maxOfIter = maxOfIter;
        this.alpha = alpha;
        this.lambda = lambda;
        //this.maxOfUsers = maxOfUsers;
        //this.maxOfItems = maxOfItems;
        Rating rating = RatingDao.readFromFile(trainFilePath);
        init(rating);
        learn(rating);
        rmseOfTest();
    }
}
