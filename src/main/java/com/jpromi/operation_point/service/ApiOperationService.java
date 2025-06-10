package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.Operation;

import java.util.List;

public interface ApiOperationService {
    List<Operation> getOperationListBurgenland();
    List<Operation> getOperationListLowerAustria();
    List<Operation> getOperationListUpperAustria();
    List<Operation> getOperationListStyria();
    List<Operation> getOperationListTyrol();
}
