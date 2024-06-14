package com.example.student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// EditStudentDialogFragment.java
public class EditStudentDialogFragment extends DialogFragment {
    private MainActivity mainActivity;
    private View dialogView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_student_dialog, null);

        // Obtenez l'étudiant à éditer et la position
        Bundle bundle = getArguments();
        if (bundle != null) {
            Student studentToEdit = bundle.getParcelable("studentToEdit");
            int position = bundle.getInt("position", -1);

            // Initialisez les champs de la boîte de dialogue avec les données de l'étudiant à éditer
            EditText editTextName = dialogView.findViewById(R.id.editTextName);
            EditText editTextFirstName = dialogView.findViewById(R.id.editTextFirstName);
            EditText editTextAge = dialogView.findViewById(R.id.editTextAge);

            if (studentToEdit != null) {
                editTextName.setText(studentToEdit.getLastName());
                editTextFirstName.setText(studentToEdit.getFirstName());
                editTextAge.setText(String.valueOf(studentToEdit.getAge()));
                // Initialisez d'autres champs selon votre modèle Student
            }

            // Ajoutez un gestionnaire de clic au bouton de modification
            builder.setView(dialogView)
                    .setTitle("Modifier l'étudiant")
                    .setPositiveButton("Modifier", (dialog, which) -> {
                        // Obtenez les nouvelles données de la boîte de dialogue
                        String newName = editTextName.getText().toString();
                        String newFirstName = editTextFirstName.getText().toString();
                        int newAge = Integer.parseInt(editTextAge.getText().toString());

                        // Mettez à jour l'étudiant dans la liste et la base de données
                        updateStudent(studentToEdit, position, newName, newFirstName, newAge);

                        // Mettez à jour la liste des étudiants dans l'activité principale
                        if (mainActivity != null) {
                            mainActivity.updateStudentList();
                        }
                    })
                    .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        }

        return builder.create();
    }

    // Méthode pour passer une référence de l'activité principale
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    // Mettez à jour la méthode updateStudent pour accepter les nouvelles valeurs
    private void updateStudent(Student student, int position, String newName, String newFirstName, int newAge) {
        if (mainActivity != null) {
            // Vérifiez que mainActivity n'est pas null avant d'appeler des méthodes dessus
            int newPosition = mainActivity.findStudentPosition(student);
            if (newPosition != -1) {
                position = newPosition;
            }

            // Mettez à jour l'étudiant avec les nouvelles valeurs
            student.setLastName(newName);
            student.setFirstName(newFirstName);
            student.setAge(newAge);

            mainActivity.updateStudentInList(position, student);

            // Mettez à jour l'étudiant dans la base de données SQLite
            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
            dbHelper.updateStudent(student);
        } else {
            Log.e("EditStudentDialogFragment", "MainActivity is null");
        }
    }
}