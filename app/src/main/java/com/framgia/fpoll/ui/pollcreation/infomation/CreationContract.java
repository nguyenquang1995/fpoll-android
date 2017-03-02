package com.framgia.fpoll.ui.pollcreation.infomation;

import com.framgia.fpoll.ui.base.BaseView;

/**
 * Created by framgia on 20/02/2017.
 */
public interface CreationContract {
    interface View extends BaseView {
        void showDatePicker();
        void nextStep();
        void bindError();
    }

    interface Presenter {
        void nextStep();
        void showDatePicker();
    }
}
