package com.example.student;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<Student> studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Trouver le bouton flottant dans le layout
        FloatingActionButton fab = findViewById(R.id.fab);

        dbHelper = new DatabaseHelper(this);  // Initialisez dbHelper ici
        recyclerView = findViewById(R.id.recyclerView);
        studentAdapter = new StudentAdapter(studentList, getSupportFragmentManager(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(studentAdapter);

        // Mettez à jour la liste des étudiants (simulons une liste vide ici)
        updateStudentList();

        // Définir le gestionnaire de clic pour le bouton flottant
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la boîte de dialogue pour ajouter ou éditer un étudiant
                showAddOrEditStudentDialog(null, -1);
            }
        });
    }

    // ...
    // Méthode pour trouver la position d'un étudiant dans la liste
    public int findStudentPosition(Student student) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).getId() == student.getId()) {
                return i;
            }
        }
        return -1;
    }

    // Méthode pour mettre à jour un étudiant dans la liste
    public void updateStudentInList(int position, Student updatedStudent) {
        if (position >= 0 && position < studentList.size()) {
            studentList.set(position, updatedStudent);
            studentAdapter.notifyItemChanged(position);
        }
    }

    // Méthode pour mettre à jour la liste des étudiants
    // Méthode pour mettre à jour la liste des étudiants
    public void updateStudentList() {
        // Mettez à jour la liste des étudiants à partir de la base de données
        studentList.clear();
        studentList.addAll(dbHelper.getAllStudents());

        // Ajoutez cette ligne pour déboguer
        Log.d("StudentAdapter", "Student list updated: " + studentList.toString());

        // Mettez à jour l'adaptateur avec la nouvelle liste
        studentAdapter.notifyDataSetChanged();
    }


    // Méthode pour afficher la boîte de dialogue d'ajout ou d'édition d'étudiant
    private void showAddOrEditStudentDialog(Student studentToEdit, int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddStudentDialogFragment dialogFragment = new AddStudentDialogFragment();

        // Passer une référence de l'activité principale à AddStudentDialogFragment
        dialogFragment.setMainActivity(this);

        // Si studentToEdit n'est pas null, c'est une édition, sinon, c'est une ajout
        if (studentToEdit != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("studentToEdit", (Parcelable) studentToEdit);
            bundle.putInt("position", position);
            dialogFragment.setArguments(bundle);
        }

        dialogFragment.show(fragmentManager, "AddStudentDialog");
    }
}
