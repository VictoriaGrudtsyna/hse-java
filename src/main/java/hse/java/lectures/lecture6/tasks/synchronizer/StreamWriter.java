package hse.java.lectures.lecture6.tasks.synchronizer;

import lombok.Getter;

import java.io.PrintStream;

public class StreamWriter implements Runnable {

    private final String message;
    @Getter
    private final int id;
    private final PrintStream output;
    private final Runnable onTick;
    private volatile StreamingMonitor monitor;

    public StreamWriter(int id, String message, PrintStream output, Runnable onTick) {
        this.message = message;
        this.id = id;
        this.output = output;
        this.onTick = onTick;
    }

    public void attachMonitor(StreamingMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while(true) {
            synchronized (monitor) {
                if (monitor.tickCounters.getOrDefault(this.id, 0) == monitor.ticksPerWriter) {
                    break;
                }

                while (monitor.currentId != this.id && !monitor.done) {
                    try {
                        monitor.wait();
                    }
                    catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (monitor.done) {
                    break;
                }

                output.print(message);
                onTick.run();

                monitor.tickCounters.put(monitor.currentId, monitor.tickCounters.get(monitor.currentId) + 1);
                monitor.globalTicks++;
                monitor.currentId = monitor.getNextId();
                monitor.notifyAll();
            }
        }
    }
}