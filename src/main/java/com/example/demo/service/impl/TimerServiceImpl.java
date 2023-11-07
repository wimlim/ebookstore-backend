package com.example.demo.service.impl;

import com.example.demo.service.TimerService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("session")
public class TimerServiceImpl implements TimerService {
    private long startTime;

    @Override
    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public long stopTimer() {
        if (startTime > 0) {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            startTime = 0; // 重置计时器
            return elapsedTime;
        }
        return 0;
    }
}
