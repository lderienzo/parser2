/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/1/19 8:51 PM
 */

package com.ef.blockedipstore;


import java.util.List;

public interface ParserStore<E> {
    void loadFile(String path);
    List<E> findIpsToBlock(SearchCriteria blockingCriteria);
    void saveIpsToBlock(SearchCriteria blockingCriteriaForComment, List<E> ips);
}
