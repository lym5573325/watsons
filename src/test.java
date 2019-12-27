
import com.cloudstore.dev.api.util.HttpManager;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.hrm.company.DepartmentComInfo;

import javax.security.sasl.SaslServer;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class test {

    public static void main(String[] args){
        String a ="a;;b;";
        String[] arrry = a.split(";");
        System.out.println(arrry.length);
        for(String aa : arrry){
            System.out.println(aa);
        }
    }

    public static boolean checkPermit(String ip){
        String permitips = "127.0.0.1,192.168.0.1,192.36.*.1";
        if(permitips.length()>0){
            if(permitips.equals("*"))  return true;
            String[] permitipArray = permitips.split(",");
            for(int i=0; i<permitipArray.length;i++){
                String permitip = permitipArray[i];
                if(ip.equals(permitip)) return true;
                if(permitip.indexOf("*")!=-1){
                    String[] permitipSplitArray = permitip.split("\\*");
                    for(int j=0;j<permitipSplitArray.length;j++){
                        String permitipSplit = permitipSplitArray[j];
                        if(ip.indexOf(permitipSplit)==-1) break;
                        if(j==permitipSplitArray.length-1)  return true;
                    }
                }
            }
        }
        return false;
    }
}



