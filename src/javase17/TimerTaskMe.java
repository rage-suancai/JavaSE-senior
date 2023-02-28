package javase17;

public class TimerTaskMe {
    Runnable task;
    long time;

    public TimerTaskMe(Runnable runnable, long time) {
        this.task = runnable;
        this.time = time;
    }

    public void start() {

        new Thread(() -> {
            try {
                Thread.sleep(time);
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void startRun() {

        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(time);
                    task.run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
