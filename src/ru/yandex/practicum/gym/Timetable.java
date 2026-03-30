package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    /* Основная структура - хэш-таблица с ключом "день недели",
    по дню недели получаем treemap с ключом "время дня"
    (используем treemap, чтобы тренировки сортировались по времени начала),
    по времени дня получаем сет со всеми тренировками, поставленными на это время
    (используем сет, потому что может быть несколько тренировок в одно и то же время
    и для того, чтобы избежать случайного дублирования тренировки)
     */
    private HashMap<DayOfWeek, TreeMap<TimeOfDay, Set<TrainingSession>>> timetable;
    private HashMap<Coach, Integer> countByCoaches;

    public Timetable() {
        timetable = new HashMap<>();
        countByCoaches = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (trainingSession != null) {
            DayOfWeek day = trainingSession.getDayOfWeek();
            TimeOfDay time = trainingSession.getTimeOfDay();
            TreeMap<TimeOfDay, Set<TrainingSession>> trainingSessionsByDay = new TreeMap<>();
            Set<TrainingSession> trainingSessionsByTime = new HashSet<>();
            if (timetable.containsKey(day)) {
                trainingSessionsByDay = timetable.get(day);
                if (trainingSessionsByDay.containsKey(time)) {
                    trainingSessionsByTime = trainingSessionsByDay.get(time);
                } else {
                    trainingSessionsByDay.put(time, trainingSessionsByTime);
                }
            } else {
                timetable.put(day, trainingSessionsByDay);
                trainingSessionsByDay.put(time, trainingSessionsByTime);
            }
            if (trainingSessionsByTime.add(trainingSession)) {
                System.out.println("Тренировка успешно добавлена.");
                addNewTrainingSessionForCoach(trainingSession.getCoach());
            } else {
                System.out.println("Такая тренировка уже есть в расписании.");
            }
        }
    }

    public void addNewTrainingSessionForCoach(Coach coach) {
        coach.setNumberOfTrainingSessions(coach.getNumberOfTrainingSessions() + 1);
        countByCoaches.put(coach, coach.getNumberOfTrainingSessions());
    }

    public TreeMap<TimeOfDay, Set<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public Set<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (timetable.get(dayOfWeek) == null) {
            System.out.println("В этот день тренировок нет.");
            return null;
        } else {
            return timetable.get(dayOfWeek).get(timeOfDay);
        }
    }

    public TreeMap<Coach, Integer> getCountByCoaches() {
        TreeMap<Coach, Integer> sorted = new TreeMap<>(new CoachTrainingsComparator().reversed());
        System.out.println();
        System.out.println(sorted);
        sorted.putAll(countByCoaches);
        return sorted;
    }
}
