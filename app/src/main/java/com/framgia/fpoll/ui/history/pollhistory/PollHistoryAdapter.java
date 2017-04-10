package com.framgia.fpoll.ui.history.pollhistory;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.poll.HistoryPoll;
import com.framgia.fpoll.databinding.ItemPollHistoryBinding;
import com.framgia.fpoll.ui.history.PollHistoryType;
import java.util.ArrayList;
import java.util.List;

public class PollHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<HistoryPoll> mListPollHistory = new ArrayList<>();
    private PollHistoryType mHistoryType;
    private PollHistoryContract.Presenter mPresenter;

    public PollHistoryAdapter(List<HistoryPoll> pollHistories, PollHistoryType pollHistoryType,
            PollHistoryContract.Presenter presenter) {
        mHistoryType = pollHistoryType;
        mListPollHistory.addAll(pollHistories);
        mPresenter = presenter;
    }

    public void update(List<HistoryPoll> pollHistories) {
        mListPollHistory.clear();
        mListPollHistory.addAll(pollHistories);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) mInflater = LayoutInflater.from(parent.getContext());
        ItemPollHistoryBinding binding =
                DataBindingUtil.inflate(mInflater, R.layout.item_poll_history, parent, false);
        binding.setHandler(new PollHistoryHandler(mPresenter));
        switch (mHistoryType) {
            case CLOSE:
                binding.setTitle(parent.getContext().getString(R.string.title_re_open));
                binding.setReopen(true);
                break;
            case INITIATE:
            case PARTICIPATE:
            default:
                binding.setTitle(parent.getContext().getString(R.string.msg_link));
                binding.setReopen(false);
        }
        return new PollHistoryHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PollHistoryHolder pollHistoryHolder = (PollHistoryHolder) holder;
        HistoryPoll item = mListPollHistory.get(position);
        if (item != null) pollHistoryHolder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mListPollHistory == null ? 0 : mListPollHistory.size();
    }

    public class PollHistoryHolder extends RecyclerView.ViewHolder {
        private ItemPollHistoryBinding mBinding;

        public PollHistoryHolder(ItemPollHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(HistoryPoll item) {
            mBinding.setPollHistory(item);
            mBinding.executePendingBindings();
        }
    }
}
