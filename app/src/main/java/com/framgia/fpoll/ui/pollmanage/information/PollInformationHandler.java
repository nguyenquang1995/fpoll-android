package com.framgia.fpoll.ui.pollmanage.information;

import android.view.View;

import com.framgia.fpoll.data.model.DataInfoItem;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by Nhahv0902 on 2/24/2017.
 * <></>
 */
public class PollInformationHandler {
    private PollInformationContract.Presenter mListener;

    public PollInformationHandler(PollInformationContract.Presenter listener) {
        mListener = listener;
    }

    public void clickLinkVote(FloatingActionsMenu menu) {
        if (mListener == null) return;
        menu.collapse();
        mListener.clickLinkVote();
    }

    public void clickViewOption(FloatingActionsMenu menu) {
        if (mListener == null) return;
        menu.collapse();
        mListener.clickViewOption();
    }

    public void clickViewSetting(FloatingActionsMenu menu) {
        if (mListener == null) return;
        menu.collapse();
        mListener.clickViewSetting();
    }

    public void saveInformation(int id) {
        if (mListener == null) return;
        mListener.saveInformation(id);
    }
}
