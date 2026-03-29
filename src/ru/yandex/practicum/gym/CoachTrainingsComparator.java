package ru.yandex.practicum.gym;

import java.util.Comparator;

public class CoachTrainingsComparator implements Comparator<Coach> {
    @Override
    public int compare(Coach c1, Coach c2) {
        return Integer.compare(c1.getNumberOfTrainingSessions(), c2.getNumberOfTrainingSessions());
    }
}

