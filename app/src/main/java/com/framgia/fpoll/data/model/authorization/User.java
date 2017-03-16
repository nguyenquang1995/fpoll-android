package com.framgia.fpoll.data.model.authorization;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.framgia.fpoll.BR;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tuanbg on 2/9/17.
 */
public class User extends BaseObservable {
    @SerializedName("name")
    private String mUsername;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("gender")
    private int mGender;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("password_confirmation")
    private String mConfirmPassword;
    @SerializedName("avatar")
    private String mAvatar;
    @SerializedName("chatwork_id")
    private String mChatWorkId;
    @SerializedName("token_verification")
    private String mToken;
    @SerializedName("is_active")
    private boolean mIsActive;

    @Bindable
    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public int getGender() {
        return mGender;
    }

    public void setGender(int gender) {
        mGender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getConfirmPassword() {
        return mConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        mConfirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Bindable
    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    @Bindable
    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
        notifyPropertyChanged(BR.token);
    }

    @Bindable
    public String getChatWorkId() {
        return mChatWorkId;
    }

    public void setChatWorkId(String chatWorkId) {
        mChatWorkId = chatWorkId;
        notifyPropertyChanged(BR.chatWorkId);
    }

    @Bindable
    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
        notifyPropertyChanged(BR.active);
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
            "mUsername='" + mUsername + '\'' +
            ", mEmail='" + mEmail + '\'' +
            ", mGender=" + mGender +
            ", mPassword='" + mPassword + '\'' +
            ", mConfirmPassword='" + mConfirmPassword + '\'' +
            ", mAvatar='" + mAvatar + '\'' +
            ", mChatWorkId='" + mChatWorkId + '\'' +
            ", mToken='" + mToken + '\'' +
            '}';
    }
}
