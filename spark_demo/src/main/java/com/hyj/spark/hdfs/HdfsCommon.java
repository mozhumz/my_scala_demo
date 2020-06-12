package com.hyj.spark.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

public class HdfsCommon {
    private Configuration conf;
    private FileSystem fs;

    public HdfsCommon() throws IOException {
        conf = new Configuration();
        fs = FileSystem.get(conf);
    }

    /**
     * 上传文件，
     *
     * @param localFile 本地路径
     * @param hdfsPath  格式为hdfs://ip:port/destination
     * @throws IOException
     */
    public void upFile(String localFile, String hdfsPath) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(localFile));
        OutputStream out = fs.create(new Path(hdfsPath));
        IOUtils.copyBytes(in, out, conf);
    }

    /**
     * 附加文件
     *
     * @param localFile
     * @param hdfsPath
     * @throws IOException
     */
    public void appendFile(String localFile, String hdfsPath) throws IOException {
        InputStream in = new FileInputStream(localFile);
        OutputStream out = fs.append(new Path(hdfsPath));
        IOUtils.copyBytes(in, out, conf);
    }

    /**
     * 下载文件
     *
     * @param hdfsPath
     * @param localPath
     * @throws IOException
     */
    public void downFile(String hdfsPath, String localPath) throws IOException {
        InputStream in = fs.open(new Path(hdfsPath));
        OutputStream out = new FileOutputStream(localPath);
        IOUtils.copyBytes(in, out, conf);
    }

    /**
     * 删除文件或目录
     *
     * @param hdfsPath
     * @throws IOException
     */
    public boolean delFile(String hdfsPath) throws IOException {
        return fs.delete(new Path(hdfsPath), true);
    }

    /**
     * 使用偏移量方法将文件上传至HDFS
     *
     * @param encryptfilename 加密文件名
     * @param request         http请求，从request中读取文件数据
     * @return
     * @throws Exception
     */
    public static long upload2HDFSinOffset(String encryptfilename, HttpServletRequest request) throws Exception {

        if (encryptfilename == null || encryptfilename.equals(""))
            return 0;

        FileSystem hadoopFS = null;
        Configuration conf = null;
        long length = 0;

        String despath =  "/" + encryptfilename;
//        conf = HDFSHandler.conf;

        try {

            if (length <= 0) {
                hadoopFS = FileSystem.get(conf);
                FSDataOutputStream fsOutputStream = null;
                // 偏移量为0，首次上传，create方法;

                if (!hadoopFS.exists(new Path(despath))) {
                    fsOutputStream = hadoopFS.create(new Path(despath));
                    hadoopFS.close();
                    hadoopFS = FileSystem.get(conf);
                } else {
                    fsOutputStream = hadoopFS.create(new Path(despath));
                }

                ServletInputStream fos = request.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;

                while ((len = fos.read(buffer)) != -1) {
                    fsOutputStream.write(buffer, 0, len);
                    length += len;
                }
                fsOutputStream.flush();
                fsOutputStream.close();
                fos.close();
                hadoopFS.close();
                System.out.println("HDFSHandler if return :" + length);
                return length;
            } else {
                //偏移量非0，文件续传，append方法
                hadoopFS = FileSystem.get(conf);
                if (!hadoopFS.exists(new Path(despath))) {
                    hadoopFS.create(new Path(despath));
                    hadoopFS.close();
                    hadoopFS = FileSystem.get(conf);
                }
                FSDataOutputStream fsOutputStream2 = null;
                fsOutputStream2 = hadoopFS.append(new Path(despath));
                ServletInputStream fos2 = request.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = fos2.read(buffer)) != -1) {
                    fsOutputStream2.write(buffer, 0, len);
                    length += len;
                }
                fsOutputStream2.flush();
                fsOutputStream2.close();
                fos2.close();
                hadoopFS.close();
                return length;
            }
        } catch (Exception e) {// 用户中断上传，传回已接收到的文件长度（记录在偏移量表中，以待用户断线续传时传给用户）
            return length;
        }
    }

//    public static void main(String[] args) throws IOException {
//        HdfsCommon hdfs=new HdfsCommon();
//        hdfs.conf.setInt("io.file.buffer.size",40960);
//        System.out.println("start...");
//        long ms=System.currentTimeMillis();
//		hdfs.upFile("G:\\bigdata\\SVIP 预习课程\\2 生态架构课.zip",
//                "hdfs://master:9000/output/zip/"+ms+"test.zip");
//        System.out.println("ms:"+(System.currentTimeMillis()-ms));
////		hdfs.downFile("hdfs://localhost:9000/user/whuqin/input/file01copy", "/home/whuqin/fileCopy");
////		hdfs.appendFile("/home/whuqin/file01", "hdfs://localhost:9000/user/whuqin/input/file01copy");
////        System.out.println(hdfs.delFile("hdfs://master:9000/output/wc"));
//    }
}
