package com.sh.barcodemanagement.network;


import com.sh.barcodemanagement.model.Bill;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.model.SubBill;
import com.sh.barcodemanagement.network.request.BillCreateUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BarcodeApiService {

    @GET("employees/login")
    Call<Store> login(@Query("username") String username,
                      @Query("password") String password,
                      @Query("serial") String serial);

    @GET("stores/{storeCode}/categories")
    Call<List<Item>> getAllItems(@Path("storeCode") String storeCode);

    @POST("stores/customers/bills")
    Call<Bill> createOrUpdateBill(@Body BillCreateUpdateRequest request);

    @GET("stores/bills/{billId}/detail")
    Call<List<SubBill>> getAllItemsOfBill(@Path("billId") long billId);
}
