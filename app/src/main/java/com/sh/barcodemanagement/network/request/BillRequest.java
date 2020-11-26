package com.sh.barcodemanagement.network.request;

import com.sh.barcodemanagement.model.SubBill;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BillRequest {

    private Long id;

    private Long totalMoney;

    private Long storeId;

    private List<SubBill> lstSubBills;
}
