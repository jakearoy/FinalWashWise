package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WashFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NumberPicker numberPicker;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WashFragment newInstance(String param1, String param2) {
        WashFragment fragment = new WashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button nextButton = view.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(this);

        numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        numberPicker.setValue(1);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateEstimatedPrice(newVal);

            }
        });

    }


    public void updateEstimatedPrice(int quantity) {
        TextView estimatedPrice = getView().findViewById(R.id.estimated_price);
        int price = quantity * 15;
        estimatedPrice.setText("£" + String.valueOf(price));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                int washQuantity = numberPicker.getValue();
                callDeliveryScreen(washQuantity);
                break;
            default:
                break;
        }
    }

    public void callDeliveryScreen(int washQuantity) {
        Intent intent = new Intent(getContext(), deliveryScreen.class);
        intent.putExtra("WASH_QUANTITY", washQuantity);
        startActivity(intent);
    }

}