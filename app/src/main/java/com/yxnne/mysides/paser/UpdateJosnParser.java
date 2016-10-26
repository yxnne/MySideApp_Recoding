package com.yxnne.mysides.paser;

import com.yxnne.mysides.entity.UpdateEntity;
import com.yxnne.mysides.util.log.LogGenerator;

import org.json.JSONObject;
/**
 * 解析升级Json
 */
public class UpdateJosnParser {
    public UpdateEntity parser(String jsongString)
    {
        UpdateEntity updateEntity=null;
        try {
            JSONObject jsonObject=new JSONObject(jsongString);
            String status=jsonObject.getString("status");
            if ("0".equals(status))
            {
                String version=jsonObject.getString("version");
                String changeLog=jsonObject.getString("changeLog");
                String apkUrl=jsonObject.getString("apkUrl");
                updateEntity= new UpdateEntity(status, version, changeLog, apkUrl);
            }

        } catch (Exception e) {
            LogGenerator.getInstance().printError(e);
            e.printStackTrace();
        }
        return updateEntity;
    }
}
