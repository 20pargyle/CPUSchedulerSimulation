import java.util.*;
public class SchedulerPriority extends SchedulerBase implements Scheduler{
    Comparator<Process> priorityComparator = new Comparator<Process>() {
        public int compare(Process p1, Process p2) {
            if (p1.getPriority() < p2.getPriority())
                return -1;
            return 1;
        }};

    private LinkedList<Process> queue = new LinkedList<Process>();
    Logger myLogger;

    public SchedulerPriority (Logger myLogger){
        this.myLogger = myLogger;
    }

    @Override
    public void notifyNewProcess(Process p) {
        this.queue.add(p);
        Collections.sort(queue, priorityComparator);
    }

    @Override
    public Process update(Process cpu) {
        if (cpu == null){
            return scheduleNext();
        }
        if (cpu.isBurstComplete()){
            myLogger.log("Process " + cpu.getName() + " burst complete");
            if (cpu.isExecutionComplete()){
                myLogger.log("Process " + cpu.getName() + " execution complete");
            }
            contextSwitches++;
            return scheduleNext();
        }
        Process nextProcess = queue.peek();
        if (nextProcess != null && nextProcess.getPriority() < cpu.getPriority()){
            myLogger.log("Preemptively removed: " + cpu.getName());
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
