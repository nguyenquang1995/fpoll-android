package com.framgia.fpoll.ui.pollmanage.information;

/**
 * Created by Nhahv0902 on 2/24/2017.
 * <></>
 */
public class PollInformationHandler {
    private PollInformationContract.Presenter mListener;

    public PollInformationHandler(
        PollInformationContract.Presenter listener) {
        mListener = listener;
    }

    public void clickLinkVote() {
        if (mListener == null) return;
        mListener.clickLinkVote();
    }

    public void clickViewOption() {
        if (mListener == null) return;
        mListener.clickViewOption();
    }

    public void clickViewSetting() {
        if (mListener == null) return;
        mListener.clickViewSetting();
    }

    public void showDatePicker() {
        if (mListener != null) mListener.showDatePicker();
    }

    public void saveData() {
        if (mListener != null) mListener.saveData();
    }
}
