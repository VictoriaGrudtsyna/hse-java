package hse.java.lectures.lecture6.tasks.synchronizer;

import java.util.*;

public class StreamingMonitor {
    List<Integer> tasksIds;
    Map<Integer, Integer> tickCounters;
    int currentId;
    boolean done;
    int globalTicks;
    int ticksPerWriter;

    public StreamingMonitor(List<StreamWriter> tasks, int ticksPerWriter) {
        this.tasksIds = new ArrayList<>();
        this.tickCounters = new HashMap<>();
        this.ticksPerWriter = ticksPerWriter;
        this.done = false;
        this.globalTicks = 0;

        for (StreamWriter elem : tasks) {
            int id = elem.getId();
            tickCounters.put(id, 0);
            tasksIds.add(id);
        }

        Collections.sort(tasksIds);
        this.currentId = tasksIds.get(0);
    }

    public int getNextId() {
        if (globalTicks == tasksIds.size() * ticksPerWriter) {
            this.done = true;
            return -1;
        }
        else {
            return tasksIds.get((tasksIds.indexOf(currentId) + 1) % tasksIds.size());
        }
    }
}