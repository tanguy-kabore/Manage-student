package com.example.student;

// AddStudentDialogFragment.java
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

public class AddStudentDialogFragment extends DialogFragment {
    private MainActivity mainActivity;
    private View dialogView;

    // Modification dans la méthode onCreateDialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_student_dialog, null);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextFirstName = dialogView.findViewById(R.id.editTextFirstName);
        EditText editTextAge = dialogView.findViewById(R.id.editTextAge);

        builder.setView(dialogView)
                .setTitle("Ajouter un étudiant")
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String studentName = editTextName.getText().toString();
                    String studentFirstName = editTextFirstName.getText().toString();
                    String studentAge = editTextAge.getText().toString();

                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    long result = databaseHelper.addStudent(studentName, studentFirstName, Integer.parseInt(studentAge));

                    if (result != -1) {
                        showSnackbar("Étudiant ajouté avec succès", true);
                        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
                        StudentAdapter adapter = (StudentAdapter) recyclerView.getAdapter();
                        adapter.addStudent(new Student(0, studentName, studentFirstName, Integer.parseInt(studentAge)));

                        // Ajoutez cette ligne pour mettre à jour la liste des étudiants
                        ((MainActivity) getActivity()).updateStudentList();
                    } else {
                        showSnackbar("Échec de l'ajout de l'étudiant", false);
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .setOnDismissListener(dialog -> {
                    // Appeler la méthode pour mettre à jour la liste des étudiants dans l'activité principale
                    if (mainActivity != null) {
                        mainActivity.updateStudentList();
                    }
                });

        return builder.create();
    }

    // Méthode pour passer une référence de l'activité principale
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private void showSnackbar(String message, boolean isSuccess) {
        Snackbar snackbar = Snackbar.make(dialogView, message, Snackbar.LENGTH_SHORT);
        if (isSuccess) {
            snackbar.setBackgroundTint(getResources().getColor(R.color.snackbarSuccessBackground));
        } else {
            snackbar.setBackgroundTint(getResources().getColor(R.color.snackbarErrorBackground));
        }
        snackbar.show();

        // Fermer le modal après 2 secondes
        new Handler().postDelayed(() -> {
            dismiss();
            // Appeler la méthode pour mettre à jour la liste des étudiants dans l'activité principale
            if (mainActivity != null) {
                mainActivity.updateStudentList();
            }
        }, 2000);
    }
}