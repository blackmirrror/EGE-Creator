package ru.blackmirrror.egetrainer.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.blackmirrror.egetrainer.R;
import ru.blackmirrror.egetrainer.databinding.FragmentAccountBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import ru.blackmirrror.egetrainer.Models.User;

public class AccountFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private FragmentAccountBinding binding;

    private static User user;

    TextView textViewEmail, textViewPassword, textViewFirstName, textViewLastName, textViewName;

    EditText password_field, firstname_field, lastname_field;
    Button logout_btn, change_password, change_firstname, change_lastname;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_EMAIL = "name";
    private static final String KEY_PASSWORD = "email";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textViewEmail = root.findViewById(R.id.textViewEmail);
        textViewPassword = root.findViewById(R.id.textViewPassword);
        logout_btn = root.findViewById(R.id.logout);
        change_password = root.findViewById(R.id.ChangePasswordButton);
        password_field = root.findViewById(R.id.ChangePasswordField);

        change_firstname = root.findViewById(R.id.ChangeFirstnameButton);
        firstname_field = root.findViewById(R.id.ChangeFirstname);

        change_lastname = root.findViewById(R.id.ChangeLastnameButton);
        lastname_field = root.findViewById(R.id.ChangeLastname);

        // textViewFirstName = root.findViewById(R.id.textViewFirstName);
        // textViewLastName = root.findViewById(R.id.textViewLastName);
        textViewName = root.findViewById(R.id.textViewName);

        RelativeLayout mainLayout;

        // Get your layout set up, this is just an example
        mainLayout = root.findViewById(R.id.PasswordLayout);

        // Получаем юзера и его поля итд
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = user.getUid();
        DatabaseReference users = database.getReference("Users");
        DatabaseReference uidRef = users.child(uid);
        uidRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            textViewEmail.setText(user.getEmail());
                            textViewName.setText(user.getFirstName() + " " + user.getLastName());
                            firstname_field.setText(user.getFirstName());
                            lastname_field.setText(user.getLastName());
                        }
                        AccountFragment.user = user;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        /* final TextView textView = binding.textAccount;
        accountViewModel.getText().

                observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        textView.setText(s);
                    }
                });*/
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

        //sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        if (email != null || password != null) {
            textViewEmail.setText("Email: " + email);
            textViewPassword.setText("Password: " + password);
        }


        //textViewFirstName.setText(user.getFirstName());

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getActivity(), "Log out successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        change_password.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User user = AccountFragment.user;
                        String new_pass = password_field.getText().toString();
                        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                        fuser.updatePassword(new_pass);
                        // user.setPass(new_pass);
                        // uidRef.setValue(user);
                        // System.out.println(user.getFirstName());
                        // Then just use the following:
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                    }
                }
        );
        change_lastname.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user = AccountFragment.user;
                        String new_lastname = lastname_field.getText().toString();
                        user.setLastName(new_lastname);
                        uidRef.setValue(user);
                        // System.out.println(user.getFirstName());
                        // Then just use the following:
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                    }
                }
        );
        change_firstname.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User user = AccountFragment.user;
                        String new_firstname = firstname_field.getText().toString();
                        user.setFirstName(new_firstname);
                        uidRef.setValue(user);
                        // System.out.println(user.getFirstName());
                        // Then just use the following:
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                    }
                }
        );
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}