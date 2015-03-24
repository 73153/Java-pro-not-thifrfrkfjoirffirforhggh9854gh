package com.df.src;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.customer.DFCPlaceOrderActivity;
import com.df.seller.DFSMyAccountActivity;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerLoginMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertInputMagnatic;
import com.smart.framework.AlertNeutral;
import com.smart.framework.CustomAlertNeutral;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.utils.Attributes;
import com.sromku.simple.fb.utils.PictureAttributes;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerLoginActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerLoginActivity extends IjoomerLoginMaster {

	private IjoomerButton btnForgetPassword;
	private IjoomerButton btnRegister;
	private IjoomerEditText edtEmail;
	private IjoomerEditText edtPassword;
	private IjoomerButton btnLogin;
    private IjoomerButton btnLoginWithFacebook;

	private SimpleFacebook simpleFacebook;
	private SimpleFacebookConfiguration.Builder simpleFacebookConfigurationBuilder;
	private SimpleFacebookConfiguration simpleFacebookConfiguration;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		IjoomerApplicationConfiguration.setDefaultConfiguration(this);
		return R.layout.ijoomer_login;
	}

	@Override
	public void initComponents() {
		edtEmail = (IjoomerEditText) findViewById(R.id.edtEmail);
		edtPassword = (IjoomerEditText) findViewById(R.id.edtPassword);
		btnLogin = (IjoomerButton) findViewById(R.id.btnLogin);
		btnRegister = (IjoomerButton) findViewById(R.id.btnRegister);
		btnForgetPassword = (IjoomerButton) findViewById(R.id.btnForgetPassword);
        btnLoginWithFacebook = (IjoomerButton) findViewById(R.id.btnLoginWithFacebook);


	}

	@Override
	public void prepareViews() {

        if(!(getSmartApplication().readSharedPreferences().getBoolean(SP_ISFACEBOOKLOGIN,false))){
            edtEmail.setText(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""));
            edtPassword.setText(getSmartApplication().readSharedPreferences().getString(SP_PASSWORD, ""));
        }

	}

	@Override
	public void setActionListeners() {

        btnLoginWithFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(simpleFacebook.isLogin()){
                    getProfile();
                }else{
                    simpleFacebook.login(new OnLoginListener());
                }
            }
        });

		btnForgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showForgotPassword();

			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadNew(IjoomerRegistrationStep1Activity.class, IjoomerLoginActivity.this, false);
			}
		});
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				hideSoftKeyboard();
				getSmartApplication().writeSharedPreferences(SP_USERNAME, edtEmail.getText().toString().trim());
				getSmartApplication().writeSharedPreferences(SP_PASSWORD, edtPassword.getText().toString().trim());

				boolean validationFlag = true;
				if (edtPassword.getText().toString().trim().length() <= 0) {
					edtPassword.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtEmail.getText().toString().trim().length() <= 0) {
					edtEmail.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}

				if (validationFlag) {
					showProgressDialog(getString(R.string.authenticating_user), IjoomerLoginActivity.this);
					IjoomerOauth.getInstance(IjoomerLoginActivity.this).authenticateUser(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(),false,
							new WebCallListener() {

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									hideProgressDialog();
									if (responseCode == 200) {
                                        getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN,false);
                                        try {
                                            if(!(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").equals(edtEmail.getText().toString().trim()))){
                                                new IjoomerCaching(IjoomerLoginActivity.this).resetDataBase();
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
                                                getSmartApplication().writeSharedPreferences(SP_DF_USERNAME, ((JSONObject) data2).getString("username"));

                                                getSmartApplication().writeSharedPreferences(SP_DFS_PAYMENT_COMPLETE, ((JSONObject) data2).getString("isSetPaypal"));
                                                getSmartApplication().writeSharedPreferences(SP_DFS_PROFILE_COMPLETE, ((JSONObject) data2).getString("isSetProfile"));

												getSmartApplication().writeSharedPreferences(SP_SERVE_CATEGORIES, ((JSONObject) data2).getString("serveCategories"));
												getSmartApplication().writeSharedPreferences(SP_NEW_MEAL_REQ,
														((JSONObject) data2).getString("newMealReq").equals("1") ? true : false);
												getSmartApplication().writeSharedPreferences(SP_NEW_DISH_OFFER,
														((JSONObject) data2).getString("newDishOffer").equals("1") ? true : false);

													loadNew(DFSMyAccountActivity.class, IjoomerLoginActivity.this, true);

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


													loadNew(DFCPlaceOrderActivity.class, IjoomerLoginActivity.this, true);

											} catch (Exception e) {
											}
										}
									} else {
										responseMessageHandler(responseCode, true);
									}
								}

								@Override
								public void onProgressUpdate(int progressCount) {
								}
							});
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		if (getSmartApplication().readSharedPreferences().getBoolean(SP_DOLOGIN, false)) {
			finish();
		} else {
			Intent intent = new Intent("clearStackActivity");
			intent.setType("text/plain");
			sendBroadcast(intent);
			moveTaskToBack(true);
		}
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
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		simpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to login using facebook user.
	 * 
	 * @param profile
	 *            facebook user profile object
	 */
	private void doFacebookLogin(final Profile profile) {
        showProgressDialog(getString(R.string.authenticating_user), IjoomerLoginActivity.this);
        IjoomerOauth.getInstance(IjoomerLoginActivity.this).authenticateUser(profile.getEmail(), profile.getId(),true,
                new WebCallListener() {

                    @Override
                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                        hideProgressDialog();
                        if (responseCode == 200) {

                            getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN,true);

                            try {
                                if(!(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").equals(edtEmail.getText().toString().trim()))){
                                    new IjoomerCaching(IjoomerLoginActivity.this).resetDataBase();
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

                                    getSmartApplication().writeSharedPreferences(SP_DF_USERNAME, ((JSONObject) data2).getString("username"));
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
                                        loadNew(DFSMyAccountActivity.class, IjoomerLoginActivity.this, true);

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


                                        Intent intent = new Intent("clearStackActivity");
                                        intent.setType("text/plain");
                                        sendBroadcast(intent);
                                        loadNew(DFCPlaceOrderActivity.class, IjoomerLoginActivity.this, true);

                                } catch (Exception e) {
                                }
                            }
                        } else {

                            if(responseCode==401 || responseCode==402){
                                try{
                                    loadNew(IjoomerRegistrationStep1Activity.class, IjoomerLoginActivity.this, false,"IN_EMAIL",profile.getEmail(),"IN_PASSWORD",profile.getId());
                                }catch (Exception e){

                                }
                            }else{
                                responseMessageHandler(responseCode, true);
                            }
                        }
                    }

                    @Override
                    public void onProgressUpdate(int progressCount) {
                    }
                });
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

	private void getProfile() {
		PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
		pictureAttributes.setHeight(100);
		pictureAttributes.setWidth(100);
		pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);


		Profile.Properties properties = new Profile.Properties.Builder().add(Properties.ID).add(Properties.NAME).add(Properties.EMAIL)
				.add(Properties.PICTURE, pictureAttributes).build();
		simpleFacebook.getProfile(properties, new OnProfileRequestListener());
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
            IjoomerUtilities.getDFInfoDialog(IjoomerLoginActivity.this,reason, getString(R.string.ok), new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                    finish();
                }
            });
        }
    }


    class OnProfileRequestListener extends OnProfileListener{
        @Override
        public void onThinking() {
            showProgressDialog("Getting Facebook Profile...", IjoomerLoginActivity.this, true);
            super.onThinking();
        }

        @Override
        public void onException(Throwable throwable) {
            hideProgressDialog();
            super.onException(throwable);
        }

        @Override
        public void onFail(String reason) {
            hideProgressDialog();
            IjoomerUtilities.getDFInfoDialog(IjoomerLoginActivity.this,reason, getString(R.string.ok), new CustomAlertNeutral() {

                @Override
                public void NeutralMethod() {
                    finish();
                }
            });
            super.onFail(reason);
        }

        @Override
        public void onComplete(Profile profile) {
            hideProgressDialog();
            doFacebookLogin(profile);
            super.onComplete(profile);
        }
    }

	private void showForgotPassword() {

		getInputDialog("Forgot Password", "Email Address:", "Submit", "Cancel", false, new AlertInputMagnatic() {

			@Override
			public void PositiveMethod(DialogInterface dialog, int id, String inputStr) {

				if (!(inputStr != null && inputStr.trim().length() > 0)) {
					getOKDialog("Forgot Password", "Please enter your registered email address.", "Ok", false, new AlertNeutral() {

						@Override
						public void NeutralMathod(DialogInterface dialog, int id) {
							showForgotPassword();
						}
					});

				} else if (!IjoomerUtilities.emailValidator(inputStr)) {
					getOKDialog("Forgot Password", "Opps! Invalid email address.", "Ok", false, new AlertNeutral() {

						@Override
						public void NeutralMathod(DialogInterface dialog, int id) {
							showForgotPassword();
						}
					});
				} else {
					submitForgotPassword(inputStr);
				}
			}

			@Override
			public void NegativeMethod(DialogInterface dialog, int id, String inputStr) {

			}
		});
	}

	private void submitForgotPassword(String email) {

		showProgressDialog("Validating email...", IjoomerLoginActivity.this);

		IjoomerOauth.getInstance(this).forgetDFPassword(email, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					IjoomerUtilities.getDFInfoDialog(IjoomerLoginActivity.this, "New password send to your email address", getString(R.string.ok), new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}



}