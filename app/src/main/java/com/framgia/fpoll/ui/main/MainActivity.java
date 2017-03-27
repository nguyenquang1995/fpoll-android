package com.framgia.fpoll.ui.main;

import android.app.ProgressDialog;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.data.source.remote.login.LoginRepository;
import com.framgia.fpoll.data.source.remote.settings.SettingRepository;
import com.framgia.fpoll.databinding.ActivityMainBinding;
import com.framgia.fpoll.ui.authenication.activity.AuthenticationActivity;
import com.framgia.fpoll.ui.editprofile.EditProfileActivity;
import com.framgia.fpoll.ui.feedback.FeedbackFragment;
import com.framgia.fpoll.ui.history.HistoryFragment;
import com.framgia.fpoll.ui.history.ViewpagerType;
import com.framgia.fpoll.ui.introduction.IntroduceAppFragment;
import com.framgia.fpoll.ui.pollcreation.infomation.CreatePollFragment;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.LanguageUtil;
import com.framgia.fpoll.util.SharePreferenceUtil;

import static com.framgia.fpoll.util.Constant.BundleConstant.BUNDLE_POLL_ITEM;
import static com.framgia.fpoll.util.Constant.Language.LANGUAGE_EN;
import static com.framgia.fpoll.util.Constant.Language.LANGUAGE_JP;
import static com.framgia.fpoll.util.Constant.Language.LANGUAGE_VN;
import static com.framgia.fpoll.util.Constant.RequestCode.REQUEST_LOGIN;

public class MainActivity extends AppCompatActivity
    implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {
    private static final int NO_ANIMATION = 0;
    private MainContract.Presenter mPresenter;
    private ActivityMainBinding mBinding;
    private DrawerLayout mDrawerLayout;
    private PollItem mPoll = new PollItem();
    private ProgressDialog mProgressDialog;

    public static Intent getMainIntent(Context context, PollItem data) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_POLL_ITEM, data);
        intent.putExtras(bundle);
        return intent;
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getParcelable(BUNDLE_POLL_ITEM) == null) return;
        mPoll = bundle.getParcelable(BUNDLE_POLL_ITEM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getDataFromIntent();
        mPresenter = new MainPresenter(this, LoginRepository.getInstance(getApplicationContext())
            , SettingRepository.getInstance(getApplicationContext()),
            SharePreferenceUtil.getIntances(this));
        mBinding.setPresenter((MainPresenter) mPresenter);
        LanguageUtil.loadLocale(this);
    }

    @Override
    public void start() {
        Toolbar toolbar = mBinding.toolbarLayout.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = mBinding.drawerLayout;
        ActionBarDrawerToggle toggle =
            new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.action_open,
                R.string.action_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mBinding.navView.setNavigationItemSelectedListener(this);
        addFragment(CreatePollFragment.newInstance(mPoll), R.string.title_home);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.msg_loading));
        }
        if (!mProgressDialog.isShowing()) mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.hide();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_guide:
                showHelp();
                break;
            case R.id.action_history:
                addFragment(HistoryFragment.newInstance(ViewpagerType.HISTORY, null, ""),
                    R.string.title_history);
                break;
            case R.id.action_feedback:
                addFragment(FeedbackFragment.newInstance(), R.string.title_feedback);
                break;
            case R.id.action_login:
                startActivityForResult(AuthenticationActivity.getAuthenticationIntent(this),
                    REQUEST_LOGIN);
                break;
            case R.id.action_home:
                addFragment(CreatePollFragment.newInstance(mPoll), R.string.title_home);
                break;
            case R.id.action_introduce:
                addFragment(IntroduceAppFragment.newInstance(), R.string.title_introduce_app);
                break;
            case R.id.action_log_out:
                mPresenter.logout();
                break;
            case R.id.action_english:
                String lang = LANGUAGE_EN;
                showConfirmDialog(lang);
                break;
            case R.id.action_vietnamese:
                lang = LANGUAGE_VN;
                showConfirmDialog(lang);
                break;
            case R.id.action_japanese:
                lang = LANGUAGE_JP;
                showConfirmDialog(lang);
                break;
            default:
                addFragment(CreatePollFragment.newInstance(mPoll), R.string.title_home);
                break;
        }
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeLang(String lang) {
        mPresenter.changeLanguage(lang);
        LanguageUtil.changeLang(lang, MainActivity.this);
        finish();
        overridePendingTransition(NO_ANIMATION, NO_ANIMATION);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    public void addFragment(Fragment fragment, int title) {
        ActivityUtil
            .addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frame_layout);
        setTitle(title);
    }

    @Override
    public void showMessage(String msg) {
        ActivityUtil.showToast(getApplicationContext(), msg);
    }

    @Override
    public void showHelp() {
        Uri helpUri = Uri.parse(Constant.WebUrl.HELP_URL);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.color_teal_500))
            .setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.color_teal_800))
            .build().launchUrl(this, helpUri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            addFragment(CreatePollFragment.newInstance(mPoll), R.string.title_home);
            mPresenter.setInformation();
            openNavigation();
        }
    }

    private void openNavigation() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (fragment instanceof CreatePollFragment) super.onBackPressed();
            else addFragment(CreatePollFragment.newInstance(mPoll), R.string.title_home);
        }
    }

    @Override
    public void startUiProfileEdition() {
        startActivity(EditProfileActivity.getProfileEditionIntent(this));
    }

    @Override
    public void changeLangStatus(String message) {
        ActivityUtil.showToast(this, message);
    }

    private void showConfirmDialog(final String lang) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
            .setCancelable(true)
            .setTitle(R.string.title_change_language)
            .setMessage(R.string.msg_change_language)
            .setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        changeLang(lang);
                    }
                })
            .setNegativeButton(android.R.string.no, null);
        alertBuilder.show();
    }
}
