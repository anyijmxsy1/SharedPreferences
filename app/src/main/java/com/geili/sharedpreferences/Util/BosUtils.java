package com.geili.sharedpreferences.Util;

import android.util.Log;

import com.baidubce.BceClientException;
import com.baidubce.BceServiceException;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.CreateBucketResponse;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.baidubce.services.bos.model.ObjectMetadata;
import com.baidubce.services.bos.model.PutObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BosUtils {
    public static void BosClienthandle(String ak, String sk, String endPoint, final String bucketName,final String path,final String fileName){
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ak, sk));
        config.setEndpoint(endPoint); //Bucket所在区域
        final BosClient client = new BosClient(config);

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    //创建Bucket
                    String newbucketName="preferences1";
                    CreateBucketResponse response = client.createBucket(newbucketName); //新建一个Bucket并指定Bucket名称
                    Log.d("xsy",response.getLocation());
                    Log.d("xsy",response.getName());

                    //上传Object
                    File file = new File(path);//上传文件的目录
                    PutObjectResponse putObjectFromFileResponse = client.putObject(newbucketName, fileName, file);
                    Log.d("xsy",putObjectFromFileResponse.getETag());

                    //查看Object
                    ListObjectsResponse list = client.listObjects(newbucketName);
                    for (BosObjectSummary objectSummary : list.getContents()) {
                        Log.d("ObjectKey: " , objectSummary.getKey());
                    }

                    // 获取Object
                    BosObject object = client.getObject(newbucketName, fileName);
                    // 获取ObjectMeta
                    ObjectMetadata meta = object.getObjectMetadata();
                    // 获取Object的输入流
                    InputStream objectContent = object.getObjectContent();
                    // 处理Object
                    FileOutputStream fos=new FileOutputStream(path);//下载文件的目录/文件名
                    byte[] buffer=new byte[2048];
                    int count=0;
                    while ((count=objectContent.read(buffer))>=0) {
                        fos.write(buffer,0,count);
                    }

                    // 关闭流
                    objectContent.close();
                    fos.close();
                    System.out.println(meta.getETag());
                    System.out.println(meta.getContentLength());

                }catch (BceServiceException e) {
                    System.out.println("Error ErrorCode: " + e.getErrorCode());
                    System.out.println("Error RequestId: " + e.getRequestId());
                    System.out.println("Error StatusCode: " + e.getStatusCode());
                    System.out.println("Error Message: " + e.getMessage());
                    System.out.println("Error ErrorType: " + e.getErrorType());
                } catch (BceClientException e) {
                    System.out.println("Error Message: " + e.getMessage());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
