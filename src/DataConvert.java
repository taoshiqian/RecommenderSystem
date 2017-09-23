import org.javatuples.Tuple;

public interface DataConvert {
    public Tuple stringsToTuple(String[] words)throws Exception;
    public String tupleToString(Tuple tuple);
}
