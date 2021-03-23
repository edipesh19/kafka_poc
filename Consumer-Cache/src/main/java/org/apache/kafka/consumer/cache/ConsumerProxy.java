package org.apache.kafka.consumer.cache;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ConsumerProxy<K, V> implements InvocationHandler {
    private static final Logger log = LoggerFactory.getLogger(ConsumerProxy.class);

    private final KafkaConsumer<K, V> consumerInstance;
    @Value("{cache.instance.heartbeatEnabled.enable:false}")
    boolean heartbeatEnabled;

    private ScheduledExecutorService timerThread;
    private ScheduledFuture<?> future;

    private volatile boolean wasPassivated = false;

    public ConsumerProxy(Properties configs) {
        consumerInstance = new KafkaConsumer<>(configs);
        if (heartbeatEnabled) {
            timerThread = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "ConsumerProxy-heartbeatEnabled-thread");
                t.setDaemon(true);
                return t;
            });
        }
        log.info("** NEW PROXY INSTANCE - heartbeatEnabled?{} **", heartbeatEnabled);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().equals("close") || method.getName().equals("unsubscribe")) {
            //skip methods
            return Void.TYPE;
        }
        if(method.getName().equals("subscribe")) {
            throw new UnsupportedOperationException(" Possibly you do not need pooling for subscriber pattern. " +
                "Pools are really intended for short lived consumer actions");
        }
        if(method.getName().equals("destroyProxy")) {
            if(heartbeatEnabled){
                if(wasPassivated)
                    future.cancel(true);
                timerThread.shutdownNow();
            }
            consumerInstance.close();
            return Void.TYPE;
        }
        if(method.getName().equals("validateProxy")) {
            try {
                consumerInstance.listTopics(Duration.ofMillis(1000));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        if(method.getName().equals("activateProxy")) {
            if (wasPassivated) {
                if (heartbeatEnabled) {
                    future.cancel(true);
                }
                consumerInstance.resume(consumerInstance.paused());
                consumerInstance.unsubscribe();
                wasPassivated = false;
            }
            return Void.TYPE;
        }
        if(method.getName().equals("passivateProxy")) {
            consumerInstance.pause(consumerInstance.assignment());
            if (heartbeatEnabled) {
                future = timerThread.scheduleWithFixedDelay(() -> consumerInstance.poll(Duration.ofMillis(100)),
                    1000, 1000, TimeUnit.MILLISECONDS);
            }
            wasPassivated = true;
            return Void.TYPE;
        }
        try {
            return method.invoke(consumerInstance, args);
        } catch (InvocationTargetException ite) {
            log.info("Caught exception invoking method {}, because {}", method.getName(), ite.getCause());
            throw ite.getCause();
        }
    }
}
