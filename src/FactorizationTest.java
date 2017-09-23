import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.List;

public class FactorizationTest {
    public static void main(String[] args) throws Exception {

        String trainFilePath = "D:\\360极速浏览器下载\\movieLens数据集\\ml-100k\\u1_1.base";//training文件路径
        String testFilePath = "D:\\360极速浏览器下载\\movieLens数据集\\ml-100k\\u1_1.test"; //test文件路径
        Rating rating = RatingDao.readFromFileWithNum(trainFilePath);

        System.out.println("梯度下降：");
        GradientDescent gd = new GradientDescent();
        gd.train(rating, 5,5,0.02,0.01);
        gd.rmseOfTestFile(testFilePath);

        System.out.println("ALS分解：");
        ALS als = new ALS();
        als.train(rating,5,5,0.01);
        als.rmseOfTestFile(testFilePath);
    }
}
