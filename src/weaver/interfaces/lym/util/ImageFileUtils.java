package weaver.interfaces.lym.util;

import weaver.conn.RecordSet;
import weaver.file.ImageFileManager;
import weaver.general.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 附件相关工具类
 */
public class ImageFileUtils {

    /**
     * 获取文件下载地址
     * @param docId
     * @return
     */
    public static String getFileUrl(String docId) {
        RecordSet rs = new RecordSet();
        rs.execute("select imagefileid from docimagefile where docid='"+docId+"'");
        rs.next();
        return "/weaver/weaver.file.FileDownload?fileid="+ Util.null2String(rs.getString("imagefileid"))+"&download=1";
    }

    /**
     * 获取附件名字
     * @param docId
     * @return
     */
    public static String getFileName(String docId) {
        RecordSet rs = new RecordSet();
        rs.execute("select imagefilename from docimagefile where docid='"+docId+"'");
        rs.next();
        return Util.null2String(rs.getString("imagefilename"));
    }

    public static int getImageFileId(String docId) {
        RecordSet rs = new RecordSet();
        rs.execute("select imagefileid from docimagefile where docid='"+docId+"'");
        rs.next();
        return Util.getIntValue(rs.getString("imagefileid"));
    }

    /**
     * 下载文档到本地
     * @param docid 文档ID
     * @param path  下载的文件地址+文件名
     */
    public static boolean downloadFile(int docid,String path){
        try{
            InputStream in = ImageFileManager.getInputStreamById(getImageFileId(docid+""));
            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while((length = in.read(buffer)) > 0){
                output.write(buffer,0,length);
            }
            fileOutputStream.write(output.toByteArray());
            in.close();
            fileOutputStream.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
