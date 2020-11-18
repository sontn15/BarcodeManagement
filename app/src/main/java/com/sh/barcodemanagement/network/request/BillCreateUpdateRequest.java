package com.sh.barcodemanagement.network.request;


import com.sh.barcodemanagement.model.SubBill;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillCreateUpdateRequest {

    private Long id;

    private String customerName;

    private String customerAddress;

    private Long totalMoney;

    private String description;

    private String customerCode;

    private String employeeUsername;

    private Long storeId;

    private Long statusBillDesktop;

    private List<SubBill> lstSubBills;


}
