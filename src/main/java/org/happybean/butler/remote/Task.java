package org.happybean.butler.remote;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wgt
 * @date 2019-04-08
 * @description
 **/
public class Task {

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    public void waitTask(){
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void signalTask(){
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
