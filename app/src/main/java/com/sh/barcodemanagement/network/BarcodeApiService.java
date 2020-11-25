package com.sh.barcodemanagement.network;


import com.sh.barcodemanagement.model.Barcode;
import com.sh.barcodemanagement.model.Bill;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.Result;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.model.Unit;
import com.sh.barcodemanagement.network.request.BillCreateUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BarcodeApiService {

    @GET("stores/login")
    Call<Store> login(@Query("username") String username,
                      @Query("password") String password,
                      @Query("serial") String serial);

    @GET("stores/{storeId}/items")
    Call<List<Item>> getAllItems(@Path("storeId") Long storeId);

    @GET("stores/{storeId}/items/barcode/{barcode}")
    Call<Item> getItemByBarcode(@Path("storeId") Long storeId,
                                @Path("barcode") String barcode);

    @POST("stores/bills")
    Call<Bill> createOrUpdateBill(@Body BillCreateUpdateRequest request);

    @GET("stores/{storeId}/units")
    Call<List<Unit>> findAllUnits(@Path("storeId") Long storeId);

    @GET("stores/{storeId}/barcodes")
    Call<List<Barcode>> findAllBarCodes(@Path("storeId") Long storeId);

    @GET("stores/{storeId}/data")
    Call<Result> checkDataIsChange(@Path("storeId") Long storeId);

    @PUT("stores/{storeId}/data")
    Call<Result> updateMarkDataChange(@Path("storeId") Long storeId,
                                      @Query("status") Boolean status);

}
