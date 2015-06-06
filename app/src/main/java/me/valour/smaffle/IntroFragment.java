package me.valour.smaffle;


import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroFragment extends Fragment {

    private EditText input_search_tag;
    private Button btn_search;
    private String searchTag = null;
    private boolean lucky = true;

    public static IntroFragment newInstance() {
        IntroFragment fragment = new IntroFragment();

        return fragment;
    }

    public IntroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_the, container, false);
        input_search_tag = (EditText) rootView.findViewById(R.id.input_search_tag);
        btn_search = (Button) rootView.findViewById(R.id.btn_search);

        /*
        input_search_tag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    updateSearchButtonText();
                }
            }
        });
        */

        input_search_tag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                updateSearchButtonText();

                return true;
            }
        });

        return rootView;
    }


    private void updateSearchButtonText(){
        searchTag = input_search_tag.getText().toString();
        if(searchTag.isEmpty()){
            if(!lucky) {
                btn_search.setText(R.string.btnlabel_find_anything);
            }
            lucky = true;
        } else {
            if(lucky) {
                btn_search.setText(R.string.btnlabel_find_tag);
            }
            lucky = false;
        }
    }


}
