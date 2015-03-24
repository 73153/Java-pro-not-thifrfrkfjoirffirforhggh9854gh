package com.df.seller;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

public class DFSMyDishesListActivity extends DFSellerMasterActivity {

    private ListView lstDishes;
    private IjoomerButton imgAddDish;

    private ArrayList<SmartListItem> listDataDishes = new ArrayList<SmartListItem>();
    private SmartListAdapterWithHolder dishListAdapterWithHolder;
    private DFCDataprovider dataprovider;

    private AQuery aQuery;

    @Override
    public int setLayoutId() {
        return R.layout.dfs_my_dishes_list;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents() {
        lstDishes = (ListView) findViewById(R.id.lstDishes);
        imgAddDish = (IjoomerButton) findViewById(R.id.imgAddDish);

        dataprovider = new DFCDataprovider(this);
        aQuery = new AQuery(this);
    }

    @Override
    public void prepareViews() {
        lstDishes.setAdapter(null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDishesList();
    }

    @Override
    public void setActionListeners() {
        imgAddDish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                loadNew(DFSAddDishesSetp1Activity.class, DFSMyDishesListActivity.this, false);
            }
        });
    }

    /**
     * Class methods
     */

    public void getDishesList() {
        showProgressDialog(getString(R.string.df_loading_dishes), this);

        dataprovider.getRestaurantDishesList(IjoomerApplicationConfiguration.getRestaurantId(), new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {

            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                hideProgressDialog();
                if (responseCode == 200) {
                    prepareList(data1);
                    dishListAdapterWithHolder = getListAdapter();
                    lstDishes.setAdapter(dishListAdapterWithHolder);
                } else {
                    if(responseCode==204){

                        if(dishListAdapterWithHolder!=null){
                            dishListAdapterWithHolder.clear();
                            dishListAdapterWithHolder.notifyDataSetChanged();
                        }
                        IjoomerUtilities.getDFInfoDialog(DFSMyDishesListActivity.this, "You currently have no dishes", getString(R.string.ok),
                                new CustomAlertNeutral() {

                                    @Override
                                    public void NeutralMethod() {

                                    }
                                });
                    }else{
                        responseMessageHandler(responseCode, false);
                    }
                }
            }
        });

    }

    private void prepareList(ArrayList<HashMap<String, String>> data) {

        if (data != null && data.size() > 0) {
            listDataDishes.clear();
            int size = data.size();
            for (int i = 0; i < size; i++) {
                SmartListItem item = new SmartListItem();
                item.setItemLayout(R.layout.dfs_dish_list_item);
                ArrayList<Object> obj = new ArrayList<Object>();
                obj.add(data.get(i));
                item.setValues(obj);
                listDataDishes.add(item);
            }
        }
    }

    public SmartListAdapterWithHolder getListAdapter() {

        SmartListAdapterWithHolder listAdapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.dfs_dish_list_item, listDataDishes, new ItemView() {

            @Override
            public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
                holder.txtDishName = (IjoomerTextView) v.findViewById(R.id.txtDishName);
                holder.imgDish = (ImageView) v.findViewById(R.id.imgDish);
                holder.txtLikeCount=(IjoomerTextView) v.findViewById(R.id.txtLikeCount);


                final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
                holder.txtDishName.setText(row.get("dishName"));
                holder.txtLikeCount.setText(row.get("likes"));

                aQuery.id(holder.imgDish).image(row.get("image"), true, true, getDeviceWidth(), 0);

                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            loadNew(DFSDisheDetailsActivity.class, DFSMyDishesListActivity.this, false, "IN_DISH_DETAIL", row);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return v;
            }

            @Override
            public View setItemView(int position, View v, SmartListItem item) {
                return null;
            }

        });
        return listAdapterWithHolder;
    }

    private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

        IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
                new CustomAlertNeutral() {

                    @Override
                    public void NeutralMethod() {

                    }
                });

    }
}
