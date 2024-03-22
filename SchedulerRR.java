import java.util.*;
public class SchedulerRR extends SchedulerBase implements Scheduler{
    private LinkedList<Process> queue = new LinkedList<Process>();
    Logger myLogger;
    int timeQuantum;

    public SchedulerRR (Logger myLogger, int quantum){
        this.myLogger = myLogger;
        this.timeQuantum = quantum;
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
            return scheduleNext();
        }
        if (cpu.isExecutionComplete()){
            myLogger.log("Process " + cpu.getName() + " execution complete");
            contextSwitches++;
            return scheduleNext();
        }
        else if (cpu.getElapsedBurst() % timeQuantum == 0){
            myLogger.log("Time quantum complete for process " + cpu.getName());
            queue.add(cpu);
            contextSwitches++;
            return scheduleNext();
        }
        else {
            return cpu;
        }
    }

    private Process scheduleNext() {
        Process nextProcess = queue.poll();
            if (nextProcess == null){
                return null;
            }
            contextSwitches++;
            myLogger.log("Scheduled: " + nextProcess.getName());
            return nextProcess;
    }
}

    // update:
    // if current burst duration >= time quantum, preempt it, return it to the queue and run the next process.
    // if current burst duration < time quantum:
    //      if it's done, run the next one.
    //      if not done, keep running. 
