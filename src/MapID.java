import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapID {
    private Map<String, Integer> mapId;//=new HashMap<>();
    private List<String> listName;
    private int size;

    public MapID() {
        mapId = new HashMap<String, Integer>();
        listName = new ArrayList<>();
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public int getId(String s) {
        if (mapId.containsKey(s)) return mapId.get(s);
        else {
            mapId.put(s, size);
            listName.add(size,s);
            return size++;
        }
    }

    public String getName(int i){
        if(i<0||i>=listName.size()) return "无";
        return listName.get(i);
    }
    public boolean contains(String s){
        if(mapId.containsKey(s)) return true;
        else return false;
    }
}
