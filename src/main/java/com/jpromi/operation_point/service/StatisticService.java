package com.jpromi.operation_point.service;

import java.util.UUID;

public interface StatisticService {
    Void setStat();
    Void setStatForDetail(String objectType, UUID objectUuid);
}
