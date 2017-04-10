package com.framgia.fpoll.ui.authenication.activity;

import com.framgia.fpoll.ui.base.BaseView;

/**
 * Created by Nhahv0902 on 2/20/2017.
 * <></>
 */
public interface AuthenticationContract {
    interface View extends BaseView {
        void nextActivity();
    }

    interface Presenter {
    }
}
