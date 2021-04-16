package edu.neu.madcourse.welink.chat;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.neu.madcourse.welink.R;

public class DeprecatedEmojiSelectionsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_emoji_selections, container, false);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            ((ButtonCallback) getActivity()).launchAction(b.getText().toString());
        }
    };

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.button1).setOnClickListener(listener);
        view.findViewById(R.id.button2).setOnClickListener(listener);
        view.findViewById(R.id.button3).setOnClickListener(listener);
        view.findViewById(R.id.button4).setOnClickListener(listener);
        view.findViewById(R.id.button5).setOnClickListener(listener);
        view.findViewById(R.id.button6).setOnClickListener(listener);
        view.findViewById(R.id.button7).setOnClickListener(listener);
        view.findViewById(R.id.button8).setOnClickListener(listener);
        view.findViewById(R.id.button9).setOnClickListener(listener);
    }


    public interface ButtonCallback {
        void launchAction(String selected);
    }

}