/*
 * Copyright (c) 2017  Ni YueMing<niyueming@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */

package net.nym.rxbuslibrary;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

/**
 * @author niyueming
 * @date 2017-03-06
 * @time 17:32
 */

public class RxBus {
    private static final RxBus INSTANCE = new RxBus();

    private final PublishProcessor<Object> mBusSubject = PublishProcessor.create();

    public static RxBus getInstance() {
        return INSTANCE;
    }

    public <T> Disposable register(final Class<T> eventClass, Consumer<T> onNext) {
        return mBusSubject
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        return o.getClass().equals(eventClass);
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(Object o) throws Exception {
                        return (T) o;
                    }
                })
                .subscribe(onNext);
    }

    public <T> Disposable registerOnMainThread(final Class<T> eventClass, Consumer<T> onNext) {
        return mBusSubject
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object o) throws Exception {
                        return o.getClass().equals(eventClass);
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(Object o) throws Exception {
                        return (T) o;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
    }

    /**
     *
     * @param event
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }
}
