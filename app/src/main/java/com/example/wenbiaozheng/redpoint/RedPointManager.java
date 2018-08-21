package com.example.wenbiaozheng.redpoint;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.example.wenbiaozheng.redpoint.view.QBadgeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RedPointManager {
    Instance;

    private static final String TAG = "RedPointManager";
    public static final int TYPE_NORMAL = 0;
    private Map<Integer, Integer> mRedPointMap = new HashMap<>(); //ViewId对应的控件上的红点数字
    private Map<Integer, List<String>> mRedPointReasonMap = new HashMap<>(); //ViewId对应的控件上的节点名称
    private Map<Integer, QBadgeView> mRedPointViewMap = new HashMap<>(); //ViewId对应的红点View
    private Map<String, Integer> mRedPointReasonCountMap = new HashMap<>(); //节点对应的红点数字
    private Map<String, List<Integer>> mRedPointReasonBindMap = new HashMap<>(); //经过该节点的ViewId

    public void add(Activity activity, int viewId, String reason, boolean supportRepeat) {
        if (mRedPointReasonMap.containsKey(viewId)) {
            List<String> reasonList = mRedPointReasonMap.get(viewId);
            if (reasonList != null) {
                if (reasonList.contains(reason)) {
                    if(!supportRepeat) {
                        Log.i(TAG, "已经添加了红点出现的节点:" + reason);
                        return;
                    }else{
                        Log.i(TAG, "重复添加红点的节点:" + reason);
                        reasonList.add(reason);
                        mRedPointReasonMap.put(viewId, reasonList);
                        addRedPointCount(viewId);
                        addRedReasonCount(reason);
                        addRedReasonBind(reason, viewId);
                    }
                } else {
                    Log.i(TAG, "添加红点的节点:" + reason);
                    reasonList.add(reason);
                    mRedPointReasonMap.put(viewId, reasonList);
                    addRedPointCount(viewId);
                    addRedPointView(activity, viewId);
                    addRedReasonCount(reason);
                    addRedReasonBind(reason, viewId);
                }
            } else {
                Log.i(TAG, "添加红点的节点:" + reason);
                reasonList = new ArrayList<>();
                reasonList.add(reason);
                mRedPointReasonMap.put(viewId, reasonList);
                addRedPointCount(viewId);
                addRedPointView(activity, viewId);
                addRedReasonCount(reason);
                addRedReasonBind(reason, viewId);
            }
        } else {
            Log.i(TAG, "添加红点的节点:" + reason);
            List<String> reasonList = new ArrayList<>();
            reasonList.add(reason);
            mRedPointReasonMap.put(viewId, reasonList);
            addRedPointCount(viewId);
            addRedPointView(activity, viewId);
            addRedReasonCount(reason);
            addRedReasonBind(reason, viewId);
        }
    }

    private void addRedReasonBind(String reason, int viewId) {
        if(mRedPointReasonBindMap.containsKey(reason)){
            List<Integer> viewIdList = mRedPointReasonBindMap.get(reason);
            if(viewIdList != null){
                if(!viewIdList.contains(viewId)){
                    viewIdList.add(viewId);
                    mRedPointReasonBindMap.put(reason, viewIdList);
                }
            }
        }else{
            List<Integer> viewIdList = new ArrayList<>();
            viewIdList.add(viewId);
            mRedPointReasonBindMap.put(reason, viewIdList);
        }
    }

    private void addRedPointView(Activity activity, int viewId) {
        if (!mRedPointViewMap.containsKey(viewId)) {
            QBadgeView badgeView = new QBadgeView(activity);
            badgeView.setBadgeGravity(Gravity.END | Gravity.TOP);
            mRedPointViewMap.put(viewId, badgeView);
        }
    }

    private void addRedReasonCount(String reason){
        if(mRedPointReasonCountMap.containsKey(reason)){
            int count = mRedPointReasonCountMap.get(reason);
            count++;
            mRedPointReasonCountMap.put(reason, count);
        }else{
            mRedPointReasonCountMap.put(reason, 1);
        }
    }

    private void addRedPointCount(int viewId) {
        if (mRedPointMap.containsKey(viewId)) {
            int count = mRedPointMap.get(viewId);
            count++;
            mRedPointMap.put(viewId, count);
        } else {
            mRedPointMap.put(viewId, 1);
        }
    }

    public void remove(int viewId, String reason) {
        if (mRedPointMap.containsKey(viewId)) {
            int count = mRedPointMap.get(viewId);
            count--;
            if (count == 0) {
                Log.i(TAG, "这个节点:" + reason + "的红点已经为0了");
                mRedPointMap.remove(viewId);
                mRedPointReasonMap.remove(viewId);
            } else {
                mRedPointMap.put(viewId, count);
                if (mRedPointReasonMap.containsKey(viewId)) {
                    List<String> reasonList = mRedPointReasonMap.get(viewId);
                    if (reasonList != null) {
                        reasonList.remove(reason);
                        mRedPointReasonMap.put(viewId, reasonList);
                        Log.i(TAG, "这个节点:" + reason + "的红点的数量:" + count);
                    }
                }
            }
        }
    }

    public void show(Activity activity, int viewId, int type) {
        if (mRedPointMap.containsKey(viewId)) {
            int count = mRedPointMap.get(viewId);
            if (count > 0) {
                Log.i(TAG, "显示红点");
                View view = activity.findViewById(viewId);
                if (mRedPointViewMap.containsKey(viewId)) {
                    QBadgeView badgeView = mRedPointViewMap.get(viewId);
                    badgeView.bindTarget(view).setBadgeNumber(count);
                }
            } else {
                Log.i(TAG, "不显示红点");
                if (mRedPointViewMap.containsKey(viewId)) {
                    QBadgeView badgeView = mRedPointViewMap.get(viewId);
                    badgeView.hide(false);
                    mRedPointViewMap.remove(viewId);
                }
            }
        } else {
            Log.i(TAG, "不显示红点");
            if (mRedPointViewMap.containsKey(viewId)) {
                QBadgeView badgeView = mRedPointViewMap.get(viewId);
                badgeView.hide(false);
                mRedPointViewMap.remove(viewId);
            }
        }
    }

    public void hide(Activity activity, String reason, boolean isAnimate){
        if(mRedPointReasonBindMap.containsKey(reason)){
            List<Integer> bindViewIdList = mRedPointReasonBindMap.get(reason);
            for(int viewId : bindViewIdList){
                if(mRedPointReasonMap.containsKey(viewId)){
                    List<String> reasonList = mRedPointReasonMap.get(viewId);
                    if(reasonList.size() > 1){
                        reasonList.remove(reason);
                        mRedPointReasonMap.put(viewId, reasonList);
                        if(mRedPointMap.containsKey(viewId)){
                            int count = mRedPointMap.get(viewId);
                            count--;
                            mRedPointMap.put(viewId, count);
                            QBadgeView badgeView = mRedPointViewMap.get(viewId);
                            View view = activity.findViewById(viewId);
                            badgeView.setBadgeNumber(count);
                            badgeView.bindTarget(view);
                        }
                    }else{
                        if(mRedPointMap.containsKey(viewId)) {
                            int count = mRedPointMap.get(viewId);
                            count--;
                            mRedPointMap.put(viewId, count);
                        }
                        mRedPointReasonMap.remove(viewId);
                        QBadgeView badgeView = mRedPointViewMap.get(viewId);
                        badgeView.hide(isAnimate);
                        Log.i(TAG, "清除reason:" + reason + "上的所有红点");
                        if(mRedPointReasonCountMap.containsKey(reason)){
                            int count = mRedPointReasonCountMap.get(reason);
                            count--;
                            mRedPointReasonCountMap.put(reason, count);
                        }
                    }
                }
            }
        }
    }

    public int countOfReason(String reason){
         if(mRedPointReasonCountMap.containsKey(reason)){
             return mRedPointReasonCountMap.get(reason);
         }else{
             return 0;
         }
    }

    public int getViewRedPoint(int viewId){
        if(mRedPointMap.containsKey(viewId)){
            return mRedPointMap.get(viewId);
        }else{
            return 0;
        }
    }
}
