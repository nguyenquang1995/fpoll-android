package com.framgia.fpoll.ui.introduction;

import com.framgia.fpoll.data.model.IntroduceItem;
import com.framgia.fpoll.ui.base.BaseView;

import java.util.List;

/**
 * Created by tuanbg on 2/22/17.
 */
public interface IntroduceAppContract {
    interface View extends BaseView {
        void openFaceBook();
        void openGitHub();
        void openLikeDin();
        void updateIntroduceView(List<IntroduceItem> list);
        void updateIntroduceError();
        void nextActivity();
    }

    interface Presenter {
        void openFacebook();
        void openGitHub();
        void openLikeDin();
    }
}
