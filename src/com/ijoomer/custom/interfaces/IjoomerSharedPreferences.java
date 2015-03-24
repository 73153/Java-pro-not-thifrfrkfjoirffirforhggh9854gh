package com.ijoomer.custom.interfaces;

/**
 * This Interface Contains All Method Related To IjoomerSharedPreferences.
 * 
 * @author tasol
 * 
 */
public interface IjoomerSharedPreferences {
	/*
	 * Http access
	 */

	public static String SP_HTTP_ACCESSS_USERNAME = "httpAccessUserName";
	public static String SP_HTTP_ACCESSS_PASSWORD = "httpAccessPassword";
	public static String SP_HTTP_ACCESSS_REMEMBER = "httpAccessIsRemember";

	/*
	 * GCM Preferences
	 */

	public static String SP_GCM_REGID = "gcmRegId";
	public static String SP_GCM_APP_VERSION = "gcmAppVersion";
	public static String SP_GCM_ERROR_DIALOG = "gcmErrorDialog";

	/*
	 * Global Configuration Preferences
	 */

	public static String SP_DEFAULT_DOWNLOAD_LOCATION = "defaultDownloadLocation";
	public static String SP_URL_SETTING = "urlsetting";
	public static String SP_LAST_ACTIVITY = "lastActivity";
	public static String SP_LAST_ACTIVITY_INTENT = "lastActivityIntent";
	public static String SP_DEFAULT_LANDING_SCREEN = "defaultLandingScreen";
	public static String SP_ICON_PRELOADER = "iconPreloader";
	public static String SP_LAST_REQUEST_DATE = "lastRequestDate";

	/*
	 * User Authentication Preferences
	 */
	public static String SP_USERNAME = "userName";
	public static String SP_DOLOGIN = "dologin";
	public static String SP_PASSWORD = "password";
	public static String SP_CLIENT_DOMAIN = "clientDomain";
	public static String SP_ISLOGOUT = "isLoggedOut";
	public static String SP_ISFACEBOOKLOGIN = "isFacebookLogin";
	public static String SP_LOGIN_REQ_OBJECT = "loginReqObject";

	public static String SP_LOGIN_TYPE = "loginType";
	public static String SP_REST_ID = "restId";

	/*
	 * Twitter Preferences
	 */

	public static String SP_TWITTER_TOKEN = "token";
	public static String SP_TWITTER_SECRET_TOKEN = "secretToken";

	/**
	 * Application Config
	 */

	public static String SP_ISENABLECOMMENTK2 = "isEnableCommentK2";
	public static String SP_ISENABLEVOICE = "isEnableVoice";
	public static String SP_MAXAUDIOLENGTH = "maxAudioLength";
	public static String SP_TERMSOBJECT = "termsObject";
	public static String SP_ISENABLETERMS = "isEnableTerms";
	public static String SP_PHOTOUPLOADSIZE = "photoUploadSize";
	public static String SP_VIDEOUPLOADSIZE = "videoUploadSize";
	public static String SP_ISVIDEOUPLOAD = "isVideoUpload";
	public static String SP_ISEVENTCREATE = "isEventCreate";
	public static String SP_ISGROUPCREATE = "isGroupCreate";
	public static String SP_ISPHOTOUPLOAD = "isPhotoUpload";
	public static String SP_SERVERTIMEZONE = "serverTimeZone";
	public static String SP_ISLOGINREQUIRED = "isLoginRequired";
	public static String SP_ALLOWREGISTRATION = "allowRegistration";
	public static String SP_ALLOWTHEMESELECTION = "allowThemeSelection";
	public static String SP_REGISTRATIONWITH = "registrationWith";
	public static String SP_DEFAULTAVATAR = "defaultAvatar";
	public static String SP_DEFAULTAVATAR_FEMALE = "defaultAvatarFemale";
	public static String SP_JOM_VERSION = "jomVersion";
	public static String SP_JBOLO_CHAT_GET_HISTORY = "jboloChatGetHistory";
	public static String SP_JBOLO_CHAT_SEND_FILE = "jboloChatSendFile";
	public static String SP_JBOLO_CHAT_SEND_FILE_MAX_LIMIT = "jboloChatSendFileMaxLimit";
	public static String SP_JBOLO_CHAT_GROUP_CHAT = "jboloChatGroupChat";

	public static String SP_JREVIEWARTICLEPHOTOUPLOADSIZE = "jreviewarticlephotoUploadSize";
	public static String SP_JREVIEWARTICLEVIDEOUPLOADSIZE = "jreviewarticlevideoUploadSize";
	public static String SP_JREVIEWARTICLEAUDIOUPLOADSIZE = "jreviewarticleaudioUploadSize";
	public static String SP_JREVIEWARTICLEATTACHMENTUPLOADSIZE = "jreviewarticleattachmentUploadSize";
	public static String SP_JREVIEWARTICLEPHOTOUPLOADLIMIT = "jreviewarticlephotoUploadLimit";
	public static String SP_JREVIEWARTICLEVIDEOUPLOADLIMIT = "jreviewarticlevideoUploadLimit";
	public static String SP_JREVIEWARTICLEAUDIOUPLOADLIMIT = "jreviewarticleaudioUploadLimit";
	public static String SP_JREVIEWARTICLEATTACHMENTUPLOADLIMIT = "jreviewarticleattachmentUploadLimit";
	public static String SP_JREVIEWALLOWSEARCHBYAUTHOR = "allowsearchbyauthor";
	public static String SP_JREVIEWPHOTOENABLE = "jreviewphotoenable";
	public static String SP_JREVIEWVIDEOENABLE = "jreviewvideoenable";
	public static String SP_JREVIEWAUDIOENABLE = "jreviewaudioenable";
	public static String SP_JREVIEWATTACHMENTENABLE = "jreviewattachmentenable";

	/**
	 * Plugins Weahter
	 */
	public static String SP_LOCATION_ID = "locationID";

	/*
	 * JReview Preferences
	 */
	public static String SP_RELOADREVIEWS = "reloadreviews";
	public static String SP_RELOADARTICLEDETAILS = "reloadarticledetails";
	public static String SP_RELOADARTICLES = "reloadarticles";
	public static String SP_RELOADFAVUORITEARTICLES = "reloadfavouritearticles";

	/*
	 * JBoloChat Preferences
	 */
	public static String SP_JBOLOCHAT_USER_STATUS = "userStatus";
	public static String SP_JBOLOCHAT_USER_STATUS_MESSAGE = "userStatusMessage";

	/**
	 * DF
	 */

	public static String SP_SERVE_CATEGORIES = "serveCategories";
	public static String SP_NEW_MEAL_REQ = "newMealReq";
	public static String SP_NEW_DISH_OFFER = "newDishOffer";

	public static String SP_ACCEPT_MEAL_REQ = "acceptMealReq";
	public static String SP_ACCEPT_DISH_OFFER = "acceptDishOffer";
	public static String SP_REWARD_POINTS = "rewardPoints";

    public static String SP_DF_USERNAME = "dfUseName";
    public static String SP_DF_ADDRESS = "dfAddress";
    public static String SP_DF_DISH_NAME = "dfDishName";
    public static String SP_DF_DISH_IMAGE = "dfDishImage";
    public static String SP_DF_SHARELINK = "dfShareLink";

    public static String SP_DF_REQ_TIMESTAMP = "dfRequestTimestamp";
    public static String SP_DF_REQ_WAS_ACCEPTED = "dfRequestWasAccepted";
    public static String SP_DF_REQ_WAS_CONFIRMED = "dfRequestWasConfirmed";


    public static String SP_DFC_PROFILE_COMPLETE = "dfcProfileComplete";
    public static String SP_DFS_PROFILE_COMPLETE = "dfSProfileComplete";
    public static String SP_DFS_PAYMENT_COMPLETE = "dfsPaymentComplete";

    public static String SP_DFC_LISTEN_COUNT = "dfcListenCount";


}
