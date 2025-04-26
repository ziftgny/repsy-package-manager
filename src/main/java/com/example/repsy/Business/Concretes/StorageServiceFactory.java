package com.example.repsy.Business.Concretes;


import com.ziftgny.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StorageServiceFactory {

    @Value("${storageStrategy}")
    private String strategyName;

    @Autowired
    private ApplicationContext context;

    public StorageService getStorageService() {
        return (StorageService) context.getBean(strategyName);
    }
}
