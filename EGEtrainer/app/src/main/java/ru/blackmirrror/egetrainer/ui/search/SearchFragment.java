package ru.blackmirrror.egetrainer.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.blackmirrror.egetrainer.ChoiserActivity;
import ru.blackmirrror.egetrainer.Questions2Activity;
import ru.blackmirrror.egetrainer.R;
import ru.blackmirrror.egetrainer.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment implements View.OnClickListener{

    //private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;

    ImageButton mathBase, mathProf, rus, eng, inf, phis, soc, his, bio, chem, geog, leat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textSearch;
        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        mathBase = root.findViewById(R.id.btnMathBase);
        mathBase.setOnClickListener(this);
        mathProf= root.findViewById(R.id.btnMathProf);
        mathProf.setOnClickListener(this);
        rus = root.findViewById(R.id.btnRus);
        rus.setOnClickListener(this);
        eng = root.findViewById(R.id.btnEng);
        eng.setOnClickListener(this);
        inf = root.findViewById(R.id.btnInf);
        inf.setOnClickListener(this);
        phis = root.findViewById(R.id.btnPhis);
        phis.setOnClickListener(this);
        soc = root.findViewById(R.id.btnSoc);
        soc.setOnClickListener(this);
        his = root.findViewById(R.id.btnHis);
        his.setOnClickListener(this);
        bio = root.findViewById(R.id.btnBio);
        bio.setOnClickListener(this);
        chem = root.findViewById(R.id.btnChem);
        chem.setOnClickListener(this);
        geog = root.findViewById(R.id.btnGeog);
        geog.setOnClickListener(this);
        leat = root.findViewById(R.id.btnLeat);
        leat.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        //TODO активность
        Intent intent = new Intent(getActivity(), ChoiserActivity.class);
        switch (v.getId()) {
            case (R.id.btnMathBase):{
                intent.putExtra("sub", "mathBase");
                break;
            }
            case (R.id.btnMathProf):{
                intent.putExtra("sub", "mathProf");
                break;
            }
            case (R.id.btnRus):{
                intent.putExtra("sub", "rus");
                break;
            }
            case (R.id.btnEng):{
                intent.putExtra("sub", "eng");
                break;
            }
            case (R.id.btnInf):{
                intent.putExtra("sub", "inf");
                break;
            }
            case (R.id.btnPhis):{
                intent.putExtra("sub", "phis");
                break;
            }
            case (R.id.btnSoc):{
                intent.putExtra("sub", "soc");
                break;
            }
            case (R.id.btnHis):{
                intent.putExtra("sub", "his");
                break;
            }
            case (R.id.btnBio):{
                intent.putExtra("sub", "bio");
                break;
            }
            case (R.id.btnChem):{
                intent.putExtra("sub", "chem");
                break;
            }
            case (R.id.btnGeog):{
                intent.putExtra("sub", "geog");
                break;
            }
            case (R.id.btnLeat):{
                intent.putExtra("sub", "leat");
                break;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}