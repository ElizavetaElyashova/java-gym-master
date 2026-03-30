package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {
    static Group groupAdult;
    static Group groupChild;
    static Coach coach1;
    static Coach coach2;
    Timetable timetable;


    @BeforeAll
    static void beforeAll() {
        groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
    }

    @BeforeEach
    void beforeEach() {
        timetable = new Timetable();
        coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        coach2 = new Coach("Иванова", "Татьяна", "Петровна");
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        TrainingSession singleTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        TreeMap<TimeOfDay, Set<TrainingSession>> result = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertTrue(result.firstKey().equals(new TimeOfDay(13, 0))
                && result.lastKey().equals(new TimeOfDay(20, 0)));
        // Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        TrainingSession singleTrainingSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Set<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        Assertions.assertEquals(1, result.size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertNull(result);
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeMultipleSessionsAtTheSameTime() {
        TrainingSession adultSession = new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        TrainingSession childSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        timetable.addNewTrainingSession(adultSession);
        timetable.addNewTrainingSession(childSession);
        Set<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testAddNewSessionSameGroupDifferentCoaches() {
        TrainingSession adultSession1 = new TrainingSession(groupAdult, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        TrainingSession adultSession2 = new TrainingSession(groupAdult, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(14, 0));
        timetable.addNewTrainingSession(adultSession1);
        timetable.addNewTrainingSession(adultSession2);
        Set<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        for (TrainingSession session : result) {
            Assertions.assertEquals(session.getCoach(), coach1);
        }
        result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.FRIDAY, new TimeOfDay(14, 0));
        for (TrainingSession session : result) {
            Assertions.assertEquals(session.getCoach(), coach2);
        }
    }

    @Test
    void testAddSameSession() {
        TrainingSession adultSession = new TrainingSession(groupAdult, coach1, DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        timetable.addNewTrainingSession(adultSession);
        timetable.addNewTrainingSession(adultSession);
        Set<TrainingSession> result = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testGetCountByCouchesDifferentCount() {
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        TreeMap<Coach, Integer> result = timetable.getCountByCoaches();
        Assertions.assertEquals(result.firstKey(), coach1);
        Assertions.assertEquals(result.lastKey(), coach2);
    }

    @Test
    void testGetCountByCouchesSameCount() {
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));
        TrainingSession adultSession = new TrainingSession(groupAdult, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        timetable.addNewTrainingSession(adultSession);

        TreeMap<Coach, Integer> result = timetable.getCountByCoaches();
        Assertions.assertEquals(2, result.get(coach1));
        Assertions.assertEquals(2, result.get(coach2));
    }

    @Test
    void testGetCountByCoachesChangingOrder() {
        TrainingSession session1 = new TrainingSession(groupChild, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(12, 0));
        TrainingSession session2 = new TrainingSession(groupChild, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0));
        TrainingSession session3 = new TrainingSession(groupChild, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0));
        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);
        timetable.addNewTrainingSession(session3);
        TreeMap<Coach, Integer> result = timetable.getCountByCoaches();
        //сейчас сессий больше у coach2, поэтому он должен быть на первом месте
        Assertions.assertEquals(result.firstKey(), coach2);
        TrainingSession session4 = new TrainingSession(groupAdult, coach1,
                DayOfWeek.THURSDAY, new TimeOfDay(19, 0));
        TrainingSession session5 = new TrainingSession(groupAdult, coach1,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(session4);
        timetable.addNewTrainingSession(session5);
        result = timetable.getCountByCoaches();
        //теперь сессий больше у coach1, проверяем, что он на первом месте
        Assertions.assertEquals(result.firstKey(), coach1);
    }

}
