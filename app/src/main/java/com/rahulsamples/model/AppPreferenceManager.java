/**
 * AppPreferenceManager.java - a class that stores the data locally in shared Preferences
 *
 * @author Ashish Kumar
 * @version 1.0
 * @createddate Feb 12, 2016
 * @change on March 15, 2017
 * @modificationlog not used longer in the code
 * @see android.content.SharedPreferences
 */

package com.rahulsamples.model;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferenceManager {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private Context context;
    private static final String SHARED_PREF = "FIRST JOB PREFERENCES";
    private static final String USERTOKEN = "USERTOKEN";
    private static final String USERNAME = "USERNAME";
    private static final String USEREMAIL = "USEREMAIL";
    private static final String USERPHONE = "USERPHONE";
    private static final String USERIMAGE = "USERIMAGE";
    private static final String USERDOB = "USERDOB";
    private static final String USERGENDER = "USERGENDER";
    private static final String USERPINCODE = "USERPINCODE";
    private static final String PROFILEPERCENT = "PROFILEPERCENT";
    private static final String RESUME = "RESUME";
    private static final String GCMTOKEN = "GCMTOKEN";
    private static final String ACTIVE_AUDITION = "ACTIVE_AUDITION";
    private static final String ACTIVE_AUDITION_ID = "ACTIVE_AUDITION_ID";
    private static final String AUDITION_SHARE_URL = "AUDITION_SHARE_URL";
    private static final String HOME_OVERLAY = "HOME_OVERLAY";
    private static final String HISTORY_OVERLAY = "HISTORY_OVERLAY";
    private static final String UPLOAD_FIRST_AUDITION = "UPLOAD_FIRST_AUDITION";
    private static final String UploadedFileCounter = "UploadedFileCounter";
    private static final String FAIL_INTERVIEW = "FAIL_INTERVIEW";
    private static final String PAYTM = "PAYTM";
    private static final String INVITES = "INVITES";
    private static final String DEEP_LINK = "DEEPLINK";
    private static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";
    private static final String  NOTIFICATION_JOBCODE= "NOTIFICATION_TYPE";

    public static final String  INTERVIEW= "INTERVIEW";
    public static final String  ASSESSMENT= "ASSESSMENT";
    public static final String  NONE= "NONE";

    private static final String  LAST_ACCESSED_JOB_TYPE= "LAST_ACCESSED_JOB_TYPE";
    public static final String  JOB_CODE= "JOB_CODE";
    public static final String  JOB_TYPE= "JOB_TYPE";
    public static final String  LAST_ACCESSED_COMPANY_NAME= "LAST_ACCESSED_COMPANY_NAME";
    public static  final String  WAS_INTERVIEW_STARTED= "WAS_INTERVIEW_STARTED";
    public static  final String  LAST_ACCESSED_QUESTION_TIMING_FOR_ASSESSMENT= "LAST_ACCESSED_QUESTION_TIMING_FOR_ASSESSMENT";
    private static final String  LAST_ACCESSED_QUESTION_DETAILS= "last_accessed_question_details";

    public static boolean  FROM_SPLASH=false;
    public static boolean  CHECK_APP_UPDATION;


    public static final  String COMING_FROM_BACKGROUND_ACTIVITY="COMING_FROM_BACKGROUND_ACTIVITY";
    public static final  String GOING_TO_BACKGROUND_ACTIVITY="GOING_TO_BACKGROUND_ACTIVITY";


    public static  boolean IS_INTENT_FIRED=false;
    public static final  String WITHIN_APP="WITHIN_APP";
    public static final  String COMING_FROM_OUTSIDE="COMING_FROM_OUTSIDE";
    public static final  String GOING_TO_OUTSIDE="GOING_TO_OUTSIDE";

    public static final  String JOBFAIR_DATA="JOBFAIR_DATA";
    public static   boolean IS_FIRST_TIME_VISITING;
    public static  String SELECTED_LOCATION="SELECTED_LOCATION";
    public static  String SELECTED_SKILLS="SELECTED_SKILLS";

    private static  String SALARY_JOB_FILTER="SALARY_JOB_FILTER";
    private static  String EDUCATION_JOB_FILTER="EDUCATION_JOB_FILTER";
    private static  String FRESHNESS_JOB_FILTER="FRESHNESS_JOB_FILTER";
    private static  String LOCATION_JOB_FILTER="LOCATION_JOB_FILTER";

    public AppPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    public void removeValues(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.clear();
        editor.commit();
    }


    public  String getLocationJobFilter() {
        return sharedPreferences.getString(LOCATION_JOB_FILTER, "");
    }
    public void setLocationJobFilter(String gcmToken) {
        editor.putString(LOCATION_JOB_FILTER, gcmToken);
        editor.commit();
    }

    public  String getFreshJobFilter() {
        return sharedPreferences.getString(FRESHNESS_JOB_FILTER, "");
    }
    public void setFreshJobFilter(String gcmToken) {
        editor.putString(FRESHNESS_JOB_FILTER, gcmToken);
        editor.commit();
    }


    public  String getEducationJobFilter() {
        return sharedPreferences.getString(EDUCATION_JOB_FILTER, "");
    }
    public void setEducationJobFilter(String gcmToken) {
        editor.putString(EDUCATION_JOB_FILTER, gcmToken);
        editor.commit();
    }

    public static int getPreferenceInt() {
        return sharedPreferences.getInt("NOTICOUNT", 0);
    }
    public static void savePreferenceLong(int gcmToken) {
        editor.putInt("NOTICOUNT", gcmToken);
        editor.commit();
    }


    public  String getSalaryJobFilter() {
        return sharedPreferences.getString(SALARY_JOB_FILTER, "");
    }
    public void setSalaryJobFilter(String gcmToken) {
        editor.putString(SALARY_JOB_FILTER, gcmToken);
        editor.commit();
    }





    public  String getComingFromBackgroundActivity() {
        return sharedPreferences.getString(COMING_FROM_BACKGROUND_ACTIVITY, "");
    }


    public void setComingFromBackgroundActivity(String gcmToken) {
        editor.putString(COMING_FROM_BACKGROUND_ACTIVITY, gcmToken);
        editor.commit();
    }

    public void setGoingToBackgroundActivity(String gcmToken) {
        editor.putString(GOING_TO_BACKGROUND_ACTIVITY, gcmToken);
        editor.commit();
    }

    public  String getGoingToBackgroundActivity() {
        return sharedPreferences.getString(GOING_TO_BACKGROUND_ACTIVITY, "");

    }

    public String getSelectedLocation() {
        return sharedPreferences.getString(SELECTED_LOCATION, "");
    }


    public void setSelectedLocation(String location) {
        editor.putString(SELECTED_LOCATION, location);
        editor.commit();
    }



    public String getUserToken() {
        return sharedPreferences.getString(USERTOKEN, "");
    }


    public void setUserToken(String gcmToken) {
        editor.putString(USERTOKEN, gcmToken);
        editor.commit();
    }

    public String getLastAccessedQuestionTimingForAssessment() {
        return sharedPreferences.getString(LAST_ACCESSED_QUESTION_TIMING_FOR_ASSESSMENT, "");
    }

    public void setLastAccessedQuestionTimingForAssessment(String timing) {
        editor.putString(LAST_ACCESSED_QUESTION_TIMING_FOR_ASSESSMENT, timing);
        editor.commit();
    }

    public boolean getWasInterviewStarted() {
        return sharedPreferences.getBoolean(WAS_INTERVIEW_STARTED, false);
    }

    public void setWasInterviewStarted(boolean wasInterviewStarted) {
        editor.putBoolean(WAS_INTERVIEW_STARTED, wasInterviewStarted);
        editor.commit();
    }

public String getJobCode() {
        return sharedPreferences.getString(JOB_CODE, "");
    }

    public void setJobCode(String jobCode) {
        editor.putString(JOB_CODE, jobCode);
        editor.commit();
    }
    public String getLastAccessedCompanyName() {
        return sharedPreferences.getString(LAST_ACCESSED_COMPANY_NAME, "");
    }

    public void setLastAccessedCompanyName(String companyName) {
        editor.putString(LAST_ACCESSED_COMPANY_NAME, companyName);
        editor.commit();
    }

    public String getJobType() {
        return sharedPreferences.getString(JOB_TYPE, "");
    }

    public void setJobType(String jobType) {
        editor.putString(JOB_TYPE, jobType);
        editor.commit();
    }

    public void setLastAccessedQuestionDetails(String questionData) {
        editor.putString(LAST_ACCESSED_QUESTION_DETAILS, String.valueOf(questionData));
        editor.commit();
    }

    public String getLastAccessedQuestionDetails() {
        return sharedPreferences.getString(LAST_ACCESSED_QUESTION_DETAILS, "");
    }

    public void setLastAccessedJobType(String jobType) {
        editor.putString(LAST_ACCESSED_JOB_TYPE, String.valueOf(jobType));
        editor.commit();
    }

    public String getLastAccessedJobType() {
        return sharedPreferences.getString(LAST_ACCESSED_JOB_TYPE, "");
    }

    public String getUserName() {
        return sharedPreferences.getString(USERNAME, "");
    }

    public void setUserName(String userName) {
        editor.putString(USERNAME, userName);
        editor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USEREMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        editor.putString(USEREMAIL, userEmail);
        editor.commit();
    }

    public String getUserPhone() {
        return sharedPreferences.getString(USERPHONE, "");
    }

    public void setUserPhone(String phone) {
        editor.putString(USERPHONE, phone);
        editor.commit();
    }

    public String getUserImage() {
        return sharedPreferences.getString(USERIMAGE, "");
    }

    public void setUserImage(String image) {
        editor.putString(USERIMAGE, image);
        editor.commit();
    }

    public String getUserDob() {
        return sharedPreferences.getString(USERDOB, "");
    }

    public void setUserDob(String dob) {
        editor.putString(USERDOB, dob);
        editor.commit();
    }

    public String getUserGender() {
        return sharedPreferences.getString(USERGENDER, "");
    }

    public void setUserGender(String gender) {
        editor.putString(USERGENDER, gender);
        editor.commit();
    }

    public String getUserResume() {
        return sharedPreferences.getString(RESUME, "");
    }

    public void setUserResume(String resume) {
        editor.putString(RESUME, resume);
        editor.commit();
    }

    public String getUserPinCode() {
        return sharedPreferences.getString(USERPINCODE, "");
    }

    public void setUserPincode(String pincode) {
        editor.putString(USERPINCODE, pincode);
        editor.commit();
    }

    public String getGCToken() {
        return sharedPreferences.getString(GCMTOKEN, "");
    }

    public void setGCToken(String gcmToken) {
        editor.putString(GCMTOKEN, gcmToken);
        editor.commit();
    }

    public int getUserprofilePercent() {
        return sharedPreferences.getInt(PROFILEPERCENT, 0);
    }

    public void setUserProfilePercent(int profilePercent) {
        editor.putInt(PROFILEPERCENT, profilePercent);
        editor.commit();
    }

    public String getActiveAudition() {
        return sharedPreferences.getString(ACTIVE_AUDITION, "");
    }

    public void setActiveAudition(String activeAudition) {
        editor.putString(ACTIVE_AUDITION, activeAudition);
        editor.commit();
    }

    public String getActiveAuditionId() {
        return sharedPreferences.getString(ACTIVE_AUDITION_ID, "");
    }

    public void setActiveAuditionId(String activeAuditionId) {
        editor.putString(ACTIVE_AUDITION_ID, activeAuditionId);
        editor.commit();
    }

    public String getAuditionShareUrl() {
        return sharedPreferences.getString(AUDITION_SHARE_URL, "");
    }

    public void setAuditionShareUrl(String auditionShareURL) {
        editor.putString(AUDITION_SHARE_URL, auditionShareURL);
        editor.commit();
    }

    public void setHomeOverlay(String homeOverlay) {
        editor.putString(HOME_OVERLAY, homeOverlay);
        editor.commit();
    }

    public String getHomeOverlay() {
        return sharedPreferences.getString(HOME_OVERLAY, "");
    }

    public void setHistoryOverlay(String historyOverlay) {
        editor.putString(HISTORY_OVERLAY, historyOverlay);
        editor.commit();
    }

    public String getHistoryOverlay() {
        return sharedPreferences.getString(HISTORY_OVERLAY, "");
    }

    public void setFirstUploadAudition(String auditionUpload) {
        editor.putString(UPLOAD_FIRST_AUDITION, auditionUpload);
        editor.commit();
    }

    public String getFirstUploadAudition() {
        return sharedPreferences.getString(UPLOAD_FIRST_AUDITION, "");
    }

    public void setUploadFileCounter(String uploadedFileCounter) {
        editor.putString(UploadedFileCounter, uploadedFileCounter);
        editor.commit();
    }

    public String getUploadFileCounter() {
        return sharedPreferences.getString(UploadedFileCounter, "");
    }

    public void setUploadFailInterview(String UploadFailInterview) {
        editor.putString(FAIL_INTERVIEW, UploadFailInterview);
        editor.commit();
    }

    public String getUploadFailInterview() {
        return sharedPreferences.getString(FAIL_INTERVIEW, "");
    }

    public void setPaytm(String paytm) {
        editor.putString(PAYTM, paytm);
        editor.commit();
    }

    public String getPaytm() {
        return sharedPreferences.getString(PAYTM, "");
    }

    public void setInvite(String invites) {
        editor.putString(INVITES, invites);
        editor.commit();
    }

    public String getInvite() {
        return sharedPreferences.getString(INVITES, "");
    }

    public void setDeepLink(String deepLink) {
        editor.putString(DEEP_LINK, deepLink);
        editor.commit();
    }

    public String getDeepLink() {
        return sharedPreferences.getString(DEEP_LINK, "");
    }

    public void setNotificationType(String type) {
        editor.putString(NOTIFICATION_TYPE, type);
        editor.commit();
    }

    public String getNotificationType() {
        return sharedPreferences.getString(NOTIFICATION_TYPE, "");
    }

    public void setNotificationCode(String code) {
        editor.putString(NOTIFICATION_JOBCODE, code);
        editor.commit();
    }

    public String getNotificationCode() {
        return sharedPreferences.getString(NOTIFICATION_JOBCODE, "");
    }
}

