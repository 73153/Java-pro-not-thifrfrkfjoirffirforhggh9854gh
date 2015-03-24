package com.df.customer;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class DFCProfileStatsFragment extends SmartFragment {

	private ImageView imgCustomer;

	private IjoomerButton imgStats;
	private IjoomerButton imgDetails;
	private IjoomerButton imgAddress;

	private IjoomerTextView txtFavouriteCuisine;
	private IjoomerTextView txtDish1;
	private IjoomerTextView txtDish2;
	private IjoomerTextView txtDish3;
	private IjoomerTextView txtDish4;
	private IjoomerTextView txtDish5;
	private IjoomerTextView txtDishesOrdered;

	private IjoomerTextView[] favouriteDishes = new IjoomerTextView[5];

	private HashMap<String, String> userDetail;

	public DFCProfileStatsFragment(HashMap<String, String> userDetail) {
		this.userDetail = userDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfc_profile_fragment_stats;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		imgCustomer = (ImageView) currentView.findViewById(R.id.imgCustomer);

		imgStats = (IjoomerButton) currentView.findViewById(R.id.imgStats);
		imgDetails = (IjoomerButton) currentView.findViewById(R.id.imgDetails);
		imgAddress = (IjoomerButton) currentView.findViewById(R.id.imgAddress);

		txtFavouriteCuisine = (IjoomerTextView) currentView.findViewById(R.id.txtFavouriteCuisine);
		txtDish1 = (IjoomerTextView) currentView.findViewById(R.id.txtDish1);
		txtDish2 = (IjoomerTextView) currentView.findViewById(R.id.txtDish2);
		txtDish3 = (IjoomerTextView) currentView.findViewById(R.id.txtDish3);
		txtDish4 = (IjoomerTextView) currentView.findViewById(R.id.txtDish4);
		txtDish5 = (IjoomerTextView) currentView.findViewById(R.id.txtDish5);
		txtDishesOrdered = (IjoomerTextView) currentView.findViewById(R.id.txtDishesOrdered);

		favouriteDishes[0] = txtDish1;
		favouriteDishes[1] = txtDish2;
		favouriteDishes[2] = txtDish3;
		favouriteDishes[3] = txtDish4;
		favouriteDishes[4] = txtDish5;

	}

	@Override
	public void prepareViews(View currentView) {
		setDetails();
	}

	@Override
	public void setActionListeners(View currentView) {
		imgCustomer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		imgStats.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		imgDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((DFCProfileActivity) getActivity()).goToProfileDetails();
			}
		});

		imgAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((DFCProfileActivity) getActivity()).goToProfileAddress();
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void setDetails() {
		imgStats.setBackgroundResource(R.drawable.df_profile_stats_selected_btn);

		txtFavouriteCuisine.setText(userDetail.get("favouriteCuisine"));
		txtDishesOrdered.setText(userDetail.get("orderCount") + " " + getString(R.string.df_dishes_ordered));

		txtDish1.setVisibility(View.GONE);
		txtDish2.setVisibility(View.GONE);
		txtDish3.setVisibility(View.GONE);
		txtDish4.setVisibility(View.GONE);
		txtDish5.setVisibility(View.GONE);

        if (userDetail.get("favouriteDishes").equals("")) {
            txtDish1.setVisibility(View.VISIBLE);
            txtDish1.setText(getString(R.string.df_profile_stat_info_msg));
        } else {

            try{
                JSONArray dishArr = new JSONArray(userDetail.get("favouriteDishes"));

                if(dishArr.length()<=0){
                    txtDish1.setVisibility(View.VISIBLE);
                    txtDish1.setText(getString(R.string.df_profile_stat_info_msg));
                }
                for (int i = 0; i < dishArr.length(); i++) {
                    final JSONObject dish = dishArr.getJSONObject(i);
                    favouriteDishes[i].setVisibility(View.VISIBLE);
                    favouriteDishes[i].setTag(dish.getString("dishID"));
                    if (dish.getString("dishID").equals("0")) {
                        favouriteDishes[i].setText(dish.getString("catName"));
                    }else{
                        favouriteDishes[i].setText(dish.getString("dishName"));
                    }

                    favouriteDishes[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String dishId = view.getTag().toString();
                            if(!dishId.equals("0")){
                                try{
                                    ((SmartActivity)getActivity()).loadNew(DFCDisheDetailsActivity.class,getActivity(),false,"IN_DISH_DETAIL",new DFCDataprovider(getActivity()).getDishDetail(dishId));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
                txtDish1.setVisibility(View.VISIBLE);
                txtDish1.setText(getString(R.string.df_profile_stat_info_msg));
            }

        }

	}

}
