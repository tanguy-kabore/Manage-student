package com.example.student;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> studentList;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;

    public StudentAdapter(List<Student> studentList, FragmentManager fragmentManager, MainActivity mainActivity) {
        this.studentList = studentList;
        this.fragmentManager = fragmentManager;
        this.mainActivity = mainActivity; // Initialisez mainActivity
    }

    // Add this method to add a new student to the list
    public void addStudent(Student student) {
        studentList.add(student);
        notifyItemInserted(studentList.size() - 1);
        notifyDataSetChanged();  // Ajoutez cette ligne pour mettre à jour l'adaptateur avec la nouvelle liste
        // Ajouter des journaux pour déboguer
        Log.d("StudentAdapter", "Student added: " + student.getFirstName() + " " + student.getLastName());
    }

    // Optional: Add a method to clear the list
    public void clearStudents() {
        studentList.clear();
        notifyDataSetChanged();
    }

    // Optional: Add a method to remove a student from the list
    public void removeStudent(int position) {
        studentList.remove(position);
        notifyItemRemoved(position);
    }

    // Optional: Add a method to get a student at a specific position
    public Student getStudent(int position) {
        return studentList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crée la vue de l'élément de la liste à partir du fichier de layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Lie les données de l'étudiant à la vue de l'élément de la liste
        Student student = studentList.get(position);
        holder.textViewId.setText("ID: " + String.valueOf(student.getId()));
        holder.textViewName.setText(student.getFirstName() + " " + student.getLastName());
        // Ajoutez d'autres données aux autres vues si nécessaire

        // Ajoutez un gestionnaire de clic à l'icône de suppression
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assurez-vous que la liste n'est pas vide
                if (!studentList.isEmpty() && position < studentList.size()) {
                    // Obtenez l'ID de l'étudiant à supprimer
                    int studentId = studentList.get(position).getId();

                    // Supprimer l'étudiant de la base de données SQLite
                    DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                    dbHelper.deleteStudent(studentId);

                    // Supprimer l'étudiant de la liste et mettre à jour l'adaptateur
                    removeStudent(position);
                }
            }
        });

        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Assurez-vous que la liste n'est pas vide
                if (!studentList.isEmpty() && position < studentList.size()) {
                    // Obtenez l'étudiant à éditer
                    Student studentToEdit = studentList.get(position);

                    // Ouvrez la boîte de dialogue pour modifier l'étudiant
                    showEditStudentDialog(studentToEdit, position);
                }
            }
        });
    }

    // Method to show the edit student dialog
    // Dans la méthode showEditStudentDialog de StudentAdapter
    private void showEditStudentDialog(Student studentToEdit, int position) {
        EditStudentDialogFragment dialogFragment = new EditStudentDialogFragment();

        // Pass the student and position to the dialog
        Bundle bundle = new Bundle();
        bundle.putParcelable("studentToEdit", (Parcelable) studentToEdit);
        bundle.putInt("position", position);
        dialogFragment.setArguments(bundle);

        // Set the reference to MainActivity
        dialogFragment.setMainActivity(mainActivity);

        // Show the edit dialog
        dialogFragment.show(fragmentManager, "EditStudentDialog");
    }

    @Override
    public int getItemCount() {
        Log.d("StudentAdapter", "getItemCount: " + studentList.size());
        return studentList.size();
    }

    // Définition de la classe ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewId;
        TextView textViewName;
        ImageButton buttonDelete; // Ajoutez une référence à l'icône de suppression
        ImageButton buttonEdit;
        // Ajoutez d'autres vues selon votre besoin

        public ViewHolder(View itemView) {
            super(itemView);
            // Initialisation des vues à l'intérieur du constructeur du ViewHolder
            textViewId = itemView.findViewById(R.id.textViewId);
            textViewName = itemView.findViewById(R.id.textViewName);
            buttonDelete = itemView.findViewById(R.id.buttonDelete); // Initialisez l'icône de suppression
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            // Initialisez d'autres vues ici
        }
    }
}
