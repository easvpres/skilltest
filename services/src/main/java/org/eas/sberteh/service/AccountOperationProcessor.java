package org.eas.sberteh.service;

import org.eas.sberteh.entity.AccountOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import static org.eas.sberteh.entity.AccountOperation.Status.REGISTERED;

/**
 * @author aesipov.
 */
@Component
public class AccountOperationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AccountOperationProcessor.class);

    @Value("${account.operation.processor.thread.count}")
    private int threadCount;

    @Value("${account.operation.processor.input.queue.capacity}")
    private int inputQueueCapacity = Integer.MAX_VALUE / 2;

    @Autowired
    private AccountOperationService accountOperationService;

    private final BlockingQueue<AccountOperation> inputQueue = new LinkedBlockingQueue(inputQueueCapacity);
    private final ConcurrentMap<Long, Object> currentlyProcessingKeys = new ConcurrentHashMap();
    private final List<Thread> threads = new ArrayList<>();

    @Scheduled(fixedRateString = "${account.operation.processor.fetch.period}")
    public void fetchOperations() throws InterruptedException {
        logger.info("fetchOperations");
        for (AccountOperation accountOperation : accountOperationService.findByStatusOrderedByTimestamp(REGISTERED, inputQueue.remainingCapacity())) {
            logger.info("accountOperation in progress = {}", accountOperation);
            accountOperation.setStatus(AccountOperation.Status.IN_PROGRESS);
            accountOperationService.save(accountOperation);

            inputQueue.put(accountOperation);
        }
    }

    @PostConstruct
    public void start() {
        System.out.println("threadCount=" + threadCount);
        for (int i = 0; i < threadCount; i++) {
            threads.add(new Thread(() -> {
                try {
                    while (true) {
                        AccountOperation accountOperation = inputQueue.take();
                        Long from = accountOperation.getFromBankAccountNumber();
                        if (currentlyProcessingKeys.putIfAbsent(from, from) == null) {
                            try {
                                accountOperationService.processOperation(accountOperation);
                            } catch (Throwable t) {
                                logger.error("can't process operation {}", accountOperation, t);
                            }
                            currentlyProcessingKeys.remove(from);
                        } else {
                            inputQueue.put(accountOperation);
                        }
                    }
                } catch (InterruptedException e) {
                }
            }));
        }
        threads.forEach(Thread::start);
    }

    @PreDestroy
    public void shutdown() {
        threads.forEach(Thread::interrupt);
    }

}
