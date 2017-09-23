import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) throws Exception {
// get name representing the running Java virtual machine.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
// get pid
        String pid = name.split("@")[0];
        System.out.println("Pid is:"+pid);


        String trainDirectory = "E:\\data\\OCN\\train";
        //String testDirectory  = "E:\\data\\OCN\\test";

        //Tester tester = new Tester4();
        //tester.testFromDirectory(trainDirectory,false, 10, 20, 0.01);
        //tester.testFromFile("E:\\data\\OCN\\split\\out1.txt", false, 10, 20, 0.01);

        Tester tester = new Tester8();
       // tester.testFromFile("E:\\data\\OCN\\train8out\\out.txt", false, 10, 20, 0.01);
        tester.testFromFile("E:\\data\\OCN\\userWatchTable8.txt", false, 10, 20, 0.01);

        //tester.testRMSE("E:\\data\\OCN\\test\\20160504.txt");
        System.out.println("总内存：" + Runtime.getRuntime().totalMemory()
                / 1024 / 1024 + "M");
        System.out.println("空闲内存：" + Runtime.getRuntime().freeMemory()
                / 1024 / 1024 + "M");
        System.out.println("最大内存："+Runtime.getRuntime().maxMemory()
                /1024/1024+"M");
        System.out.println("已使用内存："
                + (Runtime.getRuntime().totalMemory() - Runtime
                .getRuntime().freeMemory()) / 1024 / 1024 + "M");

        while (true);
    }
}




