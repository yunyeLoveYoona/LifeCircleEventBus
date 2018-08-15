package com.bus.lifecircleeventbus;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 带生命周期的EventBus
 */
public class LifeCircleEventBus {

    private static final HashMap<Object, Observable> observableHashMap = new HashMap<Object, Observable>();
    private static Class topAcClass;
    private static final HashMap<Class, ArrayList<HashMap<ArrayList<Observer>, Object>>> cacheMap = new HashMap<Class, ArrayList<HashMap<ArrayList<Observer>, Object>>>();

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                topAcClass = activity.getClass();
                if (cacheMap.get(topAcClass) != null) {
                    for (HashMap<ArrayList<Observer>, Object> temp : cacheMap.get(topAcClass)) {
                        Iterator map1it = temp.entrySet().iterator();
                        while (map1it.hasNext()) {
                            Map.Entry<ArrayList<Observer>, Object> entry = (Map.Entry<ArrayList<Observer>, Object>) map1it.next();
                            if (entry.getKey() != null) {
                                for (Observer obTemp : entry.getKey()) {
                                    obTemp.onchange(entry.getValue());
                                }
                            }
                        }
                    }
                }
                cacheMap.remove(topAcClass);
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                cacheMap.remove(activity.getClass());
                Iterator map1it = observableHashMap.entrySet().iterator();
                while (map1it.hasNext()) {
                    Map.Entry<Object, Observable> entry = (Map.Entry<Object, Observable>) map1it.next();
                    entry.getValue().removeAllObserve(activity.getClass());
                }
            }
        });
    }


    public static Observable with(Object object) {
        Observable observable;
        if (observableHashMap.get(object) == null) {
            observable = new Observable();
            observableHashMap.put(object, observable);
        } else {
            observable = observableHashMap.get(object);
        }
        return observable;
    }


    public interface Observer<T> {
        public void onchange(T value);
    }


    public static class Observable {
        private HashMap<Class<? extends Activity>, ArrayList<Observer>> observerHashMap;

        public Observable() {
            observerHashMap = new HashMap<Class<? extends Activity>, ArrayList<Observer>>();
        }

        public void observe(Class<? extends Activity> acClass, Observer observer) {
            if (observerHashMap.get(acClass) == null) {
                ArrayList<Observer> observers = new ArrayList<Observer>();
                observers.add(observer);
                observerHashMap.put(acClass, observers);
            } else {
                observerHashMap.get(acClass).add(observer);
            }
        }

        public void observeForever(Observer observer) {
            if (observerHashMap.get(null) == null) {
                ArrayList<Observer> observers = new ArrayList<Observer>();
                observers.add(observer);
                observerHashMap.put(null, observers);
            } else {
                observerHashMap.get(null).add(observer);
            }
        }

        public void removeObserveForever(Observer observer) {
            if (observerHashMap.get(null) != null)
                observerHashMap.get(null).remove(observer);
        }

        public void removeObserve(Class<? extends Activity> acClass, Observer observer) {
            if (observerHashMap.get(acClass) != null)
                observerHashMap.get(acClass).remove(observer);
        }

        public void removeAllObserve(Class<? extends Activity> acClass) {
            observerHashMap.remove(acClass);
        }

        public void setValue(Object object) {
            Iterator map1it = observerHashMap.entrySet().iterator();
            while (map1it.hasNext()) {
                Map.Entry<Class<? extends Activity>, ArrayList<Observer>> entry = (Map.Entry<Class<? extends Activity>, ArrayList<Observer>>) map1it.next();
                ArrayList<Observer> observers = entry.getValue();
                if (entry.getKey() != null) {
                    if (entry.getKey() == topAcClass && observers != null) {
                        for (Observer observer : observers) {
                            observer.onchange(object);
                        }
                    } else {
                        HashMap<ArrayList<Observer>, Object> temp = new HashMap<ArrayList<Observer>, Object>();
                        temp.put(observers, object);
                        if (cacheMap.get(entry.getKey()) != null) {
                            cacheMap.get(entry.getKey()).add(temp);
                        } else {
                            ArrayList<HashMap<ArrayList<Observer>, Object>> tempList = new ArrayList<HashMap<ArrayList<Observer>, Object>>();
                            tempList.add(temp);
                            cacheMap.put(entry.getKey(), tempList);
                        }
                    }
                } else if (observers != null) {
                    for (Observer observer : observers) {
                        observer.onchange(object);
                    }
                }

            }
        }
    }

}
