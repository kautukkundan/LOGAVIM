package androidified.logavim.fragment;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidified.logavim.R;


public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";
    Context mContext;
    FrameLayout frameLayout;


    public HelpFragment() {}
    ClipData myClip;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.helpfragmentlayout, container, false);
        frameLayout=(FrameLayout)rootView.findViewById(R.id.frame);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

