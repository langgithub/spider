package com.champion.spider.scheduler;

import com.champion.spider.download.WebRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by root on 2017/8/22.
 */
public class PriorityScheduler implements Scheduler{

    public static final int INITIAL_CAPACITY = 5;

    private BlockingQueue<WebRequest> noPriorityQueue = new LinkedBlockingQueue<WebRequest>();

    private PriorityBlockingQueue<WebRequest> priorityQueuePlus = new PriorityBlockingQueue<WebRequest>(INITIAL_CAPACITY);

    private PriorityBlockingQueue<WebRequest> priorityQueueMinus = new PriorityBlockingQueue<WebRequest>(INITIAL_CAPACITY);

    @Override
    public void put(WebRequest WebRequest) {
        if (WebRequest.getPriority() == 0) {
            noPriorityQueue.add(WebRequest);
        } else if (WebRequest.getPriority() > 0) {
            priorityQueuePlus.put(WebRequest);
        } else {
            priorityQueueMinus.put(WebRequest);
        }
    }

    @Override
    public synchronized WebRequest poll() {
        WebRequest poll = priorityQueuePlus.poll();
        if (poll != null) {
            return poll;
        }
        poll = noPriorityQueue.poll();
        if (poll != null) {
            return poll;
        }
        return priorityQueueMinus.poll();
    }
}
