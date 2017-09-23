import org.javatuples.Triplet;
import org.javatuples.Tuple;

public class Tester8 extends Tester {

    private static final int USER_ID_INDEX=0;// userIdIndex = 0;
    private static final int TYPE = 1;
    private static final int SOURCE = 2;
    private static final int USER_START_TIME=3;//userStartTime = 3;
    private static final int USER_DURATION_INDEX=4;//userDurationIndex = 4;
    private static final int ITEM_ID_INDEX=5;//itemIdIndex = 5;
    private static final int ITEM_START_TIME=6;//itemStartTime = 6;
    private static final int ITEM_DURATION_INDEX=7;//itemDurationIndex = 7;

    public Tester8(){
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
