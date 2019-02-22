/*
 * Created by Luke DeRienzo on 1/20/19 8:35 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 1/20/19 8:35 PM
 */

package com.ef.blockedIpstore;


import java.util.List;
import java.util.stream.Collectors;

import com.ef.blockedipstore.BlockedIpStoreException;
import com.ef.blockedipstore.SearchCriteria;
import com.ef.blockedipstore.SpeedmentBlockedIpStore;
import com.ef.blockedipstore.SpeedmentStoreComponents;
import com.ef.db.parser.parser.blocked_ip.BlockedIp;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Manager;

public class TestSpeedmentBlockedIpStore implements TestBlockedIpStore {
    private static SpeedmentBlockedIpStore speedmentBlockedIpStore;
    private static SpeedmentStoreComponents speedmentStoreComponents;

    private TestSpeedmentBlockedIpStore() {
        speedmentStoreComponents =
            TestParserStoreComponents.getSingletonInstance(SpeedmentStoreComponents.password);
    }

    private static class SingletonHolder {
        static final TestSpeedmentBlockedIpStore instance = new TestSpeedmentBlockedIpStore();
    }

    public static TestSpeedmentBlockedIpStore getSingletonInstance(String pwd, SpeedmentBlockedIpStore blockedIpStore) {
        SpeedmentStoreComponents.password = pwd;
        speedmentBlockedIpStore = blockedIpStore;
        return SingletonHolder.instance;
    }

    @Override
    public List<Long> readBlockedIps() {
        List<Long> blockedIps;
        try {
            blockedIps = getBlockedIpsFromEntityManagerStream();
        } catch (SpeedmentException e) {
            throw new BlockedIpStoreException("Failure in TestSpeedmentBlockedIpStore::readBlockedIps. " +
                    "Error reading blocked IPs from database.", e);
        }
        return blockedIps;
    }

    private List<Long> getBlockedIpsFromEntityManagerStream() {
        return speedmentStoreComponents.blockedIpManager.stream()
                .mapToLong(BlockedIp::getIpAddress)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public long countLogEntries() {
        return countEntriesUsingManager(speedmentStoreComponents.logEntryManager);
    }

    private long countEntriesUsingManager(Manager manager) {
        long entryCount = 0;
        if (manager != null)
            entryCount = manager.stream().count();
        return entryCount;
    }

    @Override
    public long countBlockedIps() {
        return countEntriesUsingManager(speedmentStoreComponents.blockedIpManager);
    }

    @Override
    public void clearLogEntries() {
        removeEntriesUsingManager(speedmentStoreComponents.logEntryManager);
    }

    private void removeEntriesUsingManager(Manager manager) {
        if (manager != null)
            manager.stream().forEach(manager.remover());
    }

    @Override
    public void clearBlockedIps() {
        removeEntriesUsingManager(speedmentStoreComponents.blockedIpManager);
    }

    @Override
    public void shutdownStore() {
        speedmentStoreComponents.appDb.close();
    }

    @Override
    public void loadFile(String path) {
        speedmentBlockedIpStore.loadFile(path);
    }

    @Override
    public List<Long> findIpsToBlock(SearchCriteria blockingCriteria) {
        return speedmentBlockedIpStore.findIpsToBlock(blockingCriteria);
    }

    @Override
    public void saveIpsToBlock(SearchCriteria blockingCriteriaForComment, List<Long> ips) {
        speedmentBlockedIpStore.saveIpsToBlock(blockingCriteriaForComment, ips);
    }
}
