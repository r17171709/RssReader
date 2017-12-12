package com.renyu.rssreader.git;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GitCheckMain {

    public static void main(String[] args) {
        int index = 2000;
        ExecutorService executors = Executors.newSingleThreadExecutor();
        File file=new File("D:\\github");
        File[] listFiles=file.listFiles();
        int lastValue = index+400;
        if (lastValue>listFiles.length) {
            lastValue = listFiles.length;
        }
        for (int i=index; i<lastValue ; i++) {
            final int i_ = i;
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    String path=listFiles[i_].getPath();
                    System.out.println(path);
                    Runtime runtime=Runtime.getRuntime();
                    try {
                        runtime.exec("cmd /k D: && cd "+path+" && git pull");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
