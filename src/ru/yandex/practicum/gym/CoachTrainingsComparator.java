package ru.yandex.practicum.gym;

import java.util.Comparator;

public class CoachTrainingsComparator implements Comparator<Coach> {
    @Override
    public int compare(Coach c1, Coach c2) {
        if (c1.getNumberOfTrainingSessions().equals(c2.getNumberOfTrainingSessions())) {
            return String.CASE_INSENSITIVE_ORDER.reversed().compare(c1.toString(), c2.toString());
        } else {
            return Integer.compare(c1.getNumberOfTrainingSessions(), c2.getNumberOfTrainingSessions());
        }
    }
}

