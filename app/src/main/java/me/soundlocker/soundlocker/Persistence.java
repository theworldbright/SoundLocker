package me.soundlocker.soundlocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Persistence {

    public static final String PREFS_NAME = "soundlocker";
    public static final String APPLICATION_LIST = "applications";
    public static final String FIRST_BOOT = "firstBoot";
    public static final String MASTER_ID = "master_Id";
    private Gson gson = new Gson();

    public ArrayList<Application> getApplications(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(APPLICATION_LIST)) {
            String jsonFavorites = settings.getString(APPLICATION_LIST, null);
            Application[] applicationList = gson.fromJson(jsonFavorites, Application[].class);
            ArrayList<Application> applications = new ArrayList<>(Arrays.asList(applicationList));
            return applications;
        } else {
            return null;
        }
    }

    public boolean getFirstBoot(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(FIRST_BOOT)){
            return settings.getBoolean(FIRST_BOOT,false);
        } else {
            return false;
        }
    }

    public String getMasterId(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(MASTER_ID)){
            return settings.getString(MASTER_ID,null);
        } else {
            return null;
        }
    }

    public void saveApplications(Context context, List<Application> applications) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        String jsonFavorites = gson.toJson(applications);
        editor.putString(APPLICATION_LIST, jsonFavorites);
        editor.commit();
    }

    public void saveFirstBoot(Context context, boolean firstBoot) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putBoolean(FIRST_BOOT, firstBoot);
        editor.commit();
    }

    public void saveMasterId(Context context, String masterId) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putString(MASTER_ID, masterId);
        editor.commit();
    }

    /**
     * addApplication Adds a new application to the application list in storage
     * @param context The current application context
     * @param newApplication The application to add
     * @return true if successfully added
     */
    public boolean addApplication(Context context, Application newApplication) {
        List<Application> applications = getApplications(context);
        if (applications == null) {
            applications = new ArrayList<>();
        }
        for (Application application : applications) {
            if (application.applicationName.equals(newApplication.applicationName)) {
                return false;
            }
        }
        applications.add(newApplication);
        saveApplications(context, applications);
        return true;
    }

    /**
     * getApplicationPasswordLength Gets the password length for an application
     * @param context The current application context
     * @param applicationName The name of the application to update the password length for
     * @return the application's password length or -1 if not obtainable
     */
    public int getApplicationPasswordLength(Context context, String applicationName) {
        List<Application> applications = getApplications(context);
        if (applications == null) {
            applications = new ArrayList<>();
        }
        for (Application application : applications) {
            if (application.applicationName.equals(applicationName)) {
                return application.passwordLength;
            }
        }
        return -1;
    }

    /**
     * saveApplicationPasswordLength Updates the password length for an application
     * @param context The current application context
     * @param applicationName The name of the application to update the password length for
     * @param newPasswordLength The new password length for this application
     * @return true if successfully updated
     */
    public boolean saveApplicationPasswordLength(Context context, String applicationName, int newPasswordLength) {
        List<Application> applications = getApplications(context);
        for (Application application : applications) {
            if (application.applicationName.equals(applicationName)) {
                application.passwordLength = newPasswordLength;
                saveApplications(context, applications);
                return true;
            }
        }
        return false;
    }

    public void removeApplication(Context context, Application application) {
        List<Application> applications = getApplications(context);
        if (applications != null) {
            applications.remove(application);
            saveApplications(context, applications);
        }
    }
}