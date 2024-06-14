package com.example.student;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private int id;
    private String firstName;
    private String lastName;
    private int age;

    public Student() {
        // Constructeur par défaut
    }

    public Student(int id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Ajoutez cette méthode à votre classe Student
    @Override
    public int describeContents() {
        return 0;
    }

    // Ajoutez cette méthode à votre classe Student
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeInt(age);
    }

    // Ajoutez cette partie pour créer un objet Parcelable.Creator pour votre classe
    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    // Ajoutez ce constructeur pour créer un objet Student à partir d'un Parcel
    private Student(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        age = in.readInt();
    }
}