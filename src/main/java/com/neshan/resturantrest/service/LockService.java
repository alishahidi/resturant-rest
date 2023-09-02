package com.neshan.resturantrest.service;

public interface LockService {
    String lock();
    void failLock();
    String properLock();
}
