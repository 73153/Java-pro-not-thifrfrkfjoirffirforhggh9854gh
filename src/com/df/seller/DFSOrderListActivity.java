package com.df.seller;

import android.app.NotificationManager;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.DFPushNotificationLuncherActivity;
import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;

public class DFSOrderListActivity extends DFSellerMasterActivity {

	public ActivityEventListener getActivityEventListener() {
		return activityEventListener;
	}

	public void setActivityEventListener(ActivityEventListener activityEventListener) {
		this.activityEventListener = activityEventListener;
	}

	private IjoomerButton imgCurrent;
	private IjoomerButton imgPast;
	private IjoomerButton imgDelete;
	private IjoomerButton imgBack;

	private int CURRENT = 1;
	private int PAST = 2;
	private int SELECTED = CURRENT;

	private DFSCurrentOrderListFragment currentOrderListFragment;
	private DFSPastOrderListFragment pastOrderListFragment;
	private ActivityEventListener activityEventListener;

	public interface ActivityEventListener {

		boolean onBackPressed();

		void onDeletePressed();
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_order_list;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgCurrent = (IjoomerButton) findViewById(R.id.imgCurrent);
		imgPast = (IjoomerButton) findViewById(R.id.imgPast);
		imgDelete = (IjoomerButton) findViewById(R.id.imgDelete);
		imgBack = (IjoomerButton) findViewById(R.id.imgBack);
	}

	@Override
	public void prepareViews() {

        try{
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(DFPushNotificationLuncherActivity.CONFIRMOFFER);

            notificationManager.cancel(DFPushNotificationLuncherActivity.CONFIRMREQUEST);

        }catch (Exception e){

        }
		currentOrderListFragment = new DFSCurrentOrderListFragment();
		pastOrderListFragment = new DFSPastOrderListFragment();
		imgCurrent.setBackgroundResource(R.drawable.dfs_order_current_selected);
		addFragment(R.id.lnrFragment, currentOrderListFragment);
	}

	@Override
	public void setActionListeners() {

		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getActivityEventListener() != null) {
					if (!getActivityEventListener().onBackPressed()) {
						finish();
					}
				} else {
					finish();
				}
			}
		});

		imgCurrent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (SELECTED != CURRENT) {
					SELECTED = CURRENT;
					imgPast.setBackgroundResource(R.drawable.dfs_order_past);
					imgCurrent.setBackgroundResource(R.drawable.dfs_order_current_selected);
					addFragment(R.id.lnrFragment, currentOrderListFragment);
				}
			}
		});
		imgPast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (SELECTED != PAST) {
					SELECTED = PAST;
					imgPast.setBackgroundResource(R.drawable.dfs_order_past_selected);
					imgCurrent.setBackgroundResource(R.drawable.dfs_order_current);
					addFragment(R.id.lnrFragment, pastOrderListFragment);
				}
			}
		});
		imgDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getActivityEventListener() != null) {
					try {
						getActivityEventListener().onDeletePressed();
					} catch (Exception e) {
					}
				}
			}
		});
	}

	@Override
	public void onBackPressed() {

		if (getActivityEventListener() != null) {
			if (!getActivityEventListener().onBackPressed()) {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

}
