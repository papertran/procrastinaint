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
    // Initializes the items on the page, will be assigned when View is created
    private Button loginRegisterButton;
    private Button EventAdderButton;
    private MainFragmentListener mListener;
    private final String TAG = MainFragment.class.getCanonicalName();

    // Interface to allow for MainActivity to switch between fragments
    public interface MainFragmentListener{
        void onLoginRegisterButtonClicked();
        void onEventAdderButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // This inflates the view
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // This assigns the loginButton and assigns a listener for when the user clicks on it
        loginRegisterButton = rootView.findViewById(R.id.loginRegisterButton);
        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Login Button Called");
                mListener.onLoginRegisterButtonClicked();

            }
        });

        EventAdderButton = rootView.findViewById(R.id.AddEventButton);
        EventAdderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Log.i(TAG, "onClick: Event Add Button Called");
                mListener.onEventAdderButtonClicked();
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
