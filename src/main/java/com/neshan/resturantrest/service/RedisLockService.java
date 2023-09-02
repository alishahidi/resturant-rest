package com.neshan.resturantrest.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RedisLockService implements LockService {
    static String MY_LOCK_KEY = "someLockKey";
    LockRegistry lockRegistry;

    @Override
    public String lock() {
        var lock = lockRegistry.obtain(MY_LOCK_KEY);
        String returnVal = null;
        if (lock.tryLock()) {
            returnVal = "redis lock successful";
        } else {
            returnVal = "redis lock unsuccessful";
        }
        lock.unlock();
        return returnVal;
    }

    @Override
    public String properLock() {
        String returnVal = null;
        try {
            Lock lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                if (lock.tryLock()) {
                    returnVal = "jdbc lock successful";
                } else {
                    returnVal = "jdbc lock unsuccessful";
                }
            } catch (Exception e) {
                // in a production environment this should log and do something else
                System.out.println(e.getMessage());
            } finally {
                // always have this in a `finally` block in case anything goes wrong
                lock.unlock();
            }
        } catch (Exception e) {
            // in a production environment this should be a log statement
            System.out.printf("\nUnable to obtain lock: %s%n\n", MY_LOCK_KEY);
        }
        return returnVal;
    }

    @Override
    public void failLock() {
        var executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            var lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println("Attempting to lock with thread: " + uuid);
                if (lock.tryLock()) {
                    System.out.println("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                } else {
                    System.out.println("failed to lock with thread: " + uuid);
                }
            } catch (Exception e0) {
                System.out.println("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                System.out.println("unlocked with thread: " + uuid);
            }
        };

        Runnable lockThreadTwo = () -> {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            var lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println("Attempting to lock with thread: " + uuid);
                if (lock.tryLock()) {
                    System.out.println("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                } else {
                    System.out.println("failed to lock with thread: " + uuid);
                }
            } catch (Exception e0) {
                System.out.println("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                System.out.println("unlocked with thread: " + uuid);
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }
}