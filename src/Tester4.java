import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.List;

public class Tester4 extends Tester{

    private static final int USER_ID_INDEX = 0;
    private static final int ITEM_ID_INDEX=1;
    private static final int USER_DURATION_INDEX=2;
    private static final int ITEM_DURATION_INDEX=3;



    public Tester4(){
        //mapUser.contains("55");  //mapUser是父类Tester的成员变量，protected修饰的
        super(
                new DataConvert() {   //DataConvert是一个接口
                    @Override
                    public Tuple stringsToTuple(String[] words) throws Exception{

                        int userId = mapUser.getId(words[USER_ID_INDEX]);
                        int itemId = mapItem.getId(words[ITEM_ID_INDEX]);
                        int userDuration = Integer.parseInt(words[USER_DURATION_INDEX]);
                        int itemDuration = Integer.parseInt(words[ITEM_DURATION_INDEX]);
                        double score = userDuration * 1.0 / itemDuration;//computeScore(userDuration,itemDuration);计算score
                        return new Triplet<Integer, Integer, Double>(userId, itemId, score);
                    }
                    @Override
                    public String tupleToString(Tuple tuple) {
                        int userId=(int)tuple.getValue(0);
                        int itemId=(int)tuple.getValue(1);
                        double score = (double)tuple.getValue(2);
                        return new String("给" + mapUser.getName(userId) + "推荐:" + mapItem.getName(itemId));//+",评分"+score);
                    }
                }
        );
    }

    @Override
    protected boolean isLimitedData(String[] words){ //测试用
        if(mapUser.contains(words[USER_ID_INDEX])==false) return false;
        if(mapItem.contains(words[ITEM_ID_INDEX])==false) return false;
        return true;
    }
}
