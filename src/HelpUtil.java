import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HelpUtil {
    public static void main(String[] args) throws Exception {
        //BufferedWriter w1 = new BufferedWriter(new FileWriter("E:\\data\\split\\out1.txt"));
        BufferedWriter w = new BufferedWriter(new FileWriter("E:\\201601-04.txt",true));
        File fileDirectory = new File("E:\\data\\OCN\\train8");
        if (fileDirectory.isDirectory()) {
            File[] files = fileDirectory.listFiles();
            for (File file : files) if (file.isFile()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line = null;
                    while ((line=reader.readLine())!=null){
                        String[] words = line.split("\\|");
                        String out = words[0]+"|0|东方卫视|20170101095225|"+words[2]+"|"+words[1]+"|20170101094500|"+words[3]+"\r\n";
                        w.write(out);
                    }
            }
        }
    }
}
