package com.sbi.dl.compoment;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author yue.xia
 */
@Slf4j
public class BaseCache<T> {
    private final Map<String, T> cacheMap = new ConcurrentHashMap<>();
    private final Map<String, MyTask> taskMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

    /**
     * get cache data
     *
     * @param name name of cache data
     * @return optional cache data, empty if not exist
     */
    public synchronized Optional<T> getCache(String name) {
        return Optional.ofNullable(cacheMap.get(name));
    }

    /**
     * check a cache name exist
     *
     * @param name name of cache data
     * @return if the cache exist
     */
    public synchronized boolean existCache(String name) {
        return cacheMap.containsKey(name);
    }

    /**
     * add an Object value into cache
     *
     * @param name name of cache data
     * @param value the data to cache
     * @param expiration TimeUnit.SECONDS
     */
    public synchronized void addCache(String name, T value, Long expiration) {
        cacheMap.put(name, value);
        MyTask task =
                new MyTask(name) {
                    @Override
                    public void run() {
                        log.info("cache [{}] expired", name);
                        cacheMap.remove(name);
                        taskMap.remove(name);
                    }
                };
        timer.schedule(task, expiration, TimeUnit.SECONDS);
        taskMap.put(name, task);
        log.info("cache [{}] added", name);
    }

    /**
     * expend an existing cache time
     *
     * @param name name of cache data
     * @param expiration new expiration, TimeUnit.SECONDS
     * @return if extension successful
     */
    public synchronized boolean extend(String name, Long expiration) {
        if (taskMap.containsKey(name)) {
            MyTask task = taskMap.get(name);
            task.cancel();
            taskMap.remove(name);
            MyTask newTask =
                    new MyTask(name) {
                        @Override
                        public void run() {
                            log.info("cache [{}] expired", name);
                            cacheMap.remove(name);
                            taskMap.remove(name);
                        }
                    };

            timer.schedule(newTask, expiration, TimeUnit.SECONDS);
            taskMap.put(name, task);
            log.info("cache [{}] extended", name);
            return true;

        } else {
            return false;
        }
    }

    /**
     * Manually remove cache
     *
     * @param name name of cache data
     */
    public synchronized void removeCache(String name) {
        if (taskMap.containsKey(name)) {
            MyTask task = taskMap.get(name);
            task.cancel();
            taskMap.remove(name);
            cacheMap.remove(name);
            log.info("cache [{}] removed", name);
        }
    }
}

abstract class MyTask extends TimerTask {

    MyTask(String name) {
        this.name = name;
    }

    private String name;
}
