package xyz.fz.dao;

import java.util.List;

public class PagerData<T> {

    private static final String LOAD_STATUS_Y = "y";

    private static final String LOAD_STATUS_N = "n";

    private List<T> data;

    private Long totalCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @param page 页码从1开始
     * @return y: 可以继续加载，n: 加载完毕
     */
    public String getLoadStatus(PagerData<T> pagerData, int page, int pageSize) {
        return pagerData.getTotalCount() > page * pageSize ? LOAD_STATUS_Y : LOAD_STATUS_N;
    }
}

