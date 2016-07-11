package br.com.doe.utils;

import android.os.HandlerThread;

/**
 * Helper class to improve performance
 * Created by heitornascimento on 6/4/16.
 */
public class TaskManager extends HandlerThread {


    public TaskManager(String name) {
        super(name);
    }

}
