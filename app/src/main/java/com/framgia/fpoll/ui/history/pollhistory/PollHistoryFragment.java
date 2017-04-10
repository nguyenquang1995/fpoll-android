package com.framgia.fpoll.ui.history.pollhistory;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.poll.HistoryPoll;
import com.framgia.fpoll.data.source.remote.pollmanager.ManagerRepository;
import com.framgia.fpoll.databinding.FragmentPollHistoryBinding;
import com.framgia.fpoll.ui.history.PollHistoryType;
import com.framgia.fpoll.ui.history.dialog.ClosedDialogFragment;
import com.framgia.fpoll.ui.pollmanage.ManagePollActivity;
import com.framgia.fpoll.ui.votemanager.LinkVoteActivity;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.SharePreferenceUtil;
import com.framgia.fpoll.widget.FPollProgressDialog;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_CODE_RESULT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollHistoryFragment extends Fragment implements PollHistoryContract.View {
    private FragmentPollHistoryBinding mBinding;
    private List<HistoryPoll> mListPollHistory = new ArrayList<>();
    private ObservableField<PollHistoryAdapter> mAdapter = new ObservableField<>();
    private ObservableBoolean mLoadFinish = new ObservableBoolean();
    private PollHistoryContract.Presenter mPresenter;
    private PollHistoryType mPollHistoryType;
    private FPollProgressDialog mDialog;

    public static PollHistoryFragment getInstance(PollHistoryType typeHistory) {
        PollHistoryFragment fragment = new PollHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.BundleConstant.BUNDLE_TYPE_HISTORY, typeHistory);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPollHistoryType = (PollHistoryType) bundle.getSerializable(
                    Constant.BundleConstant.BUNDLE_TYPE_HISTORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_poll_history, container, false);
        getDataFromActivity();
        mPresenter = new PollHistoryPresenter(this, mPollHistoryType,
                ManagerRepository.getInstance(getActivity()),
                SharePreferenceUtil.getIntances(getActivity()));
        mBinding.setPresenter((PollHistoryPresenter) mPresenter);
        mBinding.setFragment(this);
        mAdapter.set(new PollHistoryAdapter(mListPollHistory, mPollHistoryType, mPresenter));
        setPollHistory(mListPollHistory);
        if (SharePreferenceUtil.getIntances(getContext()).isLogin()) {
            mBinding.emptyLayout.setVisibility(View.GONE);
        }
        mPresenter.getData();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESULT && resultCode == RESULT_OK) mPresenter.getData();
    }

    @Override
    public void start() {
    }

    @Override
    public void setPollHistory(List<HistoryPoll> pollHistories) {
        mListPollHistory.clear();
        mListPollHistory.addAll(pollHistories);
        if (mListPollHistory.size() == 0) {
            mBinding.emptyLayout.setVisibility(View.VISIBLE);
            boolean isLogin = SharePreferenceUtil.getIntances(getContext()).isLogin();
            String message = getContext().getString(R.string.no_item_need_login);
            if (isLogin && mPollHistoryType.equals(PollHistoryType.CLOSE)) {
                message = getContext().getString(R.string.no_item_closed);
                mBinding.emptyLayout.setMessage(message);
                return;
            }
            if (isLogin) {
                message = getContext().getString(R.string.no_item);
                mBinding.emptyLayout.setMessage(message);
                return;
            }
            mBinding.emptyLayout.setMessage(message);
            return;
        }
        mBinding.emptyLayout.setVisibility(View.GONE);
        mAdapter.get().update(mListPollHistory);
    }

    public void updatePollHistory(HistoryPoll poll) {
        if (mListPollHistory == null || poll == null) return;
        if (mPollHistoryType == PollHistoryType.INITIATE) {
            mListPollHistory.add(0, poll);
            mAdapter.get().update(mListPollHistory);
        }
    }

    @Override
    public void onOpenManagerPollClick(String token) {
        startActivityForResult(ManagePollActivity.getTokenIntent(getActivity(), token),
                REQUEST_CODE_RESULT);
    }

    @Override
    public void onOpenVoteClick(String token) {
        startActivity(LinkVoteActivity.getTokenIntent(getActivity(), token));
    }

    public void clearData() {
        if (mListPollHistory != null && mAdapter != null && mAdapter.get() != null) {
            mListPollHistory.clear();
            setPollHistory(mListPollHistory);
            mAdapter.get().update(mListPollHistory);
        }
    }

    @Override
    public void showPollClosedDialog() {
        if (getChildFragmentManager() != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            ClosedDialogFragment.newInstance().show(transaction, "");
        }
    }

    @Override
    public void showMessage(int res) {
        ActivityUtil.showToast(getActivity(), res);
    }

    @Override
    public void showMessage(String res) {
        ActivityUtil.showToast(getActivity(), res);
    }

    @Override
    public void showDialog() {
        mBinding.emptyLayout.setVisibility(View.GONE);
        if (mDialog == null) mDialog = new FPollProgressDialog(getActivity());
        mDialog.show();
    }

    @Override
    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    public void setLoadingTrue() {
        mLoadFinish.set(true);
    }

    @Override
    public void setLoadingFalse() {
        mLoadFinish.set(false);
    }

    public ObservableField<PollHistoryAdapter> getAdapter() {
        return mAdapter;
    }

    public ObservableBoolean getLoadFinish() {
        return mLoadFinish;
    }

    public void showConfirmDialog(final HistoryPoll historyPoll) {
        AlertDialog.Builder alertBuilder =
                new AlertDialog.Builder(getActivity()).setCancelable(true)
                        .setTitle(R.string.title_re_open)
                        .setMessage(R.string.msg_reopen)
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                    public void onClick(DialogInterface dialog, int which) {
                                        mPresenter.reopenPoll(historyPoll);
                                    }
                                })
                        .setNegativeButton(android.R.string.no, null);
        alertBuilder.show();
    }
}
