package org.apache.ibatis.custom.page;

/**
 * @author ck163
 * @date 2022-04-18 17:13:42
 */
public interface Pageable {
    static final Integer DEFAULT_PAGE_SIZE = 10;
    static final Integer DEFAULT_PAGE_NUM = 1;
    Integer pageSize = 10;
    Integer pageNum = 1;
    Integer totalCount = 0;
    boolean hasNext = false;

}
