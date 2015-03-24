package com.df.src;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.df.customer.DFCPlaceOrderActivity;
import com.df.seller.DFSMyAccountActivity;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerRegistrationMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.oauth.IjoomerRegistration;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerRegistrationStep1Activity.
 * 
 * @author tasol
 * 
 */
public class IjoomerRegistrationStep1Activity extends IjoomerRegistrationMaster {

	private IjoomerEditText edtMobileNo;
	private IjoomerEditText edtUserType;
	private IjoomerEditText edtUserName;
	private IjoomerEditText edtPassword;
	private IjoomerEditText edtEmail;
	private IjoomerButton btnRegister;
	private IjoomerButton btnRegisterWithFacebook;
	private Spinner spnRegistrationType;
	private IjoomerButton btnBack;


    private SimpleFacebook simpleFacebook;
    private SimpleFacebookConfiguration.Builder simpleFacebookConfigurationBuilder;
    private SimpleFacebookConfiguration simpleFacebookConfiguration;
    private String IN_EMAIL;
    private String IN_PASSWORD;

    /**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_registration_step1;
	}

	@Override
	public void initComponents() {

		edtUserName = (IjoomerEditText) findViewById(R.id.edtUserName);
		edtPassword = (IjoomerEditText) findViewById(R.id.edtPassword);
		edtEmail = (IjoomerEditText) findViewById(R.id.edtEmail);
		edtMobileNo = (IjoomerEditText) findViewById(R.id.edtMobileNo);
		edtUserType = (IjoomerEditText) findViewById(R.id.edtUserType);
		spnRegistrationType = (Spinner) findViewById(R.id.spnRegistrationType);
		btnBack = (IjoomerButton) findViewById(R.id.btnBack);
		btnRegisterWithFacebook = (IjoomerButton) findViewById(R.id.btnRegisterWithFacebook);
		btnRegister = (IjoomerButton) findViewById(R.id.btnRegister);

        getIntentData();

	}

    @Override
    protected void onResume() {
        super.onResume();
        simpleFacebook = SimpleFacebook.getInstance(this);
        simpleFacebookConfigurationBuilder = new SimpleFacebookConfiguration.Builder();
        simpleFacebookConfigurationBuilder.setAppId(getString(R.string.facebook_app_id));
        simpleFacebookConfigurationBuilder.setNamespace("kravely");
        simpleFacebookConfigurationBuilder.setPermissions(new Permission[] { Permission.EMAIL});
        simpleFacebookConfiguration = simpleFacebookConfigurationBuilder.build();
        SimpleFacebook.setConfiguration(simpleFacebookConfiguration);
    }

    @Override
	public void prepareViews() {
        if(IN_EMAIL!=null){
            edtEmail.setText(IN_EMAIL);
            edtPassword.setText(IN_PASSWORD);
            edtEmail.setEnabled(false);
            edtPassword.setEnabled(false);
            btnRegisterWithFacebook.setVisibility(View.GONE);
            edtPassword.setVisibility(View.GONE);
        }
		ArrayList<String> values = new ArrayList<String>();
		String[] array = getResources().getStringArray(R.array.df_user_type);
		for (int i = 0; i < array.length; i++) {
			values.add(array[i]);
		}

		IjoomerUtilities.MyCustomAdapter adpater = new IjoomerUtilities.MyCustomAdapter(this, values);
		spnRegistrationType.setAdapter(adpater);

	}

	@Override
	public void setActionListeners() {

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		edtUserType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				spnRegistrationType.performClick();
			}
		});

		spnRegistrationType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				edtUserType.setText(spnRegistrationType.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});
		btnRegisterWithFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {


                    if(simpleFacebook.isLogin()){
                        getProfile();
                    }else{
                        simpleFacebook.login(new OnLoginListener());
                    }

			}
		});
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				boolean validationFlag = true;
				if (edtMobileNo.getText().toString().trim().length() <= 0) {
					edtMobileNo.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtUserName.getText().toString().trim().length() <= 0) {
					edtUserName.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}else{
                    if(!IjoomerUtilities.usernameValidator(edtUserName.getText().toString().trim())){
                        edtUserName.setError(getString(R.string.validation_invalid_username));
                        validationFlag = false;
                    }
                }
				if (edtPassword.getText().toString().trim().length() <= 0) {
					edtPassword.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtEmail.getText().toString().trim().length() <= 0) {
					edtEmail.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				} else {
					if (!IjoomerUtilities.emailValidator(edtEmail.getText().toString().trim())) {
						validationFlag = false;
						edtEmail.setError(getString(R.string.validation_invalid_email));
					}
				}

				if (edtUserType.getText().toString().length() <= 0) {
					edtUserType.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}

				if (validationFlag) {

                    getConfirmDialog("Terms and Conditions","By registering an account with Kravely, you agree to our terms and conditions which can be found <a href=\"http://kravely.com/index.php/t-c\">here</a>."," I Agree","Cancel",false,new AlertMagnatic() {
                        @Override
                        public void PositiveMethod(DialogInterface dialog, int id) {
                            showProgressDialog(getString(R.string.dialog_loading_register_newuser), IjoomerRegistrationStep1Activity.this);
                            final String profileType = spnRegistrationType.getSelectedItemPosition() == 0 ? "11" : "10";

                            if (spnRegistrationType.getSelectedItemPosition() == 1) {
                                hideProgressDialog();
                                try {
                                    loadNew(IjoomerRegistrationStep2Activity.class, IjoomerRegistrationStep1Activity.this, false, "IN_EMAIL", edtEmail.getText().toString(), "IN_USERNAME",
                                            edtUserName.getText().toString(), "IN_PASSWORD", edtPassword.getText().toString(), "IN_MONO", edtMobileNo.getText().toString(), "IN_USERTYPE",
                                            profileType,"IN_ISFB",btnRegisterWithFacebook.getVisibility()==View.VISIBLE?false:true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                new IjoomerRegistration(IjoomerRegistrationStep1Activity.this).signUpNewUser(edtUserName.getText().toString().trim(), edtPassword.getText().toString()
                                        .trim(), edtEmail.getText().toString(), edtMobileNo.getText().toString(), profileType, "",btnRegisterWithFacebook.getVisibility()==View.VISIBLE?false:true, new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {
                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        hideProgressDialog();
                                        if (responseCode == 200) {

                                            if(!(btnRegisterWithFacebook.getVisibility()==View.VISIBLE)){
                                                if (responseCode == 200) {

                                                    getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN,true);

                                                    try {
                                                        if(!(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").equals(IN_EMAIL))){
                                                            new IjoomerCaching(IjoomerRegistrationStep1Activity.this).resetDataBase();
                                                        }
                                                    }catch (Exception e){

                                                    }
                                                    try {
                                                        getSmartApplication().writeSharedPreferences(SP_LOGIN_TYPE, ((JSONObject) data2).getString("userType"));
                                                        getSmartApplication().writeSharedPreferences(SP_REST_ID, ((JSONObject) data2).getString("restids"));
                                                    } catch (Exception e) {
                                                    }

                                                    try {
                                                        getSmartApplication().writeSharedPreferences(SP_LOGIN_TYPE, ((JSONObject) data2).getString("userType"));
                                                    } catch (Exception e) {
                                                    }

                                                    if (IjoomerApplicationConfiguration.getUserType() == IjoomerApplicationConfiguration.SELLER) {
                                                        try {

                                                            getSmartApplication().writeSharedPreferences(SP_DFS_PAYMENT_COMPLETE, ((JSONObject) data2).getString("isSetPaypal"));
                                                            getSmartApplication().writeSharedPreferences(SP_DFS_PROFILE_COMPLETE, ((JSONObject) data2).getString("isSetProfile"));

                                                            getSmartApplication().writeSharedPreferences(SP_SERVE_CATEGORIES, ((JSONObject) data2).getString("serveCategories"));
                                                            getSmartApplication().writeSharedPreferences(SP_NEW_MEAL_REQ,
                                                                    ((JSONObject) data2).getString("newMealReq").equals("1") ? true : false);
                                                            getSmartApplication().writeSharedPreferences(SP_NEW_DISH_OFFER,
                                                                    ((JSONObject) data2).getString("newDishOffer").equals("1") ? true : false);

                                                            Intent intent = new Intent("clearStackActivity");
                                                            intent.setType("text/plain");
                                                            sendBroadcast(intent);
                                                            loadNew(DFSMyAccountActivity.class, IjoomerRegistrationStep1Activity.this, true);

                                                        } catch (Exception e) {
                                                        }
                                                    } else {
                                                        try {
                                                            getSmartApplication().writeSharedPreferences(SP_DF_ADDRESS, ((JSONObject) data2).getString("address"));
                                                            getSmartApplication().writeSharedPreferences(SP_DF_USERNAME, ((JSONObject) data2).getString("username"));
                                                            getSmartApplication().writeSharedPreferences(SP_DF_SHARELINK, ((JSONObject) data2).getString("shareLink"));
                                                            getSmartApplication().writeSharedPreferences(SP_DF_DISH_NAME, ((JSONObject) data2).getString("dishName"));

                                                            getSmartApplication().writeSharedPreferences(SP_DFC_PROFILE_COMPLETE, ((JSONObject) data2).getString("isSetProfile"));

                                                            getSmartApplication().writeSharedPreferences(SP_ACCEPT_MEAL_REQ,
                                                                    ((JSONObject) data2).getString("acceptMealReq").equals("1") ? true : false);
                                                            getSmartApplication().writeSharedPreferences(SP_ACCEPT_DISH_OFFER,
                                                                    ((JSONObject) data2).getString("acceptDishOffer").equals("1") ? true : false);

                                                            loadNew(DFCPlaceOrderActivity.class, IjoomerRegistrationStep1Activity.this, true);

                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                } else {
                                                    responseMessageHandler(responseCode, true);
                                                }
                                            }else{
                                                IjoomerUtilities.getDFInfoDialog(IjoomerRegistrationStep1Activity.this, getString(R.string.registration_successfully), getString(R.string.ok),
                                                        new CustomAlertNeutral() {

                                                            @Override
                                                            public void NeutralMethod() {

                                                                loadNew(IjoomerLoginActivity.class, IjoomerRegistrationStep1Activity.this, true);
                                                            }
                                                        });
                                            }
                                        } else {
                                            responseMessageHandler(responseCode, true);
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void NegativeMethod(DialogInterface dialog, int id) {

                        }
                    });


				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Class methods
	 */


    public void getIntentData(){
        IN_EMAIL = getIntent().getStringExtra("IN_EMAIL");
        IN_PASSWORD = getIntent().getStringExtra("IN_PASSWORD");
    }
	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

    private void getProfile(){
        PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
        pictureAttributes.setHeight(100);
        pictureAttributes.setWidth(100);
        pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);

        Profile.Properties properties = new Profile.Properties.Builder()
                .add(Profile.Properties.ID)
                .add(Profile.Properties.NAME)
                .add(Profile.Properties.EMAIL)
                .add(Profile.Properties.PICTURE, pictureAttributes)
                .build();
        simpleFacebook.getProfile(properties,new OnProfileRequestListener());
    }


    class OnLoginListener implements com.sromku.simple.fb.listeners.OnLoginListener{


        @Override
        public void onLogin() {
            getProfile();
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {

        }


        @Override
        public void onThinking() {
        }

        @Override
        public void onException(Throwable throwable) {

        }

        @Override
        public void onFail(String reason) {
            IjoomerUtilities.getCustomOkDialog(getString(R.string.friend),reason, getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                }
            });
        }
    }

    class OnProfileRequestListener extends OnProfileListener {


        @Override
        public void onThinking() {
            showProgressDialog("Getting Facebook Profile...",IjoomerRegistrationStep1Activity.this,true);
        }

        @Override
        public void onException(Throwable throwable) {
            hideProgressDialog();
        }

        @Override
        public void onFail(String reason) {
            hideProgressDialog();
            IjoomerUtilities.getCustomOkDialog(getString(R.string.friend),reason, getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                }
            });
        }

        @Override
        public void onComplete(Profile profile) {
            hideProgressDialog();
            doFacebookLogin(profile);
        }

        private void doFacebookLogin(final Profile profile) {
            edtEmail.setText(profile.getEmail());
            edtPassword.setText(profile.getId());
            edtEmail.setEnabled(false);
            edtPassword.setEnabled(false);
            btnRegisterWithFacebook.setVisibility(View.GONE);
            edtPassword.setVisibility(View.GONE);
        }

    }
}