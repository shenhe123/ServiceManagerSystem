package com.jssg.servicemanagersystem.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * <pre>
 *     desc  : 文件相关工具类
 * </pre>
 */
public class FileUtils {

    /**
     * 获取uri的图片真实路径
     * @param context
     * @param uri
     * @return
     */
    public static String getFileOriginPath(Context context, Uri uri ){

        String imagePath="";
        if(DocumentsContract.isDocumentUri(context,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];  //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public downloads"),Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = getImagePath(context,uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        return imagePath;
    }
    //将选择的图片Uri转换为路径
    private static String getImagePath(Context context,Uri uri, String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!= null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;

//        String filePath = null;
//        Uri _uri = data.getData();
//        Log.d("","URI = "+ _uri);
//        if (_uri != null && "content".equals(_uri.getScheme())) {
//            Cursor cursor = this.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
//            cursor.moveToFirst();
//            filePath = cursor.getString(0);
//            cursor.close();
//        } else {
//            filePath = _uri.getPath();
//        }
//        Log.d("","Chosen path = "+ filePath);
    }

}