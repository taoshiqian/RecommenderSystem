import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.List;

public class FactorizationTest {
    public static void main(String[] args) throws Exception {

        String trainFilePath = "D:\\360极速浏览器下载\\movieLens数据集\\ml-100k\\u1.base";//training文件路径
        String testFilePath = "D:\\360极速浏览器下载\\movieLens数据集\\ml-100k\\u1.test"; //test文件路径
        GradientDescent gd = new GradientDescent();
        gd.train(trainFilePath, testFilePath, 20,200,0.02,0.01);

//        Rating rating = new Rating();
//        List<Tuple> rateList = rating.getRatings();
//        ratingList.add(new Triplet<Integer, Integer, Double>(1, 2, 3.0));
//        ratingList.add(new Triplet<Integer, Integer, Double>(4, 5, 6.0));
//        System.out.println(rating);
//        System.out.println(ratingList);
//        GradientDescent g = new GradientDescent();
//        g.testTuple();
//        Rating rating = new  Rating();
//        rating.getRatings().add(new Triplet<Integer,Integer,Double>(1,2,33.3));
//        System.out.println(rating.getRatings());
    }
}
