import java.util.*;

public class SchedulerFCFS extends SchedulerBase implements Scheduler {
    private LinkedList<Process> queue = new LinkedList<Process>();
    Logger myLogger;

    public SchedulerFCFS (Logger myLogger){
        this.myLogger = myLogger;
    }

    @Override
    public int getNumberOfContextSwitches() {
        return this.contextSwitches;
    }

    @Override
    public void notifyNewProcess(Process p) {
        this.queue.add(p);
    }

    @Override
    public Process update(Process cpu) {
        if (cpu == null){
            // if there is no process running, run the next process in the queue
            cpu = queue.poll();
            if (cpu == null){
                return null;
            }
            contextSwitches++;
            myLogger.log("Scheduled: " + cpu.getName());
        }
        else if (cpu.isBurstComplete()){
            myLogger.log("Process " + cpu.getName() + " burst complete");

            if (cpu.isExecutionComplete()){
                myLogger.log("Process " + cpu.getName() + " execution complete");
            }
            else { // if it's not done yet
                queue.add(cpu);
            }
            // either way, we need a new thing to run
            contextSwitches++;
            cpu = queue.poll();
            if (cpu == null){
                return null;
            }
            contextSwitches++;
            myLogger.log("Scheduled: " + cpu.getName());
        }

        // if current process is not done with burst, not done with time, and not null, then simply continue.
        return cpu;
    }

}
