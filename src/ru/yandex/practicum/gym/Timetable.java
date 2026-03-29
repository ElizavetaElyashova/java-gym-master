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
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, Set<TrainingSession>> trainingSessionsByDay = new TreeMap<>();
        Set<TrainingSession> trainingSessionsByTime = new HashSet<>();
        //проверяем, есть ли записи по ключу day в расписании
        if (timetable.containsKey(day)) {
            //получаем записи по ключу day, сохраняем в переменную
            trainingSessionsByDay = timetable.get(day);
            //проверяем, есть ли записи по ключу time
            if (trainingSessionsByDay.containsKey(time)) {
                //получаем записи по ключу time, сохраняем в переменную
                trainingSessionsByTime = trainingSessionsByDay.get(time);
            } else {
                //записей по ключу time нет, создаем запись с ключом time и пустым сетом
                trainingSessionsByDay.put(time, trainingSessionsByTime);
            }
        } else {
            //записей по ключу day нет, создаем запись в timetable с ключом day и пустой мапой
            timetable.put(day, trainingSessionsByDay);
            //в мапе создаем запись с ключом time и пустым сетом
            trainingSessionsByDay.put(time, trainingSessionsByTime);
        }
        if (trainingSession != null) {
            //добавляем тренировку в сет
            if (trainingSessionsByTime.add(trainingSession)) {
                System.out.println("Тренировка успешно добавлена.");
                //апдейтим счетчик тренировок у тренера этой тренировки
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
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public TreeMap<Coach, Integer> getCountByCoaches() {
        TreeMap<Coach, Integer> sorted = new TreeMap<>(new CoachTrainingsComparator().reversed());
        sorted.putAll(countByCoaches);
        return sorted;
    }
}
