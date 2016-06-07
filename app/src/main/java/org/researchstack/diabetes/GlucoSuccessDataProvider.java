package org.researchstack.diabetes;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import android.content.Context;

import com.google.gson.Gson;

import rx.Observable;
import rx.Subscriber;


import org.researchstack.backbone.storage.file.StorageAccessException;
import org.researchstack.diabetes.bridge.UserSessionInfo;
import org.researchstack.skin.task.SmartSurveyTask;
import org.researchstack.backbone.storage.database.AppDatabase;
import org.researchstack.backbone.result.TaskResult;
import org.researchstack.backbone.task.Task;
import org.researchstack.backbone.utils.LogExt;
import org.researchstack.backbone.StorageAccess;
import org.researchstack.skin.DataProvider;
import org.researchstack.skin.DataResponse;
import org.researchstack.skin.ResourceManager;
import org.researchstack.skin.model.User;
import org.researchstack.skin.model.TaskModel;
import org.researchstack.skin.model.SchedulesAndTasksModel;
import org.researchstack.skin.schedule.ScheduleHelper;



/**
 * A dummy server implementation
 * Created by Dario Salvi on 01/03/2016.
 */
public class GlucoSuccessDataProvider extends DataProvider {
    public static final String TEMP_CONSENT_JSON_FILE_NAME = "/consent_sig";
    public static final String USER_SESSION_PATH           = "/user_session";
    public static final String USER_PATH                   = "/user";

    protected UserSessionInfo userSessionInfo;
    protected Gson gson     = new Gson();
    protected static boolean signedIn = false;



    Observable<DataResponse> obs;

    @Override
    public Observable<DataResponse> initialize(Context context) {
        obs = Observable.create(new Observable.OnSubscribe<DataResponse>() {

            @Override
            public void call(Subscriber<? super DataResponse> subscriber) {
                subscriber.onNext(new DataResponse(true, null));
            }
        });

        return obs;
    }

    @Override
    public Observable<DataResponse> signUp(Context context, String s, String s1, String s2) {
        LogExt.i(GlucoSuccessDataProvider.class, "User signing up");
        return obs;
    }

    @Override
    public Observable<DataResponse> signIn(Context context, String s, String s1) {
        LogExt.i(GlucoSuccessDataProvider.class, "User signing in");
        signedIn = true;
        return obs;
    }

    @Override
    public Observable<DataResponse> signOut(Context context) {
        LogExt.i(GlucoSuccessDataProvider.class, "User signing out");
        return obs;
    }

    @Override
    public Observable<DataResponse> resendEmailVerification(Context context, String s) {
        LogExt.i(GlucoSuccessDataProvider.class, "User asking to resend email");
        return obs;
    }

    @Override
    public boolean isSignedUp(Context context) {
        User user = loadUser(context);
        return user != null && user.getEmail() != null;    }

    @Override
    public boolean isSignedIn(Context context) {
        return signedIn;
    }

    @Override
    public boolean isConsented(Context context) {
        return userSessionInfo.isConsented() || StorageAccess.getInstance()
                .getFileAccess()
                .dataExists(context, TEMP_CONSENT_JSON_FILE_NAME);
    }

    @Override
    public Observable<DataResponse> withdrawConsent(Context context, String reason) {
        LogExt.i(GlucoSuccessDataProvider.class, "Withdrawing consent");
        return obs;
    }

    @Override
    public void uploadConsent(Context context, TaskResult consentResult) {
        LogExt.i(GlucoSuccessDataProvider.class, "Consent uploaded");
    }

    @Override
    public void saveConsent(Context context, TaskResult consentResult) {
        LogExt.i(GlucoSuccessDataProvider.class, "Consent saved");
    }

    @Override
    public User getUser(Context context) {
        User dummyUser = new User();
        dummyUser.setName("Dummy Boy");
        dummyUser.setEmail("dummy@dyummy.com");
        dummyUser.setBirthDate("22/10/1974");

        return dummyUser;
    }

    private User loadUser(Context context)
    {
        try
        {
            String user = loadJsonString(context, USER_PATH);
            return gson.fromJson(user, User.class);
        }
        catch(StorageAccessException e)
        {
            return null;
        }
    }

    private String loadJsonString(Context context, String path)
    {
        return new String(StorageAccess.getInstance().getFileAccess().readData(context, path));
    }

    @Override
    public String getUserSharingScope(Context context) {
        return "here";
    }

    @Override
    public void setUserSharingScope(Context context, String s) {
    }

    @Override
    public String getUserEmail(Context context) {
        return "dummy@dyummy.com";
    }

    @Override
    public void uploadTaskResult(Context context, TaskResult taskResult) {

    }

    @Override
    public SchedulesAndTasksModel loadTasksAndSchedules(Context context) {
        SchedulesAndTasksModel schedulesAndTasksModel = ResourceManager.getInstance().getTasksAndSchedules().create(context);

        AppDatabase db = StorageAccess.getInstance().getAppDatabase();

        List<SchedulesAndTasksModel.ScheduleModel> schedules = new ArrayList<>();
        for (SchedulesAndTasksModel.ScheduleModel schedule : schedulesAndTasksModel.schedules) {

            SchedulesAndTasksModel.TaskScheduleModel task = schedule.tasks.get(0);
            LogExt.d(GlucoSuccessDataProvider.class, "Loading task " + task.taskClassName);

            String resultstaskid = null;

            if (task.taskClassName.equalsIgnoreCase("APCGenericSurveyTaskViewController")) {
                TaskModel taskModel = ResourceManager.getInstance().getTask(task.taskFileName).create(context);
                resultstaskid = taskModel.identifier;
            } else resultstaskid = task.taskID;
            if (resultstaskid != null) {
                TaskResult result = db.loadLatestTaskResult(resultstaskid);

                if (result == null) {
                    schedules.add(schedule);
                } else if (StringUtils.isNotEmpty(schedule.scheduleString)) {
                    Date date = ScheduleHelper.nextSchedule(schedule.scheduleString, result.getEndDate());
                    if (date.before(new Date())) {
                        schedules.add(schedule);
                    }
                }
            }
        }

        schedulesAndTasksModel.schedules = schedules;
        return schedulesAndTasksModel;
    }


    @Override
    public Task loadTask(Context context, SchedulesAndTasksModel.TaskScheduleModel task) {
        if (task.taskClassName.equalsIgnoreCase("APCGenericSurveyTaskViewController")) {
            TaskModel taskModel = ResourceManager.getInstance().getTask(task.taskFileName).create(context);
            SmartSurveyTask smartSurveyTask = new SmartSurveyTask(context, taskModel);
            return smartSurveyTask;

        }
        /*
        else if(task.taskClassName.equalsIgnoreCase(YOUR CUSTOM TASK)){
            return new YOUR CUSTOM TASK IMPLEMENTATION;
        }
        */
        else {
            LogExt.e(GlucoSuccessDataProvider.class, "Task with classname " + task.taskClassName + "cannot be found");
            return null;
        }
    }


    @Override
    public void processInitialTaskResult(Context context, TaskResult taskResult) {

    }

    @Override
    public Observable<DataResponse> forgotPassword(Context context, String email) {
        LogExt.i(GlucoSuccessDataProvider.class, "Forgot password");
        return obs;
    }

}