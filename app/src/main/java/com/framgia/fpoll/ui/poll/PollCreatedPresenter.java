package com.framgia.fpoll.ui.poll;

/**
 * Created by tuanbg on 2/21/17.
 * <></>
 */
public class PollCreatedPresenter implements PollCreatedContract.Presenter {
    private PollCreatedContract.View mView;

    public PollCreatedPresenter(PollCreatedContract.View view) {
        this.mView = view;
    }

    @Override
    public void copyLinkInvite() {
        mView.copyLinkInvite();
    }

    @Override
    public void viewLinkInvite(String idPoll) {
        // TODO: 2/21/17 get id poll from server and change to manager link
    }

    @Override
    public void resendEmail() {
        // TODO: 2/21/17 request server resend email
    }

    @Override
    public void copyLinkManager() {
        mView.copyLinkManager();
    }

    @Override
    public void viewLinkManager() {
        mView.startUiPollManager();
    }
}
