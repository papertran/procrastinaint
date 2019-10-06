package edu.fsu.cen4020.android.procrastinaint;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    private Button loginRegisterButton;
    private MainFragmentListener mListener;
    private final String TAG = MainFragment.class.getCanonicalName();

    public interface MainFragmentListener{
        void onLoginRegisterButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        loginRegisterButton = rootView.findViewById(R.id.loginRegisterButton);

        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Login Button Called");
                mListener.onLoginRegisterButtonClicked();

            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  MainFragmentListener){
            mListener = (MainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
