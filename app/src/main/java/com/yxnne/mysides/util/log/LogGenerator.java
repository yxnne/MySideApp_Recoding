package com.yxnne.mysides.util.log;


/**
 * Created by Administrator on 2016/10/13.
 * 单例的LogGenerator类
 */

public class LogGenerator {
    private IMyLog iMyLog;
    private static LogGenerator instance = null;

    //    private LogGenerator(){
//    }
    private LogGenerator(IMyLog i) {
        iMyLog = i;
    }
    /**
     * 获得实例
     *
     * @return
     */
    public static LogGenerator getInstance() {

        //这里给他一个接口实现类,枷锁保证单例
        synchronized (LogGenerator.class) {
            if (instance == null) {
                instance = new LogGenerator(new AndroidSysLog());
            }

        }
        return instance;
    }

    /**
     * 打印信息
     */
    public void printMsg(String msg){
        iMyLog.printMsg(msg);
    }
    /**
     * 打印Log，带标签
     */
    public void printprintLog(String tag,String msg){
        iMyLog.printLog(tag, msg);
    }
    /**
     * 打印Error
     */
    public void printError(String err){
        iMyLog.printError(err);
    }
    /**
     * 打印Error
     */
    public void printError(Exception e){
        iMyLog.printError(e);
    }
}
