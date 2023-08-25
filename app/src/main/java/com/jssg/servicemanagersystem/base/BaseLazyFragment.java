package com.jssg.servicemanagersystem.base;

public abstract class BaseLazyFragment extends BaseFragment {

    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        if (isFirstResume) {
            isFirstResume = false;
            lazyLoad();
        }
        super.onResume();
    }

    protected abstract void lazyLoad();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstResume = true;
    }
}
