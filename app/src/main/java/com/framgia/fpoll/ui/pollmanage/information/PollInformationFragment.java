package com.framgia.fpoll.ui.pollmanage.information;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.databinding.FragmentInformationBinding;
import com.framgia.fpoll.ui.pollmanage.information.pollsetting.PollSettingDialogFragment;
import com.framgia.fpoll.ui.pollmanage.information.viewoption.PollOptionDialogFragment;
import com.framgia.fpoll.ui.votemanager.LinkVoteActivity;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollInformationFragment extends Fragment
    implements PollInformationContract.View
    , DatePickerDialog.OnDateSetListener
    , TimePickerDialog.OnTimeSetListener {
    private FragmentInformationBinding mBinding;
    private PollInformationContract.Presenter mPresenter;
    private DataInfoItem mPollInfo;
    public final ObservableField<Calendar> mTime = new ObservableField<>(Calendar.getInstance());
    private Calendar mSavePickCalendar = Calendar.getInstance();

    public static PollInformationFragment newInstance(DataInfoItem pollInfo) {
        PollInformationFragment fragment = new PollInformationFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.ConstantApi.KEY_POLL_INFO, pollInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_information, container, false);
        getData();
        mPresenter = new PollInformationPresenter(this);
        mBinding.setFragment(this);
        mBinding.setHandler(new PollInformationHandler(mPresenter));
        mBinding.setInformation(mPollInfo);
        return mBinding.getRoot();
    }

    public void getData() {
        Bundle bundle = getArguments();
        if (bundle != null) mPollInfo = bundle.getParcelable(Constant.ConstantApi.KEY_POLL_INFO);
    }

    @Override
    public void start() {
    }

    @Override
    public void startUiVoting() {
        startActivity(new Intent(getContext(), LinkVoteActivity.class));
    }

    @Override
    public void showDialogOption() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DialogFragment optionDialog =
            PollOptionDialogFragment.newInstance(mPollInfo.getPoll().getOptions());
        optionDialog.show(transaction, Constant.TYPE_DIALOG_FRAGMENT);
    }

    @Override
    public void showDialogSetting() {
        if (mPollInfo == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DialogFragment optionDialog =
            PollSettingDialogFragment.newInstance(mPollInfo.getPoll().getSettings());
        optionDialog.show(transaction, Constant.TYPE_DIALOG_FRAGMENT);
    }

    @Override
    public void showDatePicker() {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
            this,
            mTime.get().get(Calendar.YEAR),
            mTime.get().get(Calendar.MONTH),
            mTime.get().get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), Constant.Tag.DATE_PICKER_TAG);
    }

    @Override
    public void showTimePicker() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
            this,
            mTime.get().get(Calendar.HOUR_OF_DAY),
            mTime.get().get(Calendar.MINUTE),
            mTime.get().get(Calendar.SECOND),
            true
        );
        timePickerDialog.show(getActivity().getFragmentManager(), Constant.Tag.TIME_PICKER_TAG);
    }

    @Override
    public void bindError() {
        mBinding.setMsgError(getString(R.string.msg_content_error));
        mBinding.setMsgErrorEmail(getString(R.string.msg_email_invalidate));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mSavePickCalendar.set(Calendar.YEAR, year);
        mSavePickCalendar.set(Calendar.MONTH, monthOfYear);
        mSavePickCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        showTimePicker();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        mSavePickCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mSavePickCalendar.set(Calendar.MINUTE, minute);
        mSavePickCalendar.set(Calendar.SECOND, second);
        if (mSavePickCalendar.before(Calendar.getInstance())) {
            ActivityUtil.showToast(getContext(), R.string.msg_date_error);
        } else mTime.notifyChange();
    }
}
