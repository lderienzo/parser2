/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 4:30 PM
 */

package com.ef.blockedIpstore.config;


import java.util.List;
import java.util.stream.Collectors;

import com.ef.blockedipstore.BlockedIpStoreException;
import com.ef.blockedipstore.SearchCriteria;
import com.ef.blockedipstore.SpeedmentBlockedIpStore;
import com.ef.blockedipstore.SpeedmentStoreComponents;
import com.ef.db.parser.parser.blocked_ip.BlockedIp;
import com.speedment.runtime.core.exception.SpeedmentException;
import com.speedment.runtime.core.manager.Manager;

public class SpeedmentBlockedIpStoreForTesting implements BlockedIpStoreForTesting {
    private static SpeedmentBlockedIpStore speedmentBlockedIpStore;
    private static SpeedmentStoreComponents speedmentStoreComponents;

    private SpeedmentBlockedIpStoreForTesting() {
        speedmentStoreComponents =
            ParserStoreComponentsForTesting.getSingletonInstance(SpeedmentStoreComponents.password);
    }

    private static class SingletonHolder {
        static final SpeedmentBlockedIpStoreForTesting instance = new SpeedmentBlockedIpStoreForTesting();
    }

    public static SpeedmentBlockedIpStoreForTesting getSingletonInstance(String pwd, SpeedmentBlockedIpStore blockedIpStore) {
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
            throw new BlockedIpStoreException("Failure in SpeedmentBlockedIpStoreForTesting::readBlockedIps. " +
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

    @Override
    public void shutDownStore() {
        clearTableData();
        shutdownStore();
    }

    private void clearTableData() {
        clearLogEntries();
        clearBlockedIps();
    }
}
