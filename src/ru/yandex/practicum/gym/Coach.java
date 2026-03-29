package ru.yandex.practicum.gym;

import java.util.Objects;

public class Coach {

    //фамилия
    private String surname;
    //имя
    private String name;
    //отчество
    private String middleName;
    //количество тренировок, апдейтится при добавлении новой тренировки в классе Timetable
    private Integer numberOfTrainingSessions;

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        numberOfTrainingSessions = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(surname, coach.surname) && Objects.equals(name, coach.name) && Objects.equals(middleName, coach.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }

    @Override
    public String toString() {
        return "Тренер: " + surname + ' ' + name + ' ' + middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Integer getNumberOfTrainingSessions() {
        return numberOfTrainingSessions;
    }

    public void setNumberOfTrainingSessions(Integer numberOfTrainingSessions) {
        this.numberOfTrainingSessions = numberOfTrainingSessions;
    }
}
