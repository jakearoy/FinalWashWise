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

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IronFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IronFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NumberPicker numberPicker;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public IronFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IronFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IronFragment newInstance(String param1, String param2) {
        IronFragment fragment = new IronFragment();
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
        return inflater.inflate(R.layout.fragment_iron, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button nextButton = view.findViewById(R.id.next_btn);
        nextButton.setOnClickListener((View.OnClickListener) this);


        numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(30);
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
        double price = quantity * 1.2;
        DecimalFormat df = new DecimalFormat("#.##");
        estimatedPrice.setText("£" + df.format(price));

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                int iron_quantity = numberPicker.getValue();
                callDeliveryScreen(iron_quantity);
                break;
            default:
                break;
        }
    }

    public void callDeliveryScreen(int iron_quantity) {
        Intent intent = new Intent(getContext(), deliveryScreen.class);
        intent.putExtra("IRON_QUANTITY", iron_quantity);
        startActivity(intent);
    }


}