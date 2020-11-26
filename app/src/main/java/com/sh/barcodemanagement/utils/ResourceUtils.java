package com.sh.barcodemanagement.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.sh.barcodemanagement.adapter.ItemAdapter;
import com.sh.barcodemanagement.database.BarcodeDatabase;
import com.sh.barcodemanagement.database.entity.BarcodeEntity;
import com.sh.barcodemanagement.database.entity.ItemEntity;
import com.sh.barcodemanagement.database.entity.UnitEntity;
import com.sh.barcodemanagement.model.Barcode;
import com.sh.barcodemanagement.model.Item;
import com.sh.barcodemanagement.model.ItemInCart;
import com.sh.barcodemanagement.model.Result;
import com.sh.barcodemanagement.model.Store;
import com.sh.barcodemanagement.model.Unit;
import com.sh.barcodemanagement.network.BarcodeApiService;
import com.sh.barcodemanagement.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResourceUtils {

    public static Long calculateNewPriceForItem(Long oldPrice, Long newPrice) {
        if (oldPrice == null) {
            oldPrice = 0L;
        }
        if (newPrice == null) {
            newPrice = 0L;
        }
        Long calcPrice = newPrice;
        if (oldPrice.equals(0L) || newPrice.equals(0L)) {
            return newPrice;
        }
        if (oldPrice > newPrice) {
            long mMul = oldPrice / newPrice;
            if (mMul > 8) {
                calcPrice = calculateNewPriceForItem(oldPrice, newPrice * 10);
            }
        } else {
            long mMul = newPrice / oldPrice;
            if (mMul > 8) {
                calcPrice = calculateNewPriceForItem(oldPrice, newPrice / 10);
            }
        }
        return calcPrice;
    }

    /**
     * Lay ra he so co so cua san pham theo san pham dau vao
     */
    public static Long buildHeSoCoSoForItemInCart(Item itemClick) {
        Long heSoCoSo = 1L;
        Long unitDefault = itemClick.getUnitDefault();
        if (unitDefault != null && unitDefault != 0L) {
            if (unitDefault.equals(itemClick.getUnit1())) {
                heSoCoSo = itemClick.getQuyCach1();
            } else if (unitDefault.equals(itemClick.getUnit2())) {
                heSoCoSo = itemClick.getQuyCach2();
            } else {
                heSoCoSo = 1L;
            }
        }
        return heSoCoSo;
    }


    /**
     * Lay ra danh sach don vi tinh theo san pham dau vao
     */
    public static List<Unit> buildListUnitInItem(ItemInCart obj) {
        List<Unit> arrUnits = new ArrayList<>();
        if (obj.getItem().getUnitDefaultObj() != null) {
            arrUnits.add(obj.getItem().getUnitDefaultObj());
        }
        if (obj.getItem().getUnitMinObj() != null && !arrUnits.contains(obj.getItem().getUnitMinObj())) {
            arrUnits.add(obj.getItem().getUnitMinObj());
        }
        if (obj.getItem().getUnit1Obj() != null && !arrUnits.contains(obj.getItem().getUnit1Obj())) {
            arrUnits.add(obj.getItem().getUnit1Obj());
        }
        if (obj.getItem().getUnit2Obj() != null && !arrUnits.contains(obj.getItem().getUnit2Obj())) {
            arrUnits.add(obj.getItem().getUnit2Obj());
        }
        return arrUnits;
    }

    public static void showProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public static void hiddenProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void destroyProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void showAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public static void hiddenAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static void destroyAlertDialog(AlertDialog alertDialog) {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public static void showKeyboard(AlertDialog alertDialog) {
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void hiddenKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void syncDataFromServer(Activity activity,
                                          ProgressDialog progressDialog,
                                          BarcodeApiService barcodeApiService,
                                          Store storeLogin,
                                          List<Item> listItems,
                                          ItemAdapter itemAdapter,
                                          String action) {
        Call<List<Unit>> callUnits = barcodeApiService.findAllUnits(storeLogin.getId());
        callUnits.enqueue(new Callback<List<Unit>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UnitEntity> entityList = new ArrayList<>();
                    List<Unit> listUnits = response.body();
                    listUnits.forEach(unit -> entityList.add(UnitEntity.builder()
                            .id(unit.getId())
                            .name(unit.getName())
                            .note(unit.getNote())
                            .storeId(unit.getStoreId())
                            .build()));
                    BarcodeDatabase.getInstance(activity).barcodeDAO().insertListUnits(entityList);
                    Log.d(Const.LOG_TAG, "Size unit from server: " + listUnits.size());
                    Log.d(Const.LOG_TAG, "Size unit insert to DB: " + entityList.size());

                    Call<List<Barcode>> callBarCodes = barcodeApiService.findAllBarCodes(storeLogin.getId());
                    callBarCodes.enqueue(new Callback<List<Barcode>>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(Call<List<Barcode>> call, Response<List<Barcode>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<BarcodeEntity> barcodeEntities = new ArrayList<>();
                                List<Barcode> listBarCodes = response.body();
                                listBarCodes.forEach(barcode -> barcodeEntities.add(BarcodeEntity.builder()
                                        .id(barcode.getId())
                                        .itemCode(barcode.getItemCode())
                                        .barcode(barcode.getBarcode())
                                        .storeId(barcode.getStoreId())
                                        .build()));
                                BarcodeDatabase.getInstance(activity).barcodeDAO().insertListBarCodes(barcodeEntities);
                                Log.d(Const.LOG_TAG, "Size barcode from server: " + listBarCodes.size());
                                Log.d(Const.LOG_TAG, "Size barcode insert to DB: " + barcodeEntities.size());

                                Call<List<Item>> callItems = barcodeApiService.getAllItems(storeLogin.getId());
                                callItems.enqueue(new Callback<List<Item>>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            List<ItemEntity> itemEntities = new ArrayList<>();
                                            List<Item> listItemServer = response.body();
                                            listItemServer.forEach(item -> itemEntities.add(ItemEntity.builder()
                                                    .id(item.getId()).code(item.getCode())
                                                    .name(item.getName())
                                                    .giaBan(item.getGiaBan())
                                                    .barcode(item.getBarcode())
                                                    .unitMin(item.getUnitMin())
                                                    .unitDefault(item.getUnitDefault())
                                                    .unit1(item.getUnit1())
                                                    .quyCach1(item.getQuyCach1())
                                                    .giaQuyDoi1(item.getGiaQuyDoi1())
                                                    .unit2(item.getUnit2())
                                                    .quyCach2(item.getQuyCach2())
                                                    .giaQuyDoi2(item.getGiaQuyDoi2())
                                                    .build()));
                                            BarcodeDatabase.getInstance(activity).barcodeDAO().insertListItems(itemEntities);
                                            Log.d(Const.LOG_TAG, "Size items from server: " + listItemServer.size());
                                            Log.d(Const.LOG_TAG, "Size items insert to DB: " + itemEntities.size());

                                            if ("login".equalsIgnoreCase(action)) {
                                                activity.startActivity(new Intent(activity, MainActivity.class));
                                                activity.finish();
                                                if (progressDialog != null) {
                                                    ResourceUtils.hiddenProgressDialog(progressDialog);
                                                }
                                            } else {
                                                Call<Result> callUpdate = barcodeApiService.updateMarkDataChange(storeLogin.getId(), false);
                                                callUpdate.enqueue(new Callback<Result>() {
                                                    @Override
                                                    public void onResponse(Call<Result> call, Response<Result> response) {
                                                        listItems.clear();
                                                        listItems.addAll(ResourceUtils.getAllItemFromRoomDatabase(activity));
                                                        itemAdapter.setData(listItems);
                                                        if (progressDialog != null) {
                                                            ResourceUtils.hiddenProgressDialog(progressDialog);
                                                        }
                                                        Log.d(Const.LOG_TAG, "Update status = false for new data in server successfully!");
                                                        Log.d(Const.LOG_TAG, "LIST ITEM GET IN DATABASE = " + listItems.size());
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Result> call, Throwable t) {
                                                        if (progressDialog != null) {
                                                            ResourceUtils.hiddenProgressDialog(progressDialog);
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<Item>> call, Throwable t) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Barcode>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Item> getAllItemFromRoomDatabase(Context context) {
        List<Item> data = new ArrayList<>();
        List<ItemEntity> itemEntities = BarcodeDatabase.getInstance(context.getApplicationContext()).barcodeDAO().findAllItems();
        Map<Long, UnitEntity> unitEntityMap = BarcodeDatabase.getInstance(context.getApplicationContext()).barcodeDAO().findAllUnits()
                .stream().collect(Collectors.toMap(UnitEntity::getId, obj -> obj));
        itemEntities.forEach(itemEntity -> data.add(convertItemEntityToItem(unitEntityMap, itemEntity)));
        Log.d(Const.LOG_TAG, "Size items from room database: " + data.size());
        return data;
    }

    public static Item convertItemEntityToItem(Map<Long, UnitEntity> unitEntityMap, ItemEntity itemEntity) {
        Unit unitMinObj = null;
        if (itemEntity.getUnitMin() != null && itemEntity.getUnitMin() != 0L) {
            UnitEntity unitEntity = unitEntityMap.get(itemEntity.getUnitMin());
            if (unitEntity != null) {
                unitMinObj = Unit.builder()
                        .id(unitEntity.getId())
                        .note(unitEntity.getNote())
                        .name(unitEntity.getName())
                        .storeId(unitEntity.getStoreId())
                        .build();
            }
        }
        Unit unitDefaultObj = null;
        if (itemEntity.getUnitDefault() != null && itemEntity.getUnitDefault() != 0L) {
            UnitEntity unitEntity = unitEntityMap.get(itemEntity.getUnitMin());
            if (unitEntity != null) {
                unitDefaultObj = Unit.builder()
                        .id(unitEntity.getId())
                        .note(unitEntity.getNote())
                        .name(unitEntity.getName())
                        .storeId(unitEntity.getStoreId())
                        .build();
            }
        }
        Unit unit1Obj = null;
        if (itemEntity.getUnit1() != null && itemEntity.getUnit1() != 0L) {
            UnitEntity unitEntity = unitEntityMap.get(itemEntity.getUnitMin());
            if (unitEntity != null) {
                unit1Obj = Unit.builder()
                        .id(unitEntity.getId())
                        .note(unitEntity.getNote())
                        .name(unitEntity.getName())
                        .storeId(unitEntity.getStoreId())
                        .build();
            }
        }
        Unit unit2Obj = null;
        if (itemEntity.getUnit2() != null && itemEntity.getUnit2() != 0L) {
            UnitEntity unitEntity = unitEntityMap.get(itemEntity.getUnitMin());
            if (unitEntity != null) {
                unit2Obj = Unit.builder()
                        .id(unitEntity.getId())
                        .note(unitEntity.getNote())
                        .name(unitEntity.getName())
                        .storeId(unitEntity.getStoreId())
                        .build();
            }
        }
        return Item.builder()
                .id(itemEntity.getId())
                .code(itemEntity.getCode())
                .name(itemEntity.getName())
                .giaBan(itemEntity.getGiaBan())
                .barcode(itemEntity.getBarcode())
                .unitMin(itemEntity.getUnitMin())
                .unitMinObj(unitMinObj)
                .unitDefault(itemEntity.getUnitDefault())
                .unitDefaultObj(unitDefaultObj)
                .unit1(itemEntity.getUnit1())
                .unit1Obj(unit1Obj)
                .quyCach1(itemEntity.getQuyCach1())
                .giaQuyDoi1(itemEntity.getGiaQuyDoi1())
                .unit2(itemEntity.getUnit2())
                .unit2Obj(unit2Obj)
                .quyCach2(itemEntity.getQuyCach2())
                .giaQuyDoi2(itemEntity.getGiaQuyDoi2())
                .status(true)
                .build();
    }

}
